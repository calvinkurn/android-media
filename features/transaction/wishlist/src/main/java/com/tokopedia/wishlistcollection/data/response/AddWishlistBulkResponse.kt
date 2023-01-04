package com.tokopedia.wishlistcollection.data.response

import com.google.gson.annotations.SerializedName

data class AddWishlistBulkResponse(

    @SerializedName("add_wishlist_bulk")
    val addWishlistBulk: AddWishlistBulk = AddWishlistBulk()
) {
    data class AddWishlistBulk(

        @SerializedName("button")
        val button: Button = Button(),

        @SerializedName("product_ids")
        val productIds: List<String> = emptyList(),

        @SerializedName("success")
        val success: Boolean = false,

        @SerializedName("toaster_color")
        val toasterColor: String = "",

        @SerializedName("error_type")
        val errorType: Long = 0L,

        @SerializedName("message")
        val message: String = ""
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
