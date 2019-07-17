package com.tokopedia.tkpd.home.interactor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.entity.home.recentView.RecentViewData;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.var.TkpdCache;

import rx.subscriptions.CompositeSubscription;


/**
 * Created by ricoharisin on 12/7/15.
 */
public class CacheHomeInteractorImpl implements CacheHomeInteractor {

    private static String TAG = "CacheHome";
    Gson gson = new GsonBuilder().create();
    CompositeSubscription compositeSubscription;

    public CacheHomeInteractorImpl() {
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void setProdHistoryCache(RecentViewData productFeedData) {
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.RECENT_PRODUCT_ALL)
                .setCacheDuration(600)
                .setValue(gson.toJson(productFeedData))
                .store();
    }

    @Override
    public RecentViewData getProdHistoryCache() {
        RecentViewData result = null;
        try {
            result = gson.fromJson(new GlobalCacheManager().getValueString(TkpdCache.Key.RECENT_PRODUCT_ALL), RecentViewData.class);
        }catch(Exception e){
            result = new RecentViewData();
        }

        return result;
    }

    @Override
    public void unSubscribeObservable() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

}
