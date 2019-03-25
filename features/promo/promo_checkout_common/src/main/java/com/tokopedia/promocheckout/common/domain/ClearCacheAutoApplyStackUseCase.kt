package com.tokopedia.promocheckout.common.domain

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.R
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 18/03/19.
 */

class ClearCacheAutoApplyStackUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {

    var queryString: String = ""

    companion object {
        val PARAM_SERVICE_ID = "serviceID"
        val PARAM_PROMO_CODE = "promoCode"

        val PARAM_VALUE_MARKETPLACE = "marketplace"

        val PARAM_PLACEHOLDER_SERVICE_ID = "#serviceId"
        val PARAM_PLACEHOLDER_PROMO_CODE = "#promoCode"
    }

    fun setParams(serviceId: String, promoCodeList: ArrayList<String>) {
        queryString = GraphqlHelper.loadRawString(context.resources, R.raw.clear_cache_auto_apply_stack)
        queryString = queryString.replace(PARAM_PLACEHOLDER_SERVICE_ID, serviceId)

        queryString = queryString.replace(PARAM_PLACEHOLDER_PROMO_CODE, Gson().toJson(promoCodeList))
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(queryString, ClearCacheAutoApplyStackResponse::class.java)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }

}