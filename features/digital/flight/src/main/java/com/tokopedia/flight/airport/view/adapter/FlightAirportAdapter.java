package com.tokopedia.flight.airport.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;

import java.util.List;

public class FlightAirportAdapter extends BaseAdapter<FlightAirportAdapterTypeFactory> {

    public FlightAirportAdapter(FlightAirportAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }
}
