package com.tokopedia.flight.detail.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.detail.view.adapter.viewholder.FlightDetailOrderPassengerViewHolder;
import com.tokopedia.flight.detail.view.model.FlightDetailPassenger;

/**
 * Created by alvarisi on 12/22/17.
 */

public class FlightDetailOrderPassengerAdapterTypeFactory extends BaseAdapterTypeFactory {

    public FlightDetailOrderPassengerAdapterTypeFactory() {
    }

    public int type(FlightDetailPassenger flightDetailPassenger) {
        return FlightDetailOrderPassengerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightDetailOrderPassengerViewHolder.LAYOUT) {
            return new FlightDetailOrderPassengerViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
