package com.tokopedia.flight.dashboard.view.fragment.viewmodel;

import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;

/**
 * Created by alvarisi on 2/7/18.
 */

public class FlightDashboardAirportAndClassWrapper {
    private FlightAirportViewModel departureAirport;
    private FlightAirportViewModel arrivalAirport;
    private FlightClassEntity flightClassEntity;

    public FlightDashboardAirportAndClassWrapper() {
    }

    public FlightAirportViewModel getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(FlightAirportViewModel departureAirport) {
        this.departureAirport = departureAirport;
    }

    public FlightAirportViewModel getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(FlightAirportViewModel arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public FlightClassEntity getFlightClassEntity() {
        return flightClassEntity;
    }

    public void setFlightClassEntity(FlightClassEntity flightClassEntity) {
        this.flightClassEntity = flightClassEntity;
    }
}
