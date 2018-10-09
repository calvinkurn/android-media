package com.tokopedia.tkpd.tkpdreputation.shopreputation.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.network.apiservices.product.ReviewActService;
import com.tokopedia.core.network.apiservices.shop.ReputationActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.pojo.ActResult;

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
 * Created by Nisie on 2/9/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ActReputationRetrofitInteractorImpl implements
        ActReputationRetrofitInteractor {

    private static final String TAG = ActReputationRetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";
    private static final String PARAM_IMAGE_ID = "id";
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_WEB_SERVICE = "web_service";
    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";

    private static final String PARAM_REVIEW_ID = "review_id";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_REPORT_MESSAGE = "text_message";

    private final CompositeSubscription compositeSubscription;
    private final ReputationActService reputationActService;
    private final ReviewActService reviewActService;

    private boolean isRequesting = false;

    public ActReputationRetrofitInteractorImpl() {
        this.reputationActService = new ReputationActService();
        this.compositeSubscription = new CompositeSubscription();
        this.reviewActService = new ReviewActService();
    }

    @Override
    public void postComment(@NonNull final Context context, @NonNull final Map<String, String> params,
                            @NonNull final ActReputationListener listener) {

        Observable<Response<TkpdResponse>> observable = reputationActService.getApi()
                .insertRepReviewResponse(AuthUtil.generateParams(context, params));

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
                        listener.onSuccess(response.body().convertDataObj(ActResult.class));
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
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void deleteComment(@NonNull final Context context, @NonNull final Map<String, String> params,
                              @NonNull final ActReputationListener listener) {

        Observable<Response<TkpdResponse>> observable = reputationActService.getApi()
                .deleteRepReviewResponse(AuthUtil.generateParams(context, params));

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
                        listener.onSuccess(response.body().convertDataObj(ActResult.class));
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
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void postReport(@NonNull final Context context, @NonNull final Map<String, String> params, @NonNull final ActReputationListener listener) {

        Observable<Response<TkpdResponse>> observable = reviewActService.getApi()
                .reportReview(AuthUtil.generateParams(context, params));

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
                        listener.onSuccess(response.body().convertDataObj(ActResult.class));
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
                }
            }
        };
        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    @Override
    public void likeDislikeReview(@NonNull Context context, @NonNull Map<String, String> params, @NonNull final ActReputationListener listener) {

        Observable<Response<TkpdResponse>> observable = reviewActService.getApi()
                .likeDislikeReview(AuthUtil.generateParams(context, params));

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
                        listener.onSuccess(response.body().convertDataObj(ActResult.class));
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

}
