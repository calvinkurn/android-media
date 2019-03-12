package com.tokopedia.flight.airport.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 06/03/19.
 */
class FlightAirportListSuggestionEntity(
        @SerializedName("suggestions")
        @Expose
        val flightSuggestionList: List<FlightAirportSuggestionEntity>)