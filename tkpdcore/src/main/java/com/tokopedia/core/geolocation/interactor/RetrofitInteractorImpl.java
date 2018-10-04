package com.tokopedia.core.geolocation.interactor;

import android.content.Context;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.geolocation.domain.IMapsRepository;
import com.tokopedia.core.geolocation.domain.MapsRepository;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.geolocation.model.autocomplete.Prediction;
import com.tokopedia.core.geolocation.model.coordinate.CoordinateModel;
import com.tokopedia.core.geolocation.model.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.core.geolocation.presenter.GoogleMapPresenterImpl;
import com.tokopedia.core.network.apiservices.maps.MapService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

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
    private final IMapsRepository mapsRepository;
    private final MapService service;

    public RetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.mapsRepository = new MapsRepository();
        this.service = new MapService();
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
    public void generateLatLng(Context context, TKPDMapParam<String, String> param,
                               final GenerateLatLongListener listener) {
        TKPDMapParam<String, Object> paramaters = new TKPDMapParam<>();
        paramaters.putAll(param);
        compositeSubscription.add(mapsRepository.getLatLng(service, paramaters)
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CoordinateViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof RuntimeException)
                            listener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(CoordinateViewModel model) {
                        listener.onSuccess(model);
                    }
                }));
    }

    @Override
    public void generateLatLngGeoCode(Context context, TKPDMapParam<String, String> param,
                                      final GenerateLatLongListener listener) {
        TKPDMapParam<String, Object> paramaters = new TKPDMapParam<>();
        paramaters.putAll(param);
        compositeSubscription.add(mapsRepository.getLatLngFromGeocode(service, paramaters)
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CoordinateViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof RuntimeException)
                            listener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(CoordinateViewModel model) {
                        listener.onSuccess(model);
                    }
                }));
    }

    @Override
    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }

    @Override
    public MapService getMapService() {
        return service;
    }

    @Override
    public IMapsRepository getMapRepository() {
        return mapsRepository;
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }

}
