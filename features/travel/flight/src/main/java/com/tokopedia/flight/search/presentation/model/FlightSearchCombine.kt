package com.tokopedia.flight.search.presentation.model

import com.tokopedia.flight.common.data.model.FlightErrorResponse

/**
 * @author by astidhiyaa on 30/06/2021
 */
data class FlightSearchCombine (
        val isCombineDone: Boolean = false,
        val flightErrorResponse: FlightErrorResponse = FlightErrorResponse()
){
    data class Response(
            val flightSearchCombine: FlightSearchCombine = FlightSearchCombine()
    )
}