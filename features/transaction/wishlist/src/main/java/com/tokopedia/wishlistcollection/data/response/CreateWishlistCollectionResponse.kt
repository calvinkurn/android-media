package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class CreateWishlistCollectionResponse(
    @field:SerializedName("create_wishlist_collection")
    val createWishlistCollection: CreateWishlistCollection
) {
    data class CreateWishlistCollection(

        @field:SerializedName("data")
        val dataCreate: DataCreate = DataCreate(),

        @field:SerializedName("status")
        val status: String = "",

        @field:SerializedName("error_message")
        val errorMessage: List<String> = emptyList()
    ) {
        data class DataCreate(
            @field:SerializedName("success")
            val success: Boolean = false,

            @field:SerializedName("id")
            val id: String = "",

            @field:SerializedName("message")
            val message: String = ""
        )
    }
}
