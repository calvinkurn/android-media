package com.tokopedia.train.seat.presentation.fragment.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;

import java.util.List;

public class TrainSeatAdapter extends BaseAdapter<TrainSeatAdapterTypeFactory> {
    public TrainSeatAdapter(TrainSeatAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

    public TrainSeatAdapter(TrainSeatAdapterTypeFactory adapterTypeFactory) {
        super(adapterTypeFactory);
    }
}
