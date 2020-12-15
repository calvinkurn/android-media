package com.tokopedia.flight.orderdetail.domain

import com.tokopedia.flight.orderdetail.data.FlightOrderDetailETicketEntity
import com.tokopedia.flight.orderdetail.data.FlightOrderDetailGqlConst
import com.tokopedia.flight.orderdetail.data.FlightOrderDetailInvoiceEntity
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

/**
 * @author by furqan on 11/11/2020
 */
class FlightOrderDetailGetInvoiceEticketUseCase @Inject constructor(
        private val useCase: MultiRequestGraphqlUseCase) {

    suspend fun executeGetETicket(invoiceId: String): String {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
        useCase.clearRequest()

        val params = mapOf(PARAM_DATA to mapOf(PARAM_INVOICE_ID to invoiceId))
        val graphqlRequest = GraphqlRequest(FlightOrderDetailGqlConst.QUERY_GET_ORDER_E_TICKET,
                FlightOrderDetailETicketEntity.Response::class.java, params)
        useCase.addRequest(graphqlRequest)

        val graphqlResponse = useCase.executeOnBackground()
        val eticketResponse = graphqlResponse.getSuccessData<FlightOrderDetailETicketEntity.Response>()
        return eticketResponse.flightGetEticket.data
    }

    suspend fun executeGetInvoice(invoiceId: String): String {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
        useCase.clearRequest()

        val params = mapOf(PARAM_DATA to mapOf(PARAM_INVOICE_ID to invoiceId))
        val graphqlRequest = GraphqlRequest(FlightOrderDetailGqlConst.QUERY_GET_ORDER_INVOICE,
                FlightOrderDetailInvoiceEntity.Response::class.java, params)
        useCase.addRequest(graphqlRequest)

        val graphqlResponse = useCase.executeOnBackground()
        val invoiceResponse = graphqlResponse.getSuccessData<FlightOrderDetailInvoiceEntity.Response>()
        return invoiceResponse.flightGetInvoice.data
    }

    companion object {
        private const val PARAM_DATA = "data"
        private const val PARAM_INVOICE_ID = "invoiceID"
    }

}