package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.StatsData
import com.tokopedia.topads.dashboard.data.raw.STATS_URL
import com.tokopedia.usecase.RequestParams
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by hadi.putra on 25/04/18.
 */

@GqlQuery("StatsList", STATS_URL)
class TopAdsGetStatisticsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<StatsData>(graphqlRepository) }

    suspend fun execute(requestParams: RequestParams): StatsData {
        graphql.apply {
            setGraphqlQuery(StatsList.GQL_QUERY)
            setTypeClass(StatsData::class.java)
        }

        return graphql.run {
            setRequestParams(requestParams.parameters)
            executeOnBackground()
        }
    }

    fun createRequestParams(
        startDate: Date,
        endDate: Date,
        @TopAdsStatisticsType type: Int,
        shopId: String,
        groupId: String?,
        goalId: Int = 0
    ): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString("shopID", shopId)
        requestParams.putString(GROUP, groupId)
        requestParams.putInt(ParamObject.GOAL_ID, goalId)
        requestParams.putInt(TopAdsDashboardConstant.PARAM_TYPE, type)
        requestParams.putObject(
            TopAdsDashboardConstant.PARAM_START_DATE,
            SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(
                startDate
            )
        )
        requestParams.putObject(
            TopAdsDashboardConstant.PARAM_END_DATE,
            SimpleDateFormat(TopAdsCommonConstant.REQUEST_DATE_FORMAT, Locale.ENGLISH).format(
                endDate
            )
        )
        return requestParams
    }
}
