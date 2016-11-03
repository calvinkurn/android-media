package com.tokopedia.tkpd.manage.people.address.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.tkpd.manage.people.address.model.ChooseAddress.ChooseAddressResponse;
import com.tokopedia.tkpd.network.apiservices.transaction.TXCartService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.network.retrofit.utils.TKPDMapParam;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Alifa on 10/11/2016.
 */

public class ChooseAddressRetrofitInteractorImpl implements ChooseAddressRetrofitInteractor{

    private static final String TAG = ChooseAddressRetrofitInteractorImpl.class.getSimpleName();

    private final CompositeSubscription compositeSubscription;
    private final TXCartService service;
    private boolean isRequesting = false;


    public ChooseAddressRetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.service = new TXCartService();
    }


    @Override
    public void getAddresses(@NonNull Context context, @NonNull TKPDMapParam<String, String> params, final @NonNull ChooseAddressListener listener) {
        Observable<Response<TkpdResponse>> observable = service.getApi().cartSearchAddress(AuthUtil.generateParamsNetwork(context,params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    listener.onNoNetworkConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout("Timeout connection," +
                            " Mohon ulangi beberapa saat lagi");
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(ChooseAddressResponse.class));
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout("Timeout connection," +
                                    " Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
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
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

    @Override
    public boolean isRequesting() {
        return isRequesting;
    }
}
