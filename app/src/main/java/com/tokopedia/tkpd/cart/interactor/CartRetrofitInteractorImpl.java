package com.tokopedia.tkpd.cart.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.cart.model.CartModel;
import com.tokopedia.tkpd.cart.model.cancelcart.CancelCartData;
import com.tokopedia.tkpd.network.apiservices.transaction.TXCartActService;
import com.tokopedia.tkpd.network.apiservices.transaction.TXService;
import com.tokopedia.tkpd.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.network.retrofit.response.ErrorListener;
import com.tokopedia.tkpd.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.network.retrofit.utils.AuthUtil;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.util.HashMap;
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
 * Created by Kris on 4/8/2016.
 * Modified by Angga.Prasetiyo
 */
public class CartRetrofitInteractorImpl implements CartRetrofitInteractor {

    private final CompositeSubscription compositeSubscription;
    private final TXService txService;
    private final TXCartActService txCartActService;

    public CartRetrofitInteractorImpl() {
        this.compositeSubscription = new CompositeSubscription();
        this.txService = new TXService();
        this.txCartActService = new TXCartActService();
    }

    @Override
    public void getCartInfo(@NonNull Context context,
                            @NonNull Map<String, String> params,
                            @NonNull final OnGetCartInfo listener) {
        Observable<Response<TkpdResponse>> observable = txService.getApi()
                .doPayment(AuthUtil.generateParams(context, new HashMap<String, String>()));

        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (e instanceof SocketTimeoutException) {
                    listener.onTimeout("Timeout connection," +
                            " Mohon ulangi beberapa saat lagi");
                } else {
                    listener.onNoConnection();
                }
            }

            @Override
            public void onNext(Response<TkpdResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isError()) {
                        listener.onSuccess(response.body().convertDataObj(CartModel.class));
                    } else {
                        listener.onError(response.body().getErrorMessages().get(0));
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
    public void cancelCart(@NonNull final Context context,
                           @NonNull final Map<String, String> params, @NonNull final OnCancelCart listener) {

        Func1<CancelCartData, Observable<CancelCartData>> funcCancel =
                new Func1<CancelCartData, Observable<CancelCartData>>() {
                    @Override
                    public Observable<CancelCartData> call(CancelCartData cancelCartData) {
                        return getObserveCancelCart(cancelCartData, context, params);
                    }
                };

        Func1<CancelCartData, Observable<CancelCartData>> funcRefresh =
                new Func1<CancelCartData, Observable<CancelCartData>>() {
                    @Override
                    public Observable<CancelCartData> call(CancelCartData cancelCartData) {
                        return getObserveCartData(cancelCartData, context, params);
                    }
                };

        Subscriber<CancelCartData> subscriber = new Subscriber<CancelCartData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CancelCartData data) {
                if (!data.isSuccessRefresh() && data.isSuccessCancel()) {
                    listener.onFailedRefreshCart(data.getMessageRefreshCart());
                } else if (!data.isSuccessRefresh() && !data.isSuccessCancel()) {
                    listener.onFailedCancelCart(data.getMessageCancelCart());
                } else if (data.isSuccessRefresh() && data.isSuccessCancel()) {
                    listener.onSuccess(data.getMessageCancelCart(), data.getCartData());
                } else {
                    listener.onError(data.getMessageCancelCart() + ". " + data.getMessageRefreshCart());
                }
            }
        };
        compositeSubscription.add(Observable.just(new CancelCartData())
                .flatMap(funcCancel)
                .flatMap(funcRefresh)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));


//        Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
//                .cancelCart(AuthUtil.generateParams(context, params));
//        Subscriber<Response<TkpdResponse>> subscriber = new Subscriber<Response<TkpdResponse>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                if (e instanceof SocketTimeoutException) {
//                    listener.onError("Timeout connection," +
//                            " Mohon ulangi beberapa saat lagi");
//                } else {
//                    listener.onError("Terjadi Kesalahan, " +
//                            "Mohon ulangi beberapa saat lagi");
//                }
//            }
//
//            @Override
//            public void onNext(Response<TkpdResponse> response) {
//                if (response.isSuccess() && !response.body().isError()
//                        && !response.body().isNullData()) {
//                    if (response.body().isError()) {
//                        listener.onError(response.body().getErrorMessages().get(0));
//                    } else {
//                        if (response.body().getJsonData().isNull("is_success")) {
//                            try {
//                                int status = response.body().getJsonData().getInt("is_success");
//                                String message = status == 1 ? response.body()
//                                        .getStatusMessages().get(0)
//                                        : response.body().getErrorMessages().get(0);
//                                switch (status) {
//                                    case 1:
//                                        listener.onSuccess(message, null);
//                                        break;
//                                    default:
//                                        listener.onError(message);
//                                        break;
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                listener.onError("Terjadi Kesalahan, Mohon ulangi beberapa saat lagi");
//                            }
//                        }
//                    }
//                } else {
//                    listener.onError("Terjadi Kesalahan, Mohon ulangi beberapa saat lagi");
//                }
//            }
//        };
//
//        compositeSubscription.add(observable.subscribeOn(Schedulers.newThread())
//                .unsubscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber));
    }

    private Observable<CancelCartData> getObserveCartData(CancelCartData cancelCartData,
                                                          final Context context,
                                                          Map<String, String> params) {
        Observable<Response<TkpdResponse>> observable = txService.getApi()
                .doPayment(AuthUtil.generateParams(context, new HashMap<String, String>()));
        return Observable.zip(Observable.just(cancelCartData), observable,
                new Func2<CancelCartData, Response<TkpdResponse>, CancelCartData>() {
                    @Override
                    public CancelCartData call(CancelCartData cancelCartData,
                                               Response<TkpdResponse> response) {
                        if (response.isSuccessful() && !response.body().isError()
                                && !response.body().isNullData()) {
                            if (response.body().isError()) {
                                cancelCartData.setSuccessRefresh(false);
                                cancelCartData.setMessageRefreshCart(response.body()
                                        .getErrorMessages().get(0));
                            } else {
                                CartModel data = response.body().convertDataObj(CartModel.class);
                                if (data != null) {
                                    cancelCartData.setSuccessRefresh(true);
                                    cancelCartData.setMessageRefreshCart(response.body()
                                            .getStatusMessages().get(0));
                                    cancelCartData.setCartData(data);
                                } else {
                                    cancelCartData.setSuccessRefresh(false);
                                    cancelCartData.setMessageRefreshCart(context
                                            .getString(R.string.default_request_error_unknown));
                                }
                            }
                        } else {
                            cancelCartData.setSuccessRefresh(false);
                            cancelCartData.setMessageRefreshCart(context
                                    .getString(R.string.default_request_error_unknown));
                        }
                        return cancelCartData;
                    }
                });
    }

    private Observable<CancelCartData> getObserveCancelCart(CancelCartData cancelCartData,
                                                            final Context context,
                                                            Map<String, String> params) {
        final Observable<Response<TkpdResponse>> observable = txCartActService.getApi()
                .cancelCart(AuthUtil.generateParams(context, params));
        return Observable.zip(Observable.just(cancelCartData), observable,
                new Func2<CancelCartData, Response<TkpdResponse>, CancelCartData>() {
                    @Override
                    public CancelCartData call(CancelCartData cancelCartData,
                                               Response<TkpdResponse> response) {
                        if (response.isSuccessful() && !response.body().isError()
                                && !response.body().isNullData()) {
                            if (response.body().isError()) {
                                cancelCartData.setSuccessCancel(false);
                                cancelCartData.setMessageCancelCart(response.body()
                                        .getErrorMessages().get(0));
                            } else {
                                if (!response.body().getJsonData().isNull("is_success")) {
                                    try {
                                        int status = response.body().getJsonData()
                                                .getInt("is_success");
                                        String message = status == 1 ? response.body()
                                                .getStatusMessages().get(0)
                                                : response.body().getErrorMessages().get(0);
                                        switch (status) {
                                            case 1:
                                                cancelCartData.setSuccessCancel(true);
                                                cancelCartData.setMessageCancelCart(message);
                                                break;
                                            default:
                                                cancelCartData.setSuccessCancel(false);
                                                cancelCartData.setMessageCancelCart(message);
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        cancelCartData.setSuccessCancel(false);
                                        cancelCartData.setMessageCancelCart(context
                                                .getString(R.string.default_request_error_unknown));
                                    }
                                }
                            }
                        } else {
                            cancelCartData.setSuccessCancel(false);
                            cancelCartData.setMessageCancelCart(context
                                    .getString(R.string.default_request_error_unknown));
                        }
                        return cancelCartData;
                    }
                });
    }

    @Override
    public void destroyObservable() {
        compositeSubscription.unsubscribe();
    }


}
