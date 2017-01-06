package com.tokopedia.core.otp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.network.apiservices.user.OTPOnCallService;
import com.tokopedia.core.network.apiservices.user.InterruptActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONException;

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
 * Created by nisie on 12/20/16.
 */

public class OTPRetrofitInteractorImpl implements OTPRetrofitInteractor {

    private static final String TAG = OTPRetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";
    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";

    private final CompositeSubscription compositeSubscription;
    private final InterruptActService interruptActService;

    public OTPRetrofitInteractorImpl() {
        this.interruptActService = new InterruptActService();
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void requestOTP(@NonNull Context context,
                           @NonNull Map<String, String> params,
                           @NonNull final OTPRetrofitInteractor.RequestOTPListener listener) {
        Observable<Response<TkpdResponse>> observable = interruptActService.getApi().requestOTP(params);

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
                    try {
                        if (!response.body().isError() && response.body().getJsonData().getString("is_success").equals("1")) {
                            listener.onSuccess();
                        } else {
                            if (response.body().getStatus().equals(TOO_MANY_REQUEST))
                                listener.onError(response.body().getErrorMessages().toString().replace("[", "").replace("]", ""));
                            else if (response.body().isNullData()) listener.onNullData();
                            else listener.onError(response.body().getErrorMessages().get(0));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError(response.body().getErrorMessages().toString().replace("[", "").replace("]", ""));
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
    public void requestOTPWithCall(@NonNull Context context,
                                   @NonNull Map<String, String> params,
                                   @NonNull final OTPRetrofitInteractor.RequestOTPWithCallListener listener) {
        OTPOnCallService otpOnCallS = new OTPOnCallService(new SessionHandler(context).getAccessToken(context));

        Observable<Response<TkpdResponse>> observable = otpOnCallS.getApi().requestOTPWithCall(
                SessionHandler.getTempLoginSession(context),
                params);

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
                        listener.onSuccess(response.body().getStatusMessages().toString().replace("[", "").replace("]", ""));
                    } else {
                        if (response.body().getStatus().equals(TOO_MANY_REQUEST))
                            listener.onError(response.body().getErrorMessages().toString().replace("[", "").replace("]", ""));
                        else if (!response.body().getStatusMessages().isEmpty())
                            listener.onSuccess(response.body().getStatusMessages().toString().replace("[", "").replace("]", ""));
                        else if (!response.body().getErrorMessages().isEmpty())
                            listener.onError(response.body().getErrorMessages().toString().replace("[", "").replace("]", ""));
                        else
                            listener.onNullData();
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
    public void verifyOTP(@NonNull Context context,
                          @NonNull Map<String, String> params,
                          @NonNull final OTPRetrofitInteractor.VerifyOTPListener listener) {
        Observable<Response<TkpdResponse>> observable = interruptActService.getApi().requestOTP(params);

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
    public void unsubscribeObservable() {
        compositeSubscription.unsubscribe();
    }
}
