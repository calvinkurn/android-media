package com.tokopedia.flight.searchV2.presentation.adapter.viewholder;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.searchV2.presentation.adapter.FlightSearchAdapterTypeFactory;
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchSeeAllResultViewModel;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchSeeAllViewHolder extends AbstractViewHolder<FlightSearchSeeAllResultViewModel> {

    public static final int LAYOUT = R.layout.item_flight_search_selengkapnya;

    private FlightSearchAdapterTypeFactory.OnFlightSearchListener onFlightSearchListener;

    public FlightSearchSeeAllViewHolder(View itemView, FlightSearchAdapterTypeFactory.OnFlightSearchListener onFlightSearchListener) {
        super(itemView);

        this.onFlightSearchListener = onFlightSearchListener;
    }

    @Override
    public void bind(FlightSearchSeeAllResultViewModel element) {

    }
}
