package com.tokopedia.core.session;

/**
 * Created by stevenfredian on 12/30/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.SessionHandler;

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
import static com.tokopedia.core.network.apiservices.accounts.AccountsService.AUTH_KEY;
import static com.tokopedia.core.network.apiservices.accounts.AccountsService.USING_BOTH_AUTHORIZATION;
import static com.tokopedia.core.session.presenter.Login.MSISDN;
import static com.tokopedia.core.session.presenter.Login.USER_ID;

/**
 * Created by stevenfredian on 11/8/16.
 */

public class VerifyPhoneInteractorImpl implements VerifyPhoneInteractor{

    private CompositeSubscription compositeSubscription;
    private AccountsService accountsService;
    private final String HEADER_AUTHORIZATION_KEY = "Authorization";
    private final String HEADER_DATE_TIME_KEY = "DateTime";


    public static VerifyPhoneInteractor createInstance() {
        VerifyPhoneInteractorImpl interactor = new VerifyPhoneInteractorImpl();
        interactor.compositeSubscription = new CompositeSubscription();
        return interactor;
    }

    @Override
    public void verifyPhone(final Context context, String phoneNumber, String userId, final VerifyPhoneListener listener) {
        Map<String, String> params = new HashMap<>();
        params = AuthUtil.generateParams(getApplicationContext(),params);
        params.put(MSISDN, phoneNumber);
        params.put(USER_ID, userId);

        Bundle bundle = new Bundle();
        bundle.putBoolean(USING_BOTH_AUTHORIZATION, true);
        SessionHandler sessionHandler = new SessionHandler(context);
        bundle.putString(AccountsService.AUTH_KEY,
                String.format("Bearer %s", sessionHandler.getAccessToken(context)));
//        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
        accountsService = new AccountsService(bundle);
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

                    //get hmac from response header
                    String responseHmac = response.headers().get(HEADER_AUTHORIZATION_KEY);
                    String responseDate = response.headers().get(HEADER_DATE_TIME_KEY);

                    //first to verify that response is not tampered in between by verifying the hmac
                    String hmac = AuthUtil.generateHmacForContentTypeJson(TkpdBaseURL.Truecaller.VERIFY_PHONE, tkpdResponse.getStrResponse() , "POST" , responseDate , AuthUtil.KEY.KEY_WSV4);
                    if(responseHmac == null || !hmac.equals(responseHmac)){
                        listener.onError(context.getResources().getString(R.string.default_request_error_unknown_short));
                        return;
                    }

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