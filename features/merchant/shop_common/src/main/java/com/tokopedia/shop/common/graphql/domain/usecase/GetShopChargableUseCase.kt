package com.tokopedia.shop.common.graphql.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.common.data.source.cloud.model.TokoPlusBadgeResponse
import com.tokopedia.shop.common.graphql.domain.mapper.TokoPlusBadgeMapper
import com.tokopedia.shop.common.view.model.TokoPlusBadgeUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 01/07/22.
 */

@GqlQuery("GetShopChargableGqlQuery", GetShopChargableUseCase.QUERY)
class GetShopChargableUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val mapper: TokoPlusBadgeMapper
) : GraphqlUseCase<TokoPlusBadgeResponse>(graphqlRepository) {

    suspend fun execute(
        shopId: String,
        shopTier: Int,
        totalTransactionSuccess: Long
    ): TokoPlusBadgeUiModel {
        val parameters = createParam(shopId, shopTier, totalTransactionSuccess).parameters
        setRequestParams(parameters)
        setGraphqlQuery(GetShopChargableGqlQuery())
        setTypeClass(TokoPlusBadgeResponse::class.java)

        val data = executeOnBackground()
        return mapper.mapToUiModel(data)
    }

    private fun createParam(
        shopId: String,
        shopTier: Int,
        totalTransactionSuccess: Long
    ): RequestParams {
        return RequestParams.create().apply {
            putLong(PARAM_SHOP_ID, shopId.toLongOrZero())
            putInt(PARAM_SHOP_TIER, shopTier)
            putLong(PARAM_TOTAL_TRANSACTION_SUCCESS, totalTransactionSuccess)
        }
    }

    companion object {
        internal const val QUERY = """
            query restrictValidateRestriction(${'$'}shopId: Int!, ${'$'}shopTier: Int, ${'$'}totalTransactionSuccess: Int) {
              restrictValidateRestriction(input: { source: "seller_dashboard", dataRequest: [], version: 2, metaRequest: [{ restriction_name: "shop_chargeable", dataRequest: [{shop: {shopID: ${'$'}shopId , shopTier: ${'$'}shopTier , totalTransactionSuccess: ${'$'}totalTransactionSuccess } }] }] }) {
                metaResponse {
                  restrictionName
                  dataResponse {
                    status
                    metadata {
                      bebasOngkirMetadata {
                        badgeURL
                      }
                    }
                  }
                }
              }
            }
        """

        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_SHOP_TIER = "shopTier"
        private const val PARAM_TOTAL_TRANSACTION_SUCCESS = "totalTransactionSuccess"
    }
}
