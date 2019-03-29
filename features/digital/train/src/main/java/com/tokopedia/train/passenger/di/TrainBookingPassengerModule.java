package com.tokopedia.train.passenger.di;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.passenger.domain.TrainSoftBookingUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nabillasabbaha on 26/06/18.
 */
@Module
public class TrainBookingPassengerModule {

    @Provides
    TrainSoftBookingUseCase provideTrainSoftBookingUseCase(TrainRepository trainRepository) {
        return new TrainSoftBookingUseCase(trainRepository);
    }



}
