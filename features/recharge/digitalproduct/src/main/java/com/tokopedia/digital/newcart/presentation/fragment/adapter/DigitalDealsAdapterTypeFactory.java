package com.tokopedia.digital.newcart.presentation.fragment.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.viewHolder.DigitalDealEmptyViewHolder;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.viewHolder.DigitalDealViewHolder;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.viewHolder.model.DigitalDealEmptyViewModel;

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
        } else if (type == DigitalDealEmptyViewHolder.LAYOUT) {
            return new DigitalDealEmptyViewHolder(parent);
        }
        return super.createViewHolder(parent, type);
    }

    public int type(DealProductViewModel dealProductViewModel) {
        return DigitalDealViewHolder.LAYOUT;
    }

    public int type(DigitalDealEmptyViewModel digitalDealEmptyViewModel) {
        return DigitalDealEmptyViewHolder.LAYOUT;
    }
}
