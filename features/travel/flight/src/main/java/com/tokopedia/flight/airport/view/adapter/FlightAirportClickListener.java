package com.tokopedia.flight.airport.view.adapter;

import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;

public interface FlightAirportClickListener {
    void airportClicked(FlightAirportViewModel airportViewModel);

    String getFilterText();
}
