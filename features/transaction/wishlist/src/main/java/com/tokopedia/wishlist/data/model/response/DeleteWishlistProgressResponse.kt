package com.tokopedia.wishlist.data.model.response

import com.google.gson.annotations.SerializedName

data class DeleteWishlistProgressResponse(
    @SerializedName("delete_wishlist_progress")
    val deleteWishlistProgress: DeleteWishlistProgress = DeleteWishlistProgress()
) {
    data class DeleteWishlistProgress(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("status")
        val status: String = "",

        @SerializedName("data")
        val data: DataDeleteWishlistProgress = DataDeleteWishlistProgress()
    ) {
        data class DataDeleteWishlistProgress(
            @SerializedName("total_items")
            val totalItems: Int = 0,

            @SerializedName("successfully_removed_items")
            val successfullyRemovedItems: Int = 0,

            @SerializedName("message")
            val message: String = "",

            @SerializedName("ticker_color")
            val tickerColor: String = "",

            @SerializedName("success")
            val success: Boolean = false,

            @SerializedName("toaster_message")
            val toasterMessage: String = ""
        )
    }
}
