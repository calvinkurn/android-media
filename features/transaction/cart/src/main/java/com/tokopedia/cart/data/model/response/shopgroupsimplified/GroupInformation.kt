package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class GroupInformation(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("badge_url")
    val badgeUrl: String = "",
    @SerializedName("app_link")
    val appLink: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("description_badge_url")
    val descriptionBadgeUrl: String = ""
)
