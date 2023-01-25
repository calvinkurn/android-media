package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentData
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentGqlResponse
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentRequest
import javax.inject.Inject

class GoCicilInstallmentOptionUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) {

    @GqlQuery(GoCicilInstallmentOptionQuery, QUERY)
    suspend fun executeSuspend(param: GoCicilInstallmentRequest): GoCicilInstallmentData {
        val request = GraphqlRequest(GoCicilInstallmentOptionQuery(), GoCicilInstallmentGqlResponse::class.java, generateParam(param))
        val response = graphqlRepository.response(listOf(request)).getSuccessData<GoCicilInstallmentGqlResponse>()
        if (!response.response.success) {
            throw MessageErrorException()
        }
        return response.response.data
    }

    private fun generateParam(param: GoCicilInstallmentRequest): Map<String, Any?> {
        return mapOf(
                PARAM_GATEWAY_CODE to param.gatewayCode,
                PARAM_MERCHANT_CODE to param.merchantCode,
                PARAM_PROFILE_CODE to param.profileCode,
                PARAM_USER_DEFINED_VALUE to param.userDefinedValue,
                PARAM_PAYMENT_AMOUNT to param.paymentAmount,
                PARAM_ORDER_METADATA to param.orderMetadata,
        )
    }

    companion object {
        private const val PARAM_GATEWAY_CODE = "gatewayCode"
        private const val PARAM_MERCHANT_CODE = "merchantCode"
        private const val PARAM_PROFILE_CODE = "profileCode"
        private const val PARAM_USER_DEFINED_VALUE = "userDefinedValue"
        private const val PARAM_PAYMENT_AMOUNT = "paymentAmount"
        private const val PARAM_ORDER_METADATA = "orderMetadata"

        private const val GoCicilInstallmentOptionQuery = "GoCicilInstallmentOptionQuery"
        private const val QUERY = """
            query getInstallmentInfo(${'$'}gatewayCode: String!, ${'$'}merchantCode: String!, ${'$'}profileCode: String!, ${'$'}userDefinedValue: String!, ${'$'}paymentAmount: Float!, ${'$'}orderMetadata: String) {
                getInstallmentInfo(gatewayCode: ${'$'}gatewayCode, merchantCode: ${'$'}merchantCode, profileCode: ${'$'}profileCode, userDefinedValue: ${'$'}userDefinedValue, paymentAmount: ${'$'}paymentAmount, orderMetadata: ${'$'}orderMetadata) {
                    success
                    data {
                        ticker {
                            code
                            message
                        }
                        installment_options {
                            installment_term
                            option_id
                            first_installment_time
                            est_installment_end
                            first_due_message
                            interest_amount
                            fee_amount
                            total_amount
                            installment_amount_per_period
                            label_type
                            label_message
                            is_active
                            description
                            is_recommended
                        }
                    }
                }
            }
        """
    }
}
