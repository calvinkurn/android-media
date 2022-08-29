package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class GiftingAddOn(
        @SerializedName("ticker_text")
        val tickerText: String = "",
        @SerializedName("icon_url")
        val iconUrl: String = "",
        @SerializedName("add_on_ids")
        val addOnIds: List<String> = emptyList()
)
