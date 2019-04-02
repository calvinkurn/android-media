package com.tokopedia.topads.group.domain.repository;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * Created by hadi.putra on 09/05/18.
 */

public interface TopAdsGroupAdRepository {
    Observable<PageDataResponse<List<GroupAd>>> getGroupAd(RequestParams requestParams);

    Observable<GroupAdBulkAction> bulkAction(DataRequest<GroupAdBulkAction> dataRequest);

    Observable<List<GroupAd>> searchGroupAd(RequestParams requestParams);
}
