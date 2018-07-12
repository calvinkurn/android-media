package com.tokopedia.train.station.data;

import com.tokopedia.train.common.specification.Specification;
import com.tokopedia.train.station.data.entity.TrainStationIslandEntity;
import com.tokopedia.train.station.domain.model.TrainStation;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationDataStoreFactory {
    private TrainStationDbDataStore trainStationDbDataStore;
    private TrainStationCloudDataStore trainStationCloudDataStore;
    private TrainStationCacheDataStore trainStationCacheDataStore;

    public TrainStationDataStoreFactory(TrainStationDbDataStore trainStationDbDataStore,
                                        TrainStationCloudDataStore trainStationCloudDataStore,
                                        TrainStationCacheDataStore trainStationCacheDataStore) {
        this.trainStationDbDataStore = trainStationDbDataStore;

        this.trainStationCloudDataStore = trainStationCloudDataStore;
        this.trainStationCacheDataStore = trainStationCacheDataStore;
    }

    public Observable<List<TrainStation>> getStations(final Specification specification) {
        return trainStationCacheDataStore.isExpired()
                .flatMap((Func1<Boolean, Observable<List<TrainStation>>>)
                        isExpired -> {
                            if (isExpired) {
                                return trainStationDbDataStore.deleteAll().flatMap((Func1<Boolean, Observable<List<TrainStation>>>)
                                        isSuccessDeletingData -> {
                                            if (isSuccessDeletingData)
                                                return getDatasFromCloud(specification);
                                            else
                                                return Observable.empty();
                                        });
                            } else {
                                return trainStationDbDataStore.getDatas(specification).flatMap((Func1<List<TrainStation>, Observable<List<TrainStation>>>)
                                        trainStations -> {
                                            if (trainStations != null && trainStations.size() > 0)
                                                return Observable.just(trainStations);
                                            else
                                                return getDatasFromCloud(specification);
                                        });
                            }
                        });
    }

    private Observable<List<TrainStation>> getDatasFromCloud(final Specification specification) {
        return trainStationCloudDataStore.getDatas(specification)
                .flatMap((Func1<List<TrainStationIslandEntity>, Observable<List<TrainStation>>>)
                        trainStationIslandEntities ->
                                trainStationDbDataStore.insertAll(trainStationIslandEntities)
                                        .flatMap((Func1<Boolean, Observable<List<TrainStation>>>)
                                                isSuccessInsertData -> {
                                                    if (!isSuccessInsertData) {
                                                        return Observable.empty();
                                                    } else {
                                                        return trainStationCacheDataStore.updateExpiredTime()
                                                                .flatMap((Func1<Boolean, Observable<List<TrainStation>>>)
                                                                        isSuccessUpdateExpiredTime -> {
                                                                            if (!isSuccessUpdateExpiredTime) {
                                                                                return Observable.empty();
                                                                            } else {
                                                                                return trainStationDbDataStore.getDatas(specification);
                                                                            }
                                                                        });
                                                    }
                                                }));
    }

    public Observable<TrainStation> getStation(final Specification specification) {
        return trainStationCacheDataStore.isExpired().flatMap((Func1<Boolean, Observable<TrainStation>>)
                isExpired -> {
                    if (isExpired) {
                        return trainStationDbDataStore.deleteAll().flatMap((Func1<Boolean, Observable<TrainStation>>)
                                isSuccessDeletingData -> {
                                    if (isSuccessDeletingData)
                                        return getDataFromCloud(specification);
                                    else
                                        return Observable.empty();
                                });
                    } else {
                        return trainStationDbDataStore.getData(specification).flatMap((Func1<TrainStation, Observable<TrainStation>>)
                                trainStation -> {
                                    if (trainStation != null) {
                                        return Observable.just(trainStation);
                                    }
                                    return getDataFromCloud(specification);
                                });
                    }
                });
    }

    public Observable<TrainStation> getStation2(final Specification specification) {
        return trainStationCacheDataStore.isExpired().flatMap((Func1<Boolean, Observable<TrainStation>>)
                isExpired -> {
                    if (isExpired) {
                        return trainStationDbDataStore.deleteAll()
                                .flatMap((Func1<Boolean, Observable<TrainStation>>)
                                        isSuccessDeletingData -> {
                                            if (isSuccessDeletingData)
                                                return getDatasFromCloud(specification).
                                                        flatMap((Func1<List<TrainStation>, Observable<TrainStation>>)
                                                                trainStations -> trainStationDbDataStore.getData(specification));
                                            else
                                                return Observable.empty();
                                        });
                    } else {
                        return trainStationDbDataStore.getData(specification)
                                .flatMap((Func1<TrainStation, Observable<TrainStation>>)
                                        trainStation -> {
                                            if (trainStation != null) {
                                                return Observable.just(trainStation);
                                            }
                                            return getDataFromCloud(specification);
                                        });
                    }
                });
    }

    private Observable<TrainStation> getDataFromCloud(final Specification specification) {
        return trainStationCloudDataStore.getData(specification)
                .doOnNext(trainStationIslandEntities -> trainStationDbDataStore.insert(trainStationIslandEntities))
                .flatMap((Func1<TrainStationIslandEntity, Observable<TrainStation>>)
                        trainStationIslandEntities -> trainStationDbDataStore
                                .insert(trainStationIslandEntities)
                                .flatMap((Func1<Boolean, Observable<TrainStation>>)
                                        isSuccessInsertData -> {
                                            if (!isSuccessInsertData) {
                                                return Observable.empty();
                                            } else {
                                                return trainStationCacheDataStore.updateExpiredTime()
                                                        .flatMap((Func1<Boolean, Observable<TrainStation>>)
                                                                isSuccessUpdateExpiredTime -> {
                                                                    if (!isSuccessUpdateExpiredTime) {
                                                                        return Observable.empty();
                                                                    } else {
                                                                        return trainStationDbDataStore.getData(specification);
                                                                    }
                                                                }
                                                        );
                                            }
                                        }));
    }
}
