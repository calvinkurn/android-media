package com.tokopedia.promocheckout.detail.domain

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.detail.model.detailmodel.CouponDetailsResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*


class GetCatalogDetailUsecase(val resources: Resources)
    : GraphqlUseCase() {

    val ONE_CLICK_SHIPMENT = "oneClickShipment"
    val INPUT_CODE = "code"
    val SLUG_CODE="slug"
    val CATALOG_ID="catalog_id"

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val variables = HashMap<String, Any>()
        variables[SLUG_CODE] = requestParams?.getString(SLUG_CODE, "") ?: ""
        variables[CATALOG_ID] = requestParams?.getInt(CATALOG_ID,0)?:0
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_catalog_detail), CouponDetailsResponse::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }

    fun createRequestParams(slug: String, catalogID:Int,skipApply: Boolean = false, suggestedPromo: Boolean = false, oneClickShipment: Boolean = false): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(SLUG_CODE, slug)
        requestParams.putInt(CATALOG_ID,catalogID)
        // requestParams.putBoolean(ONE_CLICK_SHIPMENT, oneClickShipment)
        return requestParams
    }

}