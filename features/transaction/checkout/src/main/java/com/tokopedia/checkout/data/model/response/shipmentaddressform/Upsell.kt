package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

class Upsell(
        @SerializedName("is_show")
        val isShow: Boolean = false,
        @SerializedName("is_selected")
        val isSelected: Boolean = false,
        @SerializedName("description")
        val description: String = "",
        @SerializedName("app_link")
        val appLink: String = "",
        @SerializedName("image")
        val image: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: Long = 0,
        @SerializedName("duration")
        val duration: String = "",
        @SerializedName("wording")
        val wording: String = "",
        @SerializedName("button")
        val button: UpsellButton = UpsellButton()
)

data class UpsellButton(
        @SerializedName("text")
        val text: String = ""
)

/**
 * upsell_v2 {
is_show
is_selected
price
price_fmt
duration (6 bulan)
description (jangan lupa html format)
wording untuk ringkasan belanja (Langganan PLUS 6 Bulan)
image
applink
button {
text
}
}
 */