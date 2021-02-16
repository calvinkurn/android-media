package com.tokopedia.flight.promo_chips.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("flightLowestPrice")
    val flightLowestPrice: FlightLowestPrice
)