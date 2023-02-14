package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class GetWishlistCollectionNamesResponse(
    @SerializedName("get_wishlist_collection_names")
    val getWishlistCollectionNames: GetWishlistCollectionNames
) {
    data class GetWishlistCollectionNames(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("data")
        val data: List<DataItem> = emptyList(),

        @SerializedName("status")
        val status: String = ""
    ) {
        data class DataItem(
            @SerializedName("name")
            val name: String = "",

            @SerializedName("id")
            val id: String = ""
        )
    }
}
