package com.tokopedia.promocheckout.common.domain

import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.data.entity.request.CheckPromoParam
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.ResponseGetPromoStackFirst
import com.tokopedia.usecase.RequestParams
import rx.Subscriber


class CheckPromoStackingCodeUseCase(val resources: Resources) : GraphqlUseCase() {

    val variables = HashMap<String, Any?>()

    fun setParams(promo: Promo) {
        val checkPromoParam = CheckPromoParam()
        checkPromoParam.promo = promo
        val jsonTreeCheckoutRequest = Gson().toJsonTree(checkPromoParam)
        val jsonObjectCheckoutRequest = jsonTreeCheckoutRequest.asJsonObject
        variables.put("params", jsonObjectCheckoutRequest)
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.check_promo_code_promostacking), ResponseGetPromoStackFirst::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }
}