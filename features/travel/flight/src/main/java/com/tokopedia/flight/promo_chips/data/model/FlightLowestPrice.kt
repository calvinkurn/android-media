package com.tokopedia.flight.promo_chips.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FlightLowestPrice(
        @SerializedName("data")
        @Expose
        val dataPromoChips: List<DataPromoChips> = listOf()
){
    data class Response(
            @SerializedName("flightLowestPrice")
            @Expose
            val response: FlightLowestPrice = FlightLowestPrice()
    )
}