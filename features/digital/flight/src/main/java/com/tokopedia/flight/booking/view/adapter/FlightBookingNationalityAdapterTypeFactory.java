package com.tokopedia.flight.booking.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingNationalityViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

/**
 * Created by alvarisi on 12/19/17.
 */

public class FlightBookingNationalityAdapterTypeFactory extends FlightBookingPhoneCodeAdapterTypeFactory {
    public FlightBookingNationalityAdapterTypeFactory() {
    }

    @Override
    public int type(FlightBookingPhoneCodeViewModel viewModel) {
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
