package com.tokopedia.flight.resend_email.domain

import com.tokopedia.flight.resend_email.data.FlightOrderDetailResendETicketEntity
import com.tokopedia.flight.resend_email.data.FlightOrderGqlConst
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

/**
 * @author by furqan on 12/11/2020
 */
class FlightOrderResendEmailUseCase @Inject constructor(
        private val useCase: MultiRequestGraphqlUseCase) {

    suspend fun executeResendETicket(invoiceId: String, userEmail: String): Boolean {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()

        val params = mapOf(PARAM_INVOICE_ID to invoiceId,
                PARAM_EMAIL to userEmail)
        val graphqlRequest = GraphqlRequest(FlightOrderGqlConst.QUERY_RESEND_E_TICKET,
                FlightOrderDetailResendETicketEntity.Response::class.java, params)
        useCase.addRequest(graphqlRequest)

        val graphqlResponse = useCase.executeOnBackground()
        val resendStatus = graphqlResponse.getSuccessData<FlightOrderDetailResendETicketEntity.Response>().flightResendEmail.meta.status

        return resendStatus == RESEND_SUCCESS_STATUS
    }

    companion object {
        private const val PARAM_INVOICE_ID = "invoiceID"
        private const val PARAM_EMAIL = "email"

        private const val RESEND_SUCCESS_STATUS = "OK"
    }

}