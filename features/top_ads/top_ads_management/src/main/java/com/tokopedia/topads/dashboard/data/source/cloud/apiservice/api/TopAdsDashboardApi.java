package com.tokopedia.topads.dashboard.data.source.cloud.apiservice.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.data.model.data.Cell;
import com.tokopedia.topads.dashboard.data.model.data.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hadi.putra on 24/04/18.
 */

public interface TopAdsDashboardApi {

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_TOTAL_AD)
    Observable<Response<DataResponse<TotalAd>>> populateTotalAd(@QueryMap Map<String, String> params);

    @GET(TopAdsNetworkConstant.PATH_DASHBOARD_STATISTIC)
    Observable<Response<DataResponse<DataStatistic>>> getStatistics(@QueryMap Map<String, String> params);
}
