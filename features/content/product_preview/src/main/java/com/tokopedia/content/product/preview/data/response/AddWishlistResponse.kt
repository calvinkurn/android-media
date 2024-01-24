package com.tokopedia.content.product.preview.data.response

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 11/01/24
 */

data class AddWishlistResponse(
    @SerializedName("wishlist_add_v2")
    val wishlistAdd: WishlistAddV2 = WishlistAddV2()
) {
    data class WishlistAddV2(
        @SerializedName("button")
        val button: Button = Button(),

        @SerializedName("success")
        val success: Boolean = false,

        @SerializedName("id")
        val id: String = "",

        @SerializedName("message")
        val message: String = "",

        @SerializedName("toaster_color")
        val toasterColor: String = ""
    ) {
        data class Button(
            @SerializedName("action")
            val action: String = "",

            @SerializedName("text")
            val text: String = "",

            @SerializedName("url")
            val url: String = ""
        )
    }
}
