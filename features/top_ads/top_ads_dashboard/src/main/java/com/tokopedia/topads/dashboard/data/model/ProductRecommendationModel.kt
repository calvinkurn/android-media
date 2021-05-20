package com.tokopedia.topads.dashboard.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error
import kotlinx.android.parcel.Parcelize

data class ProductRecommendationModel(
        @SerializedName("topadsGetProductRecommendation")
        val topadsGetProductRecommendation: TopadsGetProductRecommendation = TopadsGetProductRecommendation()
)

data class TopadsGetProductRecommendation(
        @SerializedName("data")
        val data: ProductRecommendationData = ProductRecommendationData(),
        @SerializedName("errors")
        val errors: List<Error> = listOf()
)

@Parcelize
data class ProductRecommendationData(
        @SerializedName("info")
        val info: String = "",
        @SerializedName("nominal_id")
        val nominalId: String = "0",
        @SerializedName("products")
        val products: List<ProductRecommendation> = listOf()
):Parcelable

@Parcelize

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
        val recomBid: String = "0",
        @SerializedName("min_bid")
        val minBid: String = "0",
        var setCurrentBid: Int = 0,
        var isChecked: Boolean = true
):Parcelable