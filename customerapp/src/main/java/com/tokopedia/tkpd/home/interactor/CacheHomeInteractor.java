package com.tokopedia.tkpd.home.interactor;

import com.tokopedia.core.network.entity.home.recentView.RecentViewData;
import com.tokopedia.tkpd.home.model.FavoriteTransformData;
import com.tokopedia.tkpd.home.model.ProductFeedTransformData;

/**
 * Created by ricoharisin on 12/7/15.
 */
public interface CacheHomeInteractor {

    void setProdHistoryCache(RecentViewData productFeedData);

    RecentViewData getProdHistoryCache();
    void unSubscribeObservable();

    interface GetProductFeedCacheListener {
        void onSuccess(ProductFeedTransformData productFeedTransformData);
        void onError(Throwable e);
    }

    interface GetFavoriteCacheListener {
        void onSuccess(FavoriteTransformData favoriteTransformData);
        void onError(Throwable e);
    }



}
