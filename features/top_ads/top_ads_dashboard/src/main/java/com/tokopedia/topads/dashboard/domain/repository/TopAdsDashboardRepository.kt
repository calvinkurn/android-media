package com.tokopedia.topads.dashboard.domain.repository

import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.TotalAd
import com.tokopedia.usecase.RequestParams

import rx.Observable

/**
 * Created by nakama on 24/04/18.
 */

interface TopAdsDashboardRepository {

    fun getStatistics(requestParams: RequestParams): Observable<DataStatistic>

}
