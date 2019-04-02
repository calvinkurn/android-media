package com.tokopedia.flight.search.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightFilterDepartureTimeViewHolder;
import com.tokopedia.flight.search.presentation.model.resultstatistics.DepartureStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterDepartureTimeAdapterTypeFactory extends BaseAdapterTypeFactory implements BaseListCheckableTypeFactory<DepartureStat> {
    private BaseCheckableViewHolder.CheckableInteractionListener interactionListener;

    public FlightFilterDepartureTimeAdapterTypeFactory(BaseCheckableViewHolder.CheckableInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public int type(DepartureStat viewModel) {
        return FlightFilterDepartureTimeViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightFilterDepartureTimeViewHolder.LAYOUT) {
            return new FlightFilterDepartureTimeViewHolder(parent, interactionListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
