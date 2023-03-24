package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class AddWishlistCollectionItemsResponse(
    @SerializedName("add_wishlist_collection_items")
    val addWishlistCollectionItems: AddWishlistCollectionItems = AddWishlistCollectionItems()
) {
    data class AddWishlistCollectionItems(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("data")
        val dataItem: DataItem = DataItem(),

        @SerializedName("status")
        val status: String = ""
    ) {
        data class DataItem(

            @SerializedName("collection_id")
            val collectionId: String = "",

            @SerializedName("success")
            val success: Boolean = false,

            @SerializedName("message")
            val message: String = ""
        )
    }
}
