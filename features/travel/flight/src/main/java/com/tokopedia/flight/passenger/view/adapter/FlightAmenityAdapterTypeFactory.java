package com.tokopedia.flight.passenger.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightBookingAmenityViewHolder;
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel;

/**
 * Created by alvarisi on 12/19/17.
 */

public class FlightAmenityAdapterTypeFactory extends BaseAdapterTypeFactory {
    private FlightBookingAmenityViewHolder.ListenerCheckedLuggage listenerCheckedClass;

    public FlightAmenityAdapterTypeFactory(FlightBookingAmenityViewHolder.ListenerCheckedLuggage listenerCheckedClass) {
        this.listenerCheckedClass = listenerCheckedClass;
    }

    public int type(FlightBookingAmenityModel viewModel) {
        return FlightBookingAmenityViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightBookingAmenityViewHolder.LAYOUT) {
            return new FlightBookingAmenityViewHolder(parent, listenerCheckedClass);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
