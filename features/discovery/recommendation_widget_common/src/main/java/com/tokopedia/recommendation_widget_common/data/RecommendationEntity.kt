package com.tokopedia.recommendation_widget_common.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created by devara fikry on 16/04/19.
 * Credit errysuprayogi
 */
data class RecommendationEntity (
        @SerializedName("productRecommendationWidget")
        val productRecommendationWidget: ProductRecommendationWidget = ProductRecommendationWidget()
){

    data class ProductRecommendationWidget (
        @SerializedName("data")
        val data: List<RecommendationData> = listOf()
    )

    class Pagination(
        @SerializedName("currentPage")
        val currentPage: Int = 0,
        @SerializedName("nextPage")
        val nextPage: Int = 0,
        @SerializedName("prevPage")
        val prevPage: Int = 0,
        @SerializedName("hasNext")
        val hasNext: Boolean = false
    )

    class Recommendation {
        @SuppressLint("Invalid Data Type")
        @SerializedName("id")
        val id: Long = 0
        @SerializedName("name")
        val name: String = ""
        @SerializedName("categoryBreadcrumbs")
        val categoryBreadcrumbs: String = ""
        @SerializedName("url")
        val url: String = ""
        @SerializedName("appUrl")
        val appUrl: String = ""
        @SerializedName("clickUrl")
        val clickUrl: String = ""
        @SerializedName("wishlistUrl")
        val wishlistUrl: String = ""
        @SerializedName("discountPercentage")
        val discountPercentage: Int = 0
        @SerializedName("slashedPrice")
        val slashedPrice: String = ""
        @SerializedName("slashedPriceInt")
        val slashedPriceInt: Int = 0
        @SerializedName("trackerImageUrl")
        val trackerImageUrl: String = ""
        @SerializedName("imageUrl")
        val imageUrl: String = ""
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: String = ""
        @SerializedName("priceInt")
        val priceInt: Int = 0
        @SerializedName("shop")
        val shop: Shop = Shop()
        @SerializedName("freeOngkir")
        val freeOngkirInformation: FreeOngkirInformation = FreeOngkirInformation()
        @SerializedName("departmentId")
        val departmentId: Int = 0
        @SerializedName("rating")
        val rating: Int = 0
        @SerializedName("ratingAverage")
        val ratingAverage: String = ""
        @SerializedName("countReview")
        val countReview: Int = 0
        @SerializedName("recommendationType")
        val recommendationType: String = ""
        @SerializedName("stock")
        val stock: Int = 0
        @SerializedName("isTopads")
        val isIsTopads: Boolean = false
        @SerializedName("isWishlist")
        val isWishlist: Boolean = false
        @SerializedName("labelgroup")
        val labelGroups: List<LabelGroup> = listOf()
        @SerializedName("badges")
        val badges: List<Badges> = listOf()
        @SerializedName("minOrder")
        val minOrder: Int = 0
        @SerializedName("specificationLabels")
        val specificationsLabels: List<SpecificationsLabels> = listOf()

        class SpecificationsLabels {
            @SerializedName("key")
            val key: String = ""
            @SerializedName("value")
            val value: String = ""
        }

        class LabelGroup {
            @SerializedName("position")
            val position: String = "0"
            @SerializedName("title")
            val title: String = ""
            @SerializedName("type")
            val type: String = ""
            @SerializedName("url")
            val imageUrl: String = ""
        }

        class Shop {
            @SuppressLint("Invalid Data Type")
            @SerializedName("id")
            val id: Int = -1
            @SerializedName("name")
            val name: String = ""
            @SerializedName("city")
            val city: String = ""
            @SerializedName("isGold")
            val isGold: Boolean = false
            @SerializedName("isOfficial")
            val isOfficial: Boolean = false
        }

        class Badges {

            @SerializedName("imageUrl")
            val imageUrl: String = ""
        }

        class FreeOngkirInformation {

            @SerializedName("isActive")
            val isActive: Boolean = false
            @SerializedName("imageUrl")
            val imageUrl: String = ""
        }
    }

    data class RecommendationData(
            @SerializedName("tID")
            val tid: String = "",
            @SerializedName("source")
            val source: String = "",
            @SerializedName("title")
            val title: String = "",
            @SerializedName("subtitle")
            val subtitle: String = "",
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
            val recommendation: List<Recommendation> = listOf(),
            @SerializedName("campaign")
            val campaign: RecommendationCampaign = RecommendationCampaign()
    )

    class RecommendationCampaign {
        @SerializedName("appLandingPageLink")
        val appLandingPageLink: String = ""
        @SerializedName("landingPageLink")
        val landingPageLink: String = ""
        @SerializedName("assets")
        val assets: Assets? = Assets()
        @SerializedName("foreignTitle")
        val foreignTitle: String = ""

        class Assets {
            @SerializedName("banner")
            val banner: Banner? = Banner()
        }

        class Banner {
            @SerializedName("apps")
            val apps: String = ""
        }

    }
}
