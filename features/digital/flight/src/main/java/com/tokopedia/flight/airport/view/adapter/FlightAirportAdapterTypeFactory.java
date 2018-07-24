package com.tokopedia.flight.airport.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.airport.view.viewmodel.FlightCountryAirportViewModel;

/**
 * Created by alvarisi on 12/19/17.
 */

public class FlightAirportAdapterTypeFactory extends BaseAdapterTypeFactory {
    private FlightAirportClickListener flightAirportClickListener;

    public FlightAirportAdapterTypeFactory(FlightAirportClickListener flightAirportClickListener) {
        this.flightAirportClickListener = flightAirportClickListener;
    }

    public int type(FlightAirportViewModel viewModel) {
        return FlightAirportViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightCountryViewHolder.LAYOUT) {
            return new FlightCountryViewHolder(parent);
        } else if (type == FlightAirportViewHolder.LAYOUT) {
            return new FlightAirportViewHolder(parent, flightAirportClickListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    public int type(FlightCountryAirportViewModel flightCountryAirportViewModel) {
        return FlightCountryViewHolder.LAYOUT;
    }
}
