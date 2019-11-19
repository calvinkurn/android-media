package com.tokopedia.core.util.getproducturlutil.interactor;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.tokopedia.core.network.apiservices.ace.AceSearchService;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.shopinfo.models.productmodel.ShopProductResult;

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
 * Created by Nisie on 6/2/16.
 *
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class GetProductUrlRetrofitInteractorImpl implements GetProductUrlRetrofitInteractor {

    private static final String TAG = GetProductUrlRetrofitInteractorImpl.class.getSimpleName();
    private static final String DEFAULT_MSG_ERROR = "Terjadi Kesalahan, Mohon ulangi beberapa saat lagi";

    private final CompositeSubscription compositeSubscription;
    private final ShopService shopService;
    private final AceSearchService searchService;


    private boolean isRequesting = false;

    public GetProductUrlRetrofitInteractorImpl() {
        this.shopService = new ShopService();
        this.searchService = new AceSearchService();
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getProductUrl(@NonNull Context context,
                              @NonNull Map<String, String> params,
                              @NonNull final GetProductUrlListener listener) {
        setRequesting(true);

        Observable<Response<ShopProductResult>> observable = searchService.getApi()
                .getShopProduct(MapNulRemover.removeNull(params));

        Subscriber<Response<ShopProductResult>> subscriber = new Subscriber<Response<ShopProductResult>>() {
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
            public void onNext(Response<ShopProductResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResult().getProducts().size() == 0)
                        listener.onNullData();
                    else listener.onSuccess(response.body().getResult());

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
    public void unSubscribeObservable() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public boolean isRequesting() {
        return this.isRequesting;
    }

    @Override
    public void setRequesting(boolean isRequesting) {
        this.isRequesting = isRequesting;
    }
}
