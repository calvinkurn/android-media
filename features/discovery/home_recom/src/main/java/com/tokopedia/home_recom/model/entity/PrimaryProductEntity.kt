package com.tokopedia.home_recom.model.entity


import com.google.gson.annotations.SerializedName

/**
 * A Pojo Class for Primary Product.
 */

data class ProductRecommendationProductDetail(
        @SerializedName("data")
        val `data`: List<Data> = listOf()
)

data class PrimaryProductEntity(
        @SerializedName("productRecommendationProductDetail")
        val productRecommendationProductDetail: ProductRecommendationProductDetail = ProductRecommendationProductDetail()
)

data class Data(
        @SerializedName("foreignTitle")
        val foreignTitle: String = "",
        @SerializedName("recommendation")
        val recommendation: List<ProductDetailData> = listOf(),
        @SerializedName("source")
        val source: String = "",
        @SerializedName("tID")
        val tID: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("widgetUrl")
        val widgetUrl: String = ""
)

data class ProductDetailData(
        @SerializedName("appUrl")
        val appUrl: String = "",
        @SerializedName("badges")
        val badges: List<Badge> = listOf(),
        @SerializedName("categoryBreadcrumbs")
        val categoryBreadcrumbs: String = "",
        @SerializedName("clickUrl")
        var clickUrl: String = "",
        @SerializedName("countReview")
        val countReview: Int = -1,
        @SerializedName("countReviewFloat")
        val countReviewFloat: Int = -1,
        @SerializedName("departmentId")
        val departmentId: Int = -1,
        @SerializedName("id")
        val id: Int = -1,
        @SerializedName("status")
        val status: Int = 1,
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("isTopads")
        var isTopads: Boolean = false,
        @SerializedName("IsWishlist")
        val isWishlist: Boolean = false,
        @SerializedName("labels")
        val labels: List<Any> = listOf(),
        @SerializedName("name")
        val name: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("priceInt")
        val priceInt: Int = -1,
        @SerializedName("rating")
        val rating: Int = -1,
        @SerializedName("recommendationType")
        val recommendationType: String = "",
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("slashedPrice")
        val slashedPrice: String = "",
        @SerializedName("slashedPriceInt")
        val slashedPriceInt: Int = 0,
        @SerializedName("discountPercentage")
        val discountPercentage: Int = 0,
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("trackerImageUrl")
        var trackerImageUrl: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("wholesalePrice")
        val wholesalePrice: List<Any> = listOf(),
        @SerializedName("wishlistUrl")
        val wishlistUrl: String = "",
        @SerializedName("productKey")
        val productKey: String = "",
        @SerializedName("shopDomain")
        val shopDomain: String = "",
        @SerializedName("urlPath")
        val urlPath: String = ""
        )

data class Shop(
        @SerializedName("id")
        val id: Int = -1,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("appUrl")
        val appUrl: String = "",
        @SerializedName("isGold")
        val isGold: Boolean = false,
        @SerializedName("location")
        val location: String = "",
        @SerializedName("city")
        val city: String = "",
        @SerializedName("reputation")
        val reputation: String = "",
        @SerializedName("clover")
        val clover: String = "",
        @SerializedName("shopImage")
        val shopImage: String = ""
)

data class Badge(
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("title")
        val title: String = ""
)