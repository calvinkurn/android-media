package com.tokopedia.tkpd.msisdn.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.tkpd.msisdn.model.VerificationForm;
import com.tokopedia.tkpd.network.apiservices.user.MSISDNActService;
import com.tokopedia.tkpd.network.apiservices.user.MSISDNService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.rxjava.RxUtils;

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
 * Created by Nisie on 7/13/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class MsisdnVerificationRetrofitInteractorImpl implements MsisdnVerificationRetrofitInteractor {

    private static final String TAG = MsisdnVerificationRetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";

    private final CompositeSubscription compositeSubscription;
    private final MSISDNActService msisdnActService;
    private final MSISDNService msisdnService;


    public MsisdnVerificationRetrofitInteractorImpl() {
        this.msisdnActService = new MSISDNActService();
        this.compositeSubscription = new CompositeSubscription();
        this.msisdnService = new MSISDNService();

    }

    @Override
    public void checkVerification(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final CheckVerificationListener listener) {
        Observable<Response<TkpdResponse>> observable = msisdnService.getApi()
                .getVerificationNumberForm(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(VerificationForm.class));
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Network Forbidden Error!");
                            listener.onFailAuth();
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
    public void requestOTP(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final RequestOTPListener listener) {
        Observable<Response<TkpdResponse>> observable = msisdnActService.getApi()
                .sendVerificationOTP(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Network Forbidden Error!");
                            listener.onFailAuth();
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
    public void verifyOTP(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final VerifyOTPListener listener) {
        Observable<Response<TkpdResponse>> observable = msisdnActService.getApi()
                .doVerification(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
                    listener.onNoConnection();
                } else if (e instanceof SocketTimeoutException) {
                    listener.onTimeout();
                } else {
                    listener.onError("Terjadi Kesalahan, " +
                            "Mohon ulangi beberapa saat lagi");
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess();
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError("Network Unknown Error!");
                        }

                        @Override
                        public void onTimeout() {
                            listener.onError("Network Timeout Error!");
                            listener.onTimeout();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError("Network Internal Server Error!");
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError("Network Bad Request Error!");
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError("Network Forbidden Error!");
                            listener.onFailAuth();
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
    public void unSubscribeObservable() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }
}
