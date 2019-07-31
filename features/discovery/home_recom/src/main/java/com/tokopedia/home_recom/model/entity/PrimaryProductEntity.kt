package com.tokopedia.home_recom.model.entity


import com.google.gson.annotations.SerializedName

data class ProductRecommendationProductDetail(
        @SerializedName("data")
        val `data`: List<Data>
)

data class PrimaryProductEntity(
        @SerializedName("productRecommendationProductDetail")
        val productRecommendationProductDetail: ProductRecommendationProductDetail
)

data class Data(
        @SerializedName("foreignTitle")
        val foreignTitle: String,
        @SerializedName("recommendation")
        val recommendation: List<ProductDetailData>,
        @SerializedName("source")
        val source: String,
        @SerializedName("tID")
        val tID: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("widgetUrl")
        val widgetUrl: String
)

data class ProductDetailData(
        @SerializedName("appUrl")
        val appUrl: String,
        @SerializedName("badges")
        val badges: List<Badge>,
        @SerializedName("categoryBreadcrumbs")
        val categoryBreadcrumbs: String,
        @SerializedName("clickUrl")
        val clickUrl: String,
        @SerializedName("countReview")
        val countReview: Int,
        @SerializedName("countReviewFloat")
        val countReviewFloat: Int,
        @SerializedName("departmentId")
        val departmentId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("isTopads")
        val isTopads: Boolean,
        @SerializedName("IsWishlist")
        val isWishlist: Boolean,
        @SerializedName("labels")
        val labels: List<Any>,
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("priceInt")
        val priceInt: Int,
        @SerializedName("rating")
        val rating: Int,
        @SerializedName("recommendationType")
        val recommendationType: String,
        @SerializedName("shop")
        val shop: Shop,
        @SerializedName("slashedPrice")
        val slashedPrice: String,
        @SerializedName("slashedPriceInt")
        val slashedPriceInt: Int,
        @SerializedName("discountPercentage")
        val discountPercentage: Int,
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("minOrder")
        val minOrder: Int,
        @SerializedName("trackerImageUrl")
        val trackerImageUrl: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("wholesalePrice")
        val wholesalePrice: List<Any>,
        @SerializedName("wishlistUrl")
        val wishlistUrl: String,
        @SerializedName("productKey")
        val productKey: String,
        @SerializedName("shopDomain")
        val shopDomain: String,
        @SerializedName("urlPath")
        val urlPath: String
        )

data class Shop(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("appUrl")
        val appUrl: String,
        @SerializedName("isGold")
        val isGold: Boolean,
        @SerializedName("location")
        val location: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("reputation")
        val reputation: String,
        @SerializedName("clover")
        val clover: String,
        @SerializedName("shopImage")
        val shopImage: String
)

data class Badge(
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("title")
        val title: String
)