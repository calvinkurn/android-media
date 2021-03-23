package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Shop(
    @SerializedName("badges")
    val badges: List<Any> = listOf(),
    @SerializedName("free_shipping_icon")
    val freeShippingIcon: String = "",
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("is_gold")
    val isGold: Boolean = false,
    @SerializedName("is_official")
    val isOfficial: Boolean = false,
    @SerializedName("location")
    val location: String = "",
    @SerializedName("name")
    val name: String = ""
)