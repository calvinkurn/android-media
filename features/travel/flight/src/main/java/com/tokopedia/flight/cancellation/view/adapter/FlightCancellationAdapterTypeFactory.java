package com.tokopedia.flight.cancellation.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightCancellationViewHolder;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationModel;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationAdapterTypeFactory extends BaseAdapterTypeFactory
        implements FlightCancellationTypeFactory {

    private FlightCancellationViewHolder.FlightCancellationListener flightCancellationListener;

    public FlightCancellationAdapterTypeFactory(FlightCancellationViewHolder.FlightCancellationListener flightCancellationListener) {
        this.flightCancellationListener = flightCancellationListener;
    }

    @Override
    public int type(FlightCancellationModel viewModel) {
        return FlightCancellationViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder = null;
        if (type == FlightCancellationViewHolder.LAYOUT) {
            viewHolder = new FlightCancellationViewHolder(parent, flightCancellationListener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
