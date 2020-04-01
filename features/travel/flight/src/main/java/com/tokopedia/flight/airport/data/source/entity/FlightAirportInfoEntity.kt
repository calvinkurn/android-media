package com.tokopedia.flight.airport.data.source.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 06/03/19.
 */
class FlightAirportInfoEntity(
        @SerializedName("key")
        @Expose
        val key : Int = 0,
        @SerializedName("value")
        @Expose
        val value : String = "")