package com.tokopedia.flight.promo_chips.model


import com.google.gson.annotations.SerializedName

data class FlightLowestPrice(
    @SerializedName("data")
    val `data`: List<DataX>,
    @SerializedName("errors")
    val errors: List<Any>
)