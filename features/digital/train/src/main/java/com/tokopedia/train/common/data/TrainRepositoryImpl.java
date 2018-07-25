package com.tokopedia.train.common.data;


import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.reviewdetail.data.TrainCheckVoucherCloudDataStore;
import com.tokopedia.train.reviewdetail.data.TrainCheckVoucherEntity;
import com.tokopedia.train.reviewdetail.data.TrainPromoEntity;
import com.tokopedia.train.reviewdetail.data.specification.TrainCheckVoucherSpecification;
import com.tokopedia.train.scheduledetail.data.specification.TrainStationByStationCodeSpecification;
import com.tokopedia.train.search.data.TrainScheduleDataStoreFactory;
import com.tokopedia.train.search.data.specification.TrainAvailabilitySearchSpecification;
import com.tokopedia.train.search.data.specification.TrainDetailScheduleSpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleSpecification;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.seat.data.TrainSeatCloudDataStore;
import com.tokopedia.train.seat.data.entity.TrainSeatMapEntity;
import com.tokopedia.train.seat.data.specification.TrainChangeSeatSpecification;
import com.tokopedia.train.seat.data.specification.TrainSeatSpecification;
import com.tokopedia.train.seat.domain.model.TrainPassengerSeat;
import com.tokopedia.train.seat.domain.model.request.ChangeSeatMapRequest;
import com.tokopedia.train.station.data.TrainStationDataStoreFactory;
import com.tokopedia.train.station.data.specification.TrainPopularStationSpecification;
import com.tokopedia.train.station.data.specification.TrainStationByKeywordSpecification;
import com.tokopedia.train.station.data.specification.TrainStationCityByKeywordSpecification;
import com.tokopedia.train.station.data.specification.TrainStationSpecification;
import com.tokopedia.train.station.domain.model.TrainStation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainRepositoryImpl implements TrainRepository {

    private TrainSeatCloudDataStore trainSeatCloudDataStore;
    private TrainStationDataStoreFactory trainStationDataStoreFactory;
    private TrainScheduleDataStoreFactory trainScheduleDataStoreFactory;
    private TrainCheckVoucherCloudDataStore trainCheckVoucherCloudDataStore;

    public TrainRepositoryImpl(TrainSeatCloudDataStore trainSeatCloudDataStore,
                               TrainStationDataStoreFactory trainStationDataStoreFactory,
                               TrainScheduleDataStoreFactory scheduleDataStoreFactory,
                               TrainCheckVoucherCloudDataStore trainCheckVoucherCloudDataStore) {
        this.trainSeatCloudDataStore = trainSeatCloudDataStore;
        this.trainStationDataStoreFactory = trainStationDataStoreFactory;
        this.trainScheduleDataStoreFactory = scheduleDataStoreFactory;
        this.trainCheckVoucherCloudDataStore = trainCheckVoucherCloudDataStore;
    }

    @Override
    public Observable<List<TrainStation>> getPopularStations() {
        return trainStationDataStoreFactory.getStations(new TrainPopularStationSpecification());
    }

    @Override
    public Observable<List<TrainStation>> getAllStations() {
        return trainStationDataStoreFactory.getStations(new TrainStationSpecification());
    }

    @Override
    public Observable<List<TrainPassengerSeat>> changeSeats(List<ChangeSeatMapRequest> requests) {
        return trainSeatCloudDataStore.changeSeats(new TrainChangeSeatSpecification(requests));
    }

    @Override
    public Observable<List<TrainStation>> getStationsByKeyword(String keyword) {
        return trainStationDataStoreFactory.getStations(new TrainStationByKeywordSpecification(keyword));
    }

    @Override
    public Observable<TrainStation> getStationByStationCode(String stationCode) {
        return trainStationDataStoreFactory.getStation(new TrainStationByStationCodeSpecification(stationCode));
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
    public Observable<List<TrainScheduleViewModel>> getAvailabilitySchedule(Map<String, Object> mapParam) {
        return trainScheduleDataStoreFactory.getAvailabilitySchedule(new TrainAvailabilitySearchSpecification(mapParam));
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
    public Observable<Integer> getCountSchedule(FilterSearchData filterSearchData, int scheduleVariant,
                                                String arrivalTimestampSelected) {
        return trainScheduleDataStoreFactory.getCountSchedule(filterSearchData, scheduleVariant, arrivalTimestampSelected);
    }

    @Override
    public Observable<List<TrainSeatMapEntity>> getSeat(HashMap<String, Object> parameters) {
        return trainSeatCloudDataStore.getData(new TrainSeatSpecification(parameters));
    }

    @Override
    public Observable<TrainPromoEntity> checkVoucher(HashMap<String, Object> parameters) {
        return trainCheckVoucherCloudDataStore.getData(new TrainCheckVoucherSpecification(parameters));
    }

}
