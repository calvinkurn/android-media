package com.tokopedia.digital.newcart.presentation.fragment.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;

import java.util.List;

public class DigitalDealsAdapter extends BaseAdapter<DigitalDealsAdapterTypeFactory> {

    public DigitalDealsAdapter(DigitalDealsAdapterTypeFactory adapterTypeFactory,
                               List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }
}
