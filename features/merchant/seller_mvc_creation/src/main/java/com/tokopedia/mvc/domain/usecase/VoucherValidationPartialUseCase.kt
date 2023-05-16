package com.tokopedia.mvc.domain.usecase


import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.VoucherValidationPartialMapper
import com.tokopedia.mvc.data.request.VoucherValidationPartialRequest
import com.tokopedia.mvc.data.response.VoucherValidationPartialResponse
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import javax.inject.Inject

class VoucherValidationPartialUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: VoucherValidationPartialMapper,
) : GraphqlUseCase<String>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val SHOP_VOUCHER = 0
        private const val PRODUCT_VOUCHER = 1
        private const val REQUEST_PARAM_VOUCHER_VALIDATION_PARTIAL_INPUT = "VoucherValidationPartialInput"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "VoucherValidationPartial"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}VoucherValidationPartialInput: VoucherValidationPartialRequest!) {
                  $OPERATION_NAME(VoucherValidationPartialInput: ${'$'}VoucherValidationPartialInput) {
                    header {
                      messages
                      reason
                      error_code
                    }
                    data {
                      validation_error {
                        benefit_idr
                        benefit_max
                        benefit_percent
                        benefit_type
                        code
                        coupon_name
                        coupon_type
                        date_end
                        date_start
                        hour_end
                        hour_start
                        image
                        image_square
                        is_public
                        min_purchase
                        quota
                        minimum_tier_level
                        min_purchase_type
                      }
                      validation_product {
                        parent_product_id
                        is_variant
                        variant {
                          product_id
                          product_name
                          price
                          price_fmt
                          stock
                          sku
                          is_eligible
                          reason
                        }
                        is_eligible
                        reason
                      }
                      validation_date {
                        date_end
                        date_start
                        hour_end
                        hour_start
                        total_live_time
                        available
                        not_available_reason
                        type
                      }
                      available_month {
                        month
                        available
                      }
                      total_available_quota
                    }
                  }
            }



    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): VoucherValidationResult {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<VoucherValidationPartialResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val formattedProductIds = param.productIds.joinToString(separator = ",") { it.toString() }

        val benefitType = when(param.benefitType) {
            BenefitType.NOMINAL -> "idr"
            BenefitType.PERCENTAGE -> "percent"
        }

        val promoType = when(param.promoType) {
            PromoType.CASHBACK -> "cashback"
            PromoType.FREE_SHIPPING -> "shipping"
            PromoType.DISCOUNT -> "discount"
        }

        val isPublic = when(param.isPublic) {
            true -> 1
            else -> 0
        }

        val payload = VoucherValidationPartialRequest(
            benefitIdr = param.benefitIdr,
            benefitMax = param.benefitMax,
            benefitPercent = param.benefitPercent,
            benefitType = benefitType,
            couponType = promoType,
            isLockToProduct = if (param.isVoucherProduct) PRODUCT_VOUCHER else SHOP_VOUCHER,
            minPurchase = param.minPurchase,
            productIds = formattedProductIds,
            targetBuyer = param.targetBuyer.id,
            couponName = param.couponName,
            isPublic = isPublic,
            code = param.code,
            isPeriod = param.isPeriod,
            periodType = param.periodType,
            periodRepeat = param.periodRepeat,
            totalPeriod = param.totalPeriod,
            startDate = param.startDate,
            endDate = param.endDate,
            startHour = param.startHour,
            endHour = param.endHour,
            quota = param.quota
        )

        val params = mapOf(REQUEST_PARAM_VOUCHER_VALIDATION_PARTIAL_INPUT to payload)
        return GraphqlRequest(
            query,
            VoucherValidationPartialResponse::class.java,
            params
        )
    }

    data class Param(
        val benefitIdr: Long,
        val benefitMax: Long,
        val benefitPercent: Int,
        val benefitType : BenefitType,
        val promoType: PromoType,
        val isVoucherProduct: Boolean,
        val minPurchase: Long,
        val productIds: List<Long>,
        val targetBuyer: VoucherTargetBuyer,
        val couponName: String,
        val isPublic: Boolean,
        val code: String,
        val isPeriod: Boolean,
        val periodType: Int,
        val periodRepeat: Int,
        val totalPeriod: Int,
        val startDate: String,
        val endDate: String,
        val startHour: String,
        val endHour: String,
        val quota: Long
    )
}

