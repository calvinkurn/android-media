package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject.FILTER
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_DAILY_BUDGET_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_GROUP_BID_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_KEYWORD_BID_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_NEGATIVE_KEYWORD_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.InsightTypeConstants.INSIGHT_TYPE_POSITIVE_KEYWORD_INPUT
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.KEY_AD_GROUP_TYPES
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_INSIGHT_TYPES
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_PAGE_SETTING
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_SIZE
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PARAM_START_CURSOR
import com.tokopedia.topads.dashboard.recommendation.common.RecommendationConstants.PER_PAGE_COUNT_VALUE
import com.tokopedia.topads.dashboard.recommendation.data.mapper.InsightDataMapper
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsListAllInsightCountsResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsListAllInsightCountsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<TopAdsListAllInsightCountsResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsListAllInsightCountsResponse::class.java)
    }

    suspend operator fun invoke(
        source: String,
        adGroupType: String,
        insightType: Int,
        startCursor: String = "",
        mapper: InsightDataMapper? = null
    ):
        TopAdsListAllInsightCountsResponse {
        setRequestParams(
            createRequestParam(
                source,
                adGroupType,
                insightType,
                startCursor,
                mapper
            ).parameters
        )
        val data = executeOnBackground()

        return when {
            data.topAdsListAllInsightCounts.error.title.isEmpty() -> {
                data
            }
            else -> throw (Throwable(data.topAdsListAllInsightCounts.error.title))
        }
    }

    private fun createRequestParam(
        source: String,
        adGroupType: String,
        insightType: Int,
        startCursor: String,
        mapper: InsightDataMapper?
    ): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(
            FILTER,
            mapOf(
                SHOP_ID to userSession.shopId,
                KEY_AD_GROUP_TYPES to convertStringToList(adGroupType),
                if (insightType == 0) {
                    PARAM_INSIGHT_TYPES to listOf(
                        INSIGHT_TYPE_POSITIVE_KEYWORD_INPUT,
                        INSIGHT_TYPE_KEYWORD_BID_INPUT,
                        INSIGHT_TYPE_GROUP_BID_INPUT,
                        INSIGHT_TYPE_DAILY_BUDGET_INPUT,
                        INSIGHT_TYPE_NEGATIVE_KEYWORD_INPUT
                    )
                } else {
                    PARAM_INSIGHT_TYPES to listOf(mapper?.insightTypeInputList?.get(insightType) ?: 0)
                }

            )
        )
        requestParams.putString(SOURCE, source)
        requestParams.putObject(
            PARAM_PAGE_SETTING,
            mapOf(
                PARAM_SIZE to PER_PAGE_COUNT_VALUE,
                PARAM_START_CURSOR to startCursor
            )
        )
        return requestParams
    }

    private fun convertStringToList(input: String): List<String> {
        return if (input.contains(",")) {
            input.split(",").map { it.trim() }
        } else {
            listOf(input)
        }
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topAdsListAllInsightCounts"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}filter: TopAdsListAllInsightCountsFilter!, ${'$'}source: String!, ${'$'}pageSetting: TopAdsListAllInsightCountsPageSetting!) {
              $OPERATION_NAME(filter: ${'$'}filter, source: ${'$'}source, pageSetting: ${'$'}pageSetting) {
                adGroups {
                  adGroupID
                  adGroupName
                  adGroupType
                  count
                }
                nextCursor
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
