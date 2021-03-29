package com.tokopedia.promocheckout.common.domain.umroh

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.CheckUmrahPromoCode
import com.tokopedia.promocheckout.common.domain.model.CheckUmrahPromoCodeData
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class UmrahCheckPromoUseCase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) {

    val INPUT_CODE = "codes"
    val GRAND_TOTAL = "grandTotal"
    val PARAMS = "params"

    fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val variables = mapOf(PARAMS to it.parameters)
            val graphqlRequest = GraphqlRequest(PromoQuery.promoCheckoutUmrahCheckVoucher(), CheckUmrahPromoCode.Response::class.java, variables)
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase.execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(promoCode: String, totalPrice:Int): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(INPUT_CODE, listOf(promoCode))
        requestParams.putInt(GRAND_TOTAL, totalPrice)
        return requestParams
    }
}