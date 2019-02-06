package com.tokopedia.topads.dashboard.data.source

import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.TotalAd
import com.tokopedia.topads.dashboard.data.source.cloud.TopAdsDashboardDataSourceCloud
import com.tokopedia.usecase.RequestParams

import rx.Observable

/**
 * Created by hadi.putra on 24/04/18.
 */

class TopAdsDashboardDataSource(private val topAdsDashboardDataSourceCloud: TopAdsDashboardDataSourceCloud) {

    fun populateTotalAd(requestParams: RequestParams): Observable<TotalAd> {
        return topAdsDashboardDataSourceCloud.populateTotalAds(requestParams)
    }

    fun getStatistics(requestParams: RequestParams): Observable<DataStatistic> {
        return topAdsDashboardDataSourceCloud.getStatistics(requestParams)
    }

    fun getDashboardCredit(requestParams: RequestParams): Observable<List<DataCredit>> {
        return topAdsDashboardDataSourceCloud.getDashboardCredit(requestParams)
    }
}
