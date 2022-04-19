package com.tokopedia.flight.airport.domain

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airport.data.source.entity.ResponseFlightAirportSuggesstion
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by furqan on 19/05/2020
 */
class FlightAirportSuggestionUseCase @Inject constructor(
        private val useCase: MultiRequestGraphqlUseCase,
        @Named(NAMED_FLIGHT_AIRPORT_SUGGESTION_QUERY)
        private val flightAirportSuggestionQuery: String,
        private val flightAirportMapper: FlightAirportMapper) {

    suspend fun fetchAirportSuggestion(keyword: String): List<Visitable<*>> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()

        val params = mapOf(INPUT_PARAMS to keyword)
        val graphqlRequest = GraphqlRequest(flightAirportSuggestionQuery, ResponseFlightAirportSuggesstion::class.java, params)
        useCase.addRequest(graphqlRequest)

        val flightSuggestionList = useCase.executeOnBackground().getSuccessData<ResponseFlightAirportSuggesstion>().flightSuggestion.flightSuggestionList
        return flightAirportMapper.groupingSuggestion(flightSuggestionList).let {
            flightAirportMapper.transformToVisitable(it)
        }
    }

    companion object {
        const val NAMED_FLIGHT_AIRPORT_SUGGESTION_QUERY = "flight_airport_suggestion_query"
        private const val INPUT_PARAMS = "input"
    }
}