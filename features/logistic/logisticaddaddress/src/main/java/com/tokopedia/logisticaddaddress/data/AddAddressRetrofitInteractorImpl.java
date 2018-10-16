package com.tokopedia.logisticaddaddress.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import com.tokopedia.logisticaddaddress.di.AddressScope;
import com.tokopedia.logisticdata.data.apiservice.AddressApi;
import com.tokopedia.logisticdata.data.apiservice.PeopleActApi;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.user.session.UserSession;

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
    private final AddressApi addressService;

    @Inject
    public AddAddressRetrofitInteractorImpl(PeopleActApi peopleActApi, AddressApi addressApi) {
        this.compositeSubscription = new CompositeSubscription();
        this.peopleActService = peopleActApi;
        this.addressService = addressApi;
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
                Log.e(TAG, e.toString());
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
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
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
                Log.e(TAG, e.toString());
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
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    //todo : refactor this to network repository class
    private TKPDMapParam<String, String> generateTKPDParam(Context context, TKPDMapParam<String, String> param) {
        UserSession session = new UserSession(context);
        return AuthUtil.generateParamsNetwork(session.getUserId(), session.getDeviceId(), param);
    }

}
