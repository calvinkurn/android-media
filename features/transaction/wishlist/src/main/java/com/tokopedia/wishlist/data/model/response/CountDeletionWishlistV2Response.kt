package com.tokopedia.wishlist.data.model.response

import com.google.gson.annotations.SerializedName

data class CountDeletionWishlistV2Response(
        @SerializedName("data")
        val data: Data = Data()
) {
    data class Data(
            @SerializedName("wishlist_count_deletion")
            val countDeletionWishlistV2: CountDeletionWishlistV2 = CountDeletionWishlistV2()
    ) {
        data class CountDeletionWishlistV2(
                @SerializedName("total_items")
                val totalItems: Int = 0,

                @SerializedName("successfully_removed_items")
                val successfullyRemovedItems: Int = 0,

                @SerializedName("message")
                val message: String = "",

                @SerializedName("ticker_color")
                val tickerColor: String = ""
        )
    }
}