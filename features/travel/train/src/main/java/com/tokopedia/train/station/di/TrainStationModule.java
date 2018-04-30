package com.tokopedia.train.station.di;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.station.domain.TrainGetPopularStationsUseCase;
import com.tokopedia.train.station.domain.TrainGetStationCitiesByKeywordUseCase;
import com.tokopedia.train.station.domain.TrainGetStationsByKeywordUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvarisi on 3/5/18.
 */
@Module
public class TrainStationModule {
    @Provides
    TrainGetPopularStationsUseCase provideGetPopularStationsUseCase(TrainRepository trainRepository) {
        return new TrainGetPopularStationsUseCase(trainRepository);
    }

    @Provides
    TrainGetStationsByKeywordUseCase provideGetStationsByKeywordUseCase(TrainRepository trainRepository) {
        return new TrainGetStationsByKeywordUseCase(trainRepository);
    }

    @Provides
    TrainGetStationCitiesByKeywordUseCase provideTrainGetStationCitiesByKeywordUseCase(TrainRepository trainRepository) {
        return new TrainGetStationCitiesByKeywordUseCase(trainRepository);
    }
}
