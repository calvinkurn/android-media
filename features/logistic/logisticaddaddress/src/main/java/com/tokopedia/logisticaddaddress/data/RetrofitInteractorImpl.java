package com.tokopedia.logisticaddaddress.data;

import com.tokopedia.logisticCommon.data.entity.geolocation.coordinate.uimodel.CoordinateUiModel;
import com.tokopedia.logisticaddaddress.di.GeolocationScope;
import com.tokopedia.network.utils.TKPDMapParam;

import java.util.Map;

import javax.inject.Inject;

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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CoordinateUiModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof RuntimeException)
                            listener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(CoordinateUiModel model) {
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CoordinateUiModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof RuntimeException)
                            listener.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(CoordinateUiModel model) {
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
