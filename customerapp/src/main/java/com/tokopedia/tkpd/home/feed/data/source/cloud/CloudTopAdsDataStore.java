package com.tokopedia.tkpd.home.feed.data.source.cloud;

import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.database.model.DbTopAds;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpd.home.feed.data.mapper.TopAdsMapper;
import com.tokopedia.tkpd.home.feed.data.source.TopAdsDataSource;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.TopAdsDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.TopAds;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 12/9/16.
 */
public class CloudTopAdsDataStore implements TopAdsDataSource {

    private TopAdsDbManager topAdsDbManager;
    private TopAdsService topAdsService;
    private TopAdsMapper topAdsMapper;

    public CloudTopAdsDataStore(TopAdsDbManager dbManager,
                                TopAdsService topAdsService,
                                TopAdsMapper topAdsMapper) {
        super();
        topAdsDbManager = dbManager;
        this.topAdsService = topAdsService;
        this.topAdsMapper = topAdsMapper;
    }

    @Override
    public Observable<List<TopAds>> getTopAdsCloud(TKPDMapParam<String, Object> param) {
        return topAdsService.getTopAds(param)
                .doOnNext(saveToCache())
                .map(topAdsMapper);
    }


    @Override
    public Observable<List<TopAds>> getTopAdsCache() {

        return Observable.empty();
    }

    private Action1<Response<String>> saveToCache() {

        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DbTopAds dbTopAds = new DbTopAds();
                    dbTopAds.setId(1);
                    dbTopAds.setLastUpdated(System.currentTimeMillis());
                    dbTopAds.setContentTopAds(response.body());
                    topAdsDbManager.store(dbTopAds);
                }

            }
        };
    }

}
