package com.tokopedia.dilayanitokopedia.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.dilayanitokopedia.home.domain.model.common.FreeOngkir
import com.tokopedia.dilayanitokopedia.home.domain.model.common.Shop

/**
 * Created by irpan on 30/11/22.
 */
data class GetDtHomeRecommendationResponse(
    @SerializedName("getHomeRecommendationProductV2")
    @Expose
    val response: GetHomeRecommendationProductV2 = GetHomeRecommendationProductV2()
)

data class GetHomeRecommendationProductV2(

    @SerializedName("hasNextPage")
    @Expose
    val hasNextPage: Boolean = false,
    @SerializedName("pageName")
    @Expose
    val pageName: String = "",
    @SerializedName("positions")
    @Expose
    val positions: List<Position> = listOf(),

    @SerializedName("products")
    @Expose
    val products: List<Product> = listOf()
)

data class Position(
    @SerializedName("type")
    @Expose
    val type: String = ""
)

data class Product(

    @SerializedName("applink")
    @Expose
    val applink: String = "",
    @SerializedName("badges")
    @Expose
    val badges: List<Badge> = listOf(),
    @SerializedName("categoryBreadcrumbs")
    @Expose
    val categoryBreadcrumbs: String = "",
    @SerializedName("clickUrl")
    @Expose
    val clickUrl: String = "",
    @SerializedName("clusterID")
    @Expose
    val clusterID: Long = 0,
    @SerializedName("countReview")
    @Expose
    val countReview: Int = 0,
    @SerializedName("discountPercentage")
    @Expose
    val discountPercentage: Int = 0,
    @SerializedName("freeOngkir")
    @Expose
    val freeOngkir: FreeOngkir = FreeOngkir(),
    @SerializedName("id")
    @Expose
    val id: Long = 0,
    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String = "",
    @SerializedName("isTopads")
    @Expose
    val isTopads: Boolean = false,
    @SerializedName("isWishlist")
    @Expose
    val isWishlist: Boolean = false,
    @SerializedName("labelGroup")
    @Expose
    val labelGroup: List<LabelGroup> = listOf(),
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("price")
    @Expose
    val price: String = "",

    @SerializedName("priceInt")
    @Expose
    val priceInt: Double = 0.0,
    @SerializedName("productKey")
    @Expose
    val productKey: String = "",
    @SerializedName("rating")
    @Expose
    val rating: Int = 0,
    @SerializedName("ratingAverage")
    @Expose
    val ratingAverage: String = "",
    @SerializedName("recommendationType")
    @Expose
    val recommendationType: String = "",
    @SerializedName("shop")
    @Expose
    val shop: Shop = Shop(),
    @SerializedName("slashedPrice")
    @Expose
    val slashedPrice: String = "",
    @SerializedName("slashedPriceInt")
    @Expose
    val slashedPriceInt: Int = 0,
    @SerializedName("trackerImageUrl")
    @Expose
    val trackerImageUrl: String = "",
    @SerializedName("url")
    @Expose
    val url: String = "",
    @SerializedName("wishlistUrl")
    @Expose
    val wishlistUrl: String = ""
)

data class Badge(
    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String = "",
    @SerializedName("title")
    @Expose
    val title: String = ""
)

data class LabelGroup(
    @SerializedName("position")
    @Expose
    val position: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("url")
    @Expose
    val url: String = ""

)
