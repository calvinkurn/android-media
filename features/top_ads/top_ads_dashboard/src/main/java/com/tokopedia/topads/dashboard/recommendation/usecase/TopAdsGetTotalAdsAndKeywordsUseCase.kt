package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.dashboard.data.model.TotalProductKeyResponse
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsGetTotalAdsAndKeywordsUseCase @Inject constructor(
    val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TotalProductKeyResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TotalProductKeyResponse::class.java)
    }

    suspend operator fun invoke(groupIds: List<String>): TopadsProductListState<TotalProductKeyResponse> {
        setRequestParams(createRequestParam(groupIds).parameters)
        val data = executeOnBackground()
        return when{
            data.topAdsGetTotalAdsAndKeywords.errors.isEmpty() -> TopadsProductListState.Success(data)
            else -> TopadsProductListState.Fail(Throwable(data.topAdsGetTotalAdsAndKeywords.errors.firstOrNull()?.title))
        }
    }

    private fun createRequestParam(groupIds: List<String>): RequestParams {
        val requestParam = RequestParams.create()
        requestParam.putString(TopAdsProductRecommendationConstants.SHOP_ID_KEY_2, userSession.shopId)
        requestParam.putObject(TopAdsProductRecommendationConstants.GROUP_IDS_KEY, groupIds)
        return requestParam
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topAdsGetTotalAdsAndKeywords"
        private val QUERY = """
            mutation $OPERATION_NAME(${'$'}shopID: String!, ${'$'}groupIDs: [String]!) {
              $OPERATION_NAME(shopID: ${'$'}shopID, groupIDs: ${'$'}groupIDs) {
                data {
                  ID
                  totalAds
                  totalProducts
                  totalKeywords
                }
                errors {
                  code
                  detail
                  title
                  object {
                    type
                  }
                }
              }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = QUERY

        override fun getTopOperationName(): String = OPERATION_NAME

    }
}
