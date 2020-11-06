package com.tokopedia.top_ads_headline.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class GetRecommendedHeadlineProductsData(
        @SerializedName("data")
        var data: Data
) {
    data class Data(
            @SerializedName("topadsGetRecommendedHeadlineProducts")
            var topadsGetRecommendedHeadlineProducts: TopadsGetRecommendedHeadlineProducts
    ) {
        data class TopadsGetRecommendedHeadlineProducts(
                @SerializedName("data")
                var recommendedProducts: RecommendedProducts,
                @SerializedName("errors")
                var errors: List<Error>
        ) {
            data class RecommendedProducts(
                    @SerializedName("products")
                    var products: List<Product>
            ) {
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
                ) {
                    data class Category(
                            @SerializedName("id")
                            var id: String,
                            @SerializedName("name")
                            var name: String
                    )
                }
            }
        }
    }
}