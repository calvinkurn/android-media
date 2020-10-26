package com.tokopedia.topads.dashboard.data.repository

import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.TotalAd
import com.tokopedia.topads.dashboard.data.source.TopAdsDashboardDataSource
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository
import com.tokopedia.usecase.RequestParams

import rx.Observable

/**
 * Created by hadi.putra on 24/04/18.
 */

class TopAdsDashboardRepositoryImpl(private val topAdsDashboardDataSource: TopAdsDashboardDataSource) : TopAdsDashboardRepository {

    override fun getStatistics(requestParams: RequestParams): Observable<DataStatistic> {
        return topAdsDashboardDataSource.getStatistics(requestParams)
    }

}
