package com.tokopedia.flight.search.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightFilterAirlineViewHolder;
import com.tokopedia.flight.search.presentation.model.resultstatistics.AirlineStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterAirlineAdapterTypeFactory extends BaseAdapterTypeFactory implements BaseListCheckableTypeFactory<AirlineStat> {
    private BaseCheckableViewHolder.CheckableInteractionListener interactionListener;

    public FlightFilterAirlineAdapterTypeFactory(BaseCheckableViewHolder.CheckableInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public int type(AirlineStat viewModel) {
        return FlightFilterAirlineViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightFilterAirlineViewHolder.LAYOUT) {
            return new FlightFilterAirlineViewHolder(parent, interactionListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
