package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class DeleteWishlistCollectionItemsResponse(
    @field:SerializedName("delete_wishlist_collection_items")
    val deleteWishlistCollectionItems: DeleteWishlistCollectionItems = DeleteWishlistCollectionItems()
) {

    data class DeleteWishlistCollectionItems(
        @field:SerializedName("status")
        val status: String = "",

        @field:SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @field:SerializedName("data")
        val data: DataItem = DataItem()
    ) {
        data class DataItem(
            @field:SerializedName("success")
            val success: Boolean = false,

            @field:SerializedName("message")
            val message: String = ""
        )
    }
}
