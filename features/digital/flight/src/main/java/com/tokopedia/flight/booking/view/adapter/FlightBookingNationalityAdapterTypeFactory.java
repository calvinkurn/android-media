package com.tokopedia.flight.booking.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.common.travel.presentation.adapter.PhoneCodePickerAdapterTypeFactory;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingNationalityViewHolder;
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode;

/**
 * Created by alvarisi on 12/19/17.
 */

public class FlightBookingNationalityAdapterTypeFactory extends PhoneCodePickerAdapterTypeFactory {
    public FlightBookingNationalityAdapterTypeFactory() {
    }

    @Override
    public int type(CountryPhoneCode viewModel) {
        return FlightBookingNationalityViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (FlightBookingNationalityViewHolder.LAYOUT == type) {
            return new FlightBookingNationalityViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
