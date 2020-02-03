package com.tokopedia.flight.bookingV2.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.bookingV2.presentation.adapter.viewholder.FlightBookingPassengerViewHolder;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightBookingPassengerViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public class FlightBookingPassengerAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightBookingPassengerTypeFactory {
    private FlightBookingPassengerActionListener listener;

    public FlightBookingPassengerAdapterTypeFactory(FlightBookingPassengerActionListener listener) {
        this.listener = listener;
    }

    @Override
    public int type(FlightBookingPassengerViewModel viewModel) {
        return FlightBookingPassengerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder = null;
        if (type == FlightBookingPassengerViewHolder.LAYOUT) {
            viewHolder = new FlightBookingPassengerViewHolder(parent, listener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
