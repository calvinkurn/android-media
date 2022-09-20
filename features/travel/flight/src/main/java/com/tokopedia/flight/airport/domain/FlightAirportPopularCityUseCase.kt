package com.tokopedia.flight.airport.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airport.data.AirPortPopularCityQuery
import com.tokopedia.flight.airport.data.source.entity.ResponseFlightPopularCity
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

/**
 * @author by furqan on 19/05/2020
 */
class FlightAirportPopularCityUseCase @Inject constructor(
    private val useCase: MultiRequestGraphqlUseCase,
    private val flightAirportMapper: FlightAirportMapper
) {

    suspend fun fetchAirportPopularCity(): List<Visitable<*>> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()

        val graphqlRequest = GraphqlRequest(AirPortPopularCityQuery(), ResponseFlightPopularCity::class.java)
        useCase.addRequest(graphqlRequest)

        val popularCities = useCase.executeOnBackground().getSuccessData<ResponseFlightPopularCity>().flightPopularCities
        return flightAirportMapper.groupingPopularCity(popularCities).let {
            flightAirportMapper.transformToVisitable(it)
        }
    }
}