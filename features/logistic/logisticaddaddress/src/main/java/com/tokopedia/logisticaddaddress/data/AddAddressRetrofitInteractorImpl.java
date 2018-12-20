package com.tokopedia.logisticaddaddress.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.logisticdata.data.module.qualifier.AddressScope;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 9/6/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
@AddressScope
public class AddAddressRetrofitInteractorImpl implements AddressRepository {

    private static final String TAG = AddAddressRetrofitInteractorImpl.class.getSimpleName();
    private static final String DB_NAME = "tokopedia";

    private final CompositeSubscription compositeSubscription;
    private final PeopleActApi peopleActService;

    @Inject
    public AddAddressRetrofitInteractorImpl(PeopleActApi peopleActApi) {
        this.compositeSubscription = new CompositeSubscription();
        this.peopleActService = peopleActApi;
    }

    @Override
    public void addAddress(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final AddAddressListener listener) {
        Observable<Response<TokopediaWsV4Response>> observable = peopleActService
                .addAddress(params);

        Subscriber<Response<TokopediaWsV4Response>> subscriber = new Subscriber<Response<TokopediaWsV4Response>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    listener.onNoNetworkConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError("");
                }
            }

            @Override
            public void onNext(Response<TokopediaWsV4Response> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        try {
                            listener.onSuccess(response.body().getJsonData().getString("address_id"));
                        } catch (JSONException e) {
                            listener.onSuccess("");
                        }
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    listener.onError(response.message());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void editAddress(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final AddAddressListener listener) {
        Observable<Response<TokopediaWsV4Response>> observable = peopleActService
                .editAddAddress(params);

        Subscriber<Response<TokopediaWsV4Response>> subscriber = new Subscriber<Response<TokopediaWsV4Response>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    listener.onNoNetworkConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError("");
                }
            }

            @Override
            public void onNext(Response<TokopediaWsV4Response> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        try {
                            listener.onSuccess(response.body().getJsonData().getString("address_id"));
                        } catch (JSONException e) {
                            listener.onSuccess("");
                        }
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    listener.onError(response.message());
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

}
