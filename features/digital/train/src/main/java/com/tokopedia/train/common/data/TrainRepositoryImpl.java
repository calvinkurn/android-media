package com.tokopedia.train.common.data;


import com.tokopedia.train.checkout.data.TrainCheckoutCloudDataStore;
import com.tokopedia.train.checkout.data.entity.TrainCheckoutEntity;
import com.tokopedia.train.checkout.data.specification.TrainCheckoutSpecification;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.homepage.data.TrainBannerSpecification;
import com.tokopedia.train.homepage.data.TrainPromoCloudDataStore;
import com.tokopedia.train.homepage.data.entity.BannerDetail;
import com.tokopedia.train.passenger.data.TrainDoSoftBookingSpecification;
import com.tokopedia.train.passenger.data.cloud.TrainSoftBookingCloudDataStore;
import com.tokopedia.train.passenger.domain.model.TrainSoftbook;
import com.tokopedia.train.reviewdetail.data.TrainCheckVoucherCloudDataStore;
import com.tokopedia.train.reviewdetail.data.entity.TrainCheckVoucherEntity;
import com.tokopedia.train.reviewdetail.data.specification.TrainCheckVoucherSpecification;
import com.tokopedia.train.search.data.TrainScheduleDataStoreFactory;
import com.tokopedia.train.search.data.specification.TrainAvailabilitySearchSpecification;
import com.tokopedia.train.search.data.specification.TrainDetailScheduleSpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleSpecification;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.train.seat.data.TrainSeatCloudDataStore;
import com.tokopedia.train.seat.data.entity.TrainChangeSeatEntity;
import com.tokopedia.train.seat.data.entity.TrainSeatMapEntity;
import com.tokopedia.train.seat.data.specification.TrainChangeSeatSpecification;
import com.tokopedia.train.seat.data.specification.TrainSeatSpecification;
import com.tokopedia.train.seat.domain.model.request.ChangeSeatMapRequest;
import com.tokopedia.train.station.data.TrainStationDataStoreNewFactory;
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
    private TrainStationDataStoreNewFactory trainStationDataStoreFactory;
    private TrainScheduleDataStoreFactory trainScheduleDataStoreFactory;
    private TrainSoftBookingCloudDataStore trainSoftBookingCloudDataStore;
    private TrainCheckVoucherCloudDataStore trainCheckVoucherCloudDataStore;
    private TrainCheckoutCloudDataStore trainCheckoutCloudDataStore;
    private TrainPromoCloudDataStore trainPromoCloudDataStore;

    public TrainRepositoryImpl(TrainSeatCloudDataStore trainSeatCloudDataStore,
                               TrainStationDataStoreNewFactory trainStationDataStoreFactory,
                               TrainScheduleDataStoreFactory scheduleDataStoreFactory,
                               TrainSoftBookingCloudDataStore trainSoftBookingCloudDataStore,
                               TrainCheckVoucherCloudDataStore trainCheckVoucherCloudDataStore,
                               TrainCheckoutCloudDataStore trainCheckoutCloudDataStore,
                               TrainPromoCloudDataStore trainPromoCloudDataStore) {
        this.trainSeatCloudDataStore = trainSeatCloudDataStore;
        this.trainStationDataStoreFactory = trainStationDataStoreFactory;
        this.trainScheduleDataStoreFactory = scheduleDataStoreFactory;
        this.trainSoftBookingCloudDataStore = trainSoftBookingCloudDataStore;
        this.trainCheckVoucherCloudDataStore = trainCheckVoucherCloudDataStore;
        this.trainCheckoutCloudDataStore = trainCheckoutCloudDataStore;
        this.trainPromoCloudDataStore = trainPromoCloudDataStore;
    }

    @Override
    public Observable<List<TrainStation>> getPopularStations() {
        return trainStationDataStoreFactory.getPopularStation();
    }

    @Override
    public Observable<List<TrainStation>> getAllStations() {
        return trainStationDataStoreFactory.getAllStations();
    }

    @Override
    public Observable<List<TrainStation>> getStationsByKeyword(String keyword) {
        return trainStationDataStoreFactory.getStationByKeyword(keyword);
    }

    @Override
    public Observable<TrainStation> getStationByStationCode(String stationCode) {
        return trainStationDataStoreFactory.getStationByStationCode(stationCode);
    }

    @Override
    public Observable<List<TrainStation>> getStationCitiesByKeyword(String keyword) {
        return trainStationDataStoreFactory.getStationCitiesByKeyword(keyword);
    }

//TODO ===============================

    @Override
    public Observable<List<AvailabilityKeySchedule>> getSchedule(Map<String, Object> mapParam, int scheduleVariant) {
        return trainScheduleDataStoreFactory.getScheduleTrain(new TrainScheduleSpecification(mapParam), scheduleVariant);
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> getAvailabilitySchedule(Map<String, Object> mapParam) {
        return trainScheduleDataStoreFactory.getAvailabilitySchedule(new TrainAvailabilitySearchSpecification(mapParam));
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> getFilteredAndSortedSchedule(FilterParam filterParam, int sortOptionId, int scheduleVariant) {
        return trainScheduleDataStoreFactory.getFilteredAndSortedSchedule(filterParam, sortOptionId, scheduleVariant);
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

// TODO ================================

    @Override
    public Observable<List<TrainChangeSeatEntity>> changeSeats(List<ChangeSeatMapRequest> requests) {
        return trainSeatCloudDataStore.changeSeats(new TrainChangeSeatSpecification(requests));
    }

    @Override
    public Observable<List<TrainSeatMapEntity>> getSeat(HashMap<String, Object> parameters) {
        return trainSeatCloudDataStore.getData(new TrainSeatSpecification(parameters));
    }

    @Override
    public Observable<TrainSoftbook> doSoftBookTrainTicket(Map<String, Object> mapParam) {
        return trainSoftBookingCloudDataStore.doSoftBookingTrainTicket(new TrainDoSoftBookingSpecification(mapParam));
    }

    @Override
    public Observable<TrainCheckVoucherEntity> checkVoucher(HashMap<String, Object> parameters) {
        return trainCheckVoucherCloudDataStore.getData(new TrainCheckVoucherSpecification(parameters));
    }

    @Override
    public Observable<TrainCheckoutEntity> checkout(HashMap<String, Object> parameters) {
        return trainCheckoutCloudDataStore.checkout(new TrainCheckoutSpecification(parameters));
    }

    @Override
    public Observable<List<BannerDetail>> getBanners(HashMap<String, Object> parameters) {
        return trainPromoCloudDataStore.getData(new TrainBannerSpecification(parameters));
    }

}
