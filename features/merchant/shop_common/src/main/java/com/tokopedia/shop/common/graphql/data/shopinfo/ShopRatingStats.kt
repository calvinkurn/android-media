package com.tokopedia.shop.common.graphql.data.shopinfo


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
        val totalReview: Int = 0,

        @SerializedName("detail")
        @Expose
        val detail: Detail = Detail()
) {
    data class Detail(
            @SerializedName("fiveStar")
            @Expose
            val fiveStar: StarItem = StarItem(),
            @SerializedName("fourStar")
            @Expose
            val fourStar: StarItem = StarItem(),
            @SerializedName("threeStar")
            @Expose
            val threeStar: StarItem = StarItem(),
            @SerializedName("twoStar")
            @Expose
            val twoStar: StarItem = StarItem(),
            @SerializedName("oneStar")
            @Expose
            val oneStar: StarItem = StarItem()
    ) {
        data class StarItem(
                @SerializedName("fiveStar")
                @Expose
                val rate: Int = 0,
                @SerializedName("totalReview")
                @Expose
                val totalReview: Int = 0,
                @SerializedName("percentageWord")
                @Expose
                val percentageWord: String = "",
                @SerializedName("percentage")
                @Expose
                val percentage: Double = 0.0
        )
    }

    data class Response(
            @SerializedName("ShopRatingQuery")
            @Expose
            val shopRatingStats: ShopRatingStats = ShopRatingStats()
    )
}