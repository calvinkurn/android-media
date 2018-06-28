package com.tokopedia.train.passenger.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.train.passenger.viewmodel.TrainPassengerViewModel;

/**
 * Created by nabillasabbaha on 25/06/18.
 */
public interface TrainBookingPassengerTypeFactory {

    int type(TrainPassengerViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
