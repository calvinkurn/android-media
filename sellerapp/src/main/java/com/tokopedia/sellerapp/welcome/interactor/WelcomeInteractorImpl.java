package com.tokopedia.sellerapp.welcome.interactor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.R;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.sellerapp.welcome.presenter.WelcomeFragmentPresenterImpl;

import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by stevenfredian on 10/5/16.
 */

public class WelcomeInteractorImpl implements WelcomeInteractor {

    private CompositeSubscription compositeSubscription;
    private AccountsService accountsService;

    public static WelcomeInteractor createInstance(WelcomeFragmentPresenterImpl presenter) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
        
        WelcomeInteractorImpl interactor = new WelcomeInteractorImpl();
        interactor.compositeSubscription = new CompositeSubscription();
        interactor.accountsService = new AccountsService(bundle);
        return interactor;
    }

    @Override
    public void downloadProvider(final Context context, final DiscoverLoginListener listener) {
        if(compositeSubscription.isUnsubscribed()){
            this.compositeSubscription = new CompositeSubscription();
        }

        Observable<Response<TkpdResponse>> observable = accountsService.getApi()
                .discoverLogin(RequestParams.EMPTY.getParameters());

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("Login Provider", e.toString());
                listener.onThrowable(e);
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    final TkpdResponse tkpdResponse = response.body();
                    if (!tkpdResponse.isError()) {
                        JSONObject result = tkpdResponse.getJsonData();
                        LoginProviderModel loginProviderModel = new GsonBuilder().create()
                                .fromJson(result.toString(), LoginProviderModel.class);
                        listener.onSuccess(loginProviderModel);
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
                    }
                }
                else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError(context.getString(R.string.default_request_error_timeout));
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(context.getString(R.string.default_request_error_internal_server));
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(context.getString(R.string.default_request_error_bad_request));
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(context.getString(R.string.default_request_error_forbidden_auth));
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
        compositeSubscription.unsubscribe();
    }
}
