package com.tokopedia.topads.dashboard.data.factory;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsBulkActionMapper;
import com.tokopedia.topads.dashboard.data.mapper.TopAdsDetailProductMapper;
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsProductAdsDataSource;
import com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api.TopAdsManagementApi;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsProductAdFactory {

    private final Context context;
    private final TopAdsManagementApi topAdsManagementApi;
    private final TopAdsDetailProductMapper topAdsSearchGroupMapper;
    private TopAdsBulkActionMapper topAdsBulkActionMapper;

    @Inject
    public TopAdsProductAdFactory(@ApplicationContext Context context, TopAdsManagementApi topAdsManagementApi,
                                  TopAdsDetailProductMapper topAdsDetailProductMapper, TopAdsBulkActionMapper topAdsBulkActionMapper) {
        this.context = context;
        this.topAdsManagementApi = topAdsManagementApi;
        this.topAdsSearchGroupMapper = topAdsDetailProductMapper;
        this.topAdsBulkActionMapper = topAdsBulkActionMapper;
    }

    public TopAdsProductAdsDataSource createProductAdsDataSource() {
        return new TopAdsProductAdsDataSource(context, topAdsManagementApi, topAdsSearchGroupMapper, topAdsBulkActionMapper);
    }
}
