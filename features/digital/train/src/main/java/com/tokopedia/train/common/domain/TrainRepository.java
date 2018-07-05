package com.tokopedia.train.common.domain;


import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.seat.data.entity.TrainSeatMapEntity;
import com.tokopedia.train.station.domain.model.TrainStation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author by alvarisi on 3/5/18.
 */

public interface TrainRepository {
    Observable<List<TrainStation>> getPopularStations();

    Observable<List<TrainStation>> getStationsByKeyword(String keyword);

    Observable<TrainStation> getStationByStationCode(String stationCode);

    Observable<List<TrainStation>> getStationCitiesByKeyword(String keyword);

    Observable<List<AvailabilityKeySchedule>> getSchedule(Map<String, Object> mapParam, int scheduleVariant);

    Observable<List<TrainScheduleViewModel>> getAvailabilitySchedule(Map<String, Object> mapParam, int scheduleVariant, String arrivalTimeDepartureTripSelected);

    Observable<List<TrainScheduleViewModel>> getFilteredAndSortedSchedule(FilterParam filterParam, int sortOptionId);

    Observable<TrainScheduleViewModel> getDetailSchedule(String idSchedule);

    Observable<Integer> getCountSchedule(FilterSearchData filterSearchData);

    Observable<List<TrainScheduleViewModel>> getFilterSearchParamData(Map<String, Object> mapParam, int scheduleVariant);

    Observable<List<TrainSeatMapEntity>> getSeat(HashMap<String, Object> parameters);

    Observable<List<TrainStation>> getAllStations();
}
