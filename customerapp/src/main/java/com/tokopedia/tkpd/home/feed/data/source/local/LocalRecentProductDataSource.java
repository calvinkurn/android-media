package com.tokopedia.tkpd.home.feed.data.source.local;

import com.tokopedia.tkpd.home.feed.data.mapper.RecentProductMapper;
import com.tokopedia.core.base.common.dbManager.RecentProductDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;

import java.util.List;

import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public class LocalRecentProductDataSource {
    private RecentProductMapper recentProductMapper;
    private RecentProductDbManager recentProductDbManager;


    public LocalRecentProductDataSource(RecentProductDbManager dbManager,
                                        RecentProductMapper mapperResult) {
        recentProductDbManager = dbManager;
        recentProductMapper = mapperResult;
    }

    public Observable<List<ProductFeed>> getRecentProductFromCache() {
        return recentProductDbManager.getData().map(recentProductMapper);
    }
}
