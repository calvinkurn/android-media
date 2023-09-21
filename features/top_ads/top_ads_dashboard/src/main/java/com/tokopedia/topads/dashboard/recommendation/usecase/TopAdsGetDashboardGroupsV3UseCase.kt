package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsGetDashboardGroupsV3UseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    val userSession: UserSessionInterface,
) : GraphqlUseCase<DashGroupListResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(DashGroupListResponse::class.java)
    }

    suspend operator fun invoke(search: String, groupType: Int): DashGroupListResponse {
        setRequestParams(createRequestParam(search, groupType).parameters)
        return executeOnBackground()
    }

    private fun createRequestParam(search: String, groupType: Int): RequestParams {
        val requestParam = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[TopAdsProductRecommendationConstants.SHOP_Id_KEY] = userSession.shopId
        queryMap[TopAdsProductRecommendationConstants.KEYWORD] = search
        queryMap[TopAdsProductRecommendationConstants.GROUP_TYPE_KEY] = groupType
        requestParam.putAll(mapOf(ParamObject.QUERY_INPUT to queryMap))
        return requestParam
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "GetTopadsDashboardGroupsV3"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}queryInput: GetTopadsDashboardGroupsInputTypeV3!) {
                $OPERATION_NAME(queryInput: ${'$'}queryInput) {
                    meta {
                        page {
                            per_page
                            current
                            total
                        }
                    }
                    data {
                        group_id
                        group_type
                        total_item
                        total_keyword
                        group_status_desc
                        group_name
                    }
                }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

        override fun getQuery(): String = QUERY

        override fun getTopOperationName(): String = OPERATION_NAME

    }
}
