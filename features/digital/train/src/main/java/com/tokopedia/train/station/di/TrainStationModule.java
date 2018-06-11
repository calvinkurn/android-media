package com.tokopedia.train.station.di;

import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.station.domain.TrainGetAllStationsUseCase;
import com.tokopedia.train.station.domain.TrainGetPopularStationsUseCase;
import com.tokopedia.train.station.domain.TrainGetStationCitiesByKeywordUseCase;
import com.tokopedia.train.station.domain.TrainGetStationsByKeywordUseCase;
import com.tokopedia.train.station.domain.TraingGetStationAutoCompleteUseCase;
import com.tokopedia.train.station.presentation.adapter.viewmodel.mapper.TrainStationCityViewModelMapper;
import com.tokopedia.train.station.presentation.adapter.viewmodel.mapper.TrainStationViewModelMapper;

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

    @Provides
    TrainGetAllStationsUseCase provideTrainGetAllStationsUseCase(TrainRepository trainRepository) {
        return new TrainGetAllStationsUseCase(trainRepository);
    }

    @Provides
    TraingGetStationAutoCompleteUseCase provideTraingGetStationAutoCompleteUseCase(TrainGetStationsByKeywordUseCase trainGetStationsByKeywordUseCase,
                                                                                   TrainGetStationCitiesByKeywordUseCase trainGetStationCitiesByKeywordUseCase,
                                                                                   TrainStationViewModelMapper trainStationViewModelMapper,
                                                                                   TrainStationCityViewModelMapper trainStationCityViewModelMapper) {
        return new TraingGetStationAutoCompleteUseCase(trainGetStationsByKeywordUseCase,
                trainGetStationCitiesByKeywordUseCase,
                trainStationViewModelMapper,
                trainStationCityViewModelMapper);
    }
}
