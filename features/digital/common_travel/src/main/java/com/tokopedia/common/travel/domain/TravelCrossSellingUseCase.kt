package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.FlightCrossSellingRequest
import com.tokopedia.common.travel.data.entity.HotelCrossSellingRequest
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

class TravelCrossSellingUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase,
                                                    private val graphqlUseCase: GraphqlUseCase) {

    suspend fun execute(query: String, requestParams: RequestParams): Result<TravelCrossSelling> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()
        try {
            val graphqlRequest = GraphqlRequest(query, TravelCrossSelling.Response::class.java, requestParams.parameters)
            useCase.addRequest(graphqlRequest)

            val travelCrossSelling = useCase.executeOnBackground().getSuccessData<TravelCrossSelling.Response>().response
            return Success(travelCrossSelling)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    fun execute(query: String, requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val graphqlRequest = GraphqlRequest(query, TravelCrossSelling.Response::class.java, it.parameters)
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase.execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(product: String, flightRequest: FlightCrossSellingRequest): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_PRODUCT, product)
        requestParams.putObject(PARAM_FLIGHT_PRODUCT, flightRequest)
        return requestParams
    }

    fun createRequestParams(product: String, hotelRequest: HotelCrossSellingRequest): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_PRODUCT, product)
        requestParams.putObject(PARAM_HOTEL_PRODUCT, hotelRequest)
        return requestParams
    }

    companion object {
        const val PARAM_DATA = "data"

        const val PARAM_FLIGHT_PRODUCT = "FLIGHT"
        const val PARAM_HOTEL_PRODUCT = "HOTEL"

        const val PARAM_PRODUCT = "product"
        const val PARAM_FLIGHT_REQUEST = "flightRequest"
        const val PARAM_HOTEL_REQUEST = "hotelRequest"

    }
}