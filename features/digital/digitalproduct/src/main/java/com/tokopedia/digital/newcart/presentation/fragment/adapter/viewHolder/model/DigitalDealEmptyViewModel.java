package com.tokopedia.digital.newcart.presentation.fragment.adapter.viewHolder.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.digital.newcart.presentation.fragment.adapter.DigitalDealsAdapterTypeFactory;

public class DigitalDealEmptyViewModel implements Visitable<DigitalDealsAdapterTypeFactory> {
    @Override
    public int type(DigitalDealsAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
