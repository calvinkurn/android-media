package com.tokopedia.tkpd.home.feed.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.database.model.DbRecentProduct;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.home.feed.data.mapper.RecentProductMapperResult;
import com.tokopedia.tkpd.home.feed.data.source.local.dbManager.RecentProductDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 12/9/16.
 */

public class CloudRecentProductDataSource {

    private Context mContext;
    private RecentProductDbManager mDbManager;
    private final MojitoService mService;
    private RecentProductMapperResult mMapperResult;

    public CloudRecentProductDataSource(Context context,
                                        RecentProductDbManager dbManager,
                                        MojitoService service,
                                        RecentProductMapperResult mapperResult) {

        mContext = context;
        mDbManager = dbManager;
        mService = service;
        mMapperResult = mapperResult;
    }

    public Observable<List<ProductFeed>> getRecentProduct() {

        return mService.getRecentProduct(SessionHandler.getLoginID(mContext))
                .doOnNext(saveToCache())
                .map(mMapperResult);
    }

    private Action1<Response<String>> saveToCache() {
        return new Action1<Response<String>>() {
            @Override
            public void call(Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DbRecentProduct recentProductDb = new DbRecentProduct();
                    recentProductDb.setId(1);
                    recentProductDb.setLastUpdated(System.currentTimeMillis());
                    recentProductDb.setContentRecentProduct(response.body());
                    mDbManager.store(recentProductDb);
                }
            }
        };
    }
}
