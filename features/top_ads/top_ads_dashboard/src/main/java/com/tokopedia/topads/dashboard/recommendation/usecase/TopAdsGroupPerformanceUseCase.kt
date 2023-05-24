package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsSellerGroupPerformanceResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsGroupPerformanceUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : GraphqlUseCase<TopAdsSellerGroupPerformanceResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsSellerGroupPerformanceResponse::class.java)
    }

    suspend operator fun invoke(groupId: String, adType: String):
        TopAdsListAllInsightState<GroupPerformanceWidgetUiModel> {
        setRequestParams(createRequestParam(groupId, adType).parameters)
        val data = executeOnBackground()
        return when {
            data.getSellerGroupPerformance.errors.isEmpty() -> {
                TopAdsListAllInsightState.Success(data.toGroupPerformanceWidgetUiModel())
            }
            else -> TopAdsListAllInsightState.Fail(Throwable(data.getSellerGroupPerformance.errors.firstOrNull()?.title))
        }
    }

    private fun createRequestParam(groupId: String, adType: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString("shop_id", userSession.shopId)
        requestParams.putString("group_id", groupId)
        requestParams.putString("insight_type", "GROUP_PERFORMANCE")
        requestParams.putString("ad_type", adType)

        return requestParams
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "GetSellerGroupPerformance"
        private val QUERY = """
           query $OPERATION_NAME(${'$'}shop_id: String!, ${'$'}group_id: String, ${'$'}insight_type: String!, ${'$'}ad_type: String) {
             $OPERATION_NAME(shop_id: ${'$'}shop_id, insight_type: ${'$'}insight_type, group_id: ${'$'}group_id, ad_type: ${'$'}ad_type) {
               data {
                 group_id
                 impression_growth
                 total_sold_growth
                 click_growth
                 percent_impression_growth
                 percent_total_sold_growth
                 percent_roas_growth
                 impression
                 top_slot_impression
                 generalised_insight_category
               }
               errors {
                 code
                 detail
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
