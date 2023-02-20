package com.tokopedia.homenav.mainnav.data.pojo.wishlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WishlistCollection (
    @SerializedName("id")
    @Expose
    val id: String? = "",
    @SerializedName("name")
    @Expose
    val name: String? = "",
    @SerializedName("total_item")
    @Expose
    val totalItem: Int? = 0,
    @SerializedName("item_text")
    @Expose
    val itemText: String? = "",
    @SerializedName("images")
    @Expose
    val images: List<String>? = listOf(),
)
