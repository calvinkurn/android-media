package com.tokopedia.tkpd.home.presenter;

import android.util.Log;

import com.tokopedia.tkpd.app.MainApplication;
import com.tokopedia.tkpd.home.api.CategoryApi;
import com.tokopedia.tkpd.home.facade.FacadePromo;
import com.tokopedia.tkpd.home.model.Banner;
import com.tokopedia.tkpd.home.model.Slide;
import com.tokopedia.tkpd.home.model.Ticker;
import com.tokopedia.tkpd.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.tkpd.rxjava.RxUtils;
import com.tokopedia.tkpd.util.SessionHandler;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by noiz354 on 2/24/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class CategoryImpl implements Category {
    CategoryApi categoryApi;
    CategoryView view;
    CompositeSubscription subscription = new CompositeSubscription();

    public CategoryImpl(CategoryView view) {
        categoryApi = RetrofitUtils.createRetrofit(CategoryApi.MOJITO).create(CategoryApi.class);
        this.view = view;
    }

    private static final String TAG = CategoryImpl.class.getSimpleName();


    @Override
    public void fetchSlides(final FacadePromo.GetPromoListener listener) {
        Subscriber<Response<Slide>> subscriber = new Subscriber<Response<Slide>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, messageTAG + "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, messageTAG + " -> " + e);
                listener.OnError();
            }

            @Override
            public void onNext(Response<Slide> slideResponse) {
                Slide s = slideResponse.body();

                if (slideResponse.isSuccessful() && s.getData() != null)
                    listener.OnSuccessBanner(FacadePromo.parseTickerList(s));
                else
                    listener.OnError();
            }
        };

        subscription
                .add(
                        categoryApi.getSlides(
                                CategoryApi.size,
                                CategoryApi.number,
                                CategoryApi.ANDROID_DEVICE,
                                CategoryApi.TARGET_ALL,
                                CategoryApi.state,
                                CategoryApi.expired
                        ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(
                                        subscriber
                                )
                );
    }

    @Override
    public void fetchTickers(final FetchTickersListener listener) {
        Subscriber<Response<Ticker>> subscriber = new Subscriber<Response<Ticker>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, messageTAG + "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, messageTAG + " -> " + e);
                listener.onError();
            }

            @Override
            public void onNext(Response<Ticker> response) {
                if (response.isSuccessful() && response.body().getData() != null) {
                    listener.onSuccess(response.body().getData().getTickers());
                } else {
                    listener.onError();
                }

            }
        };

        subscription
                .add(
                        categoryApi.getTickers(
                                SessionHandler.getLoginID(MainApplication.getAppContext()),
                                CategoryApi.size,
                                CategoryApi.FILTER_ANDROID_DEVICE
                        ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(
                                        subscriber
                                )
                );
    }

    @Override
    public void fetchBanners(final FacadePromo.GetPromoListener listener) {
        Subscriber<Response<Banner>> subscriber = new Subscriber<Response<Banner>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, messageTAG + "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                listener.OnError();
            }

            @Override
            public void onNext(Response<Banner> bannerResponse) {
                Banner b = bannerResponse.body();

                if (bannerResponse.isSuccessful() && b.getData() != null)
                    view.onSuccessFetchBanners(b);

            }
        };
        subscription
                .add(
                        categoryApi.getBanners(
                                CategoryApi.size,
                                CategoryApi.ANDROID_DEVICE,
                                CategoryApi.TARGET_BANNER,
                                CategoryApi.state,
                                CategoryApi.newExpired,
                                CategoryApi.TARGET_SLIDE_TYPE
                        ).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(
                                        subscriber
                                )
                );
    }

    @Override
    public void subscribe() {
        RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
    }

    @Override
    public void unSubscribe() {
        RxUtils.unsubscribeIfNotNull(subscription);
    }
}
