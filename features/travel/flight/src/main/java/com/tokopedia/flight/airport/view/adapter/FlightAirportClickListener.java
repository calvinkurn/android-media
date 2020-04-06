package com.tokopedia.flight.airport.view.adapter;

import com.tokopedia.flight.airport.view.model.FlightAirportModel;

public interface FlightAirportClickListener {
    void airportClicked(FlightAirportModel airportViewModel);

    String getFilterText();
}
