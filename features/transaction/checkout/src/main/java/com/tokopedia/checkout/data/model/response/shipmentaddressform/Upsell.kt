package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

class Upsell(
        @SerializedName("is_show")
        val isShow: Boolean = false,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("app_link")
        val appLink: String = "",
        @SerializedName("image")
        val image: String = ""
)