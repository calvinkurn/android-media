package com.tokopedia.topads.dashboard.data.source.cloud.serviceapi;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant;
import com.tokopedia.topads.dashboard.data.model.DataCredit;
import com.tokopedia.topads.dashboard.data.model.DataStatistic;
import com.tokopedia.topads.dashboard.data.model.TotalAd;

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

    @GET(TopAdsDashboardConstant.PATH_DASHBOARD_TOTAL_AD)
    Observable<Response<DataResponse<TotalAd>>> populateTotalAd(@QueryMap Map<String, String> params);

    @GET(TopAdsDashboardConstant.PATH_DASHBOARD_STATISTIC)
    Observable<Response<DataResponse<DataStatistic>>> getStatistics(@QueryMap Map<String, String> params);

    @GET(TopAdsDashboardConstant.PATH_DASHBOARD_CREDIT)
    Observable<Response<DataResponse<List<DataCredit>>>> getDashboardCredit();
}
