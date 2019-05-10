package com.tokopedia.shop.info.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopRatingStats(
        @SerializedName("ratingScore")
        @Expose
        val ratingScore: Float = 0f,

        @SerializedName("starLevel")
        @Expose
        val starLevel: Int = 0,

        @SerializedName("totalReview")
        @Expose
        val totalReview: Int = 0
){
        data class Response(
                @SerializedName("ShopRatingQuery")
                @Expose
                val shopRatingStats: ShopRatingStats = ShopRatingStats()
        )
}