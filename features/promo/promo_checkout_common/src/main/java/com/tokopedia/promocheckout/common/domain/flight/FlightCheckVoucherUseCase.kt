package com.tokopedia.promocheckout.common.domain.flight

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.FlightCheckVoucher
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class FlightCheckVoucherUseCase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) {

    val CART_ID = "cartID"
    val VOUCHER_CODE = "voucherCode"

    fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                    R.raw.promo_checkout_flight_check_voucher), FlightCheckVoucher.Response::class.java, it.parameters, false)
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