package com.tokopedia.topads.recommendation.data


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class ProductRecommendationModel(
        @SerializedName("topadsGetProductRecommendation")
        val topadsGetProductRecommendation: TopadsGetProductRecommendation = TopadsGetProductRecommendation()
)

data class TopadsGetProductRecommendation(
        @SerializedName("data")
        val `data`: Data = Data(),
        @SerializedName("errors")
        val errors: List<Error> = listOf()
)

data class Data(
        @SerializedName("info")
        val info: String = "",
        @SerializedName("nominal_id")
        val nominalId: Int = 0,
        @SerializedName("products")
        val products: List<ProductRecommendation> = listOf()
)

data class ProductRecommendation(
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("image_url")
        val imgUrl: String = "",
        @SerializedName("search_count")
        val searchCount: Int = 0,
        @SerializedName("search_percent")
        val searchPercentage: String = "",
        @SerializedName("recommended_bid")
        val recomBid: Int = 0,
        @SerializedName("min_bid")
        val minBid: Int = 0,
)