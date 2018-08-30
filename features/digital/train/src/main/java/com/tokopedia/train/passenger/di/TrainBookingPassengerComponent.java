package com.tokopedia.train.passenger.di;

import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.common.util.TrainFlowUtil;
import com.tokopedia.train.passenger.presentation.fragment.TrainBookingPassengerFragment;

import dagger.Component;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
@TrainBookingPassengerScope
@Component(modules = TrainBookingPassengerModule.class, dependencies = TrainComponent.class)
public interface TrainBookingPassengerComponent {

    TrainFlowUtil trainFlowUtil();

    void inject(TrainBookingPassengerFragment trainBookingPassengerFragment);

}
