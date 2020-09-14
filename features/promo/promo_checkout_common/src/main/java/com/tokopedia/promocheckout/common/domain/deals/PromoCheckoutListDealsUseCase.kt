package com.tokopedia.promocheckout.common.domain.deals

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.promocheckout.common.R
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.domain.model.TravelCollectiveBanner
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class PromoCheckoutListDealsUseCase (private val context: Context, private val graphqlUseCase: GraphqlUseCase) {

    fun execute(requestParams: RequestParams, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams.let {
            val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.promo_checkout_deals), TravelCollectiveBanner.Response::class.java, it.parameters)
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase.execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(product: Type):RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_BANNER_PRODUCT_KEY, getProductString(product))
        return requestParams
    }

    fun getProductString(product: Type): String {
        return when(product) {
            Type.ALL -> PARAM_PRODUCT_ALL
            Type.HOTEL -> PARAM_PRODUCT_HOTEL
            Type.DEALS -> PARAM_PRODUCT_DEALS
            Type.FLIGHT -> PARAM_PRODUCT_FLIGHT
            Type.SUB_HOMEPAGE -> PARAM_PRODUCT_SUB_HOMEPAGE
            else -> PARAM_PRODUCT_ALL
        }
    }

    companion object {
        const val PARAM_BANNER_PRODUCT_KEY = "product"

        const val PARAM_PRODUCT_FLIGHT = "FLIGHT"
        const val PARAM_PRODUCT_HOTEL = "HOTEL"
        const val PARAM_PRODUCT_SUB_HOMEPAGE = "SUBHOMEPAGE"
        const val PARAM_PRODUCT_ALL = "ALL"
        const val PARAM_PRODUCT_DEALS = "DEALS"
    }
}