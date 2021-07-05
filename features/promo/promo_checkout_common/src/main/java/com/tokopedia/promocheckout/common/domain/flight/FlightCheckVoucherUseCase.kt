package com.tokopedia.promocheckout.common.domain.flight

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.data.PromoCheckoutCommonQueryConst
import com.tokopedia.promocheckout.common.domain.model.FlightCheckVoucher
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class FlightCheckVoucherUseCase(private val graphqlUseCase: GraphqlUseCase) {

    val CART_ID = "cartID"
    val VOUCHER_CODE = "voucherCode"

    fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val graphqlRequest = GraphqlRequest(PromoCheckoutCommonQueryConst.QUERY_FLIGHT_CHECK_VOUCHER,
                    FlightCheckVoucher.Response::class.java, it.parameters, false)
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase.execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(voucherCode: String, cartID: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(VOUCHER_CODE, voucherCode)
        requestParams.putString(CART_ID, cartID)
        return requestParams
    }

}