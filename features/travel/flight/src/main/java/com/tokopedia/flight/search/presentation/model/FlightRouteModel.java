package com.tokopedia.flight.search.presentation.model;

/**
 * Created by Rizky on 02/10/18.
 */
public class FlightRouteModel {
    private String departure;
    private String arrival;
    private String date;

    public FlightRouteModel(String departure, String arrival, String date) {
        this.departure = departure;
        this.arrival = arrival;
        this.date = date;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDate() {
        return date;
    }
}
