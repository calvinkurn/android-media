package com.tokopedia.flight.dashboard.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.dashboard.view.adapter.viewholder.FlightClassViewHolder;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;

/**
 * @author by alvarisi on 12/18/17.
 */

public class FlightClassesAdapterTypeFactory extends BaseAdapterTypeFactory {
    private FlightClassViewHolder.ListenerCheckedClass listenerCheckedClass;

    public FlightClassesAdapterTypeFactory(FlightClassViewHolder.ListenerCheckedClass listenerCheckedClass) {
        this.listenerCheckedClass = listenerCheckedClass;
    }

    public int type(FlightClassViewModel viewModel) {
        return FlightClassViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightClassViewHolder.LAYOUT) {
            return new FlightClassViewHolder(parent, listenerCheckedClass);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
