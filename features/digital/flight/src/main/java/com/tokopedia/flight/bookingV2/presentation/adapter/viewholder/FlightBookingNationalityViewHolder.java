package com.tokopedia.flight.bookingV2.presentation.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingNationalityViewHolder extends AbstractViewHolder<CountryPhoneCode> {
    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_booking_nationality;

    private TextView countryName;

    @Override
    public void bind(CountryPhoneCode phoneCode) {
        countryName.setText(phoneCode.getCountryName());
    }

    public FlightBookingNationalityViewHolder(View layoutView) {
        super(layoutView);
        countryName = (TextView) layoutView.findViewById(com.tokopedia.flight.R.id.country_name);
    }
}
