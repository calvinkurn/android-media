package com.tokopedia.wishlist.data.model.response

import com.google.gson.annotations.SerializedName

data class DeleteWishlistV2Response(
        @SerializedName("data")
        val data: Data
) {
    data class Data(
            @SerializedName("wishlist_remove_v2")
            val wishlistRemoveV2: WishlistRemoveV2
    ) {
        data class WishlistRemoveV2(
                @SerializedName("button")
                val button: Button,

                @SerializedName("success")
                val success: Boolean,

                @SerializedName("id")
                val id: String,

                @SerializedName("message")
                val message: String
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

