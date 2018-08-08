package com.tokopedia.core.deposit.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.deposit.model.SummaryWithdraw;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;

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
 * Created by Nisie on 4/7/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class DepositRetrofitInteractorImpl implements DepositRetrofitInteractor {
    private static final String TAG = DepositRetrofitInteractorImpl.class.getSimpleName();

    private final CompositeSubscription compositeSubscription;
    private final DepositService depositService;
    private boolean isRequesting = false;

    public DepositRetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.depositService = new DepositService();
    }


    @Override
    public void getSummaryDeposit(@NonNull final Context context, @NonNull final Map<String, String> params, @NonNull final DepositListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = depositService.getApi()
                .getSummary(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
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
                        listener.onSuccess(response.body().convertDataObj(SummaryWithdraw.class));
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
