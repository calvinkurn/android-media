package com.tokopedia.flight.search.data.cloud

import com.tokopedia.flight.search.data.FlightSearchThrowable
import com.tokopedia.flight.search.data.QueryFlightSearchCombine
import com.tokopedia.flight.search.data.QueryFlightSearchSingle
import com.tokopedia.flight.search.data.cloud.combine.FlightCombineRequestModel
import com.tokopedia.flight.search.data.cloud.combine.FlightSearchCombineEntity
import com.tokopedia.flight.search.data.cloud.single.FlightSearchEntity
import com.tokopedia.flight.search.data.cloud.single.FlightSearchRequestModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

/**
 * @author by furqan on 06/04/2020
 */
class FlightSearchDataCloudSource @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) {

    suspend fun getSearchSingleData(searchParam: FlightSearchRequestModel): FlightSearchEntity {
        try {
            val params = mapOf(PARAM_FLIGHT_SEARCH to searchParam)
            val grapqhRequest = GraphqlRequest(QueryFlightSearchSingle(), FlightSearchEntity.Response::class.java, params)

            val rawResponse = graphqlRepository.response(listOf(grapqhRequest))
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
            val graphqlRequest = GraphqlRequest(QueryFlightSearchCombine(), FlightSearchCombineEntity.Response::class.java, params)

            val rawResponse = graphqlRepository.response(listOf(graphqlRequest))
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