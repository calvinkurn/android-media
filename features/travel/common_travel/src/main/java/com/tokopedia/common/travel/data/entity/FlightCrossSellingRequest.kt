package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-10-03
 */

data class FlightCrossSellingRequest (
        @SerializedName("departure")
        val departure: String = "",

        @SerializedName("arrival")
        val arrival: String = "",

        @SerializedName("isRoundTrip")
        val isRoundTrip: Boolean = false,

        @SerializedName("cityID")
        val cityId: Int = 0,

        @SerializedName("departureDate")
        val departureDate: String = ""
)