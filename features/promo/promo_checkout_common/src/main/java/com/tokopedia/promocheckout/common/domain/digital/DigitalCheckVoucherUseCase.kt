package com.tokopedia.promocheckout.common.domain.digital

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigital
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class DigitalCheckVoucherUseCase(private val context: Context, private val graphqlUseCase: GraphqlUseCase) {

    val INPUT_CODE = "code"
    val PRODUCT_ID = "product_id"
    val CLIENT_NUMBER = "client_number"
    val PRICE = "price"
    val LABEL_DATA = "data"

    fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val variables = mapOf(LABEL_DATA to it.parameters)
            val graphqlRequest = GraphqlRequest(PromoQuery.promoCheckoutDigitalCheckVoucher(), CheckVoucherDigital.Response::class.java, variables)
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase.execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(promoCode: String, promoDigitalModel: PromoDigitalModel): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_CODE, promoCode)
        requestParams.putInt(PRODUCT_ID, promoDigitalModel.productId)
        if (promoDigitalModel.clientNumber.isNotEmpty()) {
            requestParams.putString(CLIENT_NUMBER, promoDigitalModel.clientNumber)
        }
        if (promoDigitalModel.price > 0) requestParams.putLong(PRICE, promoDigitalModel.price)
        return requestParams
    }

}