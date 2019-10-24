package com.tokopedia.promocheckout.common.domain.flight

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigital
import com.tokopedia.promocheckout.common.domain.model.FlightCancelVoucher
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class FlightCancelVoucherUseCase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) {

    fun execute(subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.promo_checkout_flight_cancel_voucher), FlightCancelVoucher.Response::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(RequestParams(), subscriber)
    }
}