package com.tokopedia.train.search.data;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.tokopedia.train.common.TrainDataDBSource;
import com.tokopedia.train.common.specification.DbFlowSpecification;
import com.tokopedia.train.common.specification.DbFlowWithOrderSpecification;
import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.common.util.TrainDateUtil;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable;
import com.tokopedia.train.search.data.databasetable.TrainScheduleDbTable_Table;
import com.tokopedia.train.search.data.entity.AvailabilityEntity;
import com.tokopedia.train.search.data.entity.FareEntity;
import com.tokopedia.train.search.data.entity.ScheduleEntity;
import com.tokopedia.train.search.data.entity.TrainScheduleEntity;
import com.tokopedia.train.search.data.typedef.TrainAvailabilityTypeDef;
import com.tokopedia.train.search.data.typedef.TrainScheduleTypeDef;
import com.tokopedia.train.search.domain.mapper.TrainScheduleMapper;
import com.tokopedia.train.search.presentation.model.TrainScheduleViewModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class TrainScheduleDbDataStore implements TrainDataDBSource<TrainScheduleEntity, TrainScheduleViewModel> {

    public TrainScheduleDbDataStore() {
    }

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select(Method.count()).from(TrainScheduleDbTable.class).hasData());
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(TrainScheduleDbTable.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    @Override
    public Observable<Boolean> insert(final TrainScheduleEntity trainScheduleEntity) {
        return null;
    }

    @Override
    public Observable<Boolean> insertAll(final List<TrainScheduleEntity> datas) {
        return null;
    }

    public Observable<Boolean> insertAllData(final List<ScheduleEntity> datas, final int scheduleVariant) {
        return Observable.just(datas)
                .map(new Func1<List<ScheduleEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<ScheduleEntity> trainScheduleEntities) {
                        for (ScheduleEntity scheduleEntity : datas) {
                            insertSchedule(scheduleEntity, scheduleVariant);
                        }
                        updateFilterCheapest();
                        updateFilterFastest();
                        return true;
                    }
                });
    }

    private void insertSchedule(ScheduleEntity scheduleEntity, int scheduleVariant) {
        ModelAdapter<TrainScheduleDbTable> adapter = FlowManager.getModelAdapter(TrainScheduleDbTable.class);
        for (TrainScheduleEntity trainScheduleEntity : scheduleEntity.getTrains()) {
            for (FareEntity fareEntity : trainScheduleEntity.getFares()) {
                TrainScheduleDbTable trainScheduleDbTable = new TrainScheduleDbTable();
                trainScheduleDbTable.setIdSchedule(fareEntity.getId());
                trainScheduleDbTable.setAdultFare(fareEntity.getAdultFare());
                trainScheduleDbTable.setDisplayAdultFare(fareEntity.getDisplayAdultFare());
                trainScheduleDbTable.setInfantFare(fareEntity.getInfantFare());
                trainScheduleDbTable.setDisplayInfantFare(fareEntity.getDisplayInfantFare());
                trainScheduleDbTable.setArrivalTimestamp(trainScheduleEntity.getArrivalTimestamp());
                trainScheduleDbTable.setDepartureTimestamp(trainScheduleEntity.getDepartureTimestamp());
                trainScheduleDbTable.setClassTrain(fareEntity.getScheduleClass());
                trainScheduleDbTable.setDisplayClass(fareEntity.getDisplayClass());
                trainScheduleDbTable.setSubclass(fareEntity.getSubclass());
                trainScheduleDbTable.setOrigin(scheduleEntity.getOrigin());
                trainScheduleDbTable.setDestination(scheduleEntity.getDestination());
                trainScheduleDbTable.setDisplayDuration(trainScheduleEntity.getDisplayDuration());
                trainScheduleDbTable.setDuration(trainScheduleEntity.getDuration());
                trainScheduleDbTable.setTrainKey(trainScheduleEntity.getTrainKey());
                trainScheduleDbTable.setTrainName(trainScheduleEntity.getTrainName());
                trainScheduleDbTable.setTrainNumber(trainScheduleEntity.getTrainNo());
                trainScheduleDbTable.setAvailableSeat(TrainAvailabilityTypeDef.DEFAULT_VALUE);
                trainScheduleDbTable.setCheapestFlag(false);
                trainScheduleDbTable.setFastestFlag(false);
                trainScheduleDbTable.setReturnSchedule(scheduleVariant == TrainScheduleTypeDef.RETURN_SCHEDULE);

                String departureHour = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        TrainDateUtil.FORMAT_HOUR, trainScheduleEntity.getDepartureTimestamp());
                trainScheduleDbTable.setDepartureHour(Integer.valueOf(departureHour));

                String departureTimestampCustom = TrainDateUtil.formatDate(TrainDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
                        TrainDateUtil.DEFAULT_TIMESTAMP_FORMAT, trainScheduleEntity.getDepartureTimestamp());
                trainScheduleDbTable.setDepartureTime(convertTimestampToLong(departureTimestampCustom));

                adapter.insert(trainScheduleDbTable);
            }
        }

    }

    private long convertTimestampToLong(String timeString) {
        Timestamp timestamp = Timestamp.valueOf(timeString);
        return timestamp.getTime();
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> getDatas(final Specification specification) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<TrainScheduleDbTable>>() {
            @Override
            public void call(Subscriber<? super List<TrainScheduleDbTable>> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                if (specification instanceof DbFlowSpecification) {
                    conditions = ((DbFlowSpecification) specification).getCondition();
                }
                List<OrderBy> orderBies = new ArrayList<>();
                if (specification instanceof DbFlowWithOrderSpecification) {
                    orderBies = ((DbFlowWithOrderSpecification) specification).toOrder();
                }
                List<TrainScheduleDbTable> trainScheduleDBList = new Select()
                        .from(TrainScheduleDbTable.class)
                        .where(conditions)
                        .orderByAll(orderBies)
                        .queryList();

                subscriber.onNext(trainScheduleDBList);
            }
        }).map(new TrainScheduleMapper());
    }

    @Override
    public Observable<Integer> getCount(final Specification specification) {
        return Observable.just(true).map(new Func1<Boolean, Integer>() {
            @Override
            public Integer call(Boolean aBoolean) {
                ConditionGroup conditions = ConditionGroup.clause();
                if (specification instanceof DbFlowSpecification) {
                    conditions = ((DbFlowSpecification) specification).getCondition();
                }
                List<TrainScheduleDbTable> trainScheduleDbTables = new Select()
                        .from(TrainScheduleDbTable.class)
                        .where(conditions)
                        .queryList();
                return (trainScheduleDbTables.size());
            }
        });
    }

    public Observable<Boolean> updateDataAvailability(final List<AvailabilityEntity> scheduleAvailabilityEntities) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                for (AvailabilityEntity scheduleAvailability : scheduleAvailabilityEntities) {
                    updateAvailability(scheduleAvailability);
                }
                subscriber.onNext(true);
            }

            private void updateAvailability(AvailabilityEntity availabilityEntity) {
                ConditionGroup conditions = ConditionGroup.clause();
                conditions.and(TrainScheduleDbTable_Table.schedule_id.eq(availabilityEntity.getId()));
                TrainScheduleDbTable result = new Select()
                        .from(TrainScheduleDbTable.class)
                        .where(conditions)
                        .querySingle();
                if (result != null) {
                    result.setAvailableSeat(availabilityEntity.getAvailable());
                    result.save();
                }
            }
        });
    }

    private void updateFilterCheapest() {
        TrainScheduleDbTable cheapestSchedule = new Select(Method.ALL_PROPERTY, Method.min(TrainScheduleDbTable_Table.adult_fare))
                .from(TrainScheduleDbTable.class)
                .querySingle();

        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainScheduleDbTable_Table.adult_fare.eq(cheapestSchedule.getAdultFare()));

        List<TrainScheduleDbTable> result = new Select()
                .from(TrainScheduleDbTable.class)
                .where(conditions)
                .queryList();
        for (TrainScheduleDbTable trainScheduleDbTable : result) {
            trainScheduleDbTable.setCheapestFlag(true);
            trainScheduleDbTable.save();
        }
    }

    private void updateFilterFastest() {
        TrainScheduleDbTable fastestSchedule = new Select(Method.ALL_PROPERTY, Method.min(TrainScheduleDbTable_Table.duration))
                .from(TrainScheduleDbTable.class)
                .querySingle();

        ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(TrainScheduleDbTable_Table.duration.eq(fastestSchedule.getDuration()));

        List<TrainScheduleDbTable> result = new Select()
                .from(TrainScheduleDbTable.class)
                .where(conditions)
                .queryList();
        for (TrainScheduleDbTable trainScheduleDbTable : result) {
            trainScheduleDbTable.setFastestFlag(true);
            trainScheduleDbTable.save();
        }
    }

    @Override
    public Observable<TrainScheduleViewModel> getData(final Specification specification) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<TrainScheduleDbTable>() {
            @Override
            public void call(Subscriber<? super TrainScheduleDbTable> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                if (specification instanceof DbFlowSpecification) {
                    conditions = ((DbFlowSpecification) specification).getCondition();
                }
                List<OrderBy> orderBies = new ArrayList<>();
                if (specification instanceof DbFlowWithOrderSpecification) {
                    orderBies = ((DbFlowWithOrderSpecification) specification).toOrder();
                }
                TrainScheduleDbTable trainScheduleDBList = new Select()
                        .from(TrainScheduleDbTable.class)
                        .where(conditions)
                        .orderByAll(orderBies)
                        .querySingle();

                subscriber.onNext(trainScheduleDBList);
            }
        }).map(trainScheduleDbTable -> {
            TrainScheduleMapper mapper = new TrainScheduleMapper();
            return mapper.transform(trainScheduleDbTable);
        });
    }
}
