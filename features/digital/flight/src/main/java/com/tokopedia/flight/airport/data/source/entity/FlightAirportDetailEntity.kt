package com.tokopedia.flight.airport.data.source.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 06/03/19.
 */
class FlightAirportDetailEntity(
        @SerializedName("id")
        @Expose
        val id : String = "",
        @SerializedName("name")
        @Expose
        val name : List<FlightAirportInfoEntity>)
