package com.tokopedia.topads.dashboard.domain.interactor

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.StatsData
import com.tokopedia.usecase.RequestParams
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetStatisticUseCase @Inject
constructor(): RestRequestUseCase() {

    var query = ""

    fun setQueryString(queryString: String) {
        query = queryString
    }

    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()
        val token = object : TypeToken<DataResponse<StatsData>>() {}.type

        var request: GraphqlRequest = GraphqlRequest(query, StatsData::class.java, requestParams?.parameters)
        val restReferralRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }

    fun createRequestParams(startDate: Date, endDate: Date,
                            @TopAdsStatisticsType type: Int, shopId: String, groupId: String?): RequestParams {

        var requestParams = RequestParams.create()
        requestParams.putInt("shopID", shopId.toInt())
        requestParams.putString(ParamObject.GROUP, groupId)
        requestParams.putInt(TopAdsDashboardConstant.PARAM_TYPE, type)
        requestParams.putObject(TopAdsDashboardConstant.PARAM_START_DATE,
                SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(startDate))
        requestParams.putObject(TopAdsDashboardConstant.PARAM_END_DATE,
                SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(endDate))

        return requestParams
    }
}