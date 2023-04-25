package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsListAllInsightCountsResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.InsightUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.SaranTopAdsChipsUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsListAllInsightState
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsListAllInsightCountsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface,
) : GraphqlUseCase<TopAdsListAllInsightCountsResponse>(graphqlRepository) {

    val list = mutableListOf(
        SaranTopAdsChipsUiModel("Semua"),
        SaranTopAdsChipsUiModel("keyword_bid"),
        SaranTopAdsChipsUiModel("group_daily_budget"),
        SaranTopAdsChipsUiModel("group_bid"),
        SaranTopAdsChipsUiModel("keyword_new_negative"),
        SaranTopAdsChipsUiModel("keyword_new_positive"),
    )

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsListAllInsightCountsResponse::class.java)
    }

    suspend operator fun invoke(
        source: String,
        adGroupType: String,
        insightType: Int,
        startCursor: String,
    ):
        TopAdsListAllInsightState<InsightUiModel> {
        setRequestParams(
            createRequestParam(
                source,
                adGroupType,
                insightType,
                startCursor
            ).parameters
        )
        val data = executeOnBackground()

        return when {
            data.topAdsListAllInsightCounts.error.title.isEmpty() -> {
                TopAdsListAllInsightState.Success(
                    data.toInsightUiModel().also { it.insightType = insightType })
            }
            else -> TopAdsListAllInsightState.Fail(Throwable(data.topAdsListAllInsightCounts.error.title))
        }
    }

    private fun createRequestParam(
        source: String,
        adGroupType: String,
        insightType: Int,
        startCursor: String,
    ): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(
            "filter", mapOf(
                "shopID" to "13124489",
                "adGroupTypes" to listOf(adGroupType),
                if (insightType == 0) {
                    "insightTypes" to listOf(
                        "keyword_bid",
                        "group_daily_budget",
                        "group_bid",
                        "keyword_new_negative",
                        "keyword_new_positive"
                    )
                } else {
                    "insightTypes" to listOf(list[insightType].name)
                }

            )
        )
        requestParams.putString("source", source)
        requestParams.putObject(
            "pageSetting", mapOf(
                "size" to 20,
                "startCursor" to startCursor
            )
        )
        return requestParams
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topAdsListAllInsightCounts"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}filter: TopAdsListAllInsightCountsFilter!, ${'$'}source: String!, ${'$'}pageSetting: TopAdsListAllInsightCountsPageSetting!) {
              $OPERATION_NAME(filter: ${'$'}filter, source: ${'$'}source, pageSetting: ${'$'}pageSetting) {
                adGroups {
                  adGroupID
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
