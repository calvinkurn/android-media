package com.tokopedia.recommendation_widget_common.infinite.foryou.recom

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAdsLog
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog

data class RecommendationCardModel(
    val productCardModel: ProductCardModel,
    val recommendationProductItem: ProductItem,
    val pageName: String = "",
    val layoutName: String = "",
    val position: Int = -1,
    val tabIndex: Int = -1,
    val tabName: String = "",
    val appLog: RecommendationAppLog = RecommendationAppLog(),
) : ForYouRecommendationVisitable, ImpressHolder() {

    override fun type(typeFactory: ForYouRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other is RecommendationCardModel && other.recommendationProductItem.id == recommendationProductItem.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecommendationCardModel

        if (recommendationProductItem != other.recommendationProductItem) return false

        return true
    }

    override fun hashCode(): Int {
        return recommendationProductItem.hashCode()
    }

    data class ProductItem(
        val id: String = "",
        val parentProductId: String = "",
        val name: String = "",
        val imageUrl: String = "",
        val recommendationType: String = "",
        val priceInt: Int = 0,
        val slashedPriceInt: Int = 0,
        val freeOngkirIsActive: Boolean = false,
        val labelGroup: List<LabelGroup> = emptyList(),
        val categoryBreadcrumbs: String = "",
        val clusterID: Int = 0,
        val isTopAds: Boolean = false,
        val trackerImageUrl: String = "",
        val clickUrl: String = "",
        val isWishlist: Boolean = false,
        val wishListUrl: String = "",
        val shop: Shop = Shop(),
        val countSold: Int = 0,
        val recommendationAdsLog: RecommendationAdsLog = RecommendationAdsLog()
    ) {
        data class LabelGroup(
            val position: String = "",
            val title: String = "",
            val type: String = "",
            val url: String = ""
        )

        data class Shop(
            val applink: String = "",
            val city: String = "",
            val domain: String = "",
            val id: String = "0",
            val imageUrl: String = "",
            val name: String = "",
            val reputation: String = "",
            val url: String = ""
        )
    }
}
