package com.tokopedia.train.seat.di;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.seat.domain.TrainGetSeatsUseCase;
import com.tokopedia.train.seat.presentation.viewmodel.mapper.TrainWagonViewModelMapper;

import dagger.Module;
import dagger.Provides;

@Module
public class TrainSeatModule {

    @Provides
    public TrainGetSeatsUseCase provideTrainGetSeatsUseCase(TrainRepository trainRepository, TrainWagonViewModelMapper viewModelMapper) {
        return new TrainGetSeatsUseCase(trainRepository, viewModelMapper);
    }
}
