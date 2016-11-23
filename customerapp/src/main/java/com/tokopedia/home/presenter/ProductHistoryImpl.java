package com.tokopedia.home.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.entity.home.ProductFeedData;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.home.interactor.CacheHomeInteractor;
import com.tokopedia.home.interactor.CacheHomeInteractorImpl;
import com.tokopedia.home.service.ProductFeedService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by m.normansyah on 01/12/2015.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class ProductHistoryImpl implements ProductHistory {

    ProductHistoryView productHistoryView;

    List<RecyclerViewItem> data;

    PagingHandler mPaging;

    ProductFeedService productFeedService;

    CompositeSubscription compositeSubscription = new CompositeSubscription();

    CacheHomeInteractor cache;

    public ProductHistoryImpl(ProductHistoryView productHistoryView) {
        this.productHistoryView = productHistoryView;
        mPaging = new PagingHandler();
        productFeedService = new ProductFeedService();
        cache = new CacheHomeInteractorImpl();
    }

    @Override
    public void initDataInstance(Context context) {
        if (!isAfterRotation())
            data = new ArrayList<>();
        productHistoryView.initGridLayoutManager();
        productHistoryView.initItemDecoration();
        productHistoryView.initAdapterWithData(data);
    }

    @Override
    public void fetchSavedsInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPaging.onCreate(savedInstanceState);
            data = Parcels.unwrap(savedInstanceState.getParcelable(PRODUCT_HISTORY_MODEL));
        }
    }

    @Override
    public void initAnalyticsHandler(Context context) {
        if (context != null) {

        }

    }

    @Override
    public void setData() {
        if (productHistoryView.isPullToRefresh()) {
            data.clear();
            productHistoryView.displayPull(false);
        }

        mPaging.setHasNext(false);// PagingHandler.CheckHasNext(productFeedData.getData().getPagingHandlerModel())

        if (mPaging.CheckNextPage()) {
            productHistoryView.displayLoadMore(true);
        } else {
            productHistoryView.displayLoadMore(false);
        }
        productHistoryView.setPullEnabled(true);

        productHistoryView.loadDataChange();
        productHistoryView.displayMainContent(true);
        productHistoryView.displayLoading(false);
    }

    @Override
    public void refreshData(final Context context) {
        productHistoryView.displayLoadMore(false);
        productHistoryView.loadDataChange();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (context != null)
                    fetchDataFromInternet(context);
            }
        }, 200);
    }

    @Override
    public void loadMore(Context context) {
        productHistoryView.setPullEnabled(false);
        if (mPaging.CheckNextPage()) {
            mPaging.nextPage();
            fetchDataFromInternet(context);
        }
    }

    @Override
    public void setLocalyticFlow(Context context, String screenName) {
        if (context != null) {
            ScreenTracking.screenLoca(screenName);
        }
    }

    @Override
    public void saveDataBeforeRotate(Bundle saved) {
        mPaging.onSavedInstanceState(saved);
        saved.putParcelable(PRODUCT_HISTORY_MODEL, Parcels.wrap(data));
    }

    @Override
    public boolean isAfterRotation() {
        return data != null && data.size() > 0;
    }

    @Override
    public List<RecyclerViewItem> getData() {
        return data;
    }

    @Override
    public void fetchDataFromInternet(Context context) {
        NetworkCalculator lastSeenCalculator = new NetworkCalculator(NetworkConfig.GET, context,
                TkpdBaseURL.Etc.URL_HOME + TkpdBaseURL.Etc.PATH_GET_RECENT_VIEW_PRODUCT)
                .setIdentity()
                .compileAllParam()
                .finish();
        compositeSubscription.add(productFeedService.getApi().getLastSeenProduct(
                NetworkCalculator.getContentMd5(lastSeenCalculator),//
                NetworkCalculator.getDate(lastSeenCalculator),
                NetworkCalculator.getAuthorization(lastSeenCalculator),
                NetworkCalculator.getxMethod(lastSeenCalculator),
                NetworkCalculator.getUserId(context),
                NetworkCalculator.getDeviceId(context),
                NetworkCalculator.getHash(lastSeenCalculator),
                NetworkCalculator.getDeviceTime(lastSeenCalculator))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<ProductFeedData>>() {
                    @Override
                    public void onCompleted() {
                        if (productHistoryView.isPullToRefresh()) {
                            productHistoryView.displayPull(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (productHistoryView.isPullToRefresh()) {
                            productHistoryView.displayPull(false);
                        }
                    }

                    @Override
                    public void onNext(Response<ProductFeedData> response) {

                        Log.d("MNORMANSYAH", "onResponse : " + productHistoryView.isPullToRefresh());

                        if (response.isSuccessful()) {
                            ProductFeedData productFeedData = response.body();
                            if (productFeedData.getMessageError() == null) {
                                setData(productFeedData);

                                if (mPaging.getPage() == 1)
                                    cache.setProdHistoryCache(productFeedData);
                            }
                        }
                    }
                }));
    }

    @Override
    public void setData(ProductFeedData productFeedData) {
        if (productHistoryView.isPullToRefresh()) {
            data.clear();
        }
        productHistoryView.displayPull(false);

        data.addAll(productFeedData.getData().getList());
        mPaging.setHasNext(false);// PagingHandler.CheckHasNext(productFeedData.getData().getPagingHandlerModel())

        if (mPaging.CheckNextPage()) {
            productHistoryView.displayLoadMore(true);
        } else {
            productHistoryView.displayLoadMore(false);
        }
        productHistoryView.setPullEnabled(true);

        productHistoryView.loadDataChange();
        productHistoryView.displayMainContent(true);
        productHistoryView.displayLoading(false);
    }

    @Override
    public void subscribe() {
        RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void unSubscribe() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
        cache.unSubscribeObservable();
    }

    @Override
    public void fetchDataFromCache(final Context context) {
        if (cache.getProdHistoryCache() != null) {
            setData(cache.getProdHistoryCache());
        } else {
            productHistoryView.displayPull(true);
            fetchDataFromInternet(context);
        }

//        productHistoryView.displayPull(true);
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fetchDataFromInternet(context);
//            }
//        }, 1_000);
    }
}
