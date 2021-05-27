package com.tokopedia.flight.booking.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-13
 */

data class FlightAddToCartParam (
        @SerializedName("flight")
        @Expose
        var flight: FlightData = FlightData(),

        @SerializedName("ipAddress")
        @Expose
        var ipAddress: String = "",

        @SerializedName("userAgent")
        @Expose
        var userAgent: String = "",

        @SerializedName("did")
        @Expose
        var did: Int = 0,

        @SerializedName("idempotencyKey")
        @Expose
        var idempotencyKey: String = "",

        @SerializedName("requestID")
        @Expose
        var requestId: String = ""
) {
    data class FlightData(
            @SerializedName("destination")
            @Expose
            var destination: MutableList<FlightDestination> = mutableListOf(),

            @SerializedName("adult")
            @Expose
            var adult: Int = 1,

            @SerializedName("child")
            @Expose
            var child: Int = 0,

            @SerializedName("infant")
            @Expose
            var infant: Int = 0,

            @SerializedName("class")
            @Expose
            var flightClass: Int = 0,

            @SerializedName("combo")
            @Expose
            var combo: String = ""
    )

    data class FlightDestination(
            @SerializedName("journeyID")
            @Expose
            var journeyId: String = "",

            @SerializedName("term")
            @Expose
            var term: String = ""
    )
}