package com.tokopedia.topads.group.data.apiservice;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.GroupAd;
import com.tokopedia.topads.dashboard.data.model.response.PageDataResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hadi.putra on 09/05/18.
 */

public interface TopAdsGroupAdApi {
    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_GROUP)
    Observable<Response<PageDataResponse<List<GroupAd>>>> getGroupAd(@QueryMap Map<String, String> params);
}
