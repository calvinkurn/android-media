package com.tokopedia.train.station.presentation.adapter.viewholder.listener;

import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationCityViewModel;
import com.tokopedia.train.station.presentation.adapter.viewmodel.TrainStationViewModel;

/**
 * @author by alvarisi on 3/8/18.
 */

public interface TrainStationActionListener {
    void onStationClicked(TrainStationViewModel viewModel);

    void onCityClicked(TrainStationCityViewModel element);
}
