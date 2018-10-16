package com.tokopedia.flight.searchV2.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.searchV2.presentation.adapter.FlightSearchAdapterTypeFactory;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchSeeAllResultViewModel implements Visitable<FlightSearchAdapterTypeFactory> {
    @Override
    public int type(FlightSearchAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
