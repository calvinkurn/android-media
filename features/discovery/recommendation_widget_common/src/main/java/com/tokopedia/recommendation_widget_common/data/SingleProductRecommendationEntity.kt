package com.tokopedia.recommendation_widget_common.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 29/08/19
 */

@SuppressLint("Invalid Data Type")
data class SingleProductRecommendationEntity (
    @SerializedName("productRecommendationWidgetSingle")
    val productRecommendationWidget: ProductRecommendationWidgetSingle
){

    data class ProductRecommendationWidgetSingle(@SerializedName("data") val data: RecommendationData = RecommendationData())

    data class Pagination (
        @SerializedName("current_page")
        val currentPage: Int = 0,
        @SerializedName("next_page")
        val nextPage: Int = 0,
        @SerializedName("prev_page")
        val prevPage: Int = 0,
        @SerializedName("hasNext")
        val hasNext: Boolean = false
    )

    data class Recommendation(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("categoryBreadcrumbs")
        val categoryBreadcrumbs: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("appUrl")
        val appUrl: String = "",
        @SerializedName("clickUrl")
        val clickUrl: String = "",
        @SerializedName("wishlistUrl")
        val wishlistUrl: String = "",
        @SerializedName("discountPercentage")
        val discountPercentage: Int = 0,
        @SerializedName("slashedPrice")
        val slashedPrice: String = "",
        @SerializedName("slashedPriceInt")
        val slashedPriceInt: Int = 0,
        @SerializedName("trackerImageUrl")
        val trackerImageUrl: String = "",
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("priceInt")
        val priceInt: Int = 0,
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("freeOngkir")
        val freeOngkirInformation: FreeOngkirInformation = FreeOngkirInformation(),
        @SerializedName("departmentId")
        val departmentId: Int = 0,
        @SerializedName("rating")
        val rating: Int = 0,
        @SerializedName("ratingAverage")
        val ratingAverage: String = "",
        @SerializedName("countReview")
        val countReview: Int = 0,
        @SerializedName("recommendationType")
        val recommendationType: String = "",
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("isTopads")
        val isIsTopads: Boolean = false,
        @SerializedName("isWishlist")
        val isWishlist: Boolean = false,
        @SerializedName("labels")
        val labels: List<*> = listOf<Any>(),
        @SerializedName("badges")
        val badges: List<Badges> = listOf(),
        @SerializedName("wholesalePrice")
        val wholesalePrice: List<*> = listOf<Any>(),
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("labelgroup")
        val labelGroups: List<LabelGroup> = listOf()
    )

    data class LabelGroup (
        @SerializedName("position")
        val position: String = "0",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("type")
        val type: String = ""
    )

    data class Shop (
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("city")
        val city: String =""
    )

    data class Badges(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("imageUrl")
        val imageUrl: String = ""
    )

    data class FreeOngkirInformation (
        @SerializedName("isActive")
        val isActive: Boolean = false,
        @SerializedName("imageUrl")
        val imageUrl: String = ""
    )


    data class RecommendationData(
            @SerializedName("tID")
            val tid: String = "",
            @SerializedName("source")
            val source: String = "",
            @SerializedName("title")
            val title: String = "",
            @SerializedName("foreignTitle")
            val foreignTitle: String = "",
            @SerializedName("widgetUrl")
            val widgetUrl: String = "",
            @SerializedName("pageName")
            val pageName: String = "",
            @SerializedName("layoutType")
            val layoutType: String = "",
            @SerializedName("seeMoreAppLink")
            val seeMoreAppLink: String = "",
            @SerializedName("pagination")
            val pagination: Pagination = Pagination(),
            @SerializedName("recommendation")
            val recommendation: List<Recommendation> = listOf()
    )
}
