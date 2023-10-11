package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightGqlInputSource.SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_GROUP_IDS_KEY
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsAdGroupBidInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TopAdsGetAdGroupBidInsightUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TopAdsAdGroupBidInsightResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsAdGroupBidInsightResponse::class.java)
    }

    suspend operator fun invoke(groupId: String):
        TopAdsListAllInsightState<TopAdsAdGroupBidInsightResponse> {
        setRequestParams(createRequestParam(groupId).parameters)
        val data = executeOnBackground()

        return when {
            data.topAdsBatchGetAdGroupBidInsightByGroupID.error.title.isNullOrEmpty() -> {
                TopAdsListAllInsightState.Success(data)
            }
            else -> TopAdsListAllInsightState.Fail(Throwable(data.topAdsBatchGetAdGroupBidInsightByGroupID.error.title))
        }
    }

    private fun createRequestParam(groupId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(SOURCE, SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE)
        requestParams.putObject(
            PARAM_GROUP_IDS_KEY,
            listOf(groupId)
        )
        return requestParams
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topAdsBatchGetAdGroupBidInsightByGroupID"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}$PARAM_GROUP_IDS_KEY: [String]!, ${'$'}$SOURCE: String!) {
              $OPERATION_NAME(groupIDs: ${'$'}groupIDs, source: ${'$'}source) {
                groups {
                  data {
                    groupID
                    currentBidSettings {
                      bidType
                      priceBid
                    }
                    suggestionBidSettings {
                      bidType
                      priceBid
                    }
                    predictedTotalImpression
                  }
                }
                error{
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
