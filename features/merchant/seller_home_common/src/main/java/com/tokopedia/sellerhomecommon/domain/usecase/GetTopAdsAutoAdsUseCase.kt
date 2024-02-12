package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerhomecommon.domain.model.AutoAdsResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetTopAdsAutoAdsUseCase @Inject constructor(
    repository: GraphqlRepository,
) : GraphqlUseCase<AutoAdsResponse>(repository) {


    init {
        setGraphqlQuery(GetAutoAdsV2)
        setTypeClass(AutoAdsResponse::class.java)
    }

    suspend fun execute(shopId: String): AutoAdsResponse.TopAdsGetAutoAds.Data {
        setRequestParams(
            createRequestParams(
                shopId
            ).parameters
        )

        return executeOnBackground().topAdsGetAutoAds.data
    }

    fun createRequestParams(shopId: String): RequestParams {
        return RequestParams.create().apply {
            putString(GetAutoAdsV2.PARAM_SHOP_ID, shopId)
            putString(GetAutoAdsV2.PARAM_SOURCE, GetAutoAdsV2.SOURCE_VALUE)
        }
    }

    object GetAutoAdsV2 : GqlQueryInterface {

        private const val OPERATION_NAME = "topAdsGetAutoAdsV2"
        const val PARAM_SHOP_ID = "shopID"
        const val PARAM_SOURCE = "source"
        const val SOURCE_VALUE = "android.topads_product_iklan"

        override fun getOperationNameList(): List<String> {
            return listOf(OPERATION_NAME)
        }

        override fun getQuery(): String {
            return """
            query $OPERATION_NAME(${'$'}$PARAM_SHOP_ID: String!, ${'$'}$PARAM_SOURCE: String!){
            $OPERATION_NAME(shopID: ${'$'}$PARAM_SHOP_ID, source: ${'$'}$PARAM_SOURCE){
                data {
                    shopID
                    status
                    statusDesc
                    dailyBudget
                    dailyUsage
                    type
                    typeDesc
                }
            }
        }
        """.trimIndent()
        }

        override fun getTopOperationName(): String {
            return OPERATION_NAME
        }
    }
}
