package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.data.mapper.GetInitiateVoucherPageMapper
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import javax.inject.Inject

class GetInitiateVoucherPageUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetInitiateVoucherPageMapper
) : GraphqlUseCase<VoucherCreationMetadata>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val VOUCHER_TYPE_SHOP = 0
        private const val VOUCHER_TYPE_PRODUCT = 1
        private const val REQUEST_PARAM_ACTION = "Action"
        private const val REQUEST_PARAM_TARGET_BUYER = "TargetBuyer"
        private const val REQUEST_PARAM_COUPON_TYPE = "CouponType"
        private const val REQUEST_PARAM_IS_VOUCHER_PRODUCT = "IsVoucherProduct"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getInitiateVoucherPage"
        private val QUERY = """
              query $OPERATION_NAME(${'$'}Action: String, ${'$'}TargetBuyer: Int, ${'$'}CouponType: String, ${'$'}IsVoucherProduct: Int) {
                  $OPERATION_NAME(Action: ${'$'}Action, TargetBuyer: ${'$'}TargetBuyer, CouponType: ${'$'}CouponType, IsVoucherProduct: ${'$'}IsVoucherProduct) {
                    header {
                      messages
                      reason
                      error_code
                    }
                    data {
                      access_token
                      is_eligible
                      max_product
                      prefix_voucher_code
                      shop_id
                      token
                      user_id
                      discount_active
                    }
                  }
              }


        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): VoucherCreationMetadata {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetInitiateVoucherPageResponse>()
        return mapper.map(data)
    }

    suspend fun execute(): VoucherCreationMetadata {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetInitiateVoucherPageResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(): GraphqlRequest {
        val action = "update"

        val params = mapOf(
            REQUEST_PARAM_ACTION to action
        )

        return GraphqlRequest(
            query,
            GetInitiateVoucherPageResponse::class.java,
            params
        )
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val voucherProduct = if (param.isVoucherProduct) VOUCHER_TYPE_PRODUCT else VOUCHER_TYPE_SHOP

        val action = when (param.action) {
            VoucherAction.CREATE -> "create"
            VoucherAction.UPDATE -> "update"
        }

        val promoType = when (param.promoType) {
            PromoType.CASHBACK -> "cashback"
            PromoType.FREE_SHIPPING -> "shipping"
            PromoType.DISCOUNT -> "discount"
        }

        val params = mapOf(
            REQUEST_PARAM_ACTION to action,
            REQUEST_PARAM_TARGET_BUYER to Int.ZERO,
            REQUEST_PARAM_COUPON_TYPE to promoType,
            REQUEST_PARAM_IS_VOUCHER_PRODUCT to voucherProduct
        )

        return GraphqlRequest(
            query,
            GetInitiateVoucherPageResponse::class.java,
            params
        )
    }

    data class Param(
        val action: VoucherAction,
        val promoType: PromoType,
        val isVoucherProduct: Boolean
    )
}
