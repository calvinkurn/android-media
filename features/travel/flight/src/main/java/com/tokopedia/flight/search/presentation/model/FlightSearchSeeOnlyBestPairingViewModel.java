package com.tokopedia.flight.search.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchSeeOnlyBestPairingViewModel implements Visitable<FlightSearchAdapterTypeFactory> {

    private String newPrice;

    public FlightSearchSeeOnlyBestPairingViewModel(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    @Override
    public int type(FlightSearchAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
