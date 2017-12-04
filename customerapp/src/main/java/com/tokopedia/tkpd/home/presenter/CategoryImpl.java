package com.tokopedia.tkpd.home.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashModel;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.core.network.entity.home.Slide;
import com.tokopedia.core.network.entity.home.Ticker;
import com.tokopedia.core.network.retrofit.utils.RetrofitUtils;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.home.facade.FacadePromo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by noiz354 on 2/24/16.
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class CategoryImpl implements Category {

    private static final String TICKER_CLOSED_CACHE_KEY = "TICKER_CLOSED_CACHE";
    private static final String TICKER_FIRST_ANNOUNCEMENT_KEY = "TICKER_FIRST_ANNOUNCEMENT";
    CategoryApi categoryApi;
    CategoryView view;
    CompositeSubscription subscription = new CompositeSubscription();
    private Context context;
    private final LocalCacheHandler tickerCacheHandler;

    public CategoryImpl(Context context, CategoryView view) {
        categoryApi = RetrofitUtils.createRetrofit(CategoryApi.MOJITO).create(CategoryApi.class);
        this.context = context;
        this.view = view;
        this.tickerCacheHandler = new LocalCacheHandler(this.context, TICKER_CLOSED_CACHE_KEY);
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
                                SessionHandler.getLoginID(MainApplication.getAppContext()),
                                CategoryApi.size,
                                CategoryApi.number,
                                CategoryApi.ANDROID_DEVICE,
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
        if (MainApplication.getIsResetTickerState()) {
            resetTickerState();
        }

        List<Ticker.Tickers> showedTickers = CacheUtil.convertStringToListModel(tickerCacheHandler.getString(TICKER_FIRST_ANNOUNCEMENT_KEY),
                new TypeToken<ArrayList<Ticker.Tickers>>() {
                }.getType());

        if (showedTickers != null) {
            listener.onSuccess(new ArrayList<Ticker.Tickers>(showedTickers));
        }
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
                MainApplication.setIsResetTickerState(false);
                if (response.isSuccessful() && response.body().getData() != null) {
                    ArrayList<Ticker.Tickers> showedTickers = new ArrayList<>();
                    for (Ticker.Tickers tickersItem : response.body().getData().getTickers()) {
                        showedTickers.add(tickersItem);
                    }

                    tickerCacheHandler.putString(TICKER_FIRST_ANNOUNCEMENT_KEY, CacheUtil.convertListModelToString(showedTickers,
                            new TypeToken<ArrayList<Ticker.Tickers>>() {
                            }.getType()));
                    tickerCacheHandler.applyEditor();
                    listener.onSuccess(showedTickers);
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
    public void closeTicker() {
        tickerCacheHandler.putBoolean(TICKER_CLOSED_CACHE_KEY, true);
    }

    @Override
    public boolean isTickerClosed() {
        return tickerCacheHandler.getBoolean(TICKER_CLOSED_CACHE_KEY, false);
    }

    @Override
    public void resetTickerState() {
        tickerCacheHandler.putBoolean(TICKER_CLOSED_CACHE_KEY, false);
    }

    @Override
    public void fetchCacheTokocash() {
        subscription.add(
                Observable.just(new GlobalCacheManager())
                        .map(new Func1<GlobalCacheManager, TokoCashModel>() {
                            @Override
                            public TokoCashModel call(GlobalCacheManager globalCacheManager) {
                                if (globalCacheManager.isAvailable(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE)) {
                                    String cacheWallet = globalCacheManager.getValueString(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE);
                                    TokoCashModel tokoCashModel = CacheUtil.convertStringToModel(cacheWallet,
                                            new TypeToken<TokoCashModel>() {
                                            }.getType());
                                    return tokoCashModel;
                                }
                                return null;
                            }
                        })
                        .subscribe(new Subscriber<TokoCashModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                view.onErrorFetchTokoCashDataFromCache(e.getMessage());
                            }

                            @Override
                            public void onNext(TokoCashModel tokoCashModel) {
                                if (tokoCashModel != null) {
                                    view.onSuccessFetchTokoCashDataFromCache(tokoCashModel);
                                } else {
                                    view.onErrorFetchTokoCashDataFromCache("");
                                }
                            }
                        }));
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