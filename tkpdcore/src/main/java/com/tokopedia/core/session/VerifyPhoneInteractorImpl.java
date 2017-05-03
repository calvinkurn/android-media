package com.tokopedia.core.session;

/**
 * Created by stevenfredian on 12/30/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.rxjava.RxUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.tokopedia.core.session.presenter.Login.MSISDN;
import static com.tokopedia.core.session.presenter.Login.USER_ID;

/**
 * Created by stevenfredian on 11/8/16.
 */

public class VerifyPhoneInteractorImpl implements VerifyPhoneInteractor{

    private CompositeSubscription compositeSubscription;
    private AccountsService accountsService;


    public static VerifyPhoneInteractor createInstance() {
        VerifyPhoneInteractorImpl interactor = new VerifyPhoneInteractorImpl();
        interactor.accountsService = new AccountsService(new Bundle());
        interactor.compositeSubscription = new CompositeSubscription();
        return interactor;
    }

    @Override
    public void verifyPhone(final Context context, String phoneNumber, String userId, final VerifyPhoneListener listener) {
        Map<String, String> params = new HashMap<>();
        params = AuthUtil.generateParams(getApplicationContext(),params);
        params.put(MSISDN, phoneNumber);
        params.put(USER_ID, userId);

        Observable<Response<TkpdResponse>> observable = accountsService.getApi().verifyPhone(params);

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Verify Phone Number", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        listener.onSuccess(result.optInt("allow_login", 0), result.optString("uuid"));
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                }
                else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(context.getResources().getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(context.getResources().getString(R.string.default_request_error_timeout));
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(context.getResources().getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(context.getResources().getString(R.string.default_request_error_unknown));
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(context.getResources().getString(R.string.default_request_error_forbidden_auth));
                        }
                    }, response.code());
                }
            }
        };

        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void unSubscribe() {
        if(compositeSubscription.hasSubscriptions()){
            compositeSubscription.unsubscribe();
        }
    }
}