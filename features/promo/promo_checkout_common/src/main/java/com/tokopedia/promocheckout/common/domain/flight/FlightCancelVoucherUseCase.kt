package com.tokopedia.promocheckout.common.domain.flight

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.data.PromoCheckoutCommonQueryConst
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class FlightCancelVoucherUseCase(private val graphqlUseCase: GraphqlUseCase) {

    fun execute(subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(PromoCheckoutCommonQueryConst.QUERY_FLIGHT_CANCEL_VOUCHER,
                FlightCancelVoucher.Response::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(RequestParams(), subscriber)
    }
}