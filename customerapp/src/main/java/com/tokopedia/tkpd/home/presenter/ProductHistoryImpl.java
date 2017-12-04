package com.tokopedia.tkpd.home.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tokopedia.core.network.apiservices.mojito.MojitoAuthService;
import com.tokopedia.core.network.entity.home.recentView.RecentViewData;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractor;
import com.tokopedia.tkpd.home.interactor.CacheHomeInteractorImpl;
import com.tokopedia.tkpd.home.service.ProductFeedService;

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

    private final MojitoAuthService mojitoService;

    ProductFeedService productFeedService;

    CompositeSubscription compositeSubscription = new CompositeSubscription();

    CacheHomeInteractor cache;

    public ProductHistoryImpl(ProductHistoryView productHistoryView) {
        this.productHistoryView = productHistoryView;
        mPaging = new PagingHandler();
        productFeedService = new ProductFeedService();
        cache = new CacheHomeInteractorImpl();
        mojitoService = new MojitoAuthService();
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
        String userId = SessionHandler.getLoginID(context);
        compositeSubscription.add(mojitoService.getApi()
                .getRecentViews(userId)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<RecentViewData>>() {
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
                        if (cache!=null && cache.getProdHistoryCache() != null) {
                            setData(cache.getProdHistoryCache());
                        }

                    }

                    @Override
                    public void onNext(Response<RecentViewData> response) {
                        Log.d(TAG, "onNext() called with: response = [" + response + "]"
                                + productHistoryView.isPullToRefresh());

                        if (response.isSuccessful()) {
                            RecentViewData productFeedData = response.body();
                            if (productFeedData != null) {
                                setData(productFeedData);

                                if (mPaging.getPage() == 1)
                                    cache.setProdHistoryCache(productFeedData);
                            }
                        }
                    }
                }));
    }

    @Override
    public void setData(RecentViewData recentViewData) {
        if (productHistoryView.isPullToRefresh()) {
            data.clear();
        }
        productHistoryView.displayPull(false);

        data.addAll(recentViewData.getData().getRecentView());
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
        productHistoryView.displayPull(true);
        fetchDataFromInternet(context);

//        productHistoryView.displayPull(true);
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                fetchDataFromInternet(context);
//            }
//        }, 1_000);
    }
}
