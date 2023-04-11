package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class UpdateWishlistCollectionResponse(
    @SerializedName("update_wishlist_collection")
    val updateWishlistCollection: UpdateWishlistCollection
) {
    data class UpdateWishlistCollection(

        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("data")
        val data: Data = Data(),

        @SerializedName("status")
        val status: String = ""
    ) {
        data class Data(

            @SerializedName("success")
            val success: Boolean = false,

            @SerializedName("message")
            val message: String = ""
        )
    }
}
