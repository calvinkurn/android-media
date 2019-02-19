package com.tokopedia.flight.search.presentation.adapter.viewholder;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
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
    private AppCompatTextView tvBestPairingDesc;

    public FlightSearchSeeOnlyBestPairingViewHolder(View itemView, FlightSearchAdapterTypeFactory.OnFlightSearchListener onFlightSearchListener) {
        super(itemView);

        tvBestPairingDesc = itemView.findViewById(R.id.tv_flight_search_best_pairing_desc);
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
        tvBestPairingDesc.setText(Html.fromHtml(
                getString(R.string.flight_search_show_best_pairing_desc, element.getNewPrice())));
    }
}
