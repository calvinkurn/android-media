package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject.FILTER
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_ID
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
                "adGroupTypes" to convertStringToList(adGroupType),
                if (insightType == 0) {
                    "insightTypes" to listOf(
                        "keyword_bid",
                        "group_daily_budget",
                        "group_bid",
                        "keyword_new_negative",
                        "keyword_new_positive"
                    )
                } else {
                    "insightTypes" to listOf(mapper?.insightTypeInputList?.get(insightType) ?: 0)
                }

            )
        )
        requestParams.putString("source", source)
        requestParams.putObject(
            "pageSetting",
            mapOf(
                "size" to 20,
                "startCursor" to startCursor
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
