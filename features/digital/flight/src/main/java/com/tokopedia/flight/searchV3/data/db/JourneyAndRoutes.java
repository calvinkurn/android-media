package com.tokopedia.flight.searchV3.data.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * Created by Rizky on 01/10/18.
 */
public class JourneyAndRoutes {

    @Embedded
    private FlightJourneyTable flightJourneyTable;

    @Relation(parentColumn = "id",
            entityColumn = "journeyId")
    private List<FlightRouteTable> routes;

    public JourneyAndRoutes() {
    }

    public JourneyAndRoutes(FlightJourneyTable flightJourneyTable, List<FlightRouteTable> routes) {
        this.flightJourneyTable = flightJourneyTable;
        this.routes = routes;
    }

    public void setFlightJourneyTable(FlightJourneyTable flightJourneyTable) {
        this.flightJourneyTable = flightJourneyTable;
    }

    public void setRoutes(List<FlightRouteTable> routes) {
        this.routes = routes;
    }

    public FlightJourneyTable getFlightJourneyTable() {
        return flightJourneyTable;
    }

    public List<FlightRouteTable> getRoutes() {
        return routes;
    }

}
