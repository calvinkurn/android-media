package com.tokopedia.review.feature.reading.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopRatingAndTopic(
        @SerializedName("productrevGetShopRatingAndTopics")
        @Expose
        val productrevGetShopRatingAndTopics: ProductrevGetShopRatingAndTopic = ProductrevGetShopRatingAndTopic()
)

data class ProductrevGetShopRatingAndTopic(
        @SerializedName("rating")
        @Expose
        val rating: ProductRating = ProductRating(),
        @SerializedName("topics")
        @Expose
        val topics: List<ProductTopic> = listOf(),
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