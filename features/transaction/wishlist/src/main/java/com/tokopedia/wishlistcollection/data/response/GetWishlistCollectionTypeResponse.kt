package com.tokopedia.wishlistcollection.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetWishlistCollectionTypeResponse(
	@SerializedName("get_wishlist_collection_items")
	val getWishlistCollectionItems: GetWishlistCollectionItems
) {
	data class GetWishlistCollectionItems(
        @SerializedName("collection_type")
        val collectionType: Int = 0
    )
}
