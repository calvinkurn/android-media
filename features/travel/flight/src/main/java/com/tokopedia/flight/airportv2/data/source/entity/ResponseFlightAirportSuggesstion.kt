package com.tokopedia.flight.airportv2.data.source.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 06/03/19.
 */
class ResponseFlightAirportSuggesstion(
        @SerializedName("flightSuggestion")
        @Expose
        val flightSuggestion: FlightAirportListSuggestionEntity)