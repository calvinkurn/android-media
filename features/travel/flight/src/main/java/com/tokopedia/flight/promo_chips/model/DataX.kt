package com.tokopedia.flight.promo_chips.model


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("airlinePrices")
    val airlinePrices: List<AirlinePrice>,
    @SerializedName("date")
    val date: String
)