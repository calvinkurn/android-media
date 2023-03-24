package com.tokopedia.checkout.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeGqlResponse
import com.tokopedia.checkout.domain.model.platformfee.PlatformFeeRequest
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class DynamicPlatformFeeUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository
) : UseCase<PaymentFeeGqlResponse>() {
    private var params: Map<String, Any?>? = null

    fun setParams(request: PlatformFeeRequest) {
        params = mapOf(
            PROFILE_CODE_PARAM to request.profileCode,
            GATEWAY_CODE_PARAM to request.gatewayCode,
            PAYMENT_AMOUNT_PARAM to request.transactionAmount
        )
    }

    @GqlQuery(QUERY_GET_PLATFORM_FEE, QUERY)
    override suspend fun executeOnBackground(): PaymentFeeGqlResponse {
        if (params == null) {
            throw RuntimeException("Parameter is null!")
        }

        /*val response = graphqlRepository.response(listOf(
                GraphqlRequest(
                        GetPlatformFeeQuery(),
                        PaymentFeeGqlResponse::class.java,
                        params
                )
        )).getSuccessData<PaymentFeeGqlResponse>()*/

        val jsonRaw = """
            {
              "getPaymentFee": {
                "success": true,
                "errors": [],
                "data": [
                  {
                    "code": "platform_fee",
                    "title": "Biaya Jasa Aplikasi",
                    "fee": 1000,
                    "tooltip_info": "Terima kasih sudah belanja di Tokopedia! Biaya jasa aplikasi akan kami pakai untuk terus berikan layanan terbaik buat kamu. Jumlah biaya jasa aplikasi disesuaikan dengan total tagihan dan metode pembayaran kamu.",
                    "show_tooltip": true,
                    "show_slashed": false,
                    "slashed_fee": 0
                  }
                ]
              }
            }
        """.trimIndent()
        val gson = Gson()
        val response = gson.fromJson(jsonRaw, PaymentFeeGqlResponse::class.java)

        if (response.response.success) {
            return response
        } else {
            if (response.response.errors.isNotEmpty()) {
                throw CartResponseErrorException(response.response.errors.joinToString())
            } else {
                throw CartResponseErrorException(CartConstant.CART_ERROR_GLOBAL)
            }
        }
    }
    companion object {
        private const val PROFILE_CODE_PARAM = "profileCode"
        private const val GATEWAY_CODE_PARAM = "gatewayCode"
        private const val PAYMENT_AMOUNT_PARAM = "paymentAmount"
        private const val QUERY_GET_PLATFORM_FEE = "GetPlatformFeeQuery"

        private const val QUERY = """
            query getPaymentFee(${"$"}profileCode: String!, ${"$"}gatewayCode: String!, ${"$"}paymentAmount: Float!) {
                getPaymentFee(profileCode: ${"$"}profileCode, gatewayCode: ${"$"}gatewayCode, paymentAmount: ${"$"}paymentAmount) {
                    success
                    errors {
                        code
                        message
                    }
                    data {
                        code
                        title
                        fee
                        tooltip_info
                        show_tooltip
                        show_slashed
                        slashed_fee
                    }
                }
            }
        """
    }
}
