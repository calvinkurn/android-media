package com.tokopedia.flight.cancellation.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightReviewCancellationViewHolder;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationModel;

/**
 * @author by furqan on 29/03/18.
 */

public class FlightReviewCancellationAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightCancellationTypeFactory {
    @Override
    public int type(FlightCancellationModel viewModel) {
        return FlightReviewCancellationViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder = null;
        if (type == FlightReviewCancellationViewHolder.LAYOUT) {
            viewHolder = new FlightReviewCancellationViewHolder(parent);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
