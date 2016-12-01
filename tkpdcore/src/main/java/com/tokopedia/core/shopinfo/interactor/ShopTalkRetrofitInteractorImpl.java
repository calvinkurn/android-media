package com.tokopedia.core.shopinfo.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.core.shopinfo.models.talkmodel.ShopTalkResult;

import java.net.ConnectException;
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
 * Created by nisie on 11/18/16.
 */

public class ShopTalkRetrofitInteractorImpl implements ShopTalkRetrofitInteractor {

    private static final String TAG = ShopTalkRetrofitInteractorImpl.class.getSimpleName();

    private final CompositeSubscription compositeSubscription;
    private final ShopService shopService;
    private final KunyitService kunyitService;
    private boolean isRequesting;

    public ShopTalkRetrofitInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
        shopService = new ShopService();
        kunyitService = new KunyitService();
    }

    @Override
    public void getShopTalk(@NonNull Context context,
                            @NonNull Map<String, String> params,
                            @NonNull final GetShopTalkListener listener) {
        setRequesting(true);
        Observable<Response<TkpdResponse>> observable = shopService.getApi()
                .getTalk(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    listener.onNoNetworkConnection();
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
                        listener.onSuccess(response.body().convertDataObj(ShopTalkResult.class));
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
    public void deleteTalk(@NonNull Context context, @NonNull Map<String, String> params,
                           @NonNull final ActionShopTalkListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .deleteProductTalk(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    listener.onNoNetworkConnection();
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
    public void followTalk(@NonNull Context context, @NonNull Map<String, String> params,
                           @NonNull final ActionShopTalkListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .followProductTalk(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    listener.onNoNetworkConnection();
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
    public void unfollowTalk(@NonNull Context context, @NonNull Map<String, String> params,
                             @NonNull final ActionShopTalkListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .followProductTalk(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    listener.onNoNetworkConnection();
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
    public void reportTalk(@NonNull Context context, @NonNull Map<String, String> params,
                           @NonNull final ActionShopTalkListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = kunyitService.getApi()
                .reportProductTalk(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    listener.onNoNetworkConnection();
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
        compositeSubscription.unsubscribe();
    }

    @Override
    public boolean isRequesting() {
        return isRequesting;
    }

    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }
}
