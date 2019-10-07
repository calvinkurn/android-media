package com.tokopedia.flight.dashboard.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FlightFareData(
        @SerializedName("flightFare")
        @Expose
        val flightFare: FlightFare)

class FlightFare(
        @SerializedName("id")
        @Expose
        val idFlight: String,
        @SerializedName("attributes")
        @Expose
        val attributesList: List<FlightFareAttributes>)

class FlightFareAttributes(
        @SerializedName("date")
        @Expose
        val dateFare: String,
        @SerializedName("cheapestPriceNumeric")
        @Expose
        val cheapestPriceNumeric: Long)