package com.tokopedia.flight.passenger.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingNewPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightPassengerListViewHolder;
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightPassengerNewViewHolder;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightPassengerListAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightPassengerListTypeFactory {

    private FlightPassengerListViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger;
    private FlightPassengerNewViewHolder.ListenerClickedNewPassenger listenerClickedNewPassenger;

    public FlightPassengerListAdapterTypeFactory(FlightPassengerListViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger,
                                                 FlightPassengerNewViewHolder.ListenerClickedNewPassenger listenerClickedNewPassenger) {
        this.listenerCheckedSavedPassenger = listenerCheckedSavedPassenger;
        this.listenerClickedNewPassenger = listenerClickedNewPassenger;
    }

    @Override
    public int type(FlightBookingPassengerViewModel viewModel) {
        return FlightPassengerListViewHolder.LAYOUT;
    }

    @Override
    public int type(FlightBookingNewPassengerViewModel viewModel) {
        return FlightPassengerNewViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder = null;
        if (type == FlightPassengerListViewHolder.LAYOUT) {
            viewHolder = new FlightPassengerListViewHolder(parent, listenerCheckedSavedPassenger);
        } else if(type == FlightPassengerNewViewHolder.LAYOUT) {
            viewHolder = new FlightPassengerNewViewHolder(parent, listenerClickedNewPassenger);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
