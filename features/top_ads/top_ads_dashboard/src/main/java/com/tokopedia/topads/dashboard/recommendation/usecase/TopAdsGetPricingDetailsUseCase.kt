package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightGqlInputSource.SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_SCHEME
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsGetPricingDetailsResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TopAdsGetPricingDetailsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TopAdsGetPricingDetailsResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsGetPricingDetailsResponse::class.java)
    }

    suspend operator fun invoke(adType: String):
        TopAdsGetPricingDetailsResponse {
        setRequestParams(createRequestParam(adType).parameters)

        return executeOnBackground()
    }

    private fun createRequestParam(adType: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_SCHEME, adType)
        requestParams.putString(SOURCE, SOURCE_INSIGHT_CENTER_GROUP_DETAIL_PAGE)
        return requestParams
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topadsGetPricingDetails"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}scheme: String!, ${'$'}source: String!) {
              $OPERATION_NAME(scheme: ${'$'}scheme, source: ${'$'}source) {
                minBid
                maxBid
              }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = QUERY

        override fun getTopOperationName(): String = OPERATION_NAME
    }
}
