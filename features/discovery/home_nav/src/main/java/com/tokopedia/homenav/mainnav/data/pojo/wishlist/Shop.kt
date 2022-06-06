package com.tokopedia.homenav.mainnav.data.pojo.wishlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Shop(
    @SerializedName("id")
    @Expose
    val id: String? = "",
    @SerializedName("name")
    @Expose
    val name: String? = "",
    @SerializedName("url")
    @Expose
    val url: String? = "",
    @SerializedName("location")
    @Expose
    val location: String? = "",
    @SerializedName("fulfillment")
    @Expose
    val reputation: Fulfillment? = Fulfillment(),
    @SerializedName("is_tokonow")
    @Expose
    val isTokonow: Boolean? = false
)

data class Fulfillment(
    @SerializedName("is_fulfillment")
    @Expose
    val isFulfillment: Boolean? = false,
    @SerializedName("text")
    @Expose
    val text: String? = ""
)
