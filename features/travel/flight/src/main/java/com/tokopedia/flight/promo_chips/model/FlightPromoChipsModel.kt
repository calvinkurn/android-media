package com.tokopedia.flight.promo_chips.model


import com.google.gson.annotations.SerializedName

data class FlightPromoChipsModel(
    @SerializedName("data")
    val `data`: Data
)