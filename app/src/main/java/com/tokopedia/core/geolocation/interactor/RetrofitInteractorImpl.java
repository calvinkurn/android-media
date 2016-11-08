package com.tokopedia.core.geolocation.interactor;

import android.util.Log;

import com.tokopedia.core.database.manager.LatLngCacheManager;
import com.tokopedia.core.geolocation.model.LocationPass;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hangnadi on 1/31/16.
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractorImpl.class.getSimpleName();
    private final CompositeSubscription compositeSubscription;

    public RetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void generateAddress(final GenerateAddressListener listener) {

        listener.onPreConnection();

        LatLngCacheManager cacheManager = new LatLngCacheManager();

        compositeSubscription.add(Observable.just(cacheManager)
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<LatLngCacheManager, LocationPass>() {
                    @Override
                    public LocationPass call(LatLngCacheManager cacheManager) {

                        String locationID = String.valueOf(LatLngCacheManager.CountTable());
                        cacheManager.getCache(locationID);

                        double latitude = cacheManager.getLatitude();
                        double longitude = cacheManager.getLongitude();

                        return listener.convertData(latitude, longitude);
                    }
                })
                .delaySubscription(3000, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<LocationPass>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "generateAddress: onError " + e.toString());
                        listener.onError(e);
                    }

                    @Override
                    public void onNext(LocationPass model) {
                        listener.onSuccess(model);
                    }
                }));

    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
