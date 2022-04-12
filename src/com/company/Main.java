package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\JovanaHristova\\Documents\\Employees.csv"));
        String line = "";

        List<Employee> employeeList = new ArrayList<>();
        while ((line = br.readLine()) != null)
        {
            String[] employee = line.split(",");

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            Employee e = new Employee(Long.valueOf(employee[0]),Long.valueOf(employee[1]), LocalDate.parse(employee[2], dateFormat), employee[3].equals("NULL") ? LocalDate.now() : LocalDate.parse(employee[3], dateFormat));
            employeeList.add(e);
        }

        Map<Long, List<Employee>> employeeMap = employeeList.stream().collect(groupingBy(Employee::getProjectId));

        for(List<Employee> emp : employeeMap.values()){

            Long max = Long.valueOf(0);
            Employee employee1 = null;
            Employee employee2 = null;
            for(int i = 0; i < emp.size()-1; i++){
                for(int j=i+1; j<emp.size(); j++){
                    if(workingTogether(emp.get(i), emp.get(j))>=max){
                        max = workingTogether(emp.get(i), emp.get(j));
                        employee1 = emp.get(i);
                        employee2 = emp.get(j);
                    }
                }
            }

            System.out.println("Employee1 ID: "
                    + employee1.getEmployeeId()
                    + ", Employee1 ID: "
                    + employee2.getEmployeeId()
                    + ", Project ID: "
                    + employee1.getProjectId()
                    + ", Working Hours: "
                    + max);
        }
    }

    public static Long workingTogether(Employee employee1, Employee employee2){
        LocalDate from = employee1.dateFrom.isBefore(employee2.dateFrom) ? employee2.dateFrom : employee1.dateFrom;
        LocalDate to = employee1.dateTo.isBefore(employee2.dateTo) ? employee1.dateTo : employee2.dateTo;
        if(to.isAfter(from) || to.isEqual(from))
            return Math.abs(ChronoUnit.DAYS.between(from, to));
        return  Long.valueOf(0);
    }


}
