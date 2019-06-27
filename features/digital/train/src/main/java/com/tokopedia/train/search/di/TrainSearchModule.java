package com.tokopedia.train.search.di;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.search.domain.GetAvailabilityScheduleUseCase;
import com.tokopedia.train.search.domain.GetFilterSearchParamDataUseCase;
import com.tokopedia.train.search.domain.GetFilteredAndSortedScheduleUseCase;
import com.tokopedia.train.search.domain.GetScheduleUseCase;
import com.tokopedia.train.search.domain.GetTotalScheduleUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by nabilla on 3/9/18.
 */
@Module
public class TrainSearchModule {

    @Provides
    GetScheduleUseCase provideGetScheduleUseCase(TrainRepository trainRepository) {
        return new GetScheduleUseCase(trainRepository);
    }

    @Provides
    GetAvailabilityScheduleUseCase provideGetAvailabilityScheduleUseCase(TrainRepository trainRepository) {
        return new GetAvailabilityScheduleUseCase(trainRepository);
    }

    @Provides
    GetFilteredAndSortedScheduleUseCase provideGetFilteredAndSortedScheduleUseCase(TrainRepository trainRepository) {
        return new GetFilteredAndSortedScheduleUseCase(trainRepository);
    }

    @Provides
    GetTotalScheduleUseCase provideGetTotalScheduleUseCase(TrainRepository trainRepository) {
        return new GetTotalScheduleUseCase(trainRepository);
    }

    @Provides
    GetFilterSearchParamDataUseCase provideGetFilterSearchParamDataUseCase(TrainRepository trainRepository) {
        return new GetFilterSearchParamDataUseCase(trainRepository);
    }

}
