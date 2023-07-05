package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.INSIGHT_TYPE_KEY
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightGqlInputSource.SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_ALL_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_GROUP_IDS_KEY
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGroupInsightResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TopAdsGetBatchKeywordInsightUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TopAdsBatchGroupInsightResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsBatchGroupInsightResponse::class.java)
    }

    suspend operator fun invoke(groupId: String):
        TopAdsListAllInsightState<TopAdsBatchGroupInsightResponse> {
        setRequestParams(createRequestParam(groupId).parameters)
        val data = executeOnBackground()
        return when {
            data.topAdsBatchGetKeywordInsightByGroupIDV3.error.title.isNullOrEmpty() -> {
                TopAdsListAllInsightState.Success(data)
            }
            else -> TopAdsListAllInsightState.Fail(Throwable(data.topAdsBatchGetKeywordInsightByGroupIDV3.error.title))
        }
    }

    private fun createRequestParam(groupId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INSIGHT_TYPE_KEY, PARAM_ALL_VALUE)
        requestParams.putString(SOURCE, SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE)
        requestParams.putObject(
            PARAM_GROUP_IDS_KEY,
            listOf(groupId)
        )
        return requestParams
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topAdsBatchGetKeywordInsightByGroupIDV3"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}groupIDs: [String]!, ${'$'}insightType: String!, ${'$'}source: String!) {
              $OPERATION_NAME(groupIDs: ${'$'}groupIDs, insightType: ${'$'}insightType, source: ${'$'}source) {
                groups {
                  data {
                    groupID
                    existingKeywordsBidRecom {
                      keywordID
                      keywordTag
                      keywordType
                      keywordStatus
                      currentBid
                      suggestionBid
                      predictedImpression
                      suggestionBidSource
                    }
                    newPositiveKeywordsRecom {
                      predictedImpression
                      totalSearch
                      keywordType
                      keywordStatus
                      keywordTag
                      suggestionBid
                      keywordSource
                      competition
                    }
                    newNegativeKeywordsRecom {
                      potentialSavings
                      predictedImpression
                      keywordType
                      keywordStatus
                      keywordTag
                      keywordSource
                    }
                  }
                }
                error {
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
