package com.tokopedia.flight.search.presentation.adapter.viewholder;

import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory;
import com.tokopedia.flight.search.presentation.model.FlightSearchSeeOnlyBestPairingViewModel;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightSearchSeeOnlyBestPairingViewHolder extends AbstractViewHolder<FlightSearchSeeOnlyBestPairingViewModel> {

    public static final int LAYOUT = R.layout.item_flight_search_see_best_pairing;

    private AppCompatButton btnShowBestPairing;

    public FlightSearchSeeOnlyBestPairingViewHolder(View itemView, FlightSearchAdapterTypeFactory.OnFlightSearchListener onFlightSearchListener) {
        super(itemView);

        btnShowBestPairing = itemView.findViewById(R.id.btn_show_best_pairing);
        btnShowBestPairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFlightSearchListener.onShowBestPairingClicked();
            }
        });
    }

    @Override
    public void bind(FlightSearchSeeOnlyBestPairingViewModel element) {

    }
}
