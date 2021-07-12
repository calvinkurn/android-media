package com.tokopedia.review.feature.reading.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRatingAndTopic(
        @SerializedName("productrevGetProductRatingAndTopics")
        @Expose
        val productrevGetProductRatingAndTopics: ProductrevGetProductRatingAndTopic = ProductrevGetProductRatingAndTopic()
)

data class ProductrevGetProductRatingAndTopic(
        @SerializedName("rating")
        @Expose
        val rating: ProductRating = ProductRating(),
        @SerializedName("topics")
        @Expose
        val topics: List<ProductTopic> = listOf(),
        @SerializedName("variants")
        @Expose
        val variants: List<String> = listOf(),
        @SerializedName("availableFilters")
        @Expose
        val availableFilters: AvailableFilters = AvailableFilters()
) {
    fun getTopicsMap(): Map<String, String> {
        val topicsMap = mutableMapOf<String, String>()
        this.topics.forEach {
                topicsMap[it.formatted] = it.key
        }
        return topicsMap
    }
}

data class ProductRating(
        @SerializedName("positivePercentageFmt")
        @Expose
        val satisfactionRate: String = "",
        @SerializedName("ratingScore")
        @Expose
        val ratingScore: String = "",
        @SerializedName("totalRating")
        @Expose
        val totalRating: Long = 0,
        @SerializedName("totalRatingWithImage")
        @Expose
        val totalRatingWithImage: Long = 0,
        @SerializedName("totalRatingTextAndImage")
        @Expose
        val totalRatingTextAndImage: Long = 0,
        @SerializedName("detail")
        @Expose
        val detail: List<ProductReviewDetail> = listOf()
)

data class ProductTopic(
        @SerializedName("rating")
        @Expose
        val rating: Double = 0.0,
        @SerializedName("formatted")
        @Expose
        val formatted: String = "",
        @SerializedName("reviewCount")
        @Expose
        val reviewCount: Long = 0,
        @SerializedName("key")
        @Expose
        val key: String = ""
)

data class AvailableFilters(
        @SerializedName("withAttachment")
        @Expose
        val withAttachment: Boolean = false,
        @SerializedName("rating")
        @Expose
        val rating: Boolean = false,
        @SerializedName("topics")
        @Expose
        val topics: Boolean = false,
        @SerializedName("helpfulness")
        @Expose
        val helpfulness: Boolean = false,
        @SerializedName("variant")
        @Expose
        val variant: Boolean = false
)

data class ProductReviewDetail(
        @SerializedName("rate")
        @Expose
        val rate: Int = 0,
        @SerializedName("formattedTotalReviews")
        @Expose
        val totalReviews: String = "",
        @SerializedName("percentageFloat")
        @Expose
        val percentage: Float = 0F
)