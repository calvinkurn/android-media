package com.tokopedia.wishlist.data.model.response

import com.google.gson.annotations.SerializedName

data class CountDeletionWishlistV2Response(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("status")
        val status: String = "",

        @SerializedName("data")
        val data: Data = Data()
) {
    data class Data(
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