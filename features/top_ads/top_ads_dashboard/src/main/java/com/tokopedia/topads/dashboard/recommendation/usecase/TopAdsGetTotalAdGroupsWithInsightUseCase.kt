package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.FILTER
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEY_AD_GROUP_TYPES
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsTotalAdGroupsWithInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsGetTotalAdGroupsWithInsightUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<TopAdsTotalAdGroupsWithInsightResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsTotalAdGroupsWithInsightResponse::class.java)
    }

    suspend operator fun invoke(adGroupTypeList: List<String>, source: String):
        TopAdsListAllInsightState<TopAdsTotalAdGroupsWithInsightResponse> {
        setRequestParams(createRequestParam(adGroupTypeList, source).parameters)
        val data = executeOnBackground()

        return when {
            data.topAdsGetTotalAdGroupsWithInsightByShopID.error.title.isEmpty() -> {
                TopAdsListAllInsightState.Success(data)
            }
            else -> TopAdsListAllInsightState.Fail(Throwable(data.topAdsGetTotalAdGroupsWithInsightByShopID.error.title))
        }
    }

    private fun createRequestParam(adGroupTypeList: List<String>, source: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(ParamObject.SHOP_ID, userSession.shopId)
        requestParams.putString(SOURCE, source)
        requestParams.putObject(
            FILTER,
            mapOf(
                KEY_AD_GROUP_TYPES to adGroupTypeList
            )
        )
        return requestParams
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topAdsGetTotalAdGroupsWithInsightByShopID"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}shopID: String!, ${'$'}source: String!, ${'$'}filter: TopAdsGetTotalAdGroupsWithInsightByShopIDFilter!) {
              $OPERATION_NAME(shopID: ${'$'}shopID, source: ${'$'}source, filter: ${'$'}filter) {
                data {
                  totalAdGroupsWithInsight
                  totalAdGroups
                }
                error {
                  code
                  title
                  detail
                }
              }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = QUERY

        override fun getTopOperationName(): String = OPERATION_NAME
    }
}
