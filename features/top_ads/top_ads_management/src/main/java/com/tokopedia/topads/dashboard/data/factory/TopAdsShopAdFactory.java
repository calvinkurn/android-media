package com.tokopedia.topads.dashboard.data.factory;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailShopMapper;
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsShopAdsDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsShopAdFactory {

    private final Context context;
    private final TopAdsManagementApi topAdsManagementApi;
    private final TopAdsDetailShopMapper topAdsSearchGroupMapper;

    @Inject
    public TopAdsShopAdFactory(@ApplicationContext Context context, TopAdsManagementApi topAdsManagementApi,
                               TopAdsDetailShopMapper topAdsDetailProductMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsSearchGroupMapper = topAdsDetailProductMapper;
    }

    public TopAdsShopAdsDataSource createShopAdsDataSource() {
        return new TopAdsShopAdsDataSource(context, topAdsManagementApi, topAdsSearchGroupMapper);
    }
}
