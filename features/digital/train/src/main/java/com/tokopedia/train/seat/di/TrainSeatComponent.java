package com.tokopedia.train.seat.di;

import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.seat.presentation.fragment.TrainSeatFragment;
import com.tokopedia.train.seat.presentation.fragment.TrainWagonFragment;

import dagger.Component;

@TrainSeatScope
@Component(modules = TrainSeatModule.class, dependencies = TrainComponent.class)
public interface TrainSeatComponent {

    void inject(TrainWagonFragment trainSeatFragment);

    void inject(TrainSeatFragment trainSeatFragment);
}
