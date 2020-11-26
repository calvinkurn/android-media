package com.tokopedia.travel_slice.flight.domain

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travel_slice.flight.data.FlightOrderListEntity
import com.tokopedia.travel_slice.flight.data.FlightSlicesQueries
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by furqan on 26/11/2020
 */
class FlightOrderListUseCase @Inject constructor(private val repository: GraphqlRepository)
    : UseCase<List<FlightOrderListEntity>>() {

    override suspend fun executeOnBackground(): List<FlightOrderListEntity> {
        val graphqlRequest = GraphqlRequest(FlightSlicesQueries.FLIGHT_ORDER_LIST, FlightOrderListEntity.Response::class.java, createParam())
        val response = repository.getReseponse(listOf(graphqlRequest))
        return response.getSuccessData<FlightOrderListEntity.Response>().orders
    }

    private fun createParam(): Map<String, Any> =
            mapOf(
                    PARAM_END_DATE to "",
                    PARAM_ID to 0,
                    PARAM_ORDER_STATUS to 0,
                    PARAM_PAGE to DEFAULT_PAGE,
                    PARAM_PER_PAGE to DEFAULT_PER_PAGE,
                    PARAM_SEARCH to "",
                    PARAM_START_DATE to "",
                    PARAM_ORDER_CATEGORY to DEFAULT_ORDER_CATEGORY
            )

    companion object {
        private const val PARAM_END_DATE = "EndDate"
        private const val PARAM_START_DATE = "StartDate"
        private const val PARAM_ID = "ID"
        private const val PARAM_ORDER_STATUS = "OrderStatus"
        private const val PARAM_PAGE = "Page"
        private const val PARAM_PER_PAGE = "PerPage"
        private const val PARAM_SEARCH = "Search"
        private const val PARAM_ORDER_CATEGORY = "orderCategory"

        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_PER_PAGE = 3
        private const val DEFAULT_ORDER_CATEGORY = "FLIGHTS"
    }
}