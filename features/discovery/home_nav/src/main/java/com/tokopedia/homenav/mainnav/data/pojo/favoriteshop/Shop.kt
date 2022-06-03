package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Shop(
    @SerializedName("shopID")
    @Expose
    val id: String? = "",
    @SerializedName("shopName")
    @Expose
    val name: String? = "",
    @SerializedName("logo")
    @Expose
    val imageUrl: String? = "",
    @SerializedName("location")
    @Expose
    val location: String? = "",
    @SerializedName("reputation")
    @Expose
    val reputation: ShopReputation? = ShopReputation(),
    @SerializedName("badge")
    @Expose
    val badge: List<ShopBadge>? = listOf(),
    @SerializedName("error")
    @Expose
    val error: String? = ""
)
