package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 26/10/21.
 */
data class CrossSellOrderSummary(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("price_wording")
    val priceWording: String = ""
)
