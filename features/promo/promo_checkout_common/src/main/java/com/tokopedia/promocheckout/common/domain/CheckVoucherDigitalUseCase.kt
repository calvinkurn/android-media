package com.tokopedia.promocheckout.common.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigital
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class CheckVoucherDigitalUseCase(val resources: Resources): GraphqlUseCase() {

    val INPUT_CODE = "code"
    val PRODUCT_ID = "product_id"
    val CLIENT_NUMBER = "client_number"
    val PRICE = "price"
    val LABEL_DATA = "data"

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val variables = mapOf(LABEL_DATA to it.parameters)
            val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                    R.raw.promo_checkout_digital_check_voucher), CheckVoucherDigital.Response::class.java, variables)
            clearRequest()
            addRequest(graphqlRequest)
            execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(promoCode: String, promoDigitalModel: PromoDigitalModel): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_CODE, promoCode)
        requestParams.putInt(PRODUCT_ID, promoDigitalModel.productId)
        requestParams.putString(CLIENT_NUMBER, promoDigitalModel.clientNumber)
        requestParams.putLong(PRICE, promoDigitalModel.price)
        return requestParams
    }

}