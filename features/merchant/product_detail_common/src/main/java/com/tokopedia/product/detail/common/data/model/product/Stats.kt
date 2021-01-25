package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Stats(
        @SerializedName("countReview")
        @Expose
        val countReview: String = "",

        @SerializedName("countTalk")
        @Expose
        val countTalk: String = "",

        @SerializedName("rating")
        @Expose
        val rating: Float = 0f
)

data class TxStats(
        @SerializedName("itemSold")
        @Expose
        val sold: Int = 0,

        @SerializedName("txReject")
        @Expose
        val txReject: Int = 0,

        @SerializedName("txSuccess")
        @Expose
        val txSuccess: Int = 0
)

data class WishlistCount(@SerializedName("count") @Expose val count: Int = 0){
        data class Response(@SerializedName("wishlistCount")
                            @Expose val wishlistCount: WishlistCount = WishlistCount())
}