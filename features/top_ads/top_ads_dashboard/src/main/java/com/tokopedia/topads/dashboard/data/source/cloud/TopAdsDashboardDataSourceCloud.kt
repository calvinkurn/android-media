package com.tokopedia.topads.dashboard.data.source.cloud

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.TotalAd
import com.tokopedia.topads.dashboard.data.source.cloud.serviceapi.TopAdsDashboardApi
import com.tokopedia.usecase.RequestParams

import retrofit2.Response
import rx.Observable
import rx.functions.Func1

/**
 * Created by hadi.putra on 24/04/18.
 */

class TopAdsDashboardDataSourceCloud(private val topAdsDashboardApi: TopAdsDashboardApi) {

    fun populateTotalAds(requestParams: RequestParams): Observable<TotalAd> {
        return topAdsDashboardApi.populateTotalAd(requestParams.paramsAllValueInString)
                .map(TopAdsResponseMapper())
    }

    fun getStatistics(requestParams: RequestParams): Observable<DataStatistic> {
        return topAdsDashboardApi.getStatistics(requestParams.paramsAllValueInString)
                .map(TopAdsResponseMapper())
    }

    fun getDashboardCredit(requestParams: RequestParams): Observable<List<DataCredit>> {
        return topAdsDashboardApi.getDashboardCredit().map(TopAdsResponseMapper())
    }


    internal inner class TopAdsResponseMapper<E> : Func1<Response<DataResponse<E>>, E> {

        override fun call(dataResponseResponse: Response<DataResponse<E>>): E {
            return dataResponseResponse.body().data
        }
    }
}
