package com.tokopedia.tkpd.home.feed.data.repository;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.data.factory.FeedDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.factory.HomeDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.factory.RecentProductSourceFactory;
import com.tokopedia.tkpd.home.feed.data.factory.TopAdsDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.source.FavoritShopIdDataSource;
import com.tokopedia.tkpd.home.feed.data.source.TopAdsDataSource;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudFeedDataStore;
import com.tokopedia.tkpd.home.feed.data.source.cloud.CloudRecentProductDataSource;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public class FeedRepositoryImpl implements FeedRepository {

    private final FeedDataSourceFactory feedDataSourceFactory;
    private final HomeDataSourceFactory homeDataSourceFactory;
    private final TopAdsDataSourceFactory topAdsDataSourceFactory;
    private RecentProductSourceFactory recentProductSourceFactory;

    public FeedRepositoryImpl(FeedDataSourceFactory feedDataSourceFactory,
                              HomeDataSourceFactory homeDataSourceFactory,
                              TopAdsDataSourceFactory topAdsDataSourceFactory,
                              RecentProductSourceFactory recentProducFactory) {
        this.feedDataSourceFactory = feedDataSourceFactory;
        this.homeDataSourceFactory = homeDataSourceFactory;
        this.topAdsDataSourceFactory = topAdsDataSourceFactory;
        recentProductSourceFactory = recentProducFactory;
    }

    @Override
    public Observable<List<ProductFeed>> getRecentViewProduct() {
        CloudRecentProductDataSource recentProductDataSource
                = recentProductSourceFactory.createRecentProductDataSource();
        return recentProductDataSource.getRecentProduct();
    }

    @Override
    public Observable<List<String>> getListShopId() {
        FavoritShopIdDataSource listShopIdDataSource
                = homeDataSourceFactory.createFavoriteShopIdDataSource();

        return listShopIdDataSource.getListShopId();
    }

    @Override
    public Observable<Feed> getFeed(TKPDMapParam<String, String> queryMap) {
        CloudFeedDataStore feedDataSource = feedDataSourceFactory.createFeedDataSource();
        return feedDataSource.getFeed(queryMap);
    }

    @Override
    public Observable<List<TopAds>> getTopAds(TKPDMapParam<String, String> topAdsParams) {
        TopAdsDataSource topAdsDataStore = topAdsDataSourceFactory.createTopAdsDataSource();
        return topAdsDataStore.getTopAdsCloud(topAdsParams);
    }

    @Override
    public Observable<Feed> getFeedCache() {
        return feedDataSourceFactory
                .createFeedCacheDataSource()
                .getFeedCache();
    }

    @Override
    public Observable<List<TopAds>> getTopAdsCache() {
        return topAdsDataSourceFactory
                .createTopAdsLocalDataStore().getTopAdsCache();

    }


    @Override
    public Observable<List<ProductFeed>> getRecentViewProductFromCache() {
        return recentProductSourceFactory
                .createRecentProductCacheDataStore()
                .getRecentProductFromCache();
    }

}
