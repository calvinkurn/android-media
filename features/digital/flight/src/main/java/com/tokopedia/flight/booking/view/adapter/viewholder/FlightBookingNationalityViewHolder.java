package com.tokopedia.flight.booking.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingNationalityViewHolder extends AbstractViewHolder<FlightBookingPhoneCodeViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_booking_nationality;

    private TextView countryName;

    @Override
    public void bind(FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
        countryName.setText(flightBookingPhoneCodeViewModel.getCountryName());
    }

    public FlightBookingNationalityViewHolder(View layoutView) {
        super(layoutView);
        countryName = (TextView) layoutView.findViewById(R.id.country_name);
    }
}
