package com.tokopedia.topads.dashboard.data.source.cloud.serviceapi

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.TotalAd

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

/**
 * Created by hadi.putra on 24/04/18.
 */

interface TopAdsDashboardApi {

    @GET(TopAdsDashboardConstant.PATH_DASHBOARD_CREDIT)
    fun getDashboardCredit(): Observable<Response<DataResponse<List<DataCredit>>>>

    @GET(TopAdsDashboardConstant.PATH_DASHBOARD_TOTAL_AD)
    fun populateTotalAd(@QueryMap params: Map<String, String>): Observable<Response<DataResponse<TotalAd>>>

    @GET(TopAdsDashboardConstant.PATH_DASHBOARD_STATISTIC)
    fun getStatistics(@QueryMap params: Map<String, String>): Observable<Response<DataResponse<DataStatistic>>>
}
