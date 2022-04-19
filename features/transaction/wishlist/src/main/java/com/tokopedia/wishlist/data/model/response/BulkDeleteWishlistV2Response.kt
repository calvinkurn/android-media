package com.tokopedia.wishlist.data.model.response

import com.google.gson.annotations.SerializedName

data class BulkDeleteWishlistV2Response(
        @SerializedName("data")
        val data: Data
) {
    data class Data(
            @SerializedName("wishlist_bulk_remove_v2")
            val wishlistBulkRemoveV2: WishlistBulkRemoveV2
    ) {
        data class WishlistBulkRemoveV2(
                @SerializedName("id")
                val id: String,

                @SerializedName("success")
                val success: Boolean,

                @SerializedName("message")
                val message: String,

                @SerializedName("button")
                val button: Button
        ) {
            data class Button(
                    @SerializedName("action")
                    val action: String,

                    @SerializedName("text")
                    val text: String,

                    @SerializedName("url")
                    val url: String
            )
        }
    }
}

