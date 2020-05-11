package com.tokopedia.flight.dashboard.view.fragment.model;

import com.tokopedia.flight.airport.view.model.FlightAirportModel;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;

/**
 * Created by alvarisi on 2/7/18.
 */

public class FlightDashboardAirportAndClassWrapper {
    private FlightAirportModel departureAirport;
    private FlightAirportModel arrivalAirport;
    private FlightClassEntity flightClassEntity;

    public FlightDashboardAirportAndClassWrapper() {
    }

    public FlightAirportModel getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(FlightAirportModel departureAirport) {
        this.departureAirport = departureAirport;
    }

    public FlightAirportModel getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(FlightAirportModel arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public FlightClassEntity getFlightClassEntity() {
        return flightClassEntity;
    }

    public void setFlightClassEntity(FlightClassEntity flightClassEntity) {
        this.flightClassEntity = flightClassEntity;
    }
}
