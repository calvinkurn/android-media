package com.tokopedia.buyerorder.unifiedhistory.list.domain

import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.FlightResendEmail
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 07/08/20.
 */
class FlightResendEmailUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(invoiceId: String, email: String): Result<FlightResendEmail.Data> {
        return try {
            val request = GraphqlRequest(QUERY, FlightResendEmail.Data::class.java, generateParam(invoiceId, email))
            val response = gqlRepository.getReseponse(listOf(request)).getSuccessData<FlightResendEmail.Data>()
            Success(response)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    private fun generateParam(invoiceId: String, email: String): Map<String, Any?> {
        return mapOf(UohConsts.FLIGHT_GQL_PARAM_INVOICE_ID to invoiceId,
                UohConsts.FLIGHT_GQL_PARAM_EMAIL_ID to email)
    }

    companion object {
        val QUERY = """
            mutation ResendBookingEmail(${'$'}invoiceID: String!, ${'$'}email: String!) {
              flightResendEmailV2(invoiceID:${'$'}invoiceID, email:${'$'}email) {
                meta {
                  status
                }
              }
            }
            """.trimIndent()
    }
}