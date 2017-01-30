package com.tokopedia.tkpd.home.feed.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/8/16.
 */

public interface FeedRepository {

    Observable<List<ProductFeed>> getRecentViewProduct();

    Observable<List<String>> getListShopId();

    Observable<Feed> getFeed(TKPDMapParam<String, String> queryMap);

    Observable<List<TopAds>> getTopAds(TKPDMapParam<String, String> topAdsParams);

    Observable<Feed> getFeedCache();

    Observable<List<TopAds>> getTopAdsCache();

    Observable<List<ProductFeed>> getRecentViewProductFromCache();
}
