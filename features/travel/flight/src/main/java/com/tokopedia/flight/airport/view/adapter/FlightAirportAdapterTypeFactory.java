package com.tokopedia.flight.airport.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.airport.view.model.FlightAirportModel;
import com.tokopedia.flight.airport.view.model.FlightCountryAirportModel;
import com.tokopedia.flight.common.view.model.EmptyResultModel;
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder;

/**
 * Created by alvarisi on 12/19/17.
 */

public class FlightAirportAdapterTypeFactory extends BaseAdapterTypeFactory {
    private FlightAirportClickListener flightAirportClickListener;

    public FlightAirportAdapterTypeFactory(FlightAirportClickListener flightAirportClickListener) {
        this.flightAirportClickListener = flightAirportClickListener;
    }

    public int type(FlightAirportModel viewModel) {
        return FlightAirportViewHolder.LAYOUT;
    }

    public int type(EmptyResultModel emptyResultModel) {
        return EmptyResultViewHolder.Companion.getLAYOUT();
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightCountryViewHolder.LAYOUT) {
            return new FlightCountryViewHolder(parent);
        } else if (type == FlightAirportViewHolder.LAYOUT) {
            return new FlightAirportViewHolder(parent, flightAirportClickListener);
        } else if (type == EmptyResultViewHolder.Companion.getLAYOUT()) {
            return new EmptyResultViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    public int type(FlightCountryAirportModel flightCountryAirportModel) {
        return FlightCountryViewHolder.LAYOUT;
    }
}
