package com.tokopedia.promocheckout.common.domain.hotel

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.HotelCheckVoucher
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class HotelCheckVoucherUseCase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) {

    val PARAMNAME_CART_ID = "cartID"
    val PARAMNAME_VOUCHER_CODE = "voucherCode"
    val PARAMNAME_DATA = "data"

    fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val variables = mapOf(PARAMNAME_DATA to it.parameters)
            val graphqlRequest = GraphqlRequest(PromoQuery.promoCheckoutHotelCheckVoucher(),
                    HotelCheckVoucher.Response::class.java, variables, false)
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase.execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(voucherCode: String, cartID: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAMNAME_VOUCHER_CODE, voucherCode)
        requestParams.putString(PARAMNAME_CART_ID, cartID)
        return requestParams
    }

}