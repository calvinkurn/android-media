package com.tokopedia.tkpd.selling.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.tkpd.network.apiservices.transaction.TrackingOderService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.network.retrofit.utils.DialogNoConnection;
import com.tokopedia.tkpd.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.selling.model.tracking.TrackingResponse;

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
 * Created by Alifa on 10/12/2016.
 */

public class TrackingRetrofitInteractorImpl implements TrackingRetrofitInteractor {

    private static final String TAG = TrackingRetrofitInteractorImpl.class.getSimpleName();
    private final CompositeSubscription compositeSubscription;
    private final TrackingOderService service;
    private boolean isRequesting = false;

    public TrackingRetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.service = new TrackingOderService();
    }


    @Override
    public void getDataTracking(final @NonNull Context context, final @NonNull TKPDMapParam<String, String> params, final @NonNull TrackingListener listener) {
        Observable<Response<TkpdResponse>> observable = service.getApi().trackOrder(AuthUtil.generateParamsNetwork(context,params));
        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof UnknownHostException) {
                    listener.onNoNetworkConnection();
                    DialogNoConnection.createShow(context,
                            new DialogNoConnection.ActionListener() {
                                @Override
                                public void onRetryClicked() {
                                    getDataTracking(context, params, listener);
                                }
                            });
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
                        listener.onSuccess(response.body().convertDataObj(TrackingResponse.class));
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
