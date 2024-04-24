package com.tokopedia.shop.common.graphql.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.common.data.source.cloud.model.TokoPlusBadgeResponse
import com.tokopedia.shop.common.graphql.domain.mapper.TokoPlusBadgeMapper
import com.tokopedia.usecase.RequestParams
import okhttp3.internal.toLongOrDefault
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 01/07/22.
 */

@GqlQuery("GetShopChargeableGqlQuery", GetShopChargeableUseCase.QUERY)
class GetShopChargeableUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val mapper: TokoPlusBadgeMapper
) : GraphqlUseCase<TokoPlusBadgeResponse>(graphqlRepository) {

    suspend fun execute(
        shopId: String,
        shopTier: Int,
        totalTransactionSuccess: Long
    ): Boolean {
        val parameters = createParam(shopId, shopTier, totalTransactionSuccess).parameters
        setRequestParams(parameters)
        setGraphqlQuery(GetShopChargeableGqlQuery())
        setTypeClass(TokoPlusBadgeResponse::class.java)

        val data = executeOnBackground()
        return mapper.getIsChargeable(data)
    }

    private fun createParam(
        shopId: String,
        shopTier: Int,
        totalTransactionSuccess: Long
    ): RequestParams {
        return RequestParams.create().apply {
            putLong(PARAM_SHOP_ID, shopId.toLongOrDefault(0))
            putInt(PARAM_SHOP_TIER, shopTier)
            putLong(PARAM_TOTAL_TRANSACTION_SUCCESS, totalTransactionSuccess)
        }
    }

    companion object {
        private const val SHOP_CHARGEABLE = "shop_chargeable"

        internal const val QUERY = """
            query getShopChargeable(${'$'}shopId: Int!, ${'$'}shopTier: Int, ${'$'}totalTransactionSuccess: Int) {
              restrictValidateRestriction(
                input: {
                  source: "$SHOP_CHARGEABLE",
                  squad: "seller-home",
                  platform: "android",
                  dataRequest: [
                    {
                      shop: {
                        shopID: ${'$'}shopId, 
                        shopTier: ${'$'}shopTier, 
                        totalTransactionSuccess: ${'$'}totalTransactionSuccess
                      }
                    }
                  ]
                }
              ) {
                dataResponse {
                  status
                }
              }
            }
        """

        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_USER_ID = "userId"
        private const val PARAM_SHOP_TIER = "shopTier"
        private const val PARAM_TOTAL_TRANSACTION_SUCCESS = "totalTransactionSuccess"
    }
}
