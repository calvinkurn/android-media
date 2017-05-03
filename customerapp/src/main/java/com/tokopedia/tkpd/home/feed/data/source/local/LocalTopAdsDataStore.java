package com.tokopedia.tkpd.home.feed.data.source.local;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.data.mapper.TopAdsMapper;
import com.tokopedia.tkpd.home.feed.data.source.TopAdsDataSource;
import com.tokopedia.core.base.common.dbManager.TopAdsDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/30/16.
 */
public class LocalTopAdsDataStore implements TopAdsDataSource {

    private final TopAdsDbManager topAdsDbManager;
    private final TopAdsMapper topAdsMapper;

    public LocalTopAdsDataStore(TopAdsDbManager dbManager, TopAdsMapper topAdsMapper) {
        topAdsDbManager = dbManager;

        this.topAdsMapper = topAdsMapper;
    }

    @Override
    public Observable<List<TopAds>> getTopAdsCloud(TKPDMapParam<String, Object> queryMap) {
        return Observable.empty();
    }

    @Override
    public Observable<List<TopAds>> getTopAdsCache() {
        return topAdsDbManager.getData().map(topAdsMapper);
    }
}
