import java.util.*;
import java.io.*;

public class TextEditorApp {

    private LinkedList<String> document = new LinkedList<>();
    private Stack<LinkedList<String>> undoStack = new Stack<>();
    private Stack<LinkedList<String>> redoStack = new Stack<>();
    private final String FILE_NAME = "document.txt";

    public static void main(String[] args) {
        TextEditorApp editor = new TextEditorApp();
        editor.loadFromFile();
        editor.runMenuLoop();
    }

    private void saveState() {
        undoStack.push(new LinkedList<>(document));
    }

    public void addLine(String text) {
        saveState();
        document.add(text);
        redoStack.clear();
        System.out.println("Line added.");
    }

    public void deleteLastLine() {
        if (document.isEmpty()) {
            System.out.println("Document is already empty. Nothing to delete.");
            return;
        }

        saveState();
        document.removeLast();
        redoStack.clear();
        System.out.println("Last line deleted.");
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        redoStack.push(new LinkedList<>(document));
        document = undoStack.pop();
        System.out.println("Undo done.");
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo.");
            return;
        }

        undoStack.push(new LinkedList<>(document));
        document = redoStack.pop();
        System.out.println("Redo done.");
    }

    public void displayDocument() {
        if (document.isEmpty()) {
            System.out.println("Document is empty.");
            return;
        }

        System.out.println("Document:");

        int lineNum = 1;
        for (String line : document) {
            System.out.println(lineNum + ". " + line);
            lineNum++;
        }
    }

    public void loadFromFile() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                document.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String line : document) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void runMenuLoop() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(
                "\nMenu: 1 Add line, 2 Delete last line, " +
                "3 Undo, 4 Redo, 5 Display, 6 Exit"
            );

            System.out.print("Enter choice: ");

            String choiceInput = scanner.nextLine().trim();
            int choice = -1;

            try {
                choice = Integer.parseInt(choiceInput);
            } catch (NumberFormatException e) {
                // Invalid option handled below
            }

            switch (choice) {
                case 1:
                    System.out.print("Text: ");
                    String text = scanner.nextLine();
                    addLine(text);
                    break;

                case 2:
                    deleteLastLine();
                    break;

                case 3:
                    undo();
                    break;

                case 4:
                    redo();
                    break;

                case 5:
                    displayDocument();
                    break;

                case 6:
                    saveToFile();
                    System.out.println("Document saved to " + FILE_NAME + ". Exiting application.");
                    return;

                default:
                    System.out.println("Invalid option. Choose 1 to 6.");
                    break;
            }
        }
    }
}