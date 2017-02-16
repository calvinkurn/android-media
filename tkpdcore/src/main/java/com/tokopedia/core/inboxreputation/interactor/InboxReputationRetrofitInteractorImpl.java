package com.tokopedia.core.inboxreputation.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.inboxreputation.model.inboxreputation.InboxReputation;
import com.tokopedia.core.inboxreputation.model.inboxreputationdetail.InboxReputationDetail;
import com.tokopedia.core.inboxreputation.model.inboxreputationsingle.SingleReview;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.apiservices.user.InboxReputationService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.reputationproduct.model.LikeDislike;

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
 * Created by Nisie on 21/01/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class InboxReputationRetrofitInteractorImpl implements InboxReputationRetrofitInteractor {

    private static final String TAG = InboxReputationRetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";
    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";

    private final CompositeSubscription compositeSubscription;
    private final InboxReputationService reputationService;
    private final ShopService shopService;

    private boolean isRequesting = false;

    public InboxReputationRetrofitInteractorImpl() {
        this.reputationService = new InboxReputationService();
        this.compositeSubscription = new CompositeSubscription();
        this.shopService = new ShopService();
    }

    @Override
    public void getInboxReputation(@NonNull final Context context, @NonNull final Map<String, String> params,
                                   @NonNull final InboxReputationListener listener) {

        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = reputationService.getApi()
                .getInbox(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                setRequesting(false);
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

                        listener.onSuccess(response.body().convertDataObj(InboxReputation.class));
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
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
                    setRequesting(false);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));

    }

    @Override
    public void getInboxReputationDetail(@NonNull final Context context,
                                         @NonNull final Map<String, String> params,
                                         @NonNull final InboxReputationRetrofitInteractor
                                                 .InboxReputationDetailListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = reputationService.getApi()
                .getListReview(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                setRequesting(false);
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
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
                        listener.onSuccess(response.body().convertDataObj(InboxReputationDetail.class));
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
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
                    setRequesting(false);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getSingleReview(@NonNull final Context context,
                                @NonNull final Map<String, String> params,
                                @NonNull final InboxReputationSingleListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = reputationService.getApi()
                .getSingleReview(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                setRequesting(false);
                Log.e(TAG, e.toString());
                if (e instanceof UnknownHostException) {
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
                        listener.onSuccess(response.body().convertDataObj(SingleReview.class));
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
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
                    setRequesting(false);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void getLikeDislikeReview(@NonNull final Context context, @NonNull Map<String, String> params, @NonNull final LikeDislikeListener listener) {
        setRequesting(true);

        Observable<Response<TkpdResponse>> observable = shopService.getApi()
                .getLikeReview(AuthUtil.generateParams(context, params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                setRequesting(false);
            }

            @Override
            public void onError(Throwable e) {
                setRequesting(false);
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
                        listener.onSuccess(response.body().convertDataObj(LikeDislike.class));
                    } else if (response.body().getStatus().equals(TOO_MANY_REQUEST)) {
                        listener.onError(response.body().getErrorMessageJoined());
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
                    setRequesting(false);
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }


    @Override
    public boolean isUnsubscribed() {
        return compositeSubscription.isUnsubscribed();
    }

    @Override
    public void unSubscribeObservable() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public boolean isRequesting() {
        return isRequesting;
    }

    @Override
    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }

}
