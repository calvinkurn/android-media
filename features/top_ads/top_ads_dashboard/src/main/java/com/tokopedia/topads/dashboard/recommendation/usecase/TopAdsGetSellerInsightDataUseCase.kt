package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_id
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_INSIGHT_TYPE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_INSIGHT_TYPE_VALUE
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsGetSellerInsightDataResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsGetSellerInsightDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<TopAdsGetSellerInsightDataResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsGetSellerInsightDataResponse::class.java)
    }

    suspend operator fun invoke(groupId: String):
        TopAdsListAllInsightState<TopAdsGetSellerInsightDataResponse> {
        setRequestParams(createRequestParam(groupId).parameters)
        val data = executeOnBackground()

        return when {
            data.getSellerInsightData.errors.isEmpty() -> {
                TopAdsListAllInsightState.Success(data)
            }
            else -> TopAdsListAllInsightState.Fail(Throwable(data.getSellerInsightData.errors.firstOrNull()?.title))
        }
    }

    private fun createRequestParam(groupId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(SHOP_id, userSession.shopId)
        requestParams.putString(GROUP_ID, groupId)
        requestParams.putString(PARAM_INSIGHT_TYPE, PARAM_INSIGHT_TYPE_VALUE)
        return requestParams
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "GetSellerInsightData"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}shop_id: String!, ${'$'}group_id: String, ${'$'}insight_type: String!) {
              $OPERATION_NAME(shop_id: ${'$'}shop_id, group_id: ${'$'}group_id, insight_type: ${'$'}insight_type) {
                data {
                  daily_budget_data {
                    group_id
                    group_name
                    price_daily
                    suggested_price_daily
                    avg_bid
                    impression
                    top_slot_impression
                    potential_click
                  }
                  total_potensial_click
                  page {
                    current
                    per_page
                    min
                    max
                    total
                  }
                }
                errors {
                  code
                  detail
                  object {
                    type
                    text
                  }
                  title
                }
              }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = QUERY

        override fun getTopOperationName(): String = OPERATION_NAME
    }
}
