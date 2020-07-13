package com.tokopedia.shop.score.data.source.disk;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.shop.score.data.common.ErrorCheck;
import com.tokopedia.shop.score.data.model.detail.ShopScoreDetailServiceModel;
import com.tokopedia.shop.score.data.model.summary.ShopScoreSummaryServiceModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/27/17.
 */
public class ShopScoreCache {
    private final PersistentCacheManager cacheManager;

    @Inject
    public ShopScoreCache(PersistentCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void storeShopScoreSummary(ShopScoreSummaryServiceModel serviceModel) {
        String stringData = CacheUtil.convertModelToString(
                serviceModel,
                new TypeToken<ShopScoreSummaryServiceModel>() {
                }.getType()
        );
        saveToCache(TkpdCache.Key.SHOP_SCORE_SUMMARY, stringData);
    }

    public void storeShopScoreDetail(ShopScoreDetailServiceModel serviceModel) {
        String stringData = CacheUtil.convertModelToString(
                serviceModel,
                new TypeToken<ShopScoreDetailServiceModel>() {
                }.getType()
        );
        saveToCache(TkpdCache.Key.SHOP_SCORE_DETAIL, stringData);
    }

    private void saveToCache(String key, String stringData) {
        cacheManager.put(key, stringData, 3600);
    }

    public Observable<ShopScoreDetailServiceModel> getShopScoreDetail() {
        return getShopScoreDetailCache()
                .map(new ErrorCheck<ShopScoreDetailServiceModel>());
    }

    public Observable<ShopScoreSummaryServiceModel> getShopScoreSummary() {
        return getShopScoreSummaryCache()
                .map(new ErrorCheck<ShopScoreSummaryServiceModel>());
    }

    @NonNull
    private Observable<ShopScoreSummaryServiceModel> getShopScoreSummaryCache() {
        return Observable.just(true)
                .map(new Func1<Boolean, ShopScoreSummaryServiceModel>() {
                         @Override
                         public ShopScoreSummaryServiceModel call(Boolean aBoolean) {
                             return cacheManager.get(
                                     TkpdCache.Key.SHOP_SCORE_SUMMARY,
                                     ShopScoreSummaryServiceModel.class
                             );
                         }
                     }
                );
    }

    @NonNull
    private Observable<ShopScoreDetailServiceModel> getShopScoreDetailCache() {
        return Observable.just(true)
                .map(new Func1<Boolean, ShopScoreDetailServiceModel>() {
                         @Override
                         public ShopScoreDetailServiceModel call(Boolean aBoolean) {
                             return cacheManager.get(
                                     TkpdCache.Key.SHOP_SCORE_DETAIL,
                                     ShopScoreDetailServiceModel.class
                             );
                         }
                     }
                );
    }
}
