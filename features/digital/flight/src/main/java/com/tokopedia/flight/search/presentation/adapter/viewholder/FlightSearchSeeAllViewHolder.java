package com.tokopedia.flight.search.presentation.adapter.viewholder;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory;
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeAllResultViewModel;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchSeeAllViewHolder extends AbstractViewHolder<FlightSearchSeeAllResultViewModel> {

    public static final int LAYOUT = R.layout.item_flight_search_selengkapnya;

    private ButtonCompat btnShowAllResult;

    public FlightSearchSeeAllViewHolder(View itemView, FlightSearchAdapterTypeFactory.OnFlightSearchListener onFlightSearchListener) {
        super(itemView);

        btnShowAllResult = itemView.findViewById(R.id.btn_show_all_result);
        btnShowAllResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFlightSearchListener.onShowAllClicked();
            }
        });
    }

    @Override
    public void bind(FlightSearchSeeAllResultViewModel element) {

    }
}
