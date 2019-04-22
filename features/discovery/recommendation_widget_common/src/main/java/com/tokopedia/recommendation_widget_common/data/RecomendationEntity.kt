package com.tokopedia.recommendation_widget_common.data

import com.google.gson.annotations.SerializedName

/**
 * Created by devara fikry on 16/04/19.
 * Credit errysuprayogi
 */
class RecomendationEntity {

    @SerializedName("productRecommendationWidget")
    var productRecommendationWidget: ProductRecommendationWidget? = null

    class ProductRecommendationWidget {
        @SerializedName("data")
        var data: List<RecomendationData>? = null

    }

    class Recommendation {

        @SerializedName("id")
        var id: Int = 0
        @SerializedName("name")
        var name: String? = null
        @SerializedName("categoryBreadcrumbs")
        var categoryBreadcrumbs: String? = null
        @SerializedName("url")
        var url: String? = null
        @SerializedName("appUrl")
        var appUrl: String? = null
        @SerializedName("clickUrl")
        var clickUrl: String? = null
        @SerializedName("wishlistUrl")
        var wishlistUrl: String? = null
        @SerializedName("trackerImageUrl")
        var trackerImageUrl: String? = null
        @SerializedName("imageUrl")
        var imageUrl: String? = null
        @SerializedName("price")
        var price: String? = null
        @SerializedName("priceInt")
        var priceInt: Int = 0
        @SerializedName("shop")
        var shop: Shop? = null
        @SerializedName("departmentId")
        var departmentId: Int = 0
        @SerializedName("rating")
        var rating: Int = 0
        @SerializedName("countReview")
        var countReview: Int = 0
        @SerializedName("recommendationType")
        var recommendationType: String? = null
        @SerializedName("stock")
        var stock: Int = 0
        @SerializedName("isTopads")
        var isIsTopads: Boolean = false
        @SerializedName("labels")
        var labels: List<*>? = null
        @SerializedName("badges")
        var badges: List<Badges>? = null
        @SerializedName("wholesalePrice")
        var wholesalePrice: List<*>? = null

        class Shop {
            /**
             * id : 979672
             * name : maxstoreband
             */

            @SerializedName("id")
            var id: Int = 0
            @SerializedName("name")
            var name: String? = null
        }

        class Badges {

            @SerializedName("title")
            var title: String? = null
            @SerializedName("imageUrl")
            var imageUrl: String? = null
        }
    }

    class RecomendationData {

        @SerializedName("tID")
        var tid: String? = null
        @SerializedName("source")
        var source: String? = null
        @SerializedName("title")
        var title: String? = null
        @SerializedName("foreignTitle")
        var foreignTitle: String? = null
        @SerializedName("widgetUrl")
        var widgetUrl: String? = null
        @SerializedName("recommendation")
        var recommendation: List<Recommendation>? = null

    }
}
