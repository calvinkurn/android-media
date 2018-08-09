package com.tokopedia.train.station.presentation.adapter.viewholder;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainAllStationsViewModel;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainAllStationsViewHolder extends AbstractViewHolder<TrainAllStationsViewModel> {
    public static final int LAYOUT = R.layout.view_train_all_station_item;

    public TrainAllStationsViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(TrainAllStationsViewModel element) {
    }
}
