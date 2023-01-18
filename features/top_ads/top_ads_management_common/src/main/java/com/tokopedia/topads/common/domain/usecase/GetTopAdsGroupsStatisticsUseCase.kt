package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.topads.common.data.model.AdGroupsParams
import com.tokopedia.topads.common.data.raw.TOP_ADS_GROUPS_STATISTIC_GQL
import com.tokopedia.topads.common.data.response.TopAdsGroupsStatisticResponseResponse
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.toString
import java.util.*
import javax.inject.Inject

class GetTopAdsGroupsStatisticsUseCase@Inject constructor() : GraphqlUseCase<TopAdsGroupsStatisticResponseResponse>() {

    companion object{
        private const val PARAM_KEY = "queryInput"
        private const val ROLLBACK_DAYS = 3
    }

    fun getAdGroupsStatistics(
        shopId:String,
        keyword:String = "",
        page:Int = 1,
        sort:String = "",
        success:(TopAdsGroupsStatisticResponseResponse) -> Unit,
        failure:(Throwable) -> Unit
    ){
        setRequestParams(getRequestParams(shopId, keyword, page, sort))
        setTypeClass(TopAdsGroupsStatisticResponseResponse::class.java)
        setGraphqlQuery(TOP_ADS_GROUPS_STATISTIC_GQL)
        execute({
            if(it.response?.errors?.isNotEmpty().orFalse()){
                failure.invoke(Throwable(it.response!!.errors!![0].detail))
            }
            else success.invoke(it)
        },failure)
    }

    private fun getRequestParams(shopId:String,keyword:String,page:Int,sort:String) : Map<String,Any?>{
        val endDate = DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD)
        val startDate = DateUtil.getCurrentCalendar().time.addTimeToSpesificDate(Calendar.DAY_OF_YEAR,-ROLLBACK_DAYS).toString(
            DateUtil.YYYY_MM_DD)
        return mapOf(
            PARAM_KEY to AdGroupsParams(
                shopId = shopId,
                keyword = keyword,
                page = page,
                sort = sort,
                separateStatistic = "true",
                goalId = "1",
                groupType = 1,
                startDate = startDate,
                endDate = endDate
            )
        )
    }

}
