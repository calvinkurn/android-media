package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel

data class HomeRecommendationItemDataModel(
    val productCardModel: ProductCardModel,
    val recommendationProductItem: HomeRecommendationProductItem,
    val pageName: String = "",
    val layoutName: String = "",
    val position: Int = -1,
    val tabName: String = ""
) : Visitable<HomeRecommendationTypeFactoryImpl>, ImpressHolder() {

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeRecommendationItemDataModel

        if (recommendationProductItem != other.recommendationProductItem) return false

        return true
    }

    override fun hashCode(): Int {
        return recommendationProductItem.hashCode()
    }

    data class HomeRecommendationProductItem(
        val id: String = "",
        val name: String = "",
        val imageUrl: String = "",
        val recommendationType: String = "",
        val priceInt: Int = 0,
        val freeOngkirIsActive: Boolean = false,
        val labelGroup: List<LabelGroup> = emptyList(),
        val categoryBreadcrumbs: String = "",
        val clusterID: Int = 0,
        val isTopAds: Boolean = false,
        val trackerImageUrl: String = "",
        val clickUrl: String = "",
        val isWishlist: Boolean = false,
        val wishListUrl: String = ""
    ) {
        data class LabelGroup(
            val position: String = "",
            val title: String = "",
            val type: String = "",
            val url: String = ""
        )
    }
}
