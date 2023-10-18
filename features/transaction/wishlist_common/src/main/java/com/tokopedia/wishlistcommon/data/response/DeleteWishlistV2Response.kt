package com.tokopedia.wishlistcommon.data.response

import com.google.gson.annotations.SerializedName

data class DeleteWishlistV2Response(
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("wishlist_remove_v2")
        val wishlistRemoveV2: WishlistRemoveV2 = WishlistRemoveV2()
    ) {
        data class WishlistRemoveV2(
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
}
