package com.tokopedia.wishlistcommon.data.response

data class AddToWishlistV2Response(
        val data: Data
) {
    data class Data(
            val wishlistAdd: WishlistAdd
    ) {
        data class WishlistAdd(
                val button: Button,
                val success: Boolean,
                val id: String,
                val message: String
        ) {
            data class Button(
                    val action: String,
                    val text: String,
                    val url: String
            )
        }
    }
}