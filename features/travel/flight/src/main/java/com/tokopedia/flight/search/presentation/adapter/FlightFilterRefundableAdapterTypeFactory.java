package com.tokopedia.flight.search.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.search.presentation.adapter.viewholder.FlightFilterRefundableViewHolder;
import com.tokopedia.flight.search.presentation.model.resultstatistics.RefundableStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterRefundableAdapterTypeFactory extends BaseAdapterTypeFactory implements BaseListCheckableTypeFactory<RefundableStat> {
    private BaseCheckableViewHolder.CheckableInteractionListener interactionListener;

    public FlightFilterRefundableAdapterTypeFactory(BaseCheckableViewHolder.CheckableInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public int type(RefundableStat viewModel) {
        return FlightFilterRefundableViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightFilterRefundableViewHolder.LAYOUT) {
            return new FlightFilterRefundableViewHolder(parent, interactionListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
