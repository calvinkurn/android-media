package com.tokopedia.promocheckout.common.domain

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoParam
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.ResponseGetPromoStackFinal
import com.tokopedia.promocheckout.common.util.PromoQuery
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class CheckPromoStackingCodeFinalUseCase @Inject constructor (@ApplicationContext private val context: Context)
    : GraphqlUseCase() {

    val variables = HashMap<String, Any?>()

    fun setParams(promo: Promo) {
        val checkPromoParam = CheckPromoParam()
        checkPromoParam.promo = promo
        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkPromoParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables["params"] = jsonObjectCheckoutRequest
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(PromoQuery.promoCheckPromoCodeFinalPromoStacking(), ResponseGetPromoStackFinal::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }
}