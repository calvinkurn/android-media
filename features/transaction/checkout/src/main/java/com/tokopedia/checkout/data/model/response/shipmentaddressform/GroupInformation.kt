package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class GroupInformation(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("badge_url")
    val badgeUrl: String = ""
)
