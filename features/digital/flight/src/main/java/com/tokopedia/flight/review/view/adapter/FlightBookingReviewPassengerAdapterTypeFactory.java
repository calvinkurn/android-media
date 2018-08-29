package com.tokopedia.flight.review.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.review.view.adapter.viewholder.FlightBookingReviewPassengerViewHolder;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;

/**
 * Created by alvarisi on 12/22/17.
 */

public class FlightBookingReviewPassengerAdapterTypeFactory extends BaseAdapterTypeFactory {

    public FlightBookingReviewPassengerAdapterTypeFactory() {
    }

    public int type(FlightDetailPassenger flightDetailPassenger) {
        return FlightBookingReviewPassengerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightBookingReviewPassengerViewHolder.LAYOUT) {
            return new FlightBookingReviewPassengerViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
