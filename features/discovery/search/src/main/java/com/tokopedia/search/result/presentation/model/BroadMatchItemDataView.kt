package com.tokopedia.search.result.presentation.model

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.utils.getFormattedPositionName

data class BroadMatchItemDataView(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    val url: String = "",
    val applink: String = "",
    val priceString: String = "",
    val shopLocation: String = "",
    val shopName: String = "",
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
    val dimension90: String = "",
    val componentId: String = "",
): ImpressHolder() {

    private fun asObjectDataLayer(): MutableMap<String, Any> {
        return DataLayer.mapOf(
            "name", name,
            "id", id,
            "price", price,
            "brand", "none / other",
            "category", "none / other",
            "variant", "none / other",
            "list", carouselProductType.getDataLayerList(isOrganicAds, componentId),
            "position", position,
            "dimension90", dimension90,
            "dimension115", labelGroupDataList.getFormattedPositionName(),
        )
    }

    fun asImpressionObjectDataLayer(): Any {
        return asObjectDataLayer()
    }

    fun asClickObjectDataLayer(): Any {
        return asObjectDataLayer().also {
            it["attribution"] = "none / other"
        }
    }
}