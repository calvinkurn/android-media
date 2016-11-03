package com.tokopedia.tkpd.manage.people.address.interactor;

import android.content.Context;
import android.util.Log;

import com.tokopedia.tkpd.manage.people.address.model.GetPeopleAddress;
import com.tokopedia.tkpd.network.NetworkErrorHelper;
import com.tokopedia.tkpd.network.apiservices.user.PeopleService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.MapNulRemover;

import java.io.IOException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created on 5/19/16.
 */
public class RetrofitInteractorImpl implements RetrofitInteractor {

    private static final String TAG = RetrofitInteractorImpl.class.getSimpleName();

    private final CompositeSubscription compsoiteSubscription;
    private final PeopleService peopleService;

    public RetrofitInteractorImpl() {
        this.compsoiteSubscription = new CompositeSubscription();
        this.peopleService = new PeopleService();
    }

    @Override
    public void unsubscribe() {
        compsoiteSubscription.unsubscribe();
    }

    @Override
    public void getPeopleAddress(final Context context,
                                 final Map<String, String> params,
                                 final GetPeopleAddressListener listener) {
        listener.onPreExecute(context, params);

        Observable<Response<TkpdResponse>> observable = peopleService.getApi().getAddress(MapNulRemover.removeNull(params));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getPeopleAddress(context, params, listener);
                        }
                    });
                } else {
                    listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            getPeopleAddress(context, params, listener);
                        }
                    });
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        GetPeopleAddress data =
                                response.body().convertDataObj(GetPeopleAddress.class);
                        if (data.getList().size() != 0) {
                            listener.onSuccess(context, params, data);
                        } else {
                            listener.onNullData();
                        }
                    } else {
                        if (response.body().isNullData()) {
                            listener.onNullData();
                        } else {
                            listener.onError(
                                    response.body().getErrorMessages().get(0),
                                    new NetworkErrorHelper.RetryClickedListener() {
                                        @Override
                                        public void onRetryClicked() {
                                            getPeopleAddress(context, params, listener);
                                        }
                                    });
                        }
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getPeopleAddress(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeOut(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getPeopleAddress(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getPeopleAddress(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getPeopleAddress(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(null, new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    getPeopleAddress(context, params, listener);
                                }
                            });
                        }
                    }, response.code());
                }
            }
        };

        compsoiteSubscription.add(
                observable.subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber)
        );
    }
}
