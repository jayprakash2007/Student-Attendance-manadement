
import java.io.*;
import java.util.*;
 
public class StudentAttendanceManagement {
 
    static final int MAX = 100;
 
    static int[] rollNo = new int[MAX];
    static String[] name = new String[MAX];
    static int[] attended = new int[MAX];
    static int[] totalClasses = new int[MAX];
 
    static int count = 0;
 
    static Scanner sc = new Scanner(System.in);
 
    public static void main(String[] args) {
 
        loadFromFile();
 
        int choice;
 
        do {
            System.out.println("\n===== STUDENT ATTENDANCE MANAGEMENT =====");
            System.out.println("1. Add Student");
            System.out.println("2. Mark Attendance");
            System.out.println("3. Search Student");
            System.out.println("4. Show Defaulters (<75%)");
            System.out.println("5. Sort by Attendance %");
            System.out.println("6. Display All Students");
            System.out.println("7. Save & Exit");
            System.out.print("Enter choice: ");
 
            choice = sc.nextInt();
 
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    markAttendance();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    showDefaulters();
                    break;
                case 5:
                    sortStudents();
                    break;
                case 6:
                    displayAll();
                    break;
                case 7:
                    saveToFile();
                    System.out.println("Data saved successfully.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
 
        } while (choice != 7);
    }
 
    static void addStudent() {
 
        if (count >= MAX) {
            System.out.println("Student limit reached.");
            return;
        }
 
        System.out.print("Enter Roll No: ");
        int roll = sc.nextInt();
 
        if (findStudent(roll) != -1) {
            System.out.println("Duplicate roll number not allowed.");
            return;
        }
 
        sc.nextLine();
 
        System.out.print("Enter Name: ");
        String studentName = sc.nextLine();
 
        rollNo[count] = roll;
        name[count] = studentName;
        attended[count] = 0;
        totalClasses[count] = 0;
 
        count++;
 
        System.out.println("Student added. " + roll + " | " + studentName);
    }
 
    static void markAttendance() {
 
        System.out.print("Enter Roll No: ");
        int roll = sc.nextInt();
 
        int index = findStudent(roll);
 
        if (index == -1) {
            System.out.println("Roll " + roll + " not found.");
            return;
        }
 
        System.out.print("Present or Absent (P/A): ");
        char status = sc.next().toUpperCase().charAt(0);
 
        if (status != 'P' && status != 'A') {
            System.out.println("Invalid input. Enter P or A only.");
            return;
        }
 
        totalClasses[index]++;
 
        if (status == 'P') {
            attended[index]++;
        }
 
        System.out.println("Attendance updated.");
    }
 
    static void searchStudent() {
 
        System.out.print("Enter Roll No: ");
        int roll = sc.nextInt();
 
        int index = findStudent(roll);
 
        if (index == -1) {
            System.out.println("Student not found.");
            return;
        }
 
        double percent = getPercentage(index);
 
        System.out.printf("%d | %s | Attendance: %d/%d | %.1f%%\n",
                rollNo[index],
                name[index],
                attended[index],
                totalClasses[index],
                percent);
    }
 
    static void showDefaulters() {
 
        boolean found = false;
 
        System.out.println("\n----- Defaulters (<75%) -----");
 
        for (int i = 0; i < count; i++) {
 
            double percent = getPercentage(i);
 
            if (percent < 75) {
                System.out.printf("%d | %s | %.1f%%\n",
                        rollNo[i],
                        name[i],
                        percent);
                found = true;
            }
        }
 
        if (!found) {
            System.out.println("No defaulters found.");
        }
    }
 
    static void sortStudents() {
 
        for (int i = 0; i < count - 1; i++) {
 
            for (int j = i + 1; j < count; j++) {
 
                if (getPercentage(i) < getPercentage(j)) {
 
                    swapInt(rollNo, i, j);
                    swapString(name, i, j);
                    swapInt(attended, i, j);
                    swapInt(totalClasses, i, j);
                }
            }
        }
 
        System.out.println("Students sorted by attendance percentage.");
    }
 
    static void displayAll() {
 
        if (count == 0) {
            System.out.println("No student records.");
            return;
        }
 
        System.out.println("\n----- Student Records -----");
 
        for (int i = 0; i < count; i++) {
 
            System.out.printf("%d | %s | %d/%d | %.1f%%\n",
                    rollNo[i],
                    name[i],
                    attended[i],
                    totalClasses[i],
                    getPercentage(i));
        }
    }
 
    static int findStudent(int roll) {
 
        for (int i = 0; i < count; i++) {
            if (rollNo[i] == roll) {
                return i;
            }
        }
 
        return -1;
    }
 
    static double getPercentage(int index) {
 
        if (totalClasses[index] == 0) {
            return 0;
        }
 
        return (attended[index] * 100.0) / totalClasses[index];
    }
 
    static void swapInt(int[] arr, int i, int j) {
 
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
 
    static void swapString(String[] arr, int i, int j) {
 
        String temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
 
    static void saveToFile() {
 
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("students.txt"));
 
            pw.println(count);
 
            for (int i = 0; i < count; i++) {
                pw.println(rollNo[i] + "," +
                        name[i] + "," +
                        attended[i] + "," +
                        totalClasses[i]);
            }
 
            pw.close();
 
        } catch (Exception e) {
            System.out.println("Error while saving file.");
        }
    }
 
    static void loadFromFile() {
 
        try {
 
            File file = new File("students.txt");
 
            if (!file.exists())
                return;
 
            Scanner fileScanner = new Scanner(file);
 
            count = Integer.parseInt(fileScanner.nextLine());
 
            for (int i = 0; i < count; i++) {
 
                String[] data = fileScanner.nextLine().split(",");
 
                rollNo[i] = Integer.parseInt(data[0]);
                name[i] = data[1];
                attended[i] = Integer.parseInt(data[2]);
                totalClasses[i] = Integer.parseInt(data[3]);
            }
 
            fileScanner.close();
 
        } catch (Exception e) {
            System.out.println("Error while loading file.");
        }
    }
}
 
