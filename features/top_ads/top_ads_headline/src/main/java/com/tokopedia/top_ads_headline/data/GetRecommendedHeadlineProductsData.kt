package com.tokopedia.top_ads_headline.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error
import java.io.Serializable

data class GetRecommendedHeadlineProductsData(
        @SerializedName("topadsGetRecommendedHeadlineProducts")
        var topadsGetRecommendedHeadlineProducts: TopadsGetRecommendedHeadlineProducts
)

data class TopadsGetRecommendedHeadlineProducts(
        @SerializedName("data")
        var recommendedProducts: RecommendedProducts,
        @SerializedName("errors")
        var errors: List<Error>
)

data class RecommendedProducts(
        @SerializedName("products")
        var products: List<Product>
)

data class Product(
        @SerializedName("category")
        var category: Category,
        @SerializedName("id")
        var id: String,
        @SerializedName("imageURL")
        var imageURL: String,
        @SerializedName("name")
        var name: String,
        @SerializedName("price")
        var price: Int,
        @SerializedName("priceFmt")
        var priceFmt: String,
        @SerializedName("rating")
        var rating: String,
        @SerializedName("reviewCount")
        var reviewCount: String
)

data class Category(
        @SerializedName("id")
        var id: String,
        @SerializedName("name")
        var name: String
) : Serializable {
    override fun equals(other: Any?): Boolean {
        return other is Category && other.id == this.id && other.name == this.name
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}