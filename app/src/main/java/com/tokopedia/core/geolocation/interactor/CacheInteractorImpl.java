package com.tokopedia.core.geolocation.interactor;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.database.manager.LatLngCacheManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hangnadi on 1/31/16.
 */
public class CacheInteractorImpl implements CacheInteractor {

    private static final String TAG = CacheInteractorImpl.class.getSimpleName();

    public CacheInteractorImpl() {
    }

    @Override
    public void dropCache() {
        LatLngCacheManager.dropCache();
    }

    @Override
    public void storeLatLng(double latitude, double longitude) {
        LatLngCacheManager cacheManager = new LatLngCacheManager();
        cacheManager.setLatitude(latitude);
        cacheManager.setLongitude(longitude);
        Observable.just(cacheManager)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<LatLngCacheManager, LatLngCacheManager>() {
                    @Override
                    public LatLngCacheManager call(LatLngCacheManager latLngCacheManager) {
                        return latLngCacheManager.saveCache();
                    }
                })
                .subscribe(new Subscriber<LatLngCacheManager>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "storeLatLng: onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "storeLatLng: onError - " + e.toString());
                    }

                    @Override
                    public void onNext(LatLngCacheManager latLngCacheManager) {
                        Log.d(TAG, "storeLatLng: onNext");
                    }
                });
    }

    @Override
    public void getLastCache(final OnGetLastCacheListener listener) {
        LatLngCacheManager cacheManager = new LatLngCacheManager();

        Observable.just(cacheManager)
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<LatLngCacheManager, LatLng>() {
                    @Override
                    public LatLng call(LatLngCacheManager cacheManager) {

                        String locationID = String.valueOf(LatLngCacheManager.CountTable());
                        cacheManager.getCache(locationID);

                        double latitude = cacheManager.getLatitude();
                        double longitude = cacheManager.getLongitude();

                        return new LatLng(latitude, longitude);
                    }
                })
                .delaySubscription(10000, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<LatLng>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "getLastCache: onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "getLastCache: onError " + e.toString());
                        listener.onError();
                    }

                    @Override
                    public void onNext(LatLng latLng) {
                        listener.onSuccess(latLng);
                    }
                });
    }

}
