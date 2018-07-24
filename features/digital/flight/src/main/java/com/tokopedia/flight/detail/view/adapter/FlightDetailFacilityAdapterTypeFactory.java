package com.tokopedia.flight.detail.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;

/**
 * @author by alvarisi on 12/19/17.
 */

public class FlightDetailFacilityAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightDetailRouteTypeFactory {
    public FlightDetailFacilityAdapterTypeFactory() {
    }

    public int type(FlightDetailRouteViewModel viewModel) {
        return FlightDetailFacilityViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightDetailFacilityViewHolder.LAYOUT) {
            return new FlightDetailFacilityViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
