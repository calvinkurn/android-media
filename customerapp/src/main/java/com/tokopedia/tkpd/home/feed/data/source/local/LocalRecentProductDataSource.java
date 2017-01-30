package com.tokopedia.tkpd.home.feed.data.source.local;

import com.tokopedia.tkpd.home.feed.data.mapper.RecentProductMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.RecentProductDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public class LocalRecentProductDataSource {
    private RecentProductMapperResult mMapperResult;
    private RecentProductDbManager mDbManager;


    public LocalRecentProductDataSource(RecentProductDbManager dbManager,
                                        RecentProductMapperResult mapperResult) {
        mDbManager = dbManager;
        mMapperResult = mapperResult;
    }

    public Observable<List<ProductFeed>> getRecentProductFromCache() {
        if (mDbManager.isExpired(System.currentTimeMillis())) {
            return Observable.empty();
        }
        return mDbManager.getData().map(mMapperResult);
    }
}
