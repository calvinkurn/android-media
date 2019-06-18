package com.tokopedia.promocheckout.common.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.detail.domain.DetailCouponMarkeplaceModel
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.Subscriber
import rx.functions.Func2
import java.util.*

class GetDetailCouponMarketplaceUseCase(val resources: Resources)
    : GraphqlUseCase() {

    val ONE_CLICK_SHIPMENT = "oneClickShipment"
    val INPUT_CODE = "code"

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val variables = HashMap<String, Any>()
        variables[INPUT_CODE] = requestParams?.getString(INPUT_CODE, "") ?: ""
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_detail_marketplace), DataPromoCheckoutDetail::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }

    fun createRequestParams(promoCode: String, skipApply: Boolean = false, suggestedPromo: Boolean = false, oneClickShipment: Boolean = false): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_CODE, promoCode)
        requestParams.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment)
        return requestParams
    }

}