package com.tokopedia.tkpd.home.interactor;

import com.tokopedia.core.network.entity.home.recentView.RecentViewData;

/**
 * Created by ricoharisin on 12/7/15.
 */
public interface CacheHomeInteractor {

    void setProdHistoryCache(RecentViewData productFeedData);

    RecentViewData getProdHistoryCache();
    void unSubscribeObservable();




}
