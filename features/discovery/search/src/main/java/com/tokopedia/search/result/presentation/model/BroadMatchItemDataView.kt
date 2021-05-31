package com.tokopedia.search.result.presentation.model

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.model.ImpressHolder

data class BroadMatchItemDataView(
        val id: String = "",
        val name: String = "",
        val price: Int = 0,
        val imageUrl: String = "",
        val url: String = "",
        val applink: String = "",
        val priceString: String = "",
        val shopLocation: String = "",
        val badgeItemDataViewList: List<BadgeItemDataView> = listOf(),
        val freeOngkirDataView: FreeOngkirDataView = FreeOngkirDataView(),
        var isWishlisted: Boolean = false,
        val position: Int = 0,
        val alternativeKeyword: String = "",
        val isOrganicAds: Boolean = false,
        val topAdsViewUrl: String = "",
        val topAdsClickUrl: String = "",
        val topAdsWishlistUrl: String = "",
        val ratingAverage: String = "",
        val labelGroupDataList: List<LabelGroupDataView> = listOf(),
        val carouselProductType: CarouselProductType,
): ImpressHolder() {

    fun asImpressionObjectDataLayer(): Any {
        return DataLayer.mapOf(
                "name", name,
                "id", id,
                "price", price,
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", carouselProductType.dataLayerList,
                "position", position
        )
    }

    fun asClickObjectDataLayer(): Any {
        return DataLayer.mapOf(
                "name", name,
                "id", id,
                "price", price,
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", carouselProductType.dataLayerList,
                "position", position,
                "attribution", "none / other"
        )
    }
}