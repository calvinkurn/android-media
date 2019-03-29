package com.tokopedia.train.search.data;

import android.arch.persistence.db.SimpleSQLiteQuery;

import com.tokopedia.train.common.specification.AndRoomSpecification;
import com.tokopedia.train.common.specification.RoomSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.search.data.database.TrainScheduleDao;
import com.tokopedia.train.search.data.specification.TrainScheduleClassFilterSpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleDepartureTimeFilterSpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleNameFilterSpecification;
import com.tokopedia.train.search.data.specification.TrainSchedulePriceFilterSpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleSortSpecification;
import com.tokopedia.train.search.data.specification.TrainSearchScheduleSpecification;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.domain.mapper.TrainScheduleMapper;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 01/02/19.
 */
public class TrainScheduleDataStoreFactory {

    private TrainScheduleDao trainScheduleDao;
    private TrainScheduleCloudDataStore cloudDataStore;
    private TrainScheduleMapper scheduleMapper;

    public TrainScheduleDataStoreFactory(TrainScheduleDao trainScheduleDao,
                                         TrainScheduleCloudDataStore cloudDataStore,
                                         TrainScheduleMapper scheduleMapper) {
        this.trainScheduleDao = trainScheduleDao;
        this.cloudDataStore = cloudDataStore;
        this.scheduleMapper = scheduleMapper;
    }

    public Observable<List<AvailabilityKeySchedule>> getSchedules(Specification specification, int scheduleVariant) {
        if (scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE) {
            return getDataFromCloud(specification, scheduleVariant);
        } else if (scheduleVariant == TrainScheduleTypeDef.DEPARTURE_SCHEDULE) {
            return Observable.just(true)
                    .map(it -> trainScheduleDao.deleteAll())
                    .flatMap(it -> getDataFromCloud(specification, scheduleVariant));
        }
        return Observable.empty();
    }

    private Observable<List<AvailabilityKeySchedule>> getDataFromCloud(Specification specification, int scheduleVariant) {
        return cloudDataStore.getDatasSchedule(specification)
                .flatMap(scheduleEntities -> {
                    return Observable.just(scheduleMapper.transformToTable(scheduleEntities, scheduleVariant))
                            .map(it -> trainScheduleDao.insertAll(it))
                            .map(it -> trainScheduleDao.findCheapestSchedules())
                            .map(it -> trainScheduleDao.updateCheapestFlag(it))
                            .map(it -> trainScheduleDao.findFastestSchedules())
                            .map(it -> trainScheduleDao.updateFastestFlag(it))
                            .map(it -> scheduleMapper.transformToAvailabilityKey(scheduleEntities));
                });
    }

    public Observable<List<TrainScheduleViewModel>> getAvailabilitySchedule(Specification specification) {
        return cloudDataStore.getDatasAvailability(specification)
                .flatMap(it -> Observable.from(it))
                .map(it -> trainScheduleDao.updateAvailableSeat(it.getId(), it.getAvailable()))
                .toList()
                .map(it -> trainScheduleDao.findSchedules())
                .map(it -> scheduleMapper.transformToModel(it));
    }

    public Observable<List<TrainScheduleViewModel>> getFilteredAndSortedSchedule(FilterParam filterParam, int sortOptionId, int scheduleVariant) {
        RoomSpecification specification = new TrainSchedulePriceFilterSpecification(filterParam.getMinPrice(), filterParam.getMaxPrice());
        if (!filterParam.getTrainClass().isEmpty()) {
            specification = new AndRoomSpecification(specification,
                    new TrainScheduleClassFilterSpecification(filterParam.getTrainClass()));
        }
        if (!filterParam.getTrains().isEmpty()) {
            specification = new AndRoomSpecification(specification,
                    new TrainScheduleNameFilterSpecification(filterParam.getTrains()));
        }
        if (!filterParam.getDepartureTimeList().isEmpty()) {
            specification = new AndRoomSpecification(specification,
                    new TrainScheduleDepartureTimeFilterSpecification(filterParam.getDepartureTimeList()));
        }
        specification = new AndRoomSpecification(specification,
                new TrainSearchScheduleSpecification(filterParam.getScheduleVariant(), filterParam.getArrivalTimestampSelected()));

        specification = new AndRoomSpecification(specification, new TrainScheduleSortSpecification(sortOptionId, scheduleVariant));
        String query = "SELECT * FROM TrainScheduleTable WHERE " + specification.query();
        SimpleSQLiteQuery newQuery = new SimpleSQLiteQuery(query, specification.getArgs().toArray());
        return Observable.just(newQuery)
                .map(it -> trainScheduleDao.findSchedules(it))
                .map(it -> scheduleMapper.transformToModel(it));
    }

    public Observable<Integer> getCountSchedule(FilterSearchData filterSearchData, int scheduleVariant,
                                                String arrivalTimestampSelected) {
        RoomSpecification specification = getSpecificationPrice(filterSearchData);
        specification = new AndRoomSpecification(specification, getSpecificationTrainClass(filterSearchData));
        specification = new AndRoomSpecification(specification, getSpecificationTrainName(filterSearchData));
        specification = new AndRoomSpecification(specification, getSpecificationTrainDeparture(filterSearchData));
        specification = new AndRoomSpecification(specification,
                new TrainSearchScheduleSpecification(scheduleVariant, arrivalTimestampSelected));
        String query = "SELECT * FROM TrainScheduleTable WHERE " + specification.query();
        SimpleSQLiteQuery newQuery = new SimpleSQLiteQuery(query, specification.getArgs().toArray());
        return Observable.just(newQuery)
                .map(it -> trainScheduleDao.findSchedules(newQuery))
                .map(it -> it.size());
    }

    private RoomSpecification getSpecificationPrice(FilterSearchData filterSearchData) {
        long minPrice;
        long maxPrice;
        if (filterSearchData.getSelectedMinPrice() > 0) {
            minPrice = filterSearchData.getSelectedMinPrice();
        } else {
            minPrice = filterSearchData.getMinPrice();
        }
        if (filterSearchData.getSelectedMaxPrice() > 0) {
            maxPrice = filterSearchData.getSelectedMaxPrice();
        } else {
            maxPrice = filterSearchData.getMaxPrice();
        }
        return new TrainSchedulePriceFilterSpecification(minPrice, maxPrice);
    }

    private RoomSpecification getSpecificationTrainClass(FilterSearchData filterSearchData) {
        if (filterSearchData.getSelectedTrainClass() != null && !filterSearchData.getSelectedTrainClass().isEmpty()) {
            return new TrainScheduleClassFilterSpecification(filterSearchData.getSelectedTrainClass());
        } else {
            return new TrainScheduleClassFilterSpecification(filterSearchData.getTrainClass());
        }
    }

    private RoomSpecification getSpecificationTrainName(FilterSearchData filterSearchData) {
        if (filterSearchData.getSelectedTrains() != null && !filterSearchData.getSelectedTrains().isEmpty()) {
            return new TrainScheduleNameFilterSpecification(filterSearchData.getSelectedTrains());
        } else {
            return new TrainScheduleNameFilterSpecification(filterSearchData.getTrains());
        }
    }

    private RoomSpecification getSpecificationTrainDeparture(FilterSearchData filterSearchData) {
        if (filterSearchData.getSelectedDepartureTimeList() != null && !filterSearchData.getSelectedDepartureTimeList().isEmpty()) {
            return new TrainScheduleDepartureTimeFilterSpecification(filterSearchData.getSelectedDepartureTimeList());
        } else {
            return new TrainScheduleDepartureTimeFilterSpecification(filterSearchData.getDepartureTimeList());
        }
    }

    public Observable<TrainScheduleViewModel> getDetailScheduleById(String idSchedule) {
        return Observable.just(idSchedule)
                .map(it -> trainScheduleDao.getScheduleById(it))
                .map(it -> scheduleMapper.transform(it));
    }
}
