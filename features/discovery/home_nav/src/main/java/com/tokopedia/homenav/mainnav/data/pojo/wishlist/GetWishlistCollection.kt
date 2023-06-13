package com.tokopedia.homenav.mainnav.data.pojo.wishlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetWishlistCollection(
    @SerializedName("get_wishlist_collections")
    @Expose
    val wishlist: Wishlist? = Wishlist()
)

data class Wishlist(
    @SerializedName("data")
    @Expose
    val data: WishlistData? = WishlistData()
)

data class WishlistData(
    @SerializedName("total_collection")
    @Expose
    val totalCollection: Int? = 0,
    @SerializedName("is_empty_state")
    @Expose
    val isEmptyState: Boolean? = false,
    @SerializedName("collections")
    @Expose
    val wishlistCollections: List<WishlistCollection>? = listOf()
)
