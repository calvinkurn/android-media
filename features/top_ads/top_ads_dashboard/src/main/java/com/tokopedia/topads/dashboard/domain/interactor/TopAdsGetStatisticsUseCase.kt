package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import javax.inject.Inject

import rx.Observable

/**
 * Created by hadi.putra on 25/04/18.
 */

class TopAdsGetStatisticsUseCase @Inject
constructor(private val topAdsDashboardRepository: TopAdsDashboardRepository) : UseCase<DataStatistic>() {

    override fun createObservable(requestParams: RequestParams): Observable<DataStatistic> {
        return topAdsDashboardRepository.getStatistics(requestParams)
    }

    companion object {

        @Inject
        fun createRequestParams(startDate: Date, endDate: Date,
                                @TopAdsStatisticsType type: Int, shopId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, shopId)
            requestParams.putString(TopAdsDashboardConstant.PARAM_TYPE, type.toString())
            requestParams.putObject(TopAdsDashboardConstant.PARAM_START_DATE,
                    SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate))
            requestParams.putObject(TopAdsDashboardConstant.PARAM_END_DATE,
                    SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate))
            return requestParams
        }
    }
}
