package com.tokopedia.flight.search.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightFilterTransitViewHolder;
import com.tokopedia.flight.search.presentation.model.resultstatistics.TransitStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterTransitAdapterTypeFactory extends BaseAdapterTypeFactory implements BaseListCheckableTypeFactory<TransitStat> {
    private BaseCheckableViewHolder.CheckableInteractionListener interactionListener;

    public FlightFilterTransitAdapterTypeFactory(BaseCheckableViewHolder.CheckableInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public int type(TransitStat viewModel) {
        return FlightFilterTransitViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightFilterTransitViewHolder.LAYOUT) {
            return new FlightFilterTransitViewHolder(parent, interactionListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
