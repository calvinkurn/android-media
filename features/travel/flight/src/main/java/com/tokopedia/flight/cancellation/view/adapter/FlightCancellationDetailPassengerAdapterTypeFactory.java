package com.tokopedia.flight.cancellation.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightCancellationDetailPassengerViewHolder;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListPassengerModel;

/**
 * @author by furqan on 04/05/18.
 */

public class FlightCancellationDetailPassengerAdapterTypeFactory extends BaseAdapterTypeFactory {

    public FlightCancellationDetailPassengerAdapterTypeFactory() {
    }

    public int type(FlightCancellationListPassengerModel flightCancellationListPassengerViewModel) {
        return FlightCancellationDetailPassengerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightCancellationDetailPassengerViewHolder.LAYOUT) {
            return new FlightCancellationDetailPassengerViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
