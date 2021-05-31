package com.tokopedia.review.feature.reviewdetail.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 29/04/20
 */
data class ProductFeedbackFilterResponse(
        @SerializedName("productrevFeedbackDataPerProduct")
        val productrevFeedbackDataPerProduct: ProductFeedbackFilterData = ProductFeedbackFilterData()
)

data class ProductFeedbackFilterData(
        @SerializedName("aggregatedRating")
        val aggregatedRating: List<ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct.AggregatedRating> = listOf(),
        @SerializedName("filterBy")
        val filterBy: String? = "",
        @SerializedName("hasNext")
        val hasNext: Boolean = false,
        @SerializedName("limit")
        val limit: Int? = 0,
        @SerializedName("list")
        val list: List<ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct.FeedbackList> = listOf(),
        @SerializedName("page")
        val page: Int? = 0,
        @SerializedName("sortBy")
        val sortBy: String? = "",
        @SerializedName("topics")
        val topics: List<ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct.Topic> = listOf(),
        @SerializedName("reviewCount")
        val reviewCount: Long = 0
)
