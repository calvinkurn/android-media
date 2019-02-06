package com.tokopedia.flight.passenger.data.db;

import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.Model;
import com.tokopedia.flight.common.data.db.BaseDataListDBSource;
import com.tokopedia.flight.passenger.data.cloud.entity.PassengerListEntity;
import com.tokopedia.flight.search.data.db.mapper.FlightPassengerMapper;
import com.tokopedia.flight_dbflow.FlightPassengerDB;
import com.tokopedia.flight_dbflow.FlightPassengerDB_Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author by furqan on 28/02/18.
 */

public class FlightPassengerDataListDbSource extends BaseDataListDBSource<PassengerListEntity, FlightPassengerDB> {

    public static final String PASSENGER_ID = "PASSENGER_ID";

    @Inject
    public FlightPassengerDataListDbSource() {
    }

    @Override
    protected Class<? extends Model> getDBClass() {
        return FlightPassengerDB.class;
    }

    @Override
    public Observable<Boolean> insertAll(List<PassengerListEntity> list) {
        return Observable.from(list)
                .flatMap(new Func1<PassengerListEntity, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(PassengerListEntity passengerListEntity) {
                        FlightPassengerMapper flightPassengerMapper = new FlightPassengerMapper();
                        FlightPassengerDB flightPassengerDb = flightPassengerMapper.mapToFlightPassengerDb(passengerListEntity);
                        flightPassengerDb.insert();
                        return Observable.just(true);
                    }
                })
                .toList()
                .flatMap(new Func1<List<Boolean>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<Boolean> booleans) {
                        return Observable.just(new Select(Method.count())
                                .from(FlightPassengerDB.class)
                                .hasData());
                    }
                });
    }

    public Observable<Boolean> updateIsSelected(final String passengerId, final int isSelected) {

        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                conditions.and(FlightPassengerDB_Table.id.eq(passengerId));

                FlightPassengerDB result = new Select().from(FlightPassengerDB.class)
                        .where(conditions)
                        .querySingle();

                if (result != null) {
                    result.setIsSelected(isSelected);
                    result.save();
                    subscriber.onNext(true);
                } else {
                    subscriber.onNext(false);
                }
            }
        });
    }

    public Observable<Boolean> updatePassengerData(final String previousId, final PassengerListEntity passengerListEntity) {

        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                conditions.and(FlightPassengerDB_Table.id.eq(previousId));

                FlightPassengerDB result = new Select().from(FlightPassengerDB.class)
                        .where(conditions)
                        .querySingle();

                if (result != null) {
                    result.delete();

                    result = new FlightPassengerMapper().mapToFlightPassengerDb(passengerListEntity);
                    result.insert();
                    subscriber.onNext(true);
                } else {
                    subscriber.onNext(false);
                }
            }
        });
    }

    @Override
    public Observable<List<FlightPassengerDB>> getData(HashMap<String, Object> params) {
        final ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(FlightPassengerDB_Table.is_selected.eq(0));

        if (params != null &&
                params.containsKey(PASSENGER_ID) &&
                !params.get(PASSENGER_ID).equals("")) {
            conditions.or(FlightPassengerDB_Table.id.eq((String) params.get(PASSENGER_ID)));
        }

        final List<OrderBy> orderByList = new ArrayList<>();
        orderByList.add(OrderBy.fromProperty(FlightPassengerDB_Table.first_name).ascending());
        orderByList.add(OrderBy.fromProperty(FlightPassengerDB_Table.last_name).ascending());

        return Observable.unsafeCreate(new Observable.OnSubscribe<List<FlightPassengerDB>>() {
            @Override
            public void call(Subscriber<? super List<FlightPassengerDB>> subscriber) {
                List<FlightPassengerDB> flightPassengerDbList;

                flightPassengerDbList = new Select().from(FlightPassengerDB.class)
                        .where(conditions)
                        .orderByAll(orderByList)
                        .queryList();
                subscriber.onNext(flightPassengerDbList);
            }
        });
    }

    @Override
    public Observable<Integer> getDataCount(HashMap<String, Object> params) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                long count = new Select(Method.count())
                        .from(getDBClass())
                        .count();
                subscriber.onNext((int) count);
            }
        });
    }

    public Observable<Boolean> deletePassenger(String passengerId) {
        final ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(FlightPassengerDB_Table.id.eq(passengerId));

        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                FlightPassengerDB result = new Select().from(FlightPassengerDB.class)
                        .where(conditions)
                        .querySingle();

                result.delete();

                subscriber.onNext(true);
            }
        });
    }

    public Observable<FlightPassengerDB> getSingleData(String passengerId) {
        final ConditionGroup conditions = ConditionGroup.clause();
        conditions.and(FlightPassengerDB_Table.id.eq(passengerId));

        return Observable.unsafeCreate(new Observable.OnSubscribe<FlightPassengerDB>() {
            @Override
            public void call(Subscriber<? super FlightPassengerDB> subscriber) {
                FlightPassengerDB flightPassengerDb;

                flightPassengerDb = new Select().from(FlightPassengerDB.class)
                        .where(conditions)
                        .querySingle();

                subscriber.onNext(flightPassengerDb);
            }
        });
    }
}
