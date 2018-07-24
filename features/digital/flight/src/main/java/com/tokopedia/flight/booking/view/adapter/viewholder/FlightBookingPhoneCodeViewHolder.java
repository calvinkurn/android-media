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

public class FlightBookingPhoneCodeViewHolder extends AbstractViewHolder<FlightBookingPhoneCodeViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_booking_phone_code;

    private TextView countryPhonCode;
    private TextView countryName;

    public FlightBookingPhoneCodeViewHolder(View itemView) {
        super(itemView);
        countryPhonCode = (TextView) itemView.findViewById(R.id.country_phone_code);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
    }

    @Override
    public void bind(FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel) {
        countryPhonCode.setText(itemView.getContext().getString(R.string.flight_booking_phone_code_label, flightBookingPhoneCodeViewModel.getCountryPhoneCode()));
        countryName.setText(flightBookingPhoneCodeViewModel.getCountryName());
    }
}
