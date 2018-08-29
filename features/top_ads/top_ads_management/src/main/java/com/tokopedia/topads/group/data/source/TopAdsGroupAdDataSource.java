package com.tokopedia.topads.group.data.source;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.topads.group.data.source.cloud.TopAdsGroupAdDataSourceCloud;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 09/05/18.
 */

public class TopAdsGroupAdDataSource {
    private final TopAdsGroupAdDataSourceCloud dataSourceCloud;

    public TopAdsGroupAdDataSource(TopAdsGroupAdDataSourceCloud dataSourceCloud) {
        this.dataSourceCloud = dataSourceCloud;
    }

    public Observable<PageDataResponse<List<GroupAd>>> getGroupAd(RequestParams requestParams){
        return dataSourceCloud.getGroupAd(requestParams);
    }

    public Observable<GroupAdBulkAction> bulkAction(DataRequest<GroupAdBulkAction> dataRequest) {
        return dataSourceCloud.bulkAction(dataRequest);
    }

    public Observable<List<GroupAd>> searchGroupAd(RequestParams requestParams){
        return dataSourceCloud.searchGroupAd(requestParams);
    }
}
