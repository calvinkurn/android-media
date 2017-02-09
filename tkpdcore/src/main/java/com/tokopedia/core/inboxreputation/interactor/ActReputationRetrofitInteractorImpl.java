package com.tokopedia.core.inboxreputation.interactor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.core.inboxreputation.model.ImageUpload;
import com.tokopedia.core.inboxreputation.model.actresult.ActResult;
import com.tokopedia.core.inboxreputation.model.actresult.ImageUploadResult;
import com.tokopedia.core.inboxreputation.model.param.ActReviewPass;
import com.tokopedia.core.inboxreputation.model.param.GenerateHostPass;
import com.tokopedia.core.inboxreputation.model.param.UploadImageReviewParam;
import com.tokopedia.core.network.apiservices.product.ReviewActService;
import com.tokopedia.core.network.apiservices.shop.ReputationActService;
import com.tokopedia.core.network.apiservices.upload.GenerateHostActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.GeneratedHost;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.ImageUploadHandler;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
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
    public void skipReview(@NonNull final Context context, @NonNull final Map<String, String> params,
                           @NonNull final ActReputationListener listener) {

        Observable<Response<TkpdResponse>> observable = reputationActService.getApi()
                .skipRepReview(AuthUtil.generateParams(context, params));

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
    public void postReview(@NonNull final Context context,
                           @NonNull final ActReviewPass paramReview,
                           @NonNull final ActReputationListener listener) {

        Observable<Response<TkpdResponse>> observable = Observable.just(paramReview)
                .flatMap(new Func1<ActReviewPass, Observable<ActReviewPass>>() {
                    @Override
                    public Observable<ActReviewPass> call(final ActReviewPass reviewPass) {
                        if (isHasPictures(reviewPass)) {
                            return reputationActService.getApi()
                                    .insertReviewValidation(AuthUtil.generateParams(context,
                                            reviewPass.getInsertReputationReviewParamWithImages()))
                                    .map(new Func1<Response<TkpdResponse>, ActReviewPass>() {
                                        @Override
                                        public ActReviewPass call(Response<TkpdResponse> tkpdResponse) {
                                            ActResult result = tkpdResponse.body().convertDataObj(ActResult.class);
                                            if (result.getIsSuccess() == 1) {
                                                reviewPass.setPostKey(result.getPostKey());
                                                return reviewPass;
                                            } else {
                                                String errorMessage = "";
                                                for (int i = 0; i < tkpdResponse.body().getErrorMessages().size(); i++) {
                                                    errorMessage += tkpdResponse.body().getErrorMessages().get(i);
                                                }
                                                throw new RuntimeException(errorMessage);
                                            }

                                        }
                                    });
                        } else {
                            return Observable.just(reviewPass);
                        }
                    }
                })
                .flatMap(new Func1<ActReviewPass, Observable<ActReviewPass>>() {
                    @Override
                    public Observable<ActReviewPass> call(ActReviewPass reviewPass) {
                        if (isHasPictures(reviewPass)) {
                            return getObservableGenerateHost(context, reviewPass);
                        } else {
                            return Observable.just(reviewPass);
                        }
                    }
                })
                .flatMap(new Func1<ActReviewPass, Observable<ActReviewPass>>() {
                    @Override
                    public Observable<ActReviewPass> call(ActReviewPass reviewPass) {
                        if (isHasPictures(reviewPass)) {
                            return getObservableUploadingFile(context, reviewPass);
                        } else {
                            return Observable.just(reviewPass);
                        }
                    }
                })
                .flatMap(new Func1<ActReviewPass, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(ActReviewPass reviewPass) {
                        if (isHasPictures(reviewPass)) {
                            return reputationActService.getApi()
                                    .insertReviewSubmit(AuthUtil.generateParams(context, reviewPass.getSubmitParam()));
                        } else {
                            return reputationActService.getApi()
                                    .insertReviewValidation(AuthUtil.generateParams(context, reviewPass.getInsertReputationReviewParam()));
                        }
                    }
                });


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
                } else if (e instanceof RuntimeException) {
                    listener.onError(e.getMessage());
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
    public void editReview(@NonNull final Context context, @NonNull final ActReviewPass params,
                           @NonNull final ActReputationListener listener) {

        Observable<Response<TkpdResponse>> observable = Observable.just(params)

                .flatMap(new Func1<ActReviewPass, Observable<ActReviewPass>>() {
                    @Override
                    public Observable<ActReviewPass> call(ActReviewPass reviewPass) {
                        if (isHasPictures(reviewPass)) {
                            Observable<Response<TkpdResponse>> editValidation;
                            editValidation = reputationActService.getApi().editProductReviewValidation(AuthUtil.generateParams(context, reviewPass.getEditParamWithImages()));

                            return Observable.zip(Observable.just(reviewPass), editValidation, new Func2<ActReviewPass, Response<TkpdResponse>, ActReviewPass>() {
                                @Override
                                public ActReviewPass call(ActReviewPass reviewPass, Response<TkpdResponse> response) {
                                    if (response.isSuccessful()) {
                                        ActResult result = response.body().convertDataObj(ActResult.class);
                                        if (result.getIsSuccess() == 1) {
                                            reviewPass.setPostKey(result.getPostKey());
                                            return reviewPass;
                                        } else {
                                            String errorMessage = "";
                                            for (int i = 0; i < response.body().getErrorMessages().size(); i++) {
                                                errorMessage += response.body().getErrorMessages().get(i);
                                            }
                                            throw new RuntimeException(errorMessage);
                                        }
                                    } else {
                                        String errorMessage = "";
                                        for (int i = 0; i < response.body().getErrorMessages().size(); i++) {
                                            errorMessage += response.body().getErrorMessages().get(i);
                                        }
                                        throw new RuntimeException(errorMessage);
                                    }
                                }
                            });
                        } else {
                            return Observable.just(reviewPass);
                        }
                    }
                })
                .flatMap(new Func1<ActReviewPass, Observable<ActReviewPass>>() {
                    @Override
                    public Observable<ActReviewPass> call(ActReviewPass reviewPass) {
                        if (isHasNewPicture(reviewPass)) {
                            return getObservableGenerateHost(context, reviewPass);
                        } else {
                            return Observable.just(reviewPass);
                        }
                    }
                })
                .flatMap(new Func1<ActReviewPass, Observable<ActReviewPass>>() {
                    @Override
                    public Observable<ActReviewPass> call(ActReviewPass reviewPass) {
                        if (isHasNewPicture(reviewPass)) {
                            return getObservableUploadingFile(context, reviewPass);
                        } else {
                            return Observable.just(reviewPass);
                        }
                    }
                })
                .flatMap(new Func1<ActReviewPass, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(ActReviewPass reviewPass) {
                        if (isHasPictures(reviewPass)) {
                            return reputationActService.getApi().editProductReviewSubmit(AuthUtil.generateParams(context, reviewPass.getSubmitParam()));
                        } else {
                            return reputationActService.getApi().editProductReviewValidation(AuthUtil.generateParams(context, reviewPass.getEditParam()));
                        }
                    }
                });


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
                } else if (e instanceof RuntimeException) {
                    listener.onError(e.getMessage());
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
    public void postReputation(@NonNull final Context context, @NonNull final Map<String, String> params,
                               @NonNull final ActReputationListener listener) {

        Observable<Response<TkpdResponse>> observable = reputationActService.getApi()
                .insertRep(AuthUtil.generateParams(context, params));

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

    private Observable<ActReviewPass> getObservableGenerateHost(Context context,
                                                                final ActReviewPass paramReview) {
        GenerateHostPass paramGenerateHost = new GenerateHostPass();
        paramGenerateHost.setNewAdd("2");

        GenerateHostActService generateHostActService = new GenerateHostActService();
        return generateHostActService.getApi()
                .generateHost(AuthUtil.generateParams(context, paramGenerateHost.getGenerateHostParam()))
                .map(new Func1<GeneratedHost, ActReviewPass>() {
                    @Override
                    public ActReviewPass call(GeneratedHost generatedHost) {
                        paramReview.setGeneratedHost(generatedHost);
                        return paramReview;
                    }
                });
    }

    private Observable<ActReviewPass> getObservableUploadingFile(final Context context,
                                                                 final ActReviewPass paramReview) {

        return Observable.zip(Observable.just(paramReview), uploadFile(context, paramReview),
                new Func2<ActReviewPass, List<ImageUpload>, ActReviewPass>() {
                    @Override
                    public ActReviewPass call(ActReviewPass reviewPass, List<ImageUpload> imageUploads) {
                        reviewPass.setImageUploads(imageUploads);
                        return reviewPass;
                    }
                });
    }

    private Observable<List<ImageUpload>> uploadFile(final Context context, final ActReviewPass paramReview) {
        return Observable
                .from(paramReview.getImageUploads())
                .flatMap(new Func1<ImageUpload, Observable<ImageUpload>>() {
                    @Override
                    public Observable<ImageUpload> call(ImageUpload imageUpload) {
                        if (isNewUploadImage(imageUpload)) {
                            String uploadUrl = "http://" + paramReview.getGeneratedHost().getUploadHost();
                            NetworkCalculator networkCalculator = new NetworkCalculator(
                                    NetworkConfig.POST, context,
                                    uploadUrl)
                                    .setIdentity()
                                    .addParam(PARAM_IMAGE_ID, imageUpload.getImageId())
                                    .addParam(PARAM_TOKEN, paramReview.getToken())
                                    .addParam(PARAM_WEB_SERVICE, "1")
                                    .compileAllParam()
                                    .finish();

                            File file;
                            try {
                                file = ImageUploadHandler.writeImageToTkpdPath(ImageUploadHandler.compressImage(imageUpload.getFileLoc()));
                            } catch (IOException e) {
                                throw new RuntimeException(context.getString(R.string.error_upload_image));
                            }
                            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                                    networkCalculator.getContent().get(NetworkCalculator.USER_ID));
                            RequestBody deviceId = RequestBody.create(MediaType.parse("text/plain"),
                                    networkCalculator.getContent().get(NetworkCalculator.DEVICE_ID));
                            RequestBody hash = RequestBody.create(MediaType.parse("text/plain"),
                                    networkCalculator.getContent().get(NetworkCalculator.HASH));
                            RequestBody deviceTime = RequestBody.create(MediaType.parse("text/plain"),
                                    networkCalculator.getContent().get(NetworkCalculator.DEVICE_TIME));
                            RequestBody fileToUpload = RequestBody.create(MediaType.parse("image/*"),
                                    file);
                            RequestBody imageId = RequestBody.create(MediaType.parse("text/plain"),
                                    networkCalculator.getContent().get(PARAM_IMAGE_ID));
                            RequestBody token = RequestBody.create(MediaType.parse("text/plain"),
                                    networkCalculator.getContent().get(PARAM_TOKEN));
                            RequestBody web_service = RequestBody.create(MediaType.parse("text/plain"),
                                    networkCalculator.getContent().get(PARAM_WEB_SERVICE));

                            Log.d(TAG + "(step 2):host = ", paramReview.getGeneratedHost().getUploadHost());
                            Observable<ImageUploadResult> upload = RetrofitUtils.createRetrofit(uploadUrl)
                                    .create(UploadImageReviewParam.class)
                                    .uploadImage(
                                            networkCalculator.getHeader().get(NetworkCalculator.CONTENT_MD5),// 1
                                            networkCalculator.getHeader().get(NetworkCalculator.DATE),// 2
                                            networkCalculator.getHeader().get(NetworkCalculator.AUTHORIZATION),// 3
                                            networkCalculator.getHeader().get(NetworkCalculator.X_METHOD),// 4
                                            userId,
                                            deviceId,
                                            hash,
                                            deviceTime,
                                            fileToUpload,
                                            imageId,
                                            token,
                                            web_service
                                    );


                            return Observable.zip(Observable.just(imageUpload), upload, new Func2<ImageUpload, ImageUploadResult, ImageUpload>() {
                                @Override
                                public ImageUpload call(ImageUpload imageUpload, ImageUploadResult imageUploadResult) {
                                    if (imageUploadResult.getData() != null) {
                                        imageUpload.setPicSrc(imageUploadResult.getData().getPicSrc());
                                        imageUpload.setPicObj(imageUploadResult.getData().getPicObj());
                                    } else if (imageUploadResult.getMessageError() != null) {
                                        throw new RuntimeException(imageUploadResult.getMessageError());
                                    }
                                    return imageUpload;
                                }
                            });
                        } else {
                            return Observable.just(imageUpload);
                        }
                    }
                })
                .toList();
    }

    private boolean isNewUploadImage(ImageUpload imageUpload) {
        return imageUpload.getImageId().startsWith(ActReviewPass.NEW_IMAGE_INDICATOR);
    }

    private boolean isHasPictures(ActReviewPass reviewPass) {
        return reviewPass.getImageUploads().size() > 0 || reviewPass.getDeletedImageUploads().size() != 0;
    }

    private boolean isHasNewPicture(ActReviewPass reviewPass) {
        for (ImageUpload image : reviewPass.getImageUploads()) {
            if (isNewUploadImage(image)) {
                return true;
            }
        }
        return false;
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
