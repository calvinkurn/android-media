package com.tokopedia.core.peoplefave.retrofit;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.user.FaveShopActService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.peoplefave.model.PeopleFavoritedShopData;

import java.io.IOException;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hangnadi on 10/11/16.
 */
public class PeopleFavoritedShopInteractorImpl implements PeopleFavoritedShopInteractor {

    private static final String TAG = PeopleFavoritedShopInteractorImpl.class.getSimpleName();

    private final PeopleService peopleService;
    private final CompositeSubscription compositeSubscription;
    private final FaveShopActService faveShopActService;

    public PeopleFavoritedShopInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.peopleService = new PeopleService();
        this.faveShopActService = new FaveShopActService();
    }

    @Override
    public void requestDataService(final Context context,
                                   final Map<String, String> params,
                                   final PeopleFavoritedShopListener listener) {
        listener.onStart();

        Observable<Response<TkpdResponse>> observable =
                peopleService.getApi().getFavoriteShop(params);

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            requestDataService(context, params, listener);
                        }
                    });
                } else {
                    listener.onError(null);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        PeopleFavoritedShopData data = response.body().convertDataObj(PeopleFavoritedShopData.class);
                        if (data.getList().size() != 0) {
                            listener.onSuccess(data);
                        } else {
                            listener.onNullData();
                        }
                    } else {
                        if (response.body().isNullData()) listener.onNullData();
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(null);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeout(new NetworkErrorHelper.RetryClickedListener() {
                                @Override
                                public void onRetryClicked() {
                                    requestDataService(context, params, listener);
                                }
                            });
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(null);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(null);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(null);
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
    public void updateDataService(final Context context, final Map<String, String> params, final ActionFavoritedShopListener listener) {

        listener.onStart();

        Observable<Response<TkpdResponse>> observable =
                faveShopActService.getApi().faveShop(params);

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
                if (e instanceof IOException) {
                    listener.onTimeOut();
                } else {
                    listener.onError(null);
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        PeopleFavoritedShopData data = response.body().convertDataObj(PeopleFavoritedShopData.class);
                        if (data.getIsSuccess() != 0) {
                            listener.onSuccess();
                        } else {
                            listener.onError(null);
                        }
                    } else {
                        if (response.body().isNullData()) listener.onError(null);
                        else listener.onError(response.body().getErrorMessages().get(0));
                    }
                } else {
                    new ErrorHandler(new ErrorListener() {
                        @Override
                        public void onUnknown() {
                            listener.onError(null);
                        }

                        @Override
                        public void onTimeout() {
                            listener.onTimeOut();
                        }

                        @Override
                        public void onServerError() {
                            listener.onError(null);
                        }

                        @Override
                        public void onBadRequest() {
                            listener.onError(null);
                        }

                        @Override
                        public void onForbidden() {
                            listener.onError(null);
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
}
