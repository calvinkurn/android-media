package com.tokopedia.flight.bookingV3.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-13
 */

data class FlightAddToCartParam (
        @SerializedName("flight")
        @Expose
        val flight: FlightData = FlightData(),

        @SerializedName("ipAddress")
        @Expose
        val ipAddress: String = "",

        @SerializedName("userAgent")
        @Expose
        val userAgent: String = "",

        @SerializedName("did")
        @Expose
        val did: Int = 0,

        @SerializedName("idempotencyKey")
        @Expose
        val idempotencyKey: String = ""
) {
    data class FlightData(
            @SerializedName("destination")
            @Expose
            val destination: List<FlightDestination> = listOf(),

            @SerializedName("adult")
            @Expose
            val adult: Int = 1,

            @SerializedName("child")
            @Expose
            val child: Int = 0,

            @SerializedName("infant")
            @Expose
            val infant: Int = 0,

            @SerializedName("class")
            @Expose
            val flightClass: Int = 0,

            @SerializedName("combo")
            @Expose
            val combo: String = ""
    )

    data class FlightDestination(
            @SerializedName("journeyID")
            @Expose
            val journeyId: String = "",

            @SerializedName("term")
            @Expose
            val term: String
    )
}