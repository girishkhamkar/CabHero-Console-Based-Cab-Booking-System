import java.util.*;

class Cab {
    String cabNumber, type, driverName, driverContact, CabHeroID;
    int capacity;
    boolean isAvailable;

    Cab(String cabNumber, String type, String driverName, String driverContact, String CabHeroID, int capacity) {
        this.cabNumber = cabNumber;
        this.type = type;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.CabHeroID =CabHeroID;
        this.capacity = capacity;
        this.isAvailable = true;
    }

    public void showCabDetails() {
        System.out.println("\nCabHero ID: " + CabHeroID);
        System.out.println("Cab Number: " + cabNumber);
        System.out.println("Type: " + type);
        System.out.println("Capacity: " + capacity);
        System.out.println("Pilot: " + driverName);
        System.out.println("Contact: " + driverContact);
        System.out.println("Status: " + (isAvailable ? "Available" : "Booked"));
    }
}


class Admin {
    static Scanner sc = new Scanner(System.in);
    private List<Cab> cabs;
    private int CabHeroCount = 10;

    Admin(List<Cab> cabs) {
        this.cabs = cabs;
    }

    
    public void addCabs() {
        System.out.print("How many cabs do you want to add (1 to 20): ");
        int n = sc.nextInt();
        sc.nextLine();

        if (n < 1 || n > 20) {
            System.out.println(" Invalid number  Please add between 1and 20 cabs.");
            return;
        }

        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Enter details for Cab " + (i + 1) + " ---");

            
            String cabNo;
            while (true) {
                System.out.print("Cab Number (e.g. MH-12-AB-1234): ");
                cabNo = sc.nextLine();
                if (cabNo.matches(".*\\d{2}.*")) break;
                System.out.println(" Invalid! Cab number must contain exactly two digits.");
            }

            
            String type;
            while (true) {
                System.out.print("Cab Type (hatchback/sedan/suv/xuv): ");
                type = sc.nextLine().toLowerCase();
                if (type.equals("hatchback") || type.equals("sedan") || type.equals("suv") || type.equals("xuv")) break;
                System.out.println(" Invalid! Only choose from hatchback, sedan, suv, or xuv.");
            }

            
            String driver;
            while (true) {
                System.out.print("Pilot Name (letters only): ");
                driver = sc.nextLine();
                if (driver.matches("[a-zA-Z ]+")) break;
                System.out.println(" Invalid! Driver name must contain only letters.");
            }

           
            String contact;
            while (true) {
                System.out.print("Driver Contact (10 digits): ");
                contact = sc.nextLine();
                if (contact.matches("\\d{10}")) break;
                System.out.println(" Invalid! Contact number must be exactly 10 digits.");
            }

            
            int capacity = switch (type) {
                case "hatchback" -> 3;
                case "sedan" -> 4;
                case "suv" -> 5;
                case "xuv" -> 6;
                default -> 4;
            };

            String CabHeroID = "CabHero" + CabHeroCount++;
            cabs.add(new Cab(cabNo, type, driver, contact, CabHeroID, capacity));
            System.out.println(" Cab added successfully! CabHero ID: " + CabHeroID);
        }
    }

    public void viewAllCabs() {
        if (cabs.isEmpty()) {
            System.out.println(" No cabs available.");
            return;
        }
        System.out.println("\n--- All Cab Details ---");
        for (Cab c : cabs) c.showCabDetails();
    }
}


class Passenger {
    String name, contact, pickup, drop;
    int seatsRequired;
    Cab assignedCab;

    Passenger(String name, String contact, String pickup, String drop, int seatsRequired) {
        this.name = name;
        this.contact = contact;
        this.pickup = pickup;
        this.drop = drop;
        this.seatsRequired = seatsRequired;
    }
}


class PassengerModule {
    static Scanner sc = new Scanner(System.in);
    private List<Cab> cabs;
    private List<Passenger> passengers;

    PassengerModule(List<Cab> cabs, List<Passenger> passengers) {
        this.cabs = cabs;
        this.passengers = passengers;
    }

    public void bookRide() {
        if (cabs.isEmpty()) {
            System.out.println(" No cabs available for booking.");
            return;
        }

        sc.nextLine();
        System.out.println("\n--- Enter Passenger Details ---");
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Contact: ");
        String contact = sc.nextLine();
        System.out.print("Pickup Location: ");
        String pickup = sc.nextLine();
        System.out.print("Drop Location: ");
        String drop = sc.nextLine();
        System.out.print("Number of Seats Required: ");
        int seats = sc.nextInt();

        Cab chosenCab = null;
        for (Cab c : cabs) {
            if (c.isAvailable && c.capacity >= seats) {
                if (chosenCab == null || c.capacity < chosenCab.capacity)
                    chosenCab = c;
            }
        }

        if (chosenCab == null) {
            System.out.println(" No cab available for " + seats + " passengers.");
            return;
        }

        Passenger p = new Passenger(name, contact, pickup, drop, seats);
        p.assignedCab = chosenCab;
        passengers.add(p);
        chosenCab.isAvailable = false;

        System.out.println("\n Ride booked successfully!");
        System.out.println("Passenger: " + name);
        System.out.println("Pickup: " + pickup + " ➜ Drop: " + drop);
        System.out.println("Seats Required: " + seats);
        System.out.println("\n--- Assigned Cab ---");
        chosenCab.showCabDetails();
    }

    public void cancelRide() {
        sc.nextLine();
        System.out.print("Enter Passenger Name to Cancel Ride: ");
        String name = sc.nextLine();

        for (Passenger p : passengers) {
            if (p.name.equalsIgnoreCase(name) && p.assignedCab != null) {
                p.assignedCab.isAvailable = true;
                System.out.println(" Ride cancelled successfully for " + name);
                return;
            }
        }
        System.out.println(" No active ride found for this passenger.");
    }

    public void viewRideHistory() {
        sc.nextLine();
        System.out.print("Enter Passenger Name to View History: ");
        String name = sc.nextLine();

        boolean found = false;
        for (Passenger p : passengers) {
            if (p.name.equalsIgnoreCase(name)) {
                found = true;
                System.out.println("\n--- Ride History ---");
                System.out.println("Passenger: " + p.name);
                System.out.println("Pickup: " + p.pickup + " ➜ Drop: " + p.drop);
                System.out.println("Seats: " + p.seatsRequired);
                p.assignedCab.showCabDetails();
            }
        }
        if (!found) System.out.println(" No ride history found for this passenger.");
    }
}


public class CabHeroMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Cab> cabs = new ArrayList<>();
        List<Passenger> passengers = new ArrayList<>();

        Admin admin = new Admin(cabs);
        PassengerModule passengerModule = new PassengerModule(cabs, passengers);

        int choice;
        do {
            System.out.println("\n=========CabHero SYSTEM =========");
            System.out.println("1. Admin Module");
            System.out.println("2. Passenger Module");
            System.out.println("3.Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- ADMIN MODULE ---");
                    System.out.println("1. Add Cabs");
                    System.out.println("2. View All Cabs");
                    System.out.print("Enter choice: ");
                    int adminChoice = sc.nextInt();
                    if (adminChoice == 1) admin.addCabs();
                    else if (adminChoice == 2) admin.viewAllCabs();
                }
                case 2 -> {
                    System.out.println("\n--- PASSENGER MODULE ---");
                    System.out.println("1. Book Ride");
                    System.out.println("2. Cancel Ride");
                    System.out.println("3. View Ride History");
                    System.out.print("Enter choice: ");
                    int passChoice = sc.nextInt();
                    if (passChoice == 1) passengerModule.bookRide();
                    else if (passChoice == 2) passengerModule.cancelRide();
                    else if (passChoice == 3) passengerModule.viewRideHistory();
                }
                case 3 -> System.out.println("\n Thank you for using Uber System!");
                default -> System.out.println(" Invalid choice. Try again.");
            }
        } while (choice != 3);
    }
}
