package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.GROUP_IDS_KEY
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.SHOPID
import com.tokopedia.topads.common.data.model.TotalProductKeyResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsGetTotalAdsAndKeywordsUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TotalProductKeyResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TotalProductKeyResponse::class.java)
    }

    suspend operator fun invoke(groupIds: List<String>): TotalProductKeyResponse {
        setRequestParams(createRequestParam(groupIds).parameters)
        return executeOnBackground()
    }

    private fun createRequestParam(groupIds: List<String>): RequestParams {
        val requestParam = RequestParams.create()
        requestParam.putString(SHOPID, userSession.shopId)
        requestParam.putObject(GROUP_IDS_KEY, groupIds)
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
