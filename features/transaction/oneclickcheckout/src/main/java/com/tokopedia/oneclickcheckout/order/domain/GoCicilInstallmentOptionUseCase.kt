package com.tokopedia.oneclickcheckout.order.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.oneclickcheckout.order.data.creditcard.CreditCardTenorListRequest
import com.tokopedia.oneclickcheckout.order.data.gocicil.GoCicilInstallmentOption
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentGoCicilTerms
import kotlinx.coroutines.delay
import javax.inject.Inject

class GoCicilInstallmentOptionUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) {

    @GqlQuery(GoCicilInstallmentOptionQuery, QUERY)
    suspend fun executeSuspend(param: CreditCardTenorListRequest): List<OrderPaymentGoCicilTerms> {
//        val request = GraphqlRequest(GoCicilInstallmentOptionQuery(), CreditCardTenorListResponse::class.java, generateParam(param))
//        val response = graphqlRepository.response(listOf(request)).getSuccessData<CreditCardTenorListResponse>()
        delay(5_000)
        return listOf(
                OrderPaymentGoCicilTerms(isActive = true),
                OrderPaymentGoCicilTerms(isActive = false),
                OrderPaymentGoCicilTerms(isActive = true, installmentTerm = 1),
        )
    }

    fun generateParam(input: CreditCardTenorListRequest): Map<String, Any?> {
        return mapOf(INPUT to input)
    }

    private fun mapInstallmentOptions(installmentOptions: List<GoCicilInstallmentOption>): List<OrderPaymentGoCicilTerms> {
        return installmentOptions.map {
            OrderPaymentGoCicilTerms(
                    installmentTerm = it.installmentTerm,
                    optionId = it.optionId,
                    firstInstallmentDate = it.firstInstallmentTime,
                    lastInstallmentDate = it.estInstallmentEnd,
                    firstDueMessage = it.firstDueMessage,
                    interestAmount = it.interestAmount,
                    feeAmount = it.feeAmount,
                    installmentAmountPerPeriod = it.installmentAmountPerPeriod,
                    labelType = it.labelType,
                    labelMessage = it.labelMessage,
                    isActive = it.isActive,
                    description = it.description
            )
        }
    }

    companion object {
        private const val INPUT = "input"

        private const val GoCicilInstallmentOptionQuery = "GoCicilInstallmentOptionQuery"
        private const val QUERY = """
            query getInstallmentInfo(${'$'}gatewayCode: String!, ${'$'}merchantCode: String!, ${'$'}profileCode: String!, ${'$'}userDefinedValue: String!, ${'$'}paymentAmount: Float!, ${'$'}signature: String, ${'$'}orderMetadata: String) {
                getInstallmentInfo(gatewayCode: "PEMUDACICIL", merchantCode: "tokopedia", profileCode: "TKPD_DEFAULT", userDefinedValue: "%7B%22user_id%22%3A8970876%2C%22vcc_id%22%3A0%2C%22vcc_user_id%22%3A0%2C%22is_instant%22%3Afalse%2C%22device%22%3A1%2C%22msisdn_verified%22%3A621234567890%2C%22va_code%22%3A%22081234567890%22%2C%22is_qa_account%22%3A%220%22%2C%22callback_url%22%3A%22%22%2C%22subsidized_amount%22%3A0%2C%22is_stacking%22%3Afalse%2C%22payment_level_additional_detail%22%3A%7B%22total_order_price%22%3A0%2C%22donation_amount%22%3A0%2C%22egold_amount%22%3A0%2C%22final_promo_benefit_discount%22%3A0%2C%22final_promo_benefit_cashback%22%3A0%7D%2C%22is_mitra%22%3Afalse%7D", paymentAmount: 500000, orderMetadata: "%7B%22order_data%22%3A%5B%7B%22order_id%22%3A1%2C%22merchant_type%22%3A%22RM%22%2C%22order_amount%22%3A10000%7D%2C%7B%22order_id%22%3A2%2C%22merchant_type%22%3A%22OS%22%2C%22order_amount%22%3A30000%7D%5D%7D") {
                    success
                    installment_options {
                        installment_term
                        option_id
                        installment_period
                        first_installment_time
                        est_installment_end
                        first_due_message
                        principal_amount
                        interest_amount
                        fee_amount
                        total_amount
                        installment_amount_per_period
                        label_type
                        label_message
                        is_active
                        description
                    }
                }
            }
        """
    }
}