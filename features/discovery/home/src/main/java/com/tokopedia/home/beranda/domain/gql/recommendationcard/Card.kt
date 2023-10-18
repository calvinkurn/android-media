package com.tokopedia.home.beranda.domain.gql.recommendationcard

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("badges")
    val badges: List<Badge> = listOf(),
    @SerializedName("categoryBreadcrumbs")
    val categoryBreadcrumbs: String = "",
    @SerializedName("clickUrl")
    val clickUrl: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("clusterID")
    val clusterID: Int = 0,
    @SerializedName("countReview")
    val countReview: Int = 0,
    @SerializedName("discountPercentage")
    val discountPercentage: Int = 0,
    @SerializedName("freeOngkir")
    val freeOngkir: FreeOngkir = FreeOngkir(),
    @SerializedName("iconUrl")
    val iconUrl: String = "",
    @SerializedName("id")
    val id: String = "0",
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("isTopads")
    val isTopads: Boolean = false,
    @SerializedName("isWishlist")
    val isWishlist: Boolean = false,
    @SerializedName("labelGroup")
    val labelGroup: List<LabelGroup> = listOf(),
    @SerializedName("layout")
    val layout: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price")
    val price: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("priceInt")
    val priceInt: Int = 0,
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
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("trackerImageUrl")
    val trackerImageUrl: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("wishlistUrl")
    val wishlistUrl: String = ""
) {
    data class Badge(
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("title")
        val title: String = ""
    )

    data class FreeOngkir(
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("isActive")
        val isActive: Boolean = false
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

    data class Shop(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("city")
        val city: String = "",
        @SerializedName("domain")
        val domain: String = "",
        @SerializedName("id")
        val id: String = "0",
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("reputation")
        val reputation: String = "",
        @SerializedName("url")
        val url: String = ""
    )
}
