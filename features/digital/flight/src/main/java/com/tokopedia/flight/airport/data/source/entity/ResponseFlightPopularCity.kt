package com.tokopedia.flight.airport.data.source.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 05/03/19.
 */
class ResponseFlightPopularCity(
        @SerializedName("flightPopularCity")
        @Expose
        val flightPopularCities: List<FlightPopularCityEntity>)