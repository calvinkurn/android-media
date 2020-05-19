package com.tokopedia.flight.airportv2.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airportv2.data.source.entity.ResponseFlightAirportSuggesstion
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by furqan on 19/05/2020
 */
class FlightAirportSuggestionUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named(NAMED_FLIGHT_AIRPORT_SUGGESTION_QUERY)
        private val flightAirportSuggestionQuery: String,
        private val flightAirportMapper: FlightAirportMapper) {

    suspend fun fetchAirportSuggestion(keyword: String): List<Visitable<*>> {
        val params = mapOf(INPUT_PARAMS to keyword)
        val graphqlRequest = GraphqlRequest(flightAirportSuggestionQuery, ResponseFlightAirportSuggesstion::class.java, params)
        val rawResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        val response = rawResponse.getSuccessData<ResponseFlightAirportSuggesstion>()
        return flightAirportMapper.groupingSuggestion(response.flightSuggestion.flightSuggestionList).let {
            flightAirportMapper.transformToVisitable(it)
        }
    }

    companion object {
        const val NAMED_FLIGHT_AIRPORT_SUGGESTION_QUERY = "flight_airport_suggestion_query"
        private const val INPUT_PARAMS = "input"
    }
}