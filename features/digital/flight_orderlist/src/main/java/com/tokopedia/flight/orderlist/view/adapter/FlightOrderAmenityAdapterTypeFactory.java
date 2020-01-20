package com.tokopedia.flight.orderlist.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.orderlist.view.adapter.viewholder.FlightOrderAmenityViewHolder;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderAmenityViewModel;

/**
 * Created by alvarisi on 12/19/17.
 */

public class FlightOrderAmenityAdapterTypeFactory extends BaseAdapterTypeFactory {
    private FlightOrderAmenityViewHolder.ListenerCheckedLuggage listenerCheckedClass;

    public FlightOrderAmenityAdapterTypeFactory(FlightOrderAmenityViewHolder.ListenerCheckedLuggage listenerCheckedClass) {
        this.listenerCheckedClass = listenerCheckedClass;
    }

    public int type(FlightOrderAmenityViewModel viewModel) {
        return FlightOrderAmenityViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightOrderAmenityViewHolder.LAYOUT) {
            return new FlightOrderAmenityViewHolder(parent, listenerCheckedClass);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
