package com.tokopedia.flight.airport.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlightAirportCountry(
    @SerializedName("id")
    @Expose
    var id: String,
    @SerializedName("attributes")
    @Expose
    var attributes: FlightAirportAttributes)
