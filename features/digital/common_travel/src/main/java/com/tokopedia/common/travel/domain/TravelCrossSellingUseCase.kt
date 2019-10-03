package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.FlightCrossSellingRequest
import com.tokopedia.common.travel.data.entity.HotelCrossSellingRequest
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.data.entity.TravelUpsertContactModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * @author by jessica on 2019-08-26
 */

class TravelCrossSellingUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(query: String, flightRequest: FlightCrossSellingRequest? = null,
                        hotelRequest: HotelCrossSellingRequest? = null): Result<TravelCrossSelling> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()
        try {
            var params = mapOf<String, Any>()
            if (flightRequest != null) {
                params = mapOf(PARAM_DATA to mapOf(PARAM_PRODUCT to PARAM_FLIGHT_PRODUCT,
                        PARAM_FLIGHT_REQUEST to flightRequest))
            } else if (hotelRequest != null) {
                params = mapOf(PARAM_DATA to mapOf(PARAM_PRODUCT to PARAM_HOTEL_PRODUCT,
                        PARAM_HOTEL_REQUEST to hotelRequest))
            }

            val graphqlRequest = GraphqlRequest(query, TravelCrossSelling.Response::class.java, params)
            useCase.addRequest(graphqlRequest)

            val travelCrossSelling = useCase.executeOnBackground().getSuccessData<TravelCrossSelling.Response>().response
            return Success(travelCrossSelling)
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
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