package com.tokopedia.core.geolocation.interactor;

import android.content.Context;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.core.geolocation.presenter.GoogleMapPresenterImpl;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.core.database.model.LatLngModelDB_Table.locationID;

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
    public void generateAddress(final Context context, final GenerateAddressListener listener) {

        listener.onPreConnection();

        LocalCacheHandler cache = new LocalCacheHandler(context, GoogleMapPresenterImpl.CACHE_LATITUDE_LONGITUDE);

        compositeSubscription.add(Observable.just(cache)
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<LocalCacheHandler, LocationPass>() {
                    @Override
                    public LocationPass call(LocalCacheHandler cache) {

                        double latitude = Double.parseDouble(cache.getString(GoogleMapPresenterImpl.CACHE_LATITUDE));
                        double longitude = Double.parseDouble(cache.getString(GoogleMapPresenterImpl.CACHE_LONGITUDE));

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
