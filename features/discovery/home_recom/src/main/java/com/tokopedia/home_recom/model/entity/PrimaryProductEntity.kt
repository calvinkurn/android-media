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
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("trackerImageUrl")
        val trackerImageUrl: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("wholesalePrice")
        val wholesalePrice: List<Any>,
        @SerializedName("wishlistUrl")
        val wishlistUrl: String
)

data class Shop(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("shopImage")
        val shopImage: String,
        @SerializedName("location")
        val location: String
)

data class Badge(
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("title")
        val title: String
)