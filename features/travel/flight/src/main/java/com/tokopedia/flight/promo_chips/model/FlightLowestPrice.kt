package com.tokopedia.flight.promo_chips.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FlightLowestPrice(
        @SerializedName("date")
        @Expose
        val date: String = "",

        @SerializedName("airlinePrices")
        @Expose
        val airlinePrices: List<AirlinePrice> = listOf(),
){
    data class Response(
            @SerializedName("flightLowestPrice")
            @Expose
            val response: FlightLowestPrice = FlightLowestPrice()
    )
}