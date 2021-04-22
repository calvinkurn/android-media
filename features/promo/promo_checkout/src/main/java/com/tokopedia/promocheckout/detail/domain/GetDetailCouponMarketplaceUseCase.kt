package com.tokopedia.promocheckout.detail.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.promocheckout.util.PromoCheckoutQuery
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*

class GetDetailCouponMarketplaceUseCase(val resources: Resources)
    : GraphqlUseCase() {

    val ONE_CLICK_SHIPMENT = "oneClickShipment"
    val INPUT_CODE = "code"
    val API_VERSION= "apiVersion"

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val variables = HashMap<String, Any>()
        variables[INPUT_CODE] = requestParams?.getString(INPUT_CODE, "") ?: ""
        variables[API_VERSION] = "2.0.0"

        val graphqlRequest = GraphqlRequest(PromoCheckoutQuery.promoCheckoutDetailMarketPlace(), DataPromoCheckoutDetail::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }

    fun createRequestParams(promoCode: String, skipApply: Boolean = false, suggestedPromo: Boolean = false, oneClickShipment: Boolean = false): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_CODE, promoCode)
        return requestParams
    }

}