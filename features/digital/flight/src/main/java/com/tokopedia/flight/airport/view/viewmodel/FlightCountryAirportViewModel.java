package com.tokopedia.flight.airport.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.airport.view.adapter.FlightAirportAdapterTypeFactory;

import java.util.List;

public class FlightCountryAirportViewModel implements Visitable<FlightAirportAdapterTypeFactory>{
    private String countryId;
    private String countryName;
    private List<FlightAirportViewModel> airports;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<FlightAirportViewModel> getAirports() {
        return airports;
    }

    public void setAirports(List<FlightAirportViewModel> airports) {
        this.airports = airports;
    }

    @Override
    public int type(FlightAirportAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
}
