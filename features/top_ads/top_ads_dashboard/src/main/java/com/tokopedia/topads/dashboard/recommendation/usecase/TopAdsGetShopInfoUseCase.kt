package com.tokopedia.topads.dashboard.recommendation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.recommendation.data.model.cloud.TopAdsGetShopInfoResponse
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsGetShopInfoUiModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TopAdsGetShopInfoUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<TopAdsGetShopInfoResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GqlQuery)
        setTypeClass(TopAdsGetShopInfoResponse::class.java)
    }

    suspend operator fun invoke(source: String):
        Result<TopAdsGetShopInfoUiModel> {
        setRequestParams(createRequestParam(source).parameters)
        val data = executeOnBackground()

        return when {
            data.topAdsGetShopInfoV21.errors.isEmpty() -> {
                Success(data.toTopAdsGetShopInfoModel())
            }
            else -> Fail(Throwable(data.topAdsGetShopInfoV21.errors.firstOrNull()?.title))
        }
    }

    private fun createRequestParam(source: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(SHOP_ID, userSession.shopId)
        requestParams.putString(SOURCE, source)
        return requestParams
    }

    object GqlQuery : GqlQueryInterface {
        private const val OPERATION_NAME = "topadsGetShopInfoV2_1"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}shopID: String!, ${'$'}source: String!) {
              $OPERATION_NAME(shopID: ${'$'}shopID, source: ${'$'}source) {
                data {
                  ads {
                    type
                    is_used
                  }
                }
                errors {
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
