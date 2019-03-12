package com.tokopedia.flight.airport.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlightAirportAttributes(

        @SerializedName("name")
        @Expose
        var name: String,
        @SerializedName("phone_code")
        @Expose
        var phoneCode: Long = 0,
        @SerializedName("cities")
        @Expose
        private val cities: List<String>? = null)
