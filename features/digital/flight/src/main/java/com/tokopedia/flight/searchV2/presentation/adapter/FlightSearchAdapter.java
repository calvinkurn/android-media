package com.tokopedia.flight.searchV2.presentation.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;

import java.util.List;

/**
 * @author by furqan on 02/10/18.
 */

public class FlightSearchAdapter extends BaseAdapter<FlightSearchAdapterTypeFactory> {

    public FlightSearchAdapter(FlightSearchAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

}
