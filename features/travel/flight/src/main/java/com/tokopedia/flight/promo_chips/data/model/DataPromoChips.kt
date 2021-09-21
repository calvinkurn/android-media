package com.tokopedia.flight.promo_chips.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataPromoChips(
        @SerializedName("date")
        @Expose
        val date: String = "",

        @SerializedName("airlinePrices")
        @Expose
        val airlinePrices: List<AirlinePrice> = listOf()
)
