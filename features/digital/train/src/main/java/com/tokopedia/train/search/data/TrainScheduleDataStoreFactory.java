package com.tokopedia.train.search.data;

import com.tokopedia.train.common.specification.AndDbFlowSpecification;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.search.data.entity.AvailabilityEntity;
import com.tokopedia.train.search.data.entity.ScheduleEntity;
import com.tokopedia.train.search.data.specification.TrainAvailabilitySpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleClassFilterSpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleNameFilterSpecification;
import com.tokopedia.train.search.data.specification.TrainSchedulePriceFilterSpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleSortSpecification;
import com.tokopedia.train.search.data.specification.TrainScheduleSpecification;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;
import com.tokopedia.train.search.domain.FilterParam;
import com.tokopedia.train.search.presentation.model.FilterSearchData;
import com.tokopedia.train.search.domain.mapper.AvailabilityKeysMapper;
import com.tokopedia.train.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabilla on 3/9/18.
 */

public class TrainScheduleDataStoreFactory {

    private TrainScheduleDbDataStore dbDataStore;
    private TrainScheduleCacheDataStore cacheDataStore;
    private TrainScheduleCloudDataStore cloudDataStore;

    public TrainScheduleDataStoreFactory(TrainScheduleDbDataStore dbDataStore,
                                         TrainScheduleCacheDataStore cacheDataStore,
                                         TrainScheduleCloudDataStore cloudDataStore) {
        this.dbDataStore = dbDataStore;
        this.cacheDataStore = cacheDataStore;
        this.cloudDataStore = cloudDataStore;
    }

    public Observable<List<AvailabilityKeySchedule>> getScheduleTrain(final Specification specification,
                                                                      final int scheduleVariant) {
        if (scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE) {
            return getDataFromCloud(specification, scheduleVariant);
        } else if (scheduleVariant == TrainScheduleTypeDef.DEPARTURE_SCHEDULE) {
            return dbDataStore.deleteAll().flatMap(new Func1<Boolean, Observable<List<AvailabilityKeySchedule>>>() {
                @Override
                public Observable<List<AvailabilityKeySchedule>> call(Boolean isSuccessDelete) {
                    if (isSuccessDelete) {
                        return getDataFromCloud(specification, scheduleVariant);
                    } else {
                        return Observable.empty();
                    }
                }
            });
        }
        return null;
    }

    private Observable<List<AvailabilityKeySchedule>> getDataFromCloud(Specification specification, final int scheduleVariant) {
        return cloudDataStore.getDatasSchedule(specification)
                .flatMap(new Func1<List<ScheduleEntity>, Observable<List<AvailabilityKeySchedule>>>() {
                    @Override
                    public Observable<List<AvailabilityKeySchedule>> call(final List<ScheduleEntity> scheduleEntities) {
                        return dbDataStore.insertAllData(scheduleEntities, scheduleVariant)
                                .flatMap(new Func1<Boolean, Observable<List<AvailabilityKeySchedule>>>() {
                                    @Override
                                    public Observable<List<AvailabilityKeySchedule>> call(Boolean isSuccessSaveData) {
                                        if (!isSuccessSaveData) {
                                            return Observable.empty();
                                        } else {
                                            return Observable.just(scheduleEntities)
                                                    .map(new AvailabilityKeysMapper());
                                        }
                                    }
                                });
                    }
                });
    }

    public Observable<List<TrainScheduleViewModel>> getAvailabilitySchedule(String idTrain, final int scheduleVariant) {
        return cloudDataStore.getDatasAvailability(idTrain)
                .flatMap(new Func1<List<AvailabilityEntity>, Observable<List<TrainScheduleViewModel>>>() {
                    @Override
                    public Observable<List<TrainScheduleViewModel>> call(final List<AvailabilityEntity> scheduleAvailabilityEntities) {
                        return dbDataStore.updateDataAvailability(scheduleAvailabilityEntities)
                                .flatMap(new Func1<Boolean, Observable<List<TrainScheduleViewModel>>>() {
                                    @Override
                                    public Observable<List<TrainScheduleViewModel>> call(Boolean isSuccessSavedData) {
                                        if (!isSuccessSavedData) {
                                            return Observable.empty();
                                        } else {
                                            return dbDataStore.getDatas(new TrainAvailabilitySpecification(scheduleAvailabilityEntities, scheduleVariant));
                                        }
                                    }
                                });
                    }
                });
    }

    public Observable<List<TrainScheduleViewModel>> getFilteredAndSortedSchedule(FilterParam filterParam, int sortOptionId) {
        DbFlowSpecification specification = new TrainSchedulePriceFilterSpecification(filterParam.getMinPrice(), filterParam.getMaxPrice());
        if (!filterParam.getTrainClass().isEmpty()) {
            specification = new AndDbFlowSpecification(specification,
                    new TrainScheduleClassFilterSpecification(filterParam.getTrainClass()));
        }
        if (!filterParam.getTrains().isEmpty()) {
            specification = new AndDbFlowSpecification(specification,
                    new TrainScheduleNameFilterSpecification(filterParam.getTrains()));
        }
        specification = new AndDbFlowSpecification(specification,
                new TrainScheduleSortSpecification(sortOptionId));
        return dbDataStore.getDatas(specification);
    }

    public Observable<Integer> getCountSchedule(FilterSearchData filterSearchData) {
        DbFlowSpecification specification = new TrainSchedulePriceFilterSpecification(filterSearchData.getMinPrice(), filterSearchData.getMaxPrice());
        if (filterSearchData.getTrainClass() != null && !filterSearchData.getTrainClass().isEmpty()) {
            specification = new AndDbFlowSpecification(specification,
                    new TrainScheduleClassFilterSpecification(filterSearchData.getTrainClass()));
        }
        if (filterSearchData.getTrainClass() != null && !filterSearchData.getTrains().isEmpty()) {
            specification = new AndDbFlowSpecification(specification,
                    new TrainScheduleNameFilterSpecification(filterSearchData.getTrains()));
        }
        return dbDataStore.getCount(specification);
    }

    public Observable<TrainScheduleViewModel> getDetailScheduleById(Specification specification) {
        return dbDataStore.getData(specification);
    }

    public Observable<List<TrainScheduleViewModel>> getFilterSearchParamData(Map<String, Object> mapParam, int scheduleVariant) {
        TrainScheduleSpecification trainScheduleSpecification = new TrainScheduleSpecification(mapParam);
        trainScheduleSpecification.setScheduleVariant(scheduleVariant);
        return dbDataStore.getDatas(trainScheduleSpecification);
    }
}
