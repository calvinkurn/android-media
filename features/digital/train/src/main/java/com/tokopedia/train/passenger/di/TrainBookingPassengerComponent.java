package com.tokopedia.train.passenger.di;

import com.tokopedia.train.common.di.TrainComponent;
import com.tokopedia.train.passenger.presentation.fragment.TrainBookingAddPassengerFragment;
import com.tokopedia.train.passenger.presentation.fragment.TrainBookingPassengerFragment;

import dagger.Component;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
@TrainBookingPassengerScope
@Component(modules = TrainBookingPassengerModule.class, dependencies = TrainComponent.class)
public interface TrainBookingPassengerComponent {

    void inject(TrainBookingPassengerFragment trainBookingPassengerFragment);

    void inject(TrainBookingAddPassengerFragment trainBookingAddPassengerFragment);

}
