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
        val topics: ProductTopic = ProductTopic(),
        @SerializedName("variants")
        @Expose
        val variants: List<String> = listOf(),
        @SerializedName("availableFilters")
        @Expose
        val availableFilters: AvailableFilters = AvailableFilters()
)

data class ProductRating(
        @SerializedName("ratingScore")
        @Expose
        val ratingScore: String = "",
        @SerializedName("totalRating")
        @Expose
        val totalRating: String = "",
        @SerializedName("totalRatingWithImage")
        @Expose
        val totalRatingWithImage: String = "",
        @SerializedName("totalRatingTextAndImage")
        @Expose
        val totalRatingTextAndImage: String = "",
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
        val reviewCount: Long = 0
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
        val rate: Long = 0L,
        @SerializedName("totalReviews")
        @Expose
        val totalReviews: Long = 0L,
        @SerializedName("percentage")
        @Expose
        val percentage: String = "",
)