package com.tokopedia.common.travel.data;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.tokopedia.common.travel.data.entity.TravelPassengerEntity;
import com.tokopedia.common.travel.database.TravelPassengerDb;
import com.tokopedia.common.travel.database.TravelPassengerDb_Table;
import com.tokopedia.common.travel.domain.TravelPassengerMapper;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 08/11/18.
 */
public class TravelPassengerDbDataStore implements TravelPassengerDataDbSource<TravelPassengerEntity, TravelPassenger> {

    public TravelPassengerDbDataStore() {
    }

    @Override
    public Observable<Boolean> isDataAvailable() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(new Select(Method.count()).from(TravelPassengerDb.class).hasData());
            }
        });
    }

    @Override
    public Observable<Boolean> deleteAll() {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                new Delete().from(TravelPassengerDb.class).execute();
                subscriber.onNext(true);
            }
        });
    }

    @Override
    public Observable<Boolean> insert(TravelPassengerEntity data) {
        return null;
    }

    @Override
    public Observable<Boolean> insertAll(List<TravelPassengerEntity> datas) {
        return Observable.just(datas)
                .map(new Func1<List<TravelPassengerEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<TravelPassengerEntity> travelPassengerEntities) {
                        ModelAdapter<TravelPassengerDb> adapter = FlowManager.getModelAdapter(TravelPassengerDb.class);
                        for (TravelPassengerEntity travelPassengerEntity : datas) {
                            TravelPassengerDb travelPassengerDb = mapTravelPassengerToDb(travelPassengerEntity);
                            adapter.insert(travelPassengerDb);
                        }
                        return true;
                    }
                });
    }

    private TravelPassengerDb mapTravelPassengerToDb(TravelPassengerEntity travelPassengerEntity) {
        TravelPassengerDb travelPassengerDb = new TravelPassengerDb();
        travelPassengerDb.setIdPassenger(travelPassengerEntity.getId() + travelPassengerEntity.getTravelId());
        travelPassengerDb.setId(travelPassengerEntity.getId());
        travelPassengerDb.setNamePassenger(travelPassengerEntity.getName());
        travelPassengerDb.setFirstName(travelPassengerEntity.getFirstName());
        travelPassengerDb.setLastName(travelPassengerEntity.getLastName());
        travelPassengerDb.setBirthDate(travelPassengerEntity.getBirthDate());
        travelPassengerDb.setNationality(travelPassengerEntity.getNationality());
        travelPassengerDb.setPassportNo(travelPassengerEntity.getPassportNo());
        travelPassengerDb.setPassportCountry(travelPassengerEntity.getPassportCountry());
        travelPassengerDb.setPassportExpiry(travelPassengerEntity.getPassportExpiry());
        travelPassengerDb.setTitle(travelPassengerEntity.getTitle());
        travelPassengerDb.setIdNumber(travelPassengerEntity.getIdNumber());
        travelPassengerDb.setIsBuyer(travelPassengerEntity.getIsBuyer());
        travelPassengerDb.setPaxType(travelPassengerEntity.getPaxType());
        travelPassengerDb.setTravelId(travelPassengerEntity.getTravelId());
        travelPassengerDb.setUserId(travelPassengerEntity.getUserId());
        travelPassengerDb.setSelected(false);
        return travelPassengerDb;
    }

    public Observable<Boolean> updateDatas(List<TravelPassengerEntity> datas) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                for (TravelPassengerEntity travelPassengerEntity : datas) {
                    updateInsertData(travelPassengerEntity);
                }
                subscriber.onNext(true);
            }

            private void updateInsertData(TravelPassengerEntity travelPassengerEntity) {
                ConditionGroup conditions = ConditionGroup.clause();
                conditions.and(TravelPassengerDb_Table.idPassenger.eq(travelPassengerEntity.getId() + travelPassengerEntity.getTravelId()));

                TravelPassengerDb result = new Select().from(TravelPassengerDb.class)
                        .where(conditions)
                        .querySingle();

                boolean isSelected = false;
                if (result != null) {
                    isSelected = result.isSelected();
                    result.delete();
                }
                result = mapTravelPassengerToDb(travelPassengerEntity);
                result.setSelected(isSelected);
                result.insert();
            }
        });
    }

    public Observable<Boolean> updateSelectedData(String idPassenger, boolean isSelected) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                conditions.and(TravelPassengerDb_Table.idPassenger.eq(idPassenger));

                TravelPassengerDb result = new Select().from(TravelPassengerDb.class)
                        .where(conditions)
                        .querySingle();

                if (result != null) {
                    result.setSelected(isSelected);
                    result.save();
                }
                subscriber.onNext(true);
            }
        });
    }

    @Override
    public Observable<List<TravelPassenger>> getDatas(Specification specification) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<TravelPassengerDb>>() {
            @Override
            public void call(Subscriber<? super List<TravelPassengerDb>> subscriber) {
                ConditionGroup conditions = ConditionGroup.clause();
                if (specification instanceof DbFlowSpecification) {
                    conditions = ((DbFlowSpecification) specification).getCondition();
                }
                List<TravelPassengerDb> travelPassengerDbList = new Select()
                        .from(TravelPassengerDb.class)
                        .where(conditions)
                        .queryList();

                subscriber.onNext(travelPassengerDbList);
            }
        }).map(new TravelPassengerMapper());
    }

    @Override
    public Observable<Integer> getCount(Specification specification) {
        return Observable.just(true).map(new Func1<Boolean, Integer>() {
            @Override
            public Integer call(Boolean aBoolean) {
                ConditionGroup conditions = ConditionGroup.clause();
                if (specification instanceof DbFlowSpecification) {
                    conditions = ((DbFlowSpecification) specification).getCondition();
                }
                List<TravelPassengerDb> travelPassengerDb = new Select()
                        .from(TravelPassengerDb.class)
                        .where(conditions)
                        .queryList();
                return (travelPassengerDb.size());
            }
        });
    }

    @Override
    public Observable<TravelPassenger> getData(Specification specification) {
        return null;
    }
}
