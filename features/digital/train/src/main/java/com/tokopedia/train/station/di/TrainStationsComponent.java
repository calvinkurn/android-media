package com.tokopedia.train.station.di;

import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.station.presentation.TrainStationsFragment;

import dagger.Component;

/**
 * Created by alvarisi on 3/5/18.
 */
@TrainStationsScope
@Component(modules = TrainStationModule.class, dependencies = TrainComponent.class)
public interface TrainStationsComponent {
    void inject(TrainStationsFragment trainStationsFragment);
}
