package com.tokopedia.expresscheckout.domain.usecase

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.data.entity.response.atc.AtcExpressGqlResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequestParam
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 03/02/19.
 */

class DoAtcExpressUseCase @Inject constructor(@ApplicationContext val context: Context) : GraphqlUseCase() {

    val variables = HashMap<String, Any?>()

    fun setParams(atcRequestParam: AtcRequestParam) {
        val jsonTreeAtcRequest = Gson().toJsonTree(atcRequestParam)
        val jsonObjectAtcRequest = jsonTreeAtcRequest.asJsonObject
        variables.put("params", jsonObjectAtcRequest)
    }

    override fun execute(requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,
                R.raw.mutation_atc_express), AtcExpressGqlResponse::class.java, variables)
        clearRequest()
        addRequest(graphqlRequest)

        super.execute(requestParams, subscriber)
    }

}