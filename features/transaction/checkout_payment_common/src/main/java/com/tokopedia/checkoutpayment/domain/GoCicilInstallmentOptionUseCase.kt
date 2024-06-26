package com.tokopedia.checkoutpayment.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkoutpayment.data.GoCicilInstallmentGqlResponse
import com.tokopedia.checkoutpayment.data.GoCicilInstallmentOptionResponse
import com.tokopedia.checkoutpayment.data.GoCicilInstallmentRequest
import com.tokopedia.checkoutpayment.data.GoCicilInstallmentResponse
import com.tokopedia.checkoutpayment.generateAppVersionForPayment
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class GoCicilInstallmentOptionUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GoCicilInstallmentRequest, GoCicilInstallmentData>(dispatchers.io) {

    private fun generateParam(param: GoCicilInstallmentRequest): Map<String, Any?> {
        return mapOf(
            PARAM_GATEWAY_CODE to param.gatewayCode,
            PARAM_MERCHANT_CODE to param.merchantCode,
            PARAM_PROFILE_CODE to param.profileCode,
            PARAM_USER_DEFINED_VALUE to param.userDefinedValue,
            PARAM_PAYMENT_AMOUNT to param.paymentAmount,
            PARAM_ORDER_METADATA to param.orderMetadata,
            PARAM_PROMO_PARAM to param.promoParam,
            PARAM_APP_VERSION to generateAppVersionForPayment(),
            PARAM_ADDITIONAL_DATA to param.additionalData,
            PARAM_DETAIL_DATA to param.detailData
        )
    }

    companion object {
        private const val PARAM_GATEWAY_CODE = "gatewayCode"
        private const val PARAM_MERCHANT_CODE = "merchantCode"
        private const val PARAM_PROFILE_CODE = "profileCode"
        private const val PARAM_USER_DEFINED_VALUE = "userDefinedValue"
        private const val PARAM_PAYMENT_AMOUNT = "paymentAmount"
        private const val PARAM_ORDER_METADATA = "orderMetadata"
        private const val PARAM_PROMO_PARAM = "promoParam"
        private const val PARAM_APP_VERSION = "appVersion"
        private const val PARAM_ADDITIONAL_DATA = "additionalData"
        private const val PARAM_DETAIL_DATA = "detailData"

        private const val GoCicilInstallmentOptionQuery = "GoCicilInstallmentOptionQuery"
        private const val QUERY = """
            query getInstallmentInfo(${'$'}gatewayCode: String!, ${'$'}merchantCode: String!, ${'$'}profileCode: String!, ${'$'}userDefinedValue: String!, ${'$'}paymentAmount: Float!, ${'$'}orderMetadata: String, ${'$'}promoParam: String, ${'$'}appVersion: String, ${'$'}additionalData: String, ${"$"}detailData: JSONType) {
                getInstallmentInfo(gatewayCode: ${'$'}gatewayCode, merchantCode: ${'$'}merchantCode, profileCode: ${'$'}profileCode, userDefinedValue: ${'$'}userDefinedValue, paymentAmount: ${'$'}paymentAmount, orderMetadata: ${'$'}orderMetadata, promoParam: ${'$'}promoParam, appVersion: ${'$'}appVersion, additionalData: ${'$'}additionalData, detailData: ${"$"}detailData) {
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

    override fun graphqlQuery(): String {
        return QUERY
    }

    @GqlQuery(GoCicilInstallmentOptionQuery, QUERY)
    override suspend fun execute(params: GoCicilInstallmentRequest): GoCicilInstallmentData {
        val request = GraphqlRequest(
            GoCicilInstallmentOptionQuery(),
            GoCicilInstallmentGqlResponse::class.java,
            generateParam(params)
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<GoCicilInstallmentGqlResponse>()
        if (!response.response.success) {
            throw MessageErrorException()
        }
        return mapResponse(response.response)
    }

    private fun mapResponse(response: GoCicilInstallmentResponse): GoCicilInstallmentData {
        return GoCicilInstallmentData(
            tickerMessage = response.data.ticker.message,
            installmentOptions = mapInstallmentOptions(response.data.installmentOptions)
        )
    }

    private fun mapInstallmentOptions(options: List<GoCicilInstallmentOptionResponse>): List<GoCicilInstallmentOption> {
        return options.map {
            GoCicilInstallmentOption(
                installmentTerm = it.installmentTerm,
                optionId = it.optionId,
                firstInstallmentTime = it.firstInstallmentTime,
                estInstallmentEnd = it.estInstallmentEnd,
                firstDueMessage = it.firstDueMessage,
                interestAmount = it.interestAmount,
                feeAmount = it.feeAmount,
                installmentAmountPerPeriod = it.installmentAmountPerPeriod,
                labelType = it.labelType,
                labelMessage = it.labelMessage,
                isActive = it.isActive,
                description = it.description,
                isRecommended = it.isRecommended
            )
        }
    }
}
