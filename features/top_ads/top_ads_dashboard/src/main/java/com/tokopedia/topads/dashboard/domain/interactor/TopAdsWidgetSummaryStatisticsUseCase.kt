package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.TopadsWidgetSummaryStatisticsModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsWidgetSummaryStatisticsUseCase @Inject constructor(
    private val userSession: UserSessionInterface
) {

    suspend fun getSummaryStatistics(
        startDate: String, endDate: String, adTypes: String,
    ): TopadsWidgetSummaryStatisticsModel? {
        val params = createParams(startDate, endDate, adTypes)
        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(query, TopadsWidgetSummaryStatisticsModel::class.java, params)
        gql.addRequest(request)
        val response = gql.executeOnBackground()
        return response.getData(TopadsWidgetSummaryStatisticsModel::class.java) as? TopadsWidgetSummaryStatisticsModel
    }

    private fun createParams(
        startDate: String, endDate: String, adTypes: String,
    ): Map<String, Any> {
        return mapOf(
            TopAdsDashboardConstant.PARAM_SHOP_ID to userSession.shopId,
            TopAdsDashboardConstant.PARAM_START_DATE to startDate,
            TopAdsDashboardConstant.PARAM_END_DATE to endDate,
            TopAdsDashboardConstant.PARAM_AD_TYPES to adTypes,
        )
    }

    companion object {
        private val query = """
            query topadsWidgetSummaryStatistics(${'$'}shopID: String!, ${'$'}startDate: String!, ${'$'}endDate: String!, ${'$'}adTypes: String!){
  topadsWidgetSummaryStatistics(shopID:${'$'}shopID, startDate:${'$'}startDate, endDate:${'$'}endDate, adTypes:${'$'}adTypes) {
    header {
      process_time
    } 
    data {
      cells {
        date
        day
        month
        year
        click
        impression
        sold
        income
        cost
        roas
        click_fmt
        impression_fmt
        sold_fmt
        income_fmt
        cost_fmt
        roas_fmt
      }
      summary {
        last_update
        click_sum
        impression_sum
        total_sold_sum
        income_sum
        spending_sum
        roas
        click_percent
        impression_percent
        total_sold_percent
        income_percent
        spending_percent
        roas_percent
      }
    }
    
  }
}""".trimIndent()
    }
}