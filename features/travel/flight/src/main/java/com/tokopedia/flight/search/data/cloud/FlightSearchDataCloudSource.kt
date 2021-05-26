package com.tokopedia.flight.search.data.cloud

import com.tokopedia.flight.search.data.FlightSearchThrowable
import com.tokopedia.flight.search.data.cloud.combine.FlightCombineRequestModel
import com.tokopedia.flight.search.data.cloud.combine.FlightSearchCombineEntity
import com.tokopedia.flight.search.data.cloud.single.FlightSearchEntity
import com.tokopedia.flight.search.data.cloud.single.FlightSearchRequestModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by furqan on 06/04/2020
 */
class FlightSearchDataCloudSource @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named(NAMED_FLIGHT_SEARCH_SINGLE_QUERY)
        private val flightSearchSingleQuery: String,
        @Named(NAMED_FLIGHT_SEARCH_COMBINE_QUERY)
        private val flightSearchCombineQuery: String) {

    suspend fun getSearchSingleData(searchParam: FlightSearchRequestModel): FlightSearchEntity {
        try {
            val params = mapOf(PARAM_FLIGHT_SEARCH to searchParam)
            val grapqhRequest = GraphqlRequest(flightSearchSingleQuery, FlightSearchEntity.Response::class.java, params)

            val rawResponse = graphqlRepository.getReseponse(listOf(grapqhRequest))
            val response = rawResponse.getSuccessData<FlightSearchEntity.Response>().flightSearch
            if (response.error.isNotEmpty()) {
                throw FlightSearchThrowable().apply {
                    errorList = response.error
                }
            }
            return response
        } catch (t: Throwable) {
            throw t
        }
    }

    suspend fun getSearchCombineData(combineParam: FlightCombineRequestModel): FlightSearchCombineEntity {
        try {
            val params = mapOf(PARAM_FLIGHT_SEARCH to combineParam)
            val graphqlRequest = GraphqlRequest(flightSearchCombineQuery, FlightSearchCombineEntity.Response::class.java, params)

            val rawResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
            val response = rawResponse.getSuccessData<FlightSearchCombineEntity.Response>().flightSearchCombine
            if (response.error.isNotEmpty()) {
                throw FlightSearchThrowable().apply {
                    errorList = response.error
                }
            }
            return response
        } catch (t: Throwable) {
            throw t
        }
    }

    companion object {
        private const val PARAM_FLIGHT_SEARCH = "data"

        const val NAMED_FLIGHT_SEARCH_SINGLE_QUERY = "flight_search_single_query"
        const val NAMED_FLIGHT_SEARCH_COMBINE_QUERY = "flight_search_combine_query"
    }

}