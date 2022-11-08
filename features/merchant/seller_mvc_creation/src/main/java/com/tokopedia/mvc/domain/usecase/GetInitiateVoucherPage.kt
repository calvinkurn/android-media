package com.tokopedia.mvc.domain.usecase


import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.GetInitiateVoucherPageMapper
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.data.response.ProductListResponse
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetInitiateVoucherPage @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetInitiateVoucherPageMapper,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<VoucherCreationMetadata>(repository) {

  /*  init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_ACTION = "Action"
        private const val REQUEST_PARAM_TARGET_BUYER = "TargetBuyer"
        private const val REQUEST_PARAM_COUPON_TYPE = "CouponType"
        private const val REQUEST_PARAM_IS_VOUCHER_PRODUCT = "IsVoucherProduct"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getInitiateVoucherPage"
        private val QUERY = """
              query $OPERATION_NAME($Action: String, $TargetBuyer: Int, $CouponType: String, $IsVoucherProduct: Int) {
                  $OPERATION_NAME(Action: $Action, TargetBuyer: $TargetBuyer, CouponType: $CouponType, IsVoucherProduct: $IsVoucherProduct) {
                    header {
                      messages
                      reason
                      error_code
                      __typename
                    }
                    data {
                      access_token
                      is_eligible
                      max_product
                      prefix_voucher_code
                      shop_id
                      token
                      user_id
                      __typename
                    }
                    __typename
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

    private fun buildRequest(param: Param): GraphqlRequest {
        val shopId = userSession.shopId


        val voucherProduct = if (param.isVoucherProduct) 1 else 0

        val couponType = when(param.couponType) {
            Param.CouponType.CASHBACK -> "cashback"
            Param.CouponType.FREE_SHIPPING -> "shipping"
        }
        val params = mapOf(
            REQUEST_PARAM_ACTION to shopId,
            REQUEST_PARAM_TARGET_BUYER to filter,
            REQUEST_PARAM_COUPON_TYPE to sort,
            REQUEST_PARAM_IS_VOUCHER_PRODUCT to voucherProduct
        )

        return GraphqlRequest(
            query,
            ProductListResponse::class.java,
            params
        )
    }


    data class Param(
        val action: Action,
        val targetBuyer: TargetBuyer,
        val couponType: CouponType,
        val isVoucherProduct: Boolean
    ) {
        enum class Action {
            CREATE,
            UPDATE
        }

        enum class TargetBuyer {

        },
        enum class CouponType {
            CASHBACK,
            FREE_SHIPPING
        }
    }
*/

}

