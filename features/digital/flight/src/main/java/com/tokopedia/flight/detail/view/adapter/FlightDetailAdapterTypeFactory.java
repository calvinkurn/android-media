package com.tokopedia.flight.detail.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;

/**
 * @author by alvarisi on 12/21/17.
 */

public class FlightDetailAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightDetailRouteTypeFactory {
    public interface OnFlightDetailListener {
        int getItemCount();
    }

    private OnFlightDetailListener onFlightDetailListener;

    public FlightDetailAdapterTypeFactory(OnFlightDetailListener onFlightDetailListener) {
        this.onFlightDetailListener = onFlightDetailListener;
    }

    @Override
    public int type(FlightDetailRouteViewModel viewModel) {
        return FlightDetailViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightDetailViewHolder.LAYOUT) {
            return new FlightDetailViewHolder(parent, onFlightDetailListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
