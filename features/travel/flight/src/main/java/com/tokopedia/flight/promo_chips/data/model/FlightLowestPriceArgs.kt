package com.tokopedia.flight.promo_chips.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FlightLowestPriceArgs(

        @SerializedName("departureID")
        @Expose
        val departureID: String = "",

        @SerializedName("arrivalID")
        @Expose
        val arrivalID: String = "",

        @SerializedName("startDate")
        @Expose
        val startDate: String = "",

        @SerializedName("endDate")
        @Expose
        val endDate: String = "",

        @SerializedName("class")
        @Expose
        val classID: Int = 0,

        @SerializedName("airlineID")
        @Expose
        val airlineID: String = "")
