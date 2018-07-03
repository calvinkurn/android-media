package com.tokopedia.train.passenger.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.train.passenger.presentation.viewmodel.TrainPassengerViewModel;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public class TrainBookingPassengerAdapterTypeFactory extends BaseAdapterTypeFactory
        implements TrainBookingPassengerTypeFactory {

    private TrainBookingPassengerAdapterListener listener;

    public TrainBookingPassengerAdapterTypeFactory(TrainBookingPassengerAdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public int type(TrainPassengerViewModel viewModel) {
        return TrainPassengerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder = null;
        if (type == TrainPassengerViewHolder.LAYOUT) {
            return new TrainPassengerViewHolder(parent, listener);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
