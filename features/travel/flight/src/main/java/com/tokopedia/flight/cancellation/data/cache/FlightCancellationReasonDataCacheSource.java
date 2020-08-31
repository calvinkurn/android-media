package com.tokopedia.flight.cancellation.data.cache;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.flight.cancellation.data.cloud.entity.Reason;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by furqan on 25/10/18.
 */

public class FlightCancellationReasonDataCacheSource {

    private static long FLIGHT_CANCELLATION_REASON_CACHE_TIMEOUT = TimeUnit.DAYS.toSeconds(10);
    private static String FLIGHT_CANCELLATION_REASON_CACHE_KEY = "FLIGHT_CANCELLATION_REASON_CACHE_KEY";

    @Inject
    public FlightCancellationReasonDataCacheSource() {
    }

    public Observable<Boolean> isExpired() {
        return Observable.just(PersistentCacheManager.instance.isExpired(FLIGHT_CANCELLATION_REASON_CACHE_KEY));
    }

    public Observable<Boolean> deleteCache() {
        PersistentCacheManager.instance.delete(FLIGHT_CANCELLATION_REASON_CACHE_KEY);
        return Observable.just(true);
    }

    public Observable<List<Reason>> getCache() {
        String jsonString = PersistentCacheManager.instance.getString(FLIGHT_CANCELLATION_REASON_CACHE_KEY);
        Type type = new TypeToken<List<Reason>>() {
        }.getType();
        List<Reason> data = CacheUtil.convertStringToListModel(jsonString, type);
        return Observable.just(data);
    }

    public void saveCache(List<Reason> orderEntity) {
        PersistentCacheManager.instance.delete(FLIGHT_CANCELLATION_REASON_CACHE_KEY);

        Type type = new TypeToken<List<Reason>>() {
        }.getType();

        PersistentCacheManager.instance.put(FLIGHT_CANCELLATION_REASON_CACHE_KEY,
                CacheUtil.convertListModelToString(orderEntity, type),
                FLIGHT_CANCELLATION_REASON_CACHE_TIMEOUT);
    }
}
