package com.tokopedia.digital.newcart.presentation.fragment.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.fragment.DigitalDealCheckoutFragment;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.viewHolder.DigitalDealViewHolder;

public class DigitalDealsAdapterTypeFactory extends BaseAdapterTypeFactory {
    private DigitalDealActionListener actionListener;
    private boolean insideCheckoutPage;

    public DigitalDealsAdapterTypeFactory(DigitalDealActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public DigitalDealsAdapterTypeFactory(DigitalDealActionListener actionListener, boolean insideCheckoutPage) {
        this.actionListener = actionListener;
        this.insideCheckoutPage = insideCheckoutPage;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == DigitalDealViewHolder.LAYOUT) {
            return new DigitalDealViewHolder(parent, actionListener, insideCheckoutPage);
        }
        return super.createViewHolder(parent, type);
    }

    public int type(DealProductViewModel dealProductViewModel) {
        return DigitalDealViewHolder.LAYOUT;
    }
}
