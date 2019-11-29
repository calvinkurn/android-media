package com.tokopedia.logisticaddaddress.data;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.logisticaddaddress.di.GeolocationScope;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.logisticaddaddress.features.pinpoint.GeolocationPresenter;
import com.tokopedia.network.utils.TKPDMapParam;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
 */
@GeolocationScope
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractorImpl.class.getSimpleName();
    private final CompositeSubscription compositeSubscription;
    private final IMapsRepository mapsRepository;

    @Inject
    public RetrofitInteractorImpl(MapsRepository mapsRepository) {
        this.compositeSubscription = new CompositeSubscription();
        this.mapsRepository = mapsRepository;
    }

    @Override
    public void generateLatLng(Map<String, String> param,
                               final GenerateLatLongListener listener) {
        TKPDMapParam<String, Object> paramaters = new TKPDMapParam<>();
        paramaters.putAll(param);
        compositeSubscription.add(mapsRepository.getLatLng(paramaters)
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
    public void generateLatLngGeoCode(Map<String, String> param,
                                      final GenerateLatLongListener listener) {
        TKPDMapParam<String, Object> paramaters = new TKPDMapParam<>();
        paramaters.putAll(param);
        compositeSubscription.add(mapsRepository.getLatLngFromGeocode(paramaters)
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
    public IMapsRepository getMapRepository() {
        return mapsRepository;
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }

}
