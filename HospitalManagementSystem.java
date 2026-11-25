import java.util.*;
import java.io.*;

// Patient Class
class Patient {
    String patientId;
    String name;
    int severity;
    int arrivalTime;

    Patient(String patientId, String name, int severity, int arrivalTime) {
        this.patientId = patientId;
        this.name = name;
        this.severity = severity;
        this.arrivalTime = arrivalTime;
    }

    public String toString() {
        return "Patient ID: " + patientId +
               " | Name: " + name +
               " | Severity: " + severity;
    }
}

// Hospital Management System
class HospitalSystem {

    LinkedList<Patient> patientList = new LinkedList<>();

    HashMap<String, Patient> patientMap = new HashMap<>();

    PriorityQueue<Patient> appointmentQueue =
        new PriorityQueue<>((a, b) -> {
            if (a.severity != b.severity) {
                return b.severity - a.severity;
            }
            return a.arrivalTime - b.arrivalTime;
        });

    int patientCounter = 101;
    int arrivalCounter = 1;

    // Register Patient
    Patient registerPatient(String name, int severity) {

        String id = "P" + patientCounter++;

        Patient p = new Patient(
            id, name, severity, arrivalCounter++
        );

        patientList.add(p);
        patientMap.put(id, p);
        appointmentQueue.add(p);

        return p;
    }

    // Search Patient
    Patient searchPatient(String id) {
        return patientMap.get(id);
    }

    // Update Patient
    boolean updatePatient(String id, String newName, int newSeverity) {

        Patient p = patientMap.get(id);

        if (p == null) {
            return false;
        }

        appointmentQueue.remove(p);

        p.name = newName;
        p.severity = newSeverity;

        appointmentQueue.add(p);

        return true;
    }

    // View Next Patient
    Patient viewNextPatient() {
        return appointmentQueue.peek();
    }

    // Call Next Patient
    Patient callNextPatient() {
        return appointmentQueue.poll();
    }

    // Sort Patient Records
    void sortPatients() {

        ArrayList<Patient> sortedList =
            new ArrayList<>(patientList);

        sortedList.sort((a, b) -> {
            if (a.severity != b.severity) {
                return b.severity - a.severity;
            }
            return a.arrivalTime - b.arrivalTime;
        });

        System.out.println("\nSorted Patient Records:");

        for (Patient p : sortedList) {
            System.out.println(p);
        }
    }

    // Save Records to File
    void saveToFile() {

        try (PrintWriter writer =
                 new PrintWriter(new FileWriter("hospital.txt"))) {

            for (Patient p : patientList) {
                writer.println(
                    p.patientId + "|" +
                    p.name + "|" +
                    p.severity + "|" +
                    p.arrivalTime
                );
            }

            System.out.println("Records saved successfully.");

        } catch (IOException e) {
            System.out.println("File saving error.");
        }
    }

    // Load Records from File
    void loadFromFile() {

        File file = new File("hospital.txt");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader =
                 new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split("\\|");

                if (data.length == 4) {

                    String id = data[0];
                    String name = data[1];
                    int severity = Integer.parseInt(data[2]);
                    int arrival = Integer.parseInt(data[3]);

                    Patient p = new Patient(
                        id, name, severity, arrival
                    );

                    patientList.add(p);
                    patientMap.put(id, p);
                    appointmentQueue.add(p);

                    patientCounter = Math.max(
                        patientCounter,
                        Integer.parseInt(id.substring(1)) + 1
                    );

                    arrivalCounter = Math.max(
                        arrivalCounter, arrival + 1
                    );
                }
            }

            System.out.println("Records loaded successfully.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("File loading error.");
        }
    }
}

// Main Class
public class HospitalManagementSystem {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println(" HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("==================================");

        // Load saved records
        HospitalSystem savedSystem = new HospitalSystem();
        savedSystem.loadFromFile();

        // TEST CASE 1
        System.out.println("\nTEST CASE 1: NORMAL INPUT");

        HospitalSystem test1 = new HospitalSystem();

        Patient p1 = test1.registerPatient("Riya", 3);

        System.out.println("Patient registered: " + p1);

        // TEST CASE 2
        System.out.println("\nTEST CASE 2: BOUNDARY INPUT");

        HospitalSystem test2 = new HospitalSystem();

        test2.registerPatient("Riya", 2);

        Patient next = test2.viewNextPatient();

        System.out.println("Next patient: " + next.patientId);

        // TEST CASE 3
        System.out.println("\nTEST CASE 3: INVALID INPUT");

        HospitalSystem test3 = new HospitalSystem();

        Patient result = test3.searchPatient("P999");

        if (result == null) {
            System.out.println("Patient P999 not found.");
        } else {
            System.out.println(result);
        }

        // TEST CASE 4
        System.out.println("\nTEST CASE 4: DIFFERENT VALID SCENARIO");

        HospitalSystem test4 = new HospitalSystem();

        test4.registerPatient("Aman", 2);
        test4.registerPatient("Riya", 5);

        Patient called = test4.callNextPatient();

        System.out.println("Patient called first: " + called);

        // Sorting demonstration
        test4.sortPatients();

        // Save records
        test1.saveToFile();

        System.out.println("\n==================================");
        System.out.println(" ALL 4 TEST CASES COMPLETED");
        System.out.println("==================================");
    }
}