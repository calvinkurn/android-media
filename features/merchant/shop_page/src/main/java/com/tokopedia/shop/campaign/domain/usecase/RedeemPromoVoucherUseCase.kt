package com.tokopedia.shop.campaign.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.campaign.data.mapper.RedeemPromoVoucherMapper
import com.tokopedia.shop.campaign.data.response.RedeemPromoVoucherResponse
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.common.constant.ShopPageConstant
import javax.inject.Inject

/**
 * Generated by Tokopedia GQL UseCase Generator plugins version 1.0
 */
class RedeemPromoVoucherUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: RedeemPromoVoucherMapper
) : GraphqlUseCase<RedeemPromoVoucherResult>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY_CATALOG_ID= "catalog_id"
        private const val REQUEST_PARAM_KEY_API_VERSION = "apiVersion"
        private const val REQUEST_PARAM_KEY_IS_GIFT = "is_gift"
    }

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }


    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "hachikoRedeem"
        private val MUTATION =
            """mutation hachikoRedeem(${'$'}catalog_id: Int, ${'$'}is_gift: Int, ${'$'}apiVersion: String) {
  hachikoRedeem(catalog_id: ${'$'}catalog_id, is_gift: ${'$'}is_gift, apiVersion: ${'$'}apiVersion) {
    coupons {
      owner
      cta
      code
      cta_desktop
      description
      id
      promo_id
      title
    }
    redeemMessage
    reward_points
  }
}
"""

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }


    suspend fun execute(param: Param): RedeemPromoVoucherResult {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<RedeemPromoVoucherResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val params = mapOf(
            REQUEST_PARAM_KEY_CATALOG_ID to param.catalogId,
            REQUEST_PARAM_KEY_IS_GIFT to param.isGift,
            REQUEST_PARAM_KEY_API_VERSION to ShopPageConstant.HACHIKO_VOUCHER_GRAPHQL_API_VERSION
        )

        return GraphqlRequest(
            mutation,
            RedeemPromoVoucherResponse::class.java,
            params
        )
    }

    data class Param(val catalogId: Long, val isGift: Int)


}
