package com.tokopedia.train.station.data;

import com.tokopedia.train.station.data.database.TrainStationDao;
import com.tokopedia.train.station.data.entity.TrainStationIslandEntity;
import com.tokopedia.train.station.domain.model.TrainStation;
import com.tokopedia.train.station.domain.model.mapper.TrainStationMapper;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 31/01/19.
 */
public class TrainStationDataStoreNewFactory {

    private TrainStationCloudDataStore trainStationCloudDataStore;
    private TrainStationCacheDataStore trainStationCacheDataStore;
    private TrainStationDao trainStationDao;
    private TrainStationMapper mapper;

    public TrainStationDataStoreNewFactory(TrainStationCloudDataStore trainStationCloudDataStore,
                                           TrainStationCacheDataStore trainStationCacheDataStore,
                                           TrainStationDao trainStationDao,
                                           TrainStationMapper mapper) {
        this.trainStationCloudDataStore = trainStationCloudDataStore;
        this.trainStationCacheDataStore = trainStationCacheDataStore;
        this.trainStationDao = trainStationDao;
        this.mapper = mapper;
    }

    public Observable<List<TrainStation>> getAllStations() {
        return getStations().map(it -> trainStationDao.getAllStation())
                .map(it -> mapper.transformToModel(it));
    }

    public Observable<List<TrainStation>> getPopularStation() {
        return getStations().map(it -> trainStationDao.getPopularStations())
                .map(it -> mapper.transformToModel(it));
    }

    public Observable<List<TrainStation>> getStationByKeyword(String keyword) {
        String query = keyword + "%";
        return getStations().map(it -> trainStationDao.getStationsByKeyword(query))
                .map(it -> mapper.transformToModel(it));
    }

    public Observable<List<TrainStation>> getStationCitiesByKeyword(String keyword) {
        String query = keyword + "%";
        return getStations().map(it -> trainStationDao.getStationsCitiesByKeyword(query))
                .map(it -> mapper.transformToModel(it));
    }

    public Observable<TrainStation> getStationByStationCode(String stationCode) {
        return Observable.just(trainStationDao.getStationByStationCode(stationCode))
                .map(it -> mapper.transform(it));
    }

    public Observable<Boolean> getStations() {
        return trainStationCacheDataStore.isExpired()
                .flatMap((Func1<Boolean, Observable<Boolean>>)
                        (Boolean isExpired) -> {
                            if (isExpired) {
                                return Observable.just(trainStationDao.deleteAll())
                                        .flatMap((Func1<Integer, Observable<List<Long>>>)
                                                integer -> getDatasFromCloud())
                                        .map(longs -> true);
                            } else {
                                return Observable.just(true);
                            }
                        });
    }

    private Observable<List<Long>> getDatasFromCloud() {
        return trainStationCloudDataStore.getDatas()
                .flatMap((Func1<List<TrainStationIslandEntity>, Observable<List<Long>>>)
                        trainStationIslandEntities -> Observable.just(trainStationIslandEntities)
                                .map(it -> mapper.transformToTable(it))
                                .map(it -> trainStationDao.insertAll(it)));
    }
}
