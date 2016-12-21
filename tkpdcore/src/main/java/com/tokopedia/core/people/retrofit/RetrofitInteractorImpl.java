package com.tokopedia.core.people.retrofit;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.network.NetworkErrorHelper.RetryClickedListener;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.people.model.InputOutputData;
import com.tokopedia.core.people.model.PeopleAddressData;
import com.tokopedia.core.people.model.PeopleFavShop;
import com.tokopedia.core.people.model.PeopleInfoData;
import com.tokopedia.core.people.model.PeoplePrivacyData;

import java.io.IOException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 5/31/16.
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractorImpl.class.getSimpleName();
    private static final int RESPONSE_NULL_DATA = 7;
    private static final int RESPONSE_ERROR_DATA = 8;
    private static final int REQUEST_NETWORK_ERROR = 9;

    private final CompositeSubscription compositeSubscription;

    public RetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public void getPeopleInfo(final Context context,
                              final Map<String, String> params,
                              final GetPeopleInfoListener listener) {

        InputOutputData data = new InputOutputData();
        data.setParams(params);

        Observable<InputOutputData> observable =
                Observable
                        .just(data)
                        .flatMap(new Func1<InputOutputData, Observable<InputOutputData>>() {
                            @Override
                            public Observable<InputOutputData> call(InputOutputData IOData) {
                                return getPeopleInfoData(IOData);
                            }
                        })
                        .flatMap(new Func1<InputOutputData, Observable<InputOutputData>>() {
                            @Override
                            public Observable<InputOutputData> call(InputOutputData IOData) {
                                if (IOData.isByPass()) {
                                    return Observable.just(IOData);
                                } else {
                                    return getPeopleFavorite(IOData);
                                }
                            }
                        })
                        .flatMap(new Func1<InputOutputData, Observable<InputOutputData>>() {
                            @Override
                            public Observable<InputOutputData> call(InputOutputData IOData) {
                                if (IOData.isByPass()) {
                                    return Observable.just(IOData);
                                } else {
                                    return getPeoplePrivacy(IOData);
                                }
                            }
                        })
                        .flatMap(new Func1<InputOutputData, Observable<InputOutputData>>() {
                            @Override
                            public Observable<InputOutputData> call(InputOutputData IOData) {
                                if (IOData.isByPass()) {
                                    return Observable.just(IOData);
                                } else {
                                    return getPeopleAddress(IOData);
                                }
                            }
                        });

        Subscriber<InputOutputData> subscriber =
                new Subscriber<InputOutputData>() {
                    @Override
                    public void onCompleted() {
                        listener.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        if (e instanceof IOException) {
                            listener.onTimeout(new RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getPeopleInfo(context, params, listener);
                                }
                            });
                        }  else {
                            listener.onError("Terjadi Kesalahan, " +
                                    "Mohon ulangi beberapa saat lagi");
                        }
                    }

                    @Override
                    public void onNext(InputOutputData data) {
                        Log.d(TAG + "onNext()", String.valueOf(data.isByPass()));
                        if (data.isByPass()) {
                            switch (data.getErrorType()) {
                                case RESPONSE_ERROR_DATA:
                                    listener.onError(data.getErrorMessage());
                                    break;
                                case RESPONSE_NULL_DATA:
                                    listener.onNullData();
                                    break;
                                case REQUEST_NETWORK_ERROR:
                                    new ErrorHandler(new ErrorListener() {
                                        @Override
                                        public void onUnknown() {
                                            listener.onError("Terjadi Kesalahan, " +
                                                    "Mohon ulangi beberapa saat lagi");
                                        }

                                        @Override
                                        public void onTimeout() {
                                            listener.onTimeout(new RetryClickedListener() {
                                                @Override
                                                public void onRetryClicked() {
                                                    getPeopleInfo(context, params, listener);
                                                }
                                            });
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
                                    }, data.getErrorCode());
                                    break;
                                default:
                                    throw new RuntimeException("Something worst happened");
                            }
                        } else {
                            listener.onSuccess(data);
                        }
                    }
                };

        compositeSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }

    private Observable<InputOutputData> getPeopleFavorite(InputOutputData data) {
        PeopleService peopleService = new PeopleService();
        Observable<Response<TkpdResponse>> observable1 = peopleService.getApi().getRandomFavShop(data.getParams());
        return Observable.zip(Observable.just(data), observable1, new Func2<InputOutputData, Response<TkpdResponse>, InputOutputData>() {
            @Override
            public InputOutputData call(InputOutputData data, Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG + "getPeopleFavorite()", "response success");
                    if (!response.body().isError()) {
                        data.setPeopleFavShopData(response.body().convertDataObj(PeopleFavShop.class));
                    } else {
                        if (response.body().isNullData()) {
                            data.setByPass(true);
                            data.setErrorType(RESPONSE_NULL_DATA);
                        } else {
                            data.setByPass(true);
                            data.setErrorType(RESPONSE_ERROR_DATA);
                            data.setErrorMessage(response.body().getErrorMessages().get(0));
                        }
                    }
                } else {
                    data.setByPass(true);
                    data.setErrorType(REQUEST_NETWORK_ERROR);
                    data.setErrorCode(response.code());
                }
                return data;
            }
        });
    }

    private Observable<InputOutputData> getPeopleAddress(InputOutputData data) {
        PeopleService peopleService = new PeopleService();
        Observable<Response<TkpdResponse>> observable1 = peopleService.getApi().getAddress(data.getParams());
        return Observable.zip(Observable.just(data), observable1, new Func2<InputOutputData, Response<TkpdResponse>, InputOutputData>() {
            @Override
            public InputOutputData call(InputOutputData data, Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG + "getPeopleAddress()", "response success");
                    if (!response.body().isError()) {
                        data.setPeopleAddressData(response.body().convertDataObj(PeopleAddressData.class));
                    } else {
                        if (response.body().isNullData()) {
                            data.setByPass(true);
                            data.setErrorType(RESPONSE_NULL_DATA);
                        } else {
                            data.setByPass(true);
                            data.setErrorType(RESPONSE_ERROR_DATA);
                            data.setErrorMessage(response.body().getErrorMessages().get(0));
                        }
                    }
                } else {
                    data.setByPass(true);
                    data.setErrorType(REQUEST_NETWORK_ERROR);
                    data.setErrorCode(response.code());
                }
                return data;
            }
        });
    }

    private Observable<InputOutputData> getPeoplePrivacy(InputOutputData data) {
        PeopleService peopleService = new PeopleService();
        Observable<Response<TkpdResponse>> observable1 = peopleService.getApi().getPrivacy(data.getParams());
        return Observable.zip(Observable.just(data), observable1, new Func2<InputOutputData, Response<TkpdResponse>, InputOutputData>() {
            @Override
            public InputOutputData call(InputOutputData data, Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG + "getPeoplePrivacy()", "response success");
                    if (!response.body().isError()) {
                        data.setPeoplePrivacyData(response.body().convertDataObj(PeoplePrivacyData.class));
                    } else {
                        if (response.body().isNullData()) {
                            data.setByPass(true);
                            data.setErrorType(RESPONSE_NULL_DATA);
                        } else {
                            data.setByPass(true);
                            data.setErrorType(RESPONSE_ERROR_DATA);
                            data.setErrorMessage(response.body().getErrorMessages().get(0));
                        }
                    }
                } else {
                    data.setByPass(true);
                    data.setErrorType(REQUEST_NETWORK_ERROR);
                    data.setErrorCode(response.code());
                }
                return data;
            }
        });
    }

    private Observable<InputOutputData> getPeopleInfoData(InputOutputData data) {
        PeopleService peopleService = new PeopleService();
        Observable<Response<TkpdResponse>> observable1 = peopleService.getApi().getPeopleInfo(data.getParams());
        return Observable.zip(Observable.just(data), observable1, new Func2<InputOutputData, Response<TkpdResponse>, InputOutputData>() {
            @Override
            public InputOutputData call(InputOutputData data, Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG + "getPeopleInfoData()", "response success");
                    if (!response.body().isError()) {
                        data.setPeopleInfoData(response.body().convertDataObj(PeopleInfoData.class));
                    } else {
                        if (response.body().isNullData()) {
                            data.setByPass(true);
                            data.setErrorType(RESPONSE_NULL_DATA);
                        } else {
                            data.setByPass(true);
                            data.setErrorType(RESPONSE_ERROR_DATA);
                            data.setErrorMessage(response.body().getErrorMessages().get(0));
                        }
                    }
                } else {
                    data.setByPass(true);
                    data.setErrorType(REQUEST_NETWORK_ERROR);
                    data.setErrorCode(response.code());
                }
                return data;
            }
        });
    }
}
