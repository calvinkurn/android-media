package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Shop(
    @SerializedName("id")
    @Expose
    val id: String? = "",
    @SerializedName("name")
    @Expose
    val name: String? = "",
    @SerializedName("image")
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
    @SerializedName("stats")
    @Expose
    val stats: ShopStats? = ShopStats(),
    @SerializedName("paging")
    @Expose
    val paging: ShopPaging? = ShopPaging(),
    @SerializedName("error")
    @Expose
    val error: String? = ""
)
