package com.tokopedia.flight.search.presentation.model

import com.tokopedia.flight.common.data.model.FlightErrorResponse

/**
 * @author by astidhiyaa on 30/06/2021
 */
data class FlightSearchModel(
        val flightSearchMetaModel: FlightSearchMetaModel = FlightSearchMetaModel(),
        val flightErrorResponse: FlightErrorResponse = FlightErrorResponse()
){
    data class Response(
            val flightSearchModel: FlightSearchModel = FlightSearchModel()
    )
}
