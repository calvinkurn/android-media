package com.tokopedia.dilayanitokopedia.home.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.dilayanitokopedia.home.domain.model.common.FreeOngkir
import com.tokopedia.dilayanitokopedia.home.domain.model.common.Shop

/**
 * Created by irpan on 30/11/22.
 */
data class GetDtHomeRecommendationResponse(
    @SerializedName("getHomeRecommendationProductV2")
    val response: GetHomeRecommendationProductV2 = GetHomeRecommendationProductV2()
)

data class GetHomeRecommendationProductV2(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean = false,
    @SerializedName("pageName")
    val pageName: String = "",
    @SerializedName("positions")
    val positions: List<Position> = listOf(),
    @SerializedName("products")
    val products: List<Product> = listOf()
)

data class Position(
    @SerializedName("type")
    val type: String = ""
)

data class Product(

    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("badges")
    val badges: List<Badge> = listOf(),
    @SerializedName("categoryBreadcrumbs")
    val categoryBreadcrumbs: String = "",
    @SerializedName("clickUrl")
    val clickUrl: String = "",
    @SerializedName("clusterID")
    val clusterID: Long = 0,
    @SerializedName("countReview")
    val countReview: Int = 0,
    @SerializedName("discountPercentage")
    val discountPercentage: Int = 0,
    @SerializedName("freeOngkir")
    val freeOngkir: FreeOngkir = FreeOngkir(),
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("isTopads")
    val isTopads: Boolean = false,
    @SerializedName("isWishlist")
    val isWishlist: Boolean = false,
    @SerializedName("labelGroup")
    val labelGroup: List<LabelGroup> = listOf(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price")
    val price: String = "",

    @SerializedName("priceInt")
    val priceInt: Double = 0.0,
    @SerializedName("productKey")
    val productKey: String = "",
    @SerializedName("rating")
    val rating: Int = 0,
    @SerializedName("ratingAverage")
    val ratingAverage: String = "",
    @SerializedName("recommendationType")
    val recommendationType: String = "",
    @SerializedName("shop")
    val shop: Shop = Shop(),
    @SerializedName("slashedPrice")
    val slashedPrice: String = "",
    @SerializedName("slashedPriceInt")
    val slashedPriceInt: Int = 0,
    @SerializedName("trackerImageUrl")
    val trackerImageUrl: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("wishlistUrl")
    val wishlistUrl: String = ""
)

data class Badge(
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("title")
    val title: String = ""
)

data class LabelGroup(
    @SerializedName("position")
    val position: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val url: String = ""
)
