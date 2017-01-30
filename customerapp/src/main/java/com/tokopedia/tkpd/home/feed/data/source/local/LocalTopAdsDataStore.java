package com.tokopedia.tkpd.home.feed.data.source.local;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.data.mapper.TopAdsMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.TopAdsDataSource;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.TopAdsDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/30/16.
 */
public class LocalTopAdsDataStore implements TopAdsDataSource {

    private final TopAdsDbManager mDbManager;
    private final TopAdsMapperResult mTopAdsMapperResult;

    public LocalTopAdsDataStore(TopAdsDbManager dbManager, TopAdsMapperResult topAdsMapperResult) {
        mDbManager = dbManager;

        mTopAdsMapperResult = topAdsMapperResult;
    }

    @Override
    public Observable<List<TopAds>> getTopAdsCloud(TKPDMapParam<String, String> queryMap) {
        return Observable.empty();
    }

    @Override
    public Observable<List<TopAds>> getTopAdsCache() {
        if (mDbManager.isExpired(System.currentTimeMillis())) {
            return Observable.empty();
        }
        return mDbManager.getData().map(mTopAdsMapperResult);
    }
}
