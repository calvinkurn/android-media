package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_AD_GROUP_IDS_KEY
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsBatchGetInsightCountByAdGroupIDResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TopAdsBatchGetInsightCountByAdGroupIDUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TopAdsBatchGetInsightCountByAdGroupIDResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsBatchGetInsightCountByAdGroupIDResponse::class.java)
    }

    suspend operator fun invoke(groupId: String, source: String):
        TopAdsListAllInsightState<TopAdsBatchGetInsightCountByAdGroupIDResponse> {
        setRequestParams(createRequestParam(groupId, source).parameters)
        val data = executeOnBackground()

        return when {
            data.topAdsBatchGetInsightCountByAdGroupID.error.title.isNullOrEmpty() -> {
                TopAdsListAllInsightState.Success(data)
            }

            else -> TopAdsListAllInsightState.Fail(Throwable(data.topAdsBatchGetInsightCountByAdGroupID.error.title))
        }
    }

    private fun createRequestParam(groupId: String, source: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(SOURCE, source)
        requestParams.putObject(
            PARAM_AD_GROUP_IDS_KEY,
            listOf(groupId)
        )
        return requestParams
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topAdsBatchGetInsightCountByAdGroupID"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}adGroupIDs:[String]!,${'$'}source:String!){
                $OPERATION_NAME(adGroupIDs:${'$'}adGroupIDs,source:${'$'}source){
                groups{
                  data{
                    count
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
