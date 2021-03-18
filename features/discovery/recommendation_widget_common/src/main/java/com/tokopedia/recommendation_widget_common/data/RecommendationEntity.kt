package com.tokopedia.recommendation_widget_common.data

import com.google.gson.annotations.SerializedName

/**
 * Created by devara fikry on 16/04/19.
 * Credit errysuprayogi
 */
class RecommendationEntity {

    @SerializedName("productRecommendationWidget")
    var productRecommendationWidget: ProductRecommendationWidget? = ProductRecommendationWidget()

    class ProductRecommendationWidget {
        @SerializedName("data")
        var data: List<RecomendationData>? = listOf()
    }

    class Pagination {
        @SerializedName("currentPage")
        var currentPage: Int = 0
        @SerializedName("nextPage")
        var nextPage: Int = 0
        @SerializedName("prevPage")
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
        @SerializedName("freeOngkir")
        var freeOngkirInformation: FreeOngkirInformation? = FreeOngkirInformation()
        @SerializedName("departmentId")
        var departmentId: Int = 0
        @SerializedName("rating")
        var rating: Int = 0
        @SerializedName("ratingAverage")
        val ratingAverage: String = ""
        @SerializedName("countReview")
        var countReview: Int = 0
        @SerializedName("recommendationType")
        var recommendationType: String? = ""
        @SerializedName("stock")
        var stock: Int = 0
        @SerializedName("isTopads")
        var isIsTopads: Boolean = false
        @SerializedName("isWishlist")
        var isWishlist: Boolean = false
        @SerializedName("labelgroup")
        var labelGroups: List<LabelGroup>? = listOf<LabelGroup>()
        @SerializedName("badges")
        var badges: List<Badges>? = listOf()
        @SerializedName("minOrder")
        var minOrder: Int? = 0
        @SerializedName("specificationsLabels")
        val specificationsLabels: List<SingleProductRecommendationEntity.SpecificationsLabels> = listOf()

        class SpecificationsLabels {
            @SerializedName("key")
            val key: String = "",
            @SerializedName("value")
            val value: String = ""
        }

        class LabelGroup {
            @SerializedName("position")
            var position: String = "0"
            @SerializedName("title")
            var title: String? = ""
            @SerializedName("type")
            var type: String? = ""
        }

        class Shop {
            @SerializedName("id")
            var id: Int = 0
            @SerializedName("name")
            var name: String? = ""
            @SerializedName("city")
            var city: String? =""
            @SerializedName("isGold")
            var isGold: Boolean ?= false
            @SerializedName("isOfficial")
            var isOfficial: Boolean ?= false
        }

        class Badges {

            @SerializedName("imageUrl")
            var imageUrl: String? = ""
        }

        class FreeOngkirInformation {

            @SerializedName("isActive")
            var isActive: Boolean? = false
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
        @SerializedName("seeMoreAppLink")
        var seeMoreAppLink: String? = ""
        @SerializedName("pagination")
        var pagination: Pagination = Pagination()
        @SerializedName("recommendation")
        var recommendation: List<Recommendation>? = listOf()

    }
}
