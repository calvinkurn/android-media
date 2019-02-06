package com.tokopedia.flight.cancellation.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightCancellationReasonViewHolder;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonViewModel;

/**
 * @author by furqan on 30/10/18.
 */

public class FlightCancellationReasonAdapterTypeFactory extends BaseAdapterTypeFactory {

    FlightCancellationReasonViewHolder.ReasonListener reasonListener;

    public FlightCancellationReasonAdapterTypeFactory(FlightCancellationReasonViewHolder.ReasonListener reasonListener) {
        this.reasonListener = reasonListener;
    }

    public int type(FlightCancellationReasonViewModel viewModel) {
        return FlightCancellationReasonViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightCancellationReasonViewHolder.LAYOUT) {
            return new FlightCancellationReasonViewHolder(parent, reasonListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
