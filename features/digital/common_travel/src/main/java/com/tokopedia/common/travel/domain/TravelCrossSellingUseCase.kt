package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-26
 */

class TravelCrossSellingUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) {

    fun execute(query: String, requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val graphqlRequest = GraphqlRequest(query, TravelCrossSelling.Response::class.java, it.parameters)
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase.execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(orderId: String, orderCategory: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(ORDER_ID, orderId)
        requestParams.putString(ORDER_CATEGORY, orderCategory)
        return requestParams
    }

    companion object {
        const val PARAM_FLIGHT_PRODUCT = "FLIGHTS"

        const val ORDER_ID = "orderId"
        const val ORDER_CATEGORY = "orderCategoryStr"
    }
}