package com.tokopedia.flight.cancellation.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightCancellationListViewHolder;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListModel;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListAdapterTypeFactory extends BaseAdapterTypeFactory
        implements FlightCancellationListTypeFactory {

    public FlightCancellationListAdapterTypeFactory() {
    }

    @Override
    public int type(FlightCancellationListModel viewModel) {
        return FlightCancellationListViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder = null;
        if (type == FlightCancellationListViewHolder.LAYOUT) {
            viewHolder = new FlightCancellationListViewHolder(parent);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
