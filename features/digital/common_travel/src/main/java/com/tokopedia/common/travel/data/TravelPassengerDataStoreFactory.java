package com.tokopedia.common.travel.data;

import com.tokopedia.common.travel.data.entity.TravelPassengerEntity;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 08/11/18.
 */
public class TravelPassengerDataStoreFactory {

    private TravelPassengerDbDataStore dbDataStore;

    public TravelPassengerDataStoreFactory(TravelPassengerDbDataStore dbDataStore) {
        this.dbDataStore = dbDataStore;
    }

    public Observable<List<TravelPassenger>> getPassengerListLocal(List<TravelPassengerEntity> travelPassengerListNetwork, boolean resetPassengerListSelected, String idPassengerPrevious) {
        return Observable.just(resetPassengerListSelected)
                .flatMap(new Func1<Boolean, Observable<List<TravelPassenger>>>() {
                    @Override
                    public Observable<List<TravelPassenger>> call(Boolean isReset) {
                        if (isReset) {
                            return dbDataStore.deleteAll().flatMap(new Func1<Boolean, Observable<List<TravelPassenger>>>() {
                                @Override
                                public Observable<List<TravelPassenger>> call(Boolean isSuccessDelete) {
                                    if (isSuccessDelete) {
                                        return insertPassengerList(travelPassengerListNetwork);
                                    } else {
                                        return Observable.empty();
                                    }
                                }
                            });
                        } else {
                            return dbDataStore.isDataAvailable()
                                    .flatMap(new Func1<Boolean, Observable<List<TravelPassenger>>>() {
                                        @Override
                                        public Observable<List<TravelPassenger>> call(Boolean isDataAvailable) {
                                            if (isDataAvailable) {
                                                return updatePassengerList(travelPassengerListNetwork, idPassengerPrevious);
                                            } else {
                                                return insertPassengerList(travelPassengerListNetwork);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private Observable<List<TravelPassenger>> updatePassengerList(List<TravelPassengerEntity> travelPassengerListNetwork, String idPassengerPrevious) {
        return dbDataStore.updateDatas(travelPassengerListNetwork, idPassengerPrevious)
                .flatMap(new Func1<Boolean, Observable<List<TravelPassenger>>>() {
                    @Override
                    public Observable<List<TravelPassenger>> call(Boolean isSuccess) {
                        if (isSuccess) {
                            return dbDataStore.getDatas();
                        } else {
                            return Observable.empty();
                        }
                    }
                });
    }

    private Observable<List<TravelPassenger>> insertPassengerList(List<TravelPassengerEntity> travelPassengerListNetwork) {
        return dbDataStore.insertAll(travelPassengerListNetwork)
                .flatMap(new Func1<Boolean, Observable<List<TravelPassenger>>>() {
                    @Override
                    public Observable<List<TravelPassenger>> call(Boolean isSuccess) {
                        if (isSuccess) {
                            return dbDataStore.getDatas();
                        } else {
                            return Observable.empty();
                        }
                    }
                });
    }

    public Observable<Boolean> deletePassenger(String idPassenger) {
        return dbDataStore.deleteData(idPassenger);
    }

    public Observable<Boolean> updatePassenger(String idPassenger, boolean isSelected) {
        return dbDataStore.updateSelectedData(idPassenger, isSelected);
    }
}
