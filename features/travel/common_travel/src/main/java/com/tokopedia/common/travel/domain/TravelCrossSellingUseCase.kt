package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-26
 */

class TravelCrossSellingUseCase @Inject constructor(private val useCase: MultiRequestGraphqlUseCase,
                                                    private val graphqlUseCase: GraphqlUseCase) {

    suspend fun execute(query: String, orderId: String, orderCategory: String): Result<TravelCrossSelling> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()

        val params = mapOf(ORDER_ID to orderId, ORDER_CATEGORY to orderCategory)

        return try {
            val graphqlRequest = GraphqlRequest(query, TravelCrossSelling.Response::class.java, params)
            useCase.addRequest(graphqlRequest)

            val travelCrossSelling = useCase.executeOnBackground().getSuccessData<TravelCrossSelling.Response>().response

            Success(travelCrossSelling)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun executeRx(query: String, requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
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
        const val PARAM_FLIGHT_PRODUCT = "FLIGHT"
        const val PARAM_HOTEL_PRODUCT = "HOTEL"

        const val ORDER_ID = "orderID"
        const val ORDER_CATEGORY = "orderCategory"
    }
}