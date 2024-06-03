package com.tokopedia.home.beranda.domain.gql.recommendationcard

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.RecomTemporary
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAdsLog

data class RecommendationCard(
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("badges")
    val badges: List<Badge> = emptyList(),
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
    @SerializedName("parentProductID")
    val parentProductId: String = "0",
    @SerializedName("categoryID")
    val categoryID: String = "0",
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("isTopads")
    val isTopads: Boolean = false,
    @SerializedName("isWishlist")
    val isWishlist: Boolean = false,
    @SerializedName("labelGroup")
    val labelGroup: List<LabelGroup> = emptyList(),
    @SerializedName("layout")
    val layout: String = "",
    @SerializedName("layoutTracker")
    val layoutTracker: String = "",
    @SerializedName("dataStringJson")
    val dataStringJson: String = "",
    @SerializedName("gradientColor")
    val gradientColor: List<String> = emptyList(),
    @SerializedName("label")
    val label: RecommendationLabelData = RecommendationLabelData(),
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
    val wishlistUrl: String = "",
    @SerializedName("recParam")
    val recParam: String = "",
    @SerializedName("creativeID")
    val creativeID: String = "",
    @SerializedName("logExtra")
    val logExtra: String = "",
    @SerializedName("countSold")
    val countSold: Int = 0,
) {

    fun mapToHomeRecommendationProductItem(): HomeRecommendationItemDataModel.HomeRecommendationProductItem {
        return HomeRecommendationItemDataModel.HomeRecommendationProductItem(
            id = id,
            name = name,
            imageUrl = imageUrl,
            recommendationType = recommendationType,
            priceInt = priceInt,
            slashedPriceInt = slashedPriceInt,
            freeOngkirIsActive = freeOngkir.isActive,
            labelGroup = labelGroup.map {
                HomeRecommendationItemDataModel.HomeRecommendationProductItem.LabelGroup(
                    position = it.position,
                    title = it.title,
                    type = it.type,
                    url = it.url,
                )
            },
            categoryBreadcrumbs = categoryBreadcrumbs,
            clusterID = clusterID,
            isTopAds = isTopads,
            trackerImageUrl = trackerImageUrl,
            clickUrl = clickUrl,
            isWishlist = isWishlist,
            wishListUrl = wishlistUrl,
            shop = shop.let {
                HomeRecommendationItemDataModel.HomeRecommendationProductItem.Shop(
                    id = it.id,
                    applink = it.applink,
                    city = it.city,
                    domain = it.domain,
                    imageUrl = it.imageUrl,
                    name = it.name,
                    reputation = it.reputation,
                    url = it.url,
                )
            },
            recParam = recParam,
            recommendationAdsLog = RecommendationAdsLog(
                creativeID = creativeID,
                logExtra = logExtra
            )
        )
    }

    @RecomTemporary
    fun mapToHomeGlobalRecommendationProductItem(): RecommendationCardModel.ProductItem {
        return RecommendationCardModel.ProductItem(
            id = id,
            parentProductId = parentProductId,
            name = name,
            imageUrl = imageUrl,
            recommendationType = recommendationType,
            priceInt = priceInt,
            freeOngkirIsActive = freeOngkir.isActive,
            labelGroup = labelGroup.map {
                RecommendationCardModel.ProductItem.LabelGroup(
                    position = it.position,
                    title = it.title,
                    type = it.type,
                    url = it.url
                )
            },
            categoryBreadcrumbs = categoryBreadcrumbs,
            clusterID = clusterID,
            isTopAds = isTopads,
            trackerImageUrl = trackerImageUrl,
            clickUrl = clickUrl,
            isWishlist = isWishlist,
            wishListUrl = wishlistUrl,
            countSold = countSold,
            shop = RecommendationCardModel.ProductItem.Shop(
                id = shop.id,
                city = shop.city,
                applink = shop.applink,
                domain = shop.domain,
                imageUrl = shop.imageUrl,
                name = shop.name,
                reputation = shop.reputation,
            ),
            recommendationAdsLog = RecommendationAdsLog(
                creativeID = creativeID,
                logExtra = logExtra
            )
        )
    }

    data class RecommendationLabelData(
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("textColor")
        val textColor: String = "",
        @SerializedName("backColor")
        val backColor: String = ""
    )
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
        val url: String = "",
        @SerializedName("styles")
        val styles: List<Styles> = listOf(),
    ) {
        data class Styles(
            @SerializedName("key")
            val key: String = "",
            @SerializedName("value")
            val value: String = "",
        )
    }

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
