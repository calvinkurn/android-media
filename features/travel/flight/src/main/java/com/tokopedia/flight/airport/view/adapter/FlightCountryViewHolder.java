package com.tokopedia.flight.airport.view.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.airport.view.model.FlightCountryAirportModel;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightCountryViewHolder extends AbstractViewHolder<FlightCountryAirportModel> {
    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_country;

    private TextView countryTextView;

    public FlightCountryViewHolder(View itemView) {
        super(itemView);
        countryTextView = (TextView) itemView.findViewById(com.tokopedia.flight.R.id.country);
    }

    @Override
    public void bind(FlightCountryAirportModel country) {
        countryTextView.setText(country.getCountryName().toUpperCase());
    }
}
