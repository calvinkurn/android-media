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
    @SerializedName("badge")
    @Expose
    val badge: ShopBadge? = ShopBadge(),
    @SerializedName("error")
    @Expose
    val error: String? = ""
)
