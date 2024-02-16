package com.tokopedia.checkoutpayment.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkoutpayment.data.PaymentFeeGqlResponse
import com.tokopedia.checkoutpayment.data.PaymentFeeRequest
import com.tokopedia.checkoutpayment.view.OrderPaymentFee
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class DynamicPaymentFeeUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    executorDispatchers: CoroutineDispatchers
) : CoroutineUseCase<PaymentFeeRequest, List<OrderPaymentFee>>(executorDispatchers.io) {

    override fun graphqlQuery(): String {
        return QUERY
    }

    @GqlQuery("GetPaymentFeeQuery", QUERY)
    override suspend fun execute(params: PaymentFeeRequest): List<OrderPaymentFee> {
        val response = graphqlRepository.response(
            listOf(
                GraphqlRequest(
                    GetPaymentFeeQuery(),
                    PaymentFeeGqlResponse::class.java,
                    mapOf(
                        PROFILE_CODE_PARAM to params.profileCode,
                        GATEWAY_CODE_PARAM to params.gatewayCode,
                        PAYMENT_AMOUNT_PARAM to params.transactionAmount,
                        ADDITIONAL_DATA_PARAM to params.additionalData,
                        DETAIL_DATA_PARAM to params.paymentRequest
                    )
                )
            )
        ).getData<PaymentFeeGqlResponse>(PaymentFeeGqlResponse::class.java)
        if (!response.response.success) {
            throw MessageErrorException(response.response.errors.getOrNull(0)?.message)
        }
        return response.response.data.map {
            OrderPaymentFee(
                it.title,
                it.fee,
                it.showTooltip,
                it.showSlashed,
                it.slashedFee,
                it.tooltipInfo
            )
        }
    }

    companion object {
        private const val PROFILE_CODE_PARAM = "profileCode"
        private const val GATEWAY_CODE_PARAM = "gatewayCode"
        private const val PAYMENT_AMOUNT_PARAM = "paymentAmount"
        private const val ADDITIONAL_DATA_PARAM = "additionalData"
        private const val DETAIL_DATA_PARAM = "detailData"

        private const val QUERY = """
            query getPaymentFee(${"$"}profileCode: String!, ${"$"}gatewayCode: String!, ${"$"}paymentAmount: Float!, ${"$"}additionalData: String!, ${"$"}detailData: JSONType) {
                getPaymentFee(profileCode: ${"$"}profileCode, gatewayCode: ${"$"}gatewayCode, paymentAmount: ${"$"}paymentAmount, additionalData: ${"$"}additionalData, detailData: ${"$"}detailData) {
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
