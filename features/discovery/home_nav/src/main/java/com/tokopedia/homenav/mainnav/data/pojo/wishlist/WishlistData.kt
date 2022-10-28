package com.tokopedia.homenav.mainnav.data.pojo.wishlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WishlistData(
    @SerializedName("wishlist_v2")
    @Expose
    val wishlist: Wishlist? = Wishlist()
)

data class Wishlist(
    @SerializedName("has_next_page")
    @Expose
    val hasNext: Boolean? = false,
    @SerializedName("items")
    @Expose
    val wishlistItems: List<WishlistItem>? = listOf()
)
