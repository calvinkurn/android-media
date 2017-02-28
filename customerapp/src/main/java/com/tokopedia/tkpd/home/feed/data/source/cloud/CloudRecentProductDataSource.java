package com.tokopedia.tkpd.home.feed.data.source.cloud;

import android.content.Context;

import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.database.model.DbRecentProduct;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.home.feed.data.mapper.RecentProductMapper;
import com.tokopedia.core.base.common.dbManager.RecentProductDbManager;
import com.tokopedia.tkpd.home.feed.domain.model.ProductFeed;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * @author Kulomady on 12/9/16.
 */

public class CloudRecentProductDataSource {

    private Context context;
    private RecentProductDbManager recentProductDbManager;
    private final MojitoService mojitoService;
    private RecentProductMapper recentProductMapper;

    public CloudRecentProductDataSource(Context context,
                                        RecentProductDbManager recentProductDbManager,
                                        MojitoService mojitoService,
                                        RecentProductMapper recentProductMapper) {

        this.context = context;
        this.recentProductDbManager = recentProductDbManager;
        this.mojitoService = mojitoService;
        this.recentProductMapper = recentProductMapper;
    }

    public Observable<List<ProductFeed>> getRecentProduct() {

        return mojitoService.getRecentProduct(SessionHandler.getLoginID(context))
                .doOnNext(saveToCache())
                .map(recentProductMapper);
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
                    recentProductDbManager.store(recentProductDb);
                }
            }
        };
    }
}
