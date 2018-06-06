package com.tokopedia.train.common.data;


import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.search.data.TrainScheduleDataStoreFactory;
import com.tokopedia.train.search.data.specification.TrainAvailabilitySearchSpecification;
import com.tokopedia.train.search.data.specification.TrainDetailScheduleSpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleSpecification;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.station.data.TrainStationDataStoreFactory;
import com.tokopedia.train.station.data.specification.TrainPopularStationSpecification;
import com.tokopedia.train.station.data.specification.TrainStationByKeywordSpecification;
import com.tokopedia.train.station.data.specification.TrainStationCityByKeywordSpecification;
import com.tokopedia.train.station.domain.model.TrainStation;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainRepositoryImpl implements TrainRepository {

    private TrainDataStoreFactory dataStoreFactory;
    private TrainStationDataStoreFactory trainStationDataStoreFactory;
    private TrainScheduleDataStoreFactory trainScheduleDataStoreFactory;

    public TrainRepositoryImpl(TrainDataStoreFactory dataStoreFactory,
                               TrainStationDataStoreFactory trainStationDataStoreFactory,
                               TrainScheduleDataStoreFactory scheduleDataStoreFactory) {
        this.dataStoreFactory = dataStoreFactory;
        this.trainStationDataStoreFactory = trainStationDataStoreFactory;
        this.trainScheduleDataStoreFactory = scheduleDataStoreFactory;
    }

    @Override
    public Observable<List<TrainStation>> getPopularStations() {
        return trainStationDataStoreFactory.getStations(new TrainPopularStationSpecification());
    }

    @Override
    public Observable<List<TrainStation>> getStationsByKeyword(String keyword) {
        return trainStationDataStoreFactory.getStations(new TrainStationByKeywordSpecification(keyword));
    }

    @Override
    public Observable<List<AvailabilityKeySchedule>> getSchedule(Map<String, Object> mapParam, int scheduleVariant) {
        return trainScheduleDataStoreFactory.getScheduleTrain(new TrainScheduleSpecification(mapParam), scheduleVariant);
    }

    @Override
    public Observable<List<TrainStation>> getStationCitiesByKeyword(String keyword) {
        return trainStationDataStoreFactory.getStations(new TrainStationCityByKeywordSpecification(keyword));
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> getAvailabilitySchedule(Map<String, Object> mapParam, int scheduleVariant) {
        return trainScheduleDataStoreFactory.getAvailabilitySchedule(new TrainAvailabilitySearchSpecification(mapParam), scheduleVariant);
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> getFilteredAndSortedSchedule(FilterParam filterParam, int sortOptionId) {
        return trainScheduleDataStoreFactory.getFilteredAndSortedSchedule(filterParam, sortOptionId);
    }

    @Override
    public Observable<TrainScheduleViewModel> getDetailSchedule(String idSchedule) {
        return trainScheduleDataStoreFactory.getDetailScheduleById(new TrainDetailScheduleSpecification(idSchedule));
    }

    @Override
    public Observable<Integer> getCountSchedule(FilterSearchData filterSearchData) {
        return trainScheduleDataStoreFactory.getCountSchedule(filterSearchData);
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> getFilterSearchParamData(Map<String, Object> mapParam, int scheduleVariant) {
        return trainScheduleDataStoreFactory.getFilterSearchParamData(mapParam, scheduleVariant);
    }
}
