package com.tokopedia.train.passenger.presentation.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;

import java.util.List;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public class TrainBookingPassengerAdapter extends BaseAdapter<TrainBookingPassengerAdapterTypeFactory> {

    public TrainBookingPassengerAdapter(TrainBookingPassengerAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }
}
