package com.tokopedia.core.shipping.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.shipping.model.kero.Rates;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Herdi_WORK on 20.09.16.
 */
public class KeroNetInteractorImpl implements  KeroNetInteractor{

    private final KeroAuthService keroService;
    private CompositeSubscription compositeSubscription;

    public KeroNetInteractorImpl(){
        keroService = new KeroAuthService();
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void calculateShipping(@NonNull Context context, @NonNull TKPDMapParam<String, String> params, @NonNull final CalculationListener listener) {
        Observable<Response<Rates>> observable = keroService
                .getApi()
                .calculateShippingRate(AuthUtil.generateParamsNetwork(context, params));

        Subscriber<Response<Rates>> subscriber = new Subscriber<Response<Rates>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Response<Rates> rates) {
                listener.onSuccess(rates.body().getData());
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void calculateKeroCartAddressShipping(@NonNull final Context context, @NonNull final TKPDMapParam<String, String> param, @NonNull final OnCalculateKeroAddressShipping listener) {
        Observable<Response<Rates>> observable = keroService.getApi().calculateShippingRate(param);
        Subscriber<Response<Rates>> subscriber = new Subscriber<Response<Rates>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                calculateKeroCartAddressShipping(context, param, listener);
            }

            @Override
            public void onNext(Response<Rates> response) {
                if (response.isSuccessful() && !response.body().isNullData()) {
                    listener.onSuccess(response.body().getData().getAttributes());
                } else {
                    calculateKeroCartAddressShipping(context, param, listener);
                }

            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void onViewDestroyed() {
        compositeSubscription.unsubscribe();
    }
}
