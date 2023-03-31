package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class GroupInformation(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("badge_url")
    val badgeUrl: String = ""
)
