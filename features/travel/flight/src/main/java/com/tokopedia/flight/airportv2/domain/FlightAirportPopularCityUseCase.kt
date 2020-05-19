package com.tokopedia.flight.airportv2.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airportv2.data.source.entity.ResponseFlightPopularCity
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by furqan on 19/05/2020
 */
class FlightAirportPopularCityUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named(NAMED_FLIGHT_AIRPORT_POPULAR_CITY_QUERY)
        private val flightAirportPopularCityQuery: String,
        private val flightAirportMapper: FlightAirportMapper) {

    suspend fun fetchAirportPopularCity(): List<Visitable<*>> {
        val graphqlRequest = GraphqlRequest(flightAirportPopularCityQuery, ResponseFlightPopularCity::class.java)
        val rawResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        val response = rawResponse.getSuccessData<ResponseFlightPopularCity>()
        return flightAirportMapper.groupingPopularCity(response.flightPopularCities).let {
            flightAirportMapper.transformToVisitable(it)
        }
    }

    companion object {
        const val NAMED_FLIGHT_AIRPORT_POPULAR_CITY_QUERY = "flight_airport_popular_city_query"
    }
}