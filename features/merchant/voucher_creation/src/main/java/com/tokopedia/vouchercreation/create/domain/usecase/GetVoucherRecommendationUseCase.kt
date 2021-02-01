package com.tokopedia.vouchercreation.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.vouchercreation.create.domain.model.GetVoucherRecommendationResponse
import com.tokopedia.vouchercreation.create.domain.model.VoucherRecommendationData
import javax.inject.Inject

class GetVoucherRecommendationUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<VoucherRecommendationData>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): VoucherRecommendationData {
        val graphqlRequest = GraphqlRequest(query, GetVoucherRecommendationResponse::class.java, params.parameters)
        val graphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        val errors = graphqlResponse.getError(GetVoucherRecommendationResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = graphqlResponse.getData<GetVoucherRecommendationResponse>(GetVoucherRecommendationResponse::class.java)
            with(data.voucherRecommendation.header) {
                if (!errorCode.isNullOrEmpty()) {
                    throw MessageErrorException(messages.first())
                }
            }
            return data.voucherRecommendation.data
        } else {
            throw MessageErrorException(errors.joinToString(", ") {
                it.message
            })
        }
    }

    companion object {
        private const val VOUCHER_TYPE_KEY = "voucherType"
        private const val VOUCHER_DISCOUNT_TYPE = "voucherDiscountType"

        @JvmStatic
        fun createRequestParam(voucherType: Int, voucherDiscountType: Int): RequestParams = RequestParams.create().apply {
            putInt(VOUCHER_TYPE_KEY, voucherType)
            putInt(VOUCHER_DISCOUNT_TYPE, voucherDiscountType)
        }

        private val query = """
            query MerchantPromotionGetVoucherRecommendation(${'$'}voucherType: Int!, ${'$'}voucherDiscountType: Int!) {
            MerchantPromotionGetVoucherRecommendation(VoucherType: ${'$'}voucherType, VoucherDiscountType: ${'$'}voucherDiscountType) {
                    header{
                        process_time
                        message
                        reason
                        error_code
                    }
                    data{
                      ShopID
                      VoucherType
                      VoucherBenefitType
                      VoucherDiscountAmt
                      VoucherDiscountAmtMax
                      VoucherMinimumAmt
                      VoucherQuota
                      HaveRecommendation
                    }
                }
            }
        """.trimIndent()
    }
}