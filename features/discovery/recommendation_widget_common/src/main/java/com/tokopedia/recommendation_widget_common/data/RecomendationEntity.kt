package com.tokopedia.recommendation_widget_common.data

import com.google.gson.annotations.SerializedName

/**
 * Created by devara fikry on 16/04/19.
 * Credit errysuprayogi
 */
class RecomendationEntity {

    @SerializedName("productRecommendationWidget")
    var productRecommendationWidget: ProductRecommendationWidget? = ProductRecommendationWidget()

    class ProductRecommendationWidget {
        @SerializedName("data")
        var data: List<RecomendationData>? = listOf()
    }

    class Pagination {
        @SerializedName("current_page")
        var currentPage: Int = 0
        @SerializedName("next_page")
        var nextPage: Int = 0
        @SerializedName("prev_page")
        var prevPage: Int = 0
        @SerializedName("hasNext")
        var hasNext: Boolean = false
    }

    class Recommendation {

        @SerializedName("id")
        var id: Int = 0
        @SerializedName("name")
        var name: String? = ""
        @SerializedName("categoryBreadcrumbs")
        var categoryBreadcrumbs: String? = ""
        @SerializedName("url")
        var url: String? = ""
        @SerializedName("appUrl")
        var appUrl: String? = ""
        @SerializedName("clickUrl")
        var clickUrl: String? = ""
        @SerializedName("wishlistUrl")
        var wishlistUrl: String? = ""

        @SerializedName("discountPercentage")
        var discountPercentage: Int = 0
        @SerializedName("slashedPrice")
        var slashedPrice: String? = ""
        @SerializedName("slashedPriceInt")
        var slashedPriceInt: Int = 0

        @SerializedName("trackerImageUrl")
        var trackerImageUrl: String? = ""
        @SerializedName("imageUrl")
        var imageUrl: String? = ""
        @SerializedName("price")
        var price: String? = ""
        @SerializedName("priceInt")
        var priceInt: Int = 0
        @SerializedName("shop")
        var shop: Shop? = Shop()
        @SerializedName("departmentId")
        var departmentId: Int = 0
        @SerializedName("rating")
        var rating: Int = 0
        @SerializedName("countReview")
        var countReview: Int = 0
        @SerializedName("recommendationType")
        var recommendationType: String? = ""
        @SerializedName("stock")
        var stock: Int = 0
        @SerializedName("isTopads")
        var isIsTopads: Boolean = false
        @SerializedName("labels")
        var labels: List<*>? = listOf<Any>()
        @SerializedName("badges")
        var badges: List<Badges>? = listOf()
        @SerializedName("wholesalePrice")
        var wholesalePrice: List<*>? = listOf<Any>()
        @SerializedName("minOrder")
        var minOrder: Int? = 0

        class Shop {
            @SerializedName("id")
            var id: Int = 0
            @SerializedName("name")
            var name: String? = ""
        }

        class Badges {

            @SerializedName("title")
            var title: String? = ""
            @SerializedName("imageUrl")
            var imageUrl: String? = ""
        }
    }

    class RecomendationData {

        @SerializedName("tID")
        var tid: String? = ""
        @SerializedName("source")
        var source: String? = ""
        @SerializedName("title")
        var title: String? = ""
        @SerializedName("foreignTitle")
        var foreignTitle: String? = ""
        @SerializedName("widgetUrl")
        var widgetUrl: String? = ""
        @SerializedName("pageName")
        var pageName: String? = ""
        @SerializedName("layoutType")
        var layoutType: String? = ""
        @SerializedName("pagination")
        var pagination: Pagination = Pagination()
        @SerializedName("recommendation")
        var recommendation: List<Recommendation>? = listOf()

    }
}
