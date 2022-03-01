package com.tokopedia.vouchercreation.product.list.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.product.list.domain.model.response.VoucherValidationPartialResponse
import javax.inject.Inject

class ValidateVoucherUseCase @Inject constructor(@ApplicationContext repository: GraphqlRepository)
    : GraphqlUseCase<VoucherValidationPartialResponse>(repository) {

    companion object {

        private const val PARAM_TARGET_BUYER = "target_buyer"
        private const val PARAM_BENEFIT_TYPE = "benefit_type"
        private const val PARAM_COUPON_TYPE = "coupon_type"
        private const val PARAM_BENEFIT_IDR = "benefit_idr"
        private const val PARAM_BENEFIT_MAX = "benefit_max"
        private const val PARAM_BENEFIT_PERCENT = "benefit_percent"
        private const val PARAM_MIN_PURCHASE = "min_purchase"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_IS_LOCK_TO_PRODUCT = "is_lock_to_product"
        private const val PARAM_PRODUCT_IDS = "product_ids"
        private const val TARGET_BUYER_VALUE = 0
        private const val IS_LOCK_TO_PRODUCT_VALUE = 1
        private const val SOURCE_VALUE = "seller app"

        @JvmStatic
        fun createRequestParams(
                benefitType: String,
                couponType: String,
                benefitIdr: Int,
                benefitMax: Int,
                benefitPercent: Int,
                minPurchase: Int,
                productIds: List<String>
        ): RequestParams {
            return RequestParams().apply {
                putInt(PARAM_TARGET_BUYER, TARGET_BUYER_VALUE)
                putString(PARAM_BENEFIT_TYPE, benefitType)
                putString(PARAM_COUPON_TYPE, couponType)
                putInt(PARAM_BENEFIT_IDR, benefitIdr)
                putInt(PARAM_BENEFIT_MAX, benefitMax)
                putInt(PARAM_BENEFIT_PERCENT, benefitPercent)
                putInt(PARAM_MIN_PURCHASE, minPurchase)
                putString(PARAM_SOURCE, SOURCE_VALUE)
                putInt(PARAM_IS_LOCK_TO_PRODUCT, IS_LOCK_TO_PRODUCT_VALUE)
                putObject(PARAM_PRODUCT_IDS, productIds.joinToString(separator = ","))
            }
        }
    }

    private val query = """
        query VoucherValidationPartial(
            ${'$'}target_buyer:Int,
            ${'$'}benefit_type:String,
            ${'$'}coupon_type:String, 
            ${'$'}benefit_idr:Int,
            ${'$'}benefit_max:Int,
            ${'$'}benefit_percent:Int,
            ${'$'}min_purchase:Int,
            ${'$'}source:String!,
            ${'$'}is_lock_to_product:Int, 
            ${'$'}product_ids:String
        ) {
          VoucherValidationPartial(VoucherValidationPartialInput:{
            target_buyer:${'$'}target_buyer,
            benefit_type:${'$'}benefit_type,
            coupon_type:${'$'}coupon_type,
            benefit_idr:${'$'}benefit_idr,
            benefit_max:${'$'}benefit_max,
            benefit_percent:${'$'}benefit_percent,
            min_purchase:${'$'}min_purchase,
            source:${'$'}source,
            is_lock_to_product:${'$'}is_lock_to_product,
            product_ids:${'$'}product_ids
          }) {
            header{
              process_time
              messages
              reason
              error_code
            }
            data{
              validation_product {
                parent_product_id
                is_variant
                is_eligible
                reason
                variant {
                  product_id
                  product_name
                  price
                  price_fmt
                  stock
                  is_eligible
                  reason
                }
              }
            }            
          }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setTypeClass(VoucherValidationPartialResponse::class.java)
    }
}