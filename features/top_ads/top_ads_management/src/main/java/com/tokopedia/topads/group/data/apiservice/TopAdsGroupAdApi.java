package com.tokopedia.topads.group.data.apiservice;

import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.data.GroupAdBulkAction;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hadi.putra on 09/05/18.
 */

public interface TopAdsGroupAdApi {
    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_GROUP)
    Observable<Response<PageDataResponse<List<GroupAd>>>> getGroupAd(@QueryMap Map<String, String> params);

    @Headers({TopAdsNetworkConstant.CONTENT_TYPE_APPLICATION_JSON})
    @PATCH(TopAdsNetworkConstant.PATH_BULK_ACTION_GROUP_AD)
    Observable<Response<DataResponse<GroupAdBulkAction>>> bulkActionGroupAd(@Body DataRequest<GroupAdBulkAction> body);

    @GET(TopAdsNetworkConstant.PATH_SEARCH_GROUP)
    Observable<Response<DataResponse<List<GroupAd>>>> searchGroupAd(@QueryMap Map<String, String> params);
}
