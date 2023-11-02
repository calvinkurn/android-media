package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.topads.common.data.model.AdGroupStatsParam
import com.tokopedia.topads.common.data.response.TopAdsGroupsStatisticResponseResponse
import com.tokopedia.topads.common.domain.query.GetTopadsDashboardGroupStatisticsV3
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.toString
import java.util.*
import javax.inject.Inject

class GetTopAdsGroupsStatisticsUseCase@Inject constructor() : GraphqlUseCase<TopAdsGroupsStatisticResponseResponse>() {

    companion object{
        private const val PARAM_KEY = "queryInput"
        private const val ROLLBACK_DAYS = 3
        private const val GOAL_ID_PARAM = "1"
        private const val SEPARATE_STATISTIC_PARAM = "true"
    }

    fun getAdGroupsStatistics(
        shopId:String,
        keyword:String = "",
        page:Int = 1,
        sort:String = "",
        groupIds:String = "",
        success:(TopAdsGroupsStatisticResponseResponse) -> Unit,
        failure:(Throwable) -> Unit
    ){
        setRequestParams(getRequestParams(shopId, keyword, page, sort,groupIds))
        setTypeClass(TopAdsGroupsStatisticResponseResponse::class.java)
        setGraphqlQuery(GetTopadsDashboardGroupStatisticsV3)
        execute({
            if(it.response?.errors?.isNotEmpty().orFalse()){
                failure.invoke(Throwable(it.response!!.errors!![0].detail))
            }
            else success.invoke(it)
        },failure)
    }

    private fun getRequestParams(shopId:String,keyword:String,page:Int,sort:String,groupIds:String) : Map<String,Any?>{
        val endDate = DateUtil.getCurrentDate().toString(DateUtil.YYYY_MM_DD)
        val startDate = DateUtil.getCurrentCalendar().time.addTimeToSpesificDate(Calendar.DAY_OF_YEAR,-ROLLBACK_DAYS).toString(
            DateUtil.YYYY_MM_DD)
        return mapOf(
            PARAM_KEY to AdGroupStatsParam(
                shopId = shopId,
                keyword = keyword,
                page = page,
                sort = sort,
                separateStatistic = SEPARATE_STATISTIC_PARAM,
                goalId = GOAL_ID_PARAM,
                startDate = startDate,
                endDate = endDate,
                groupIds = groupIds
            )
        )
    }

}

