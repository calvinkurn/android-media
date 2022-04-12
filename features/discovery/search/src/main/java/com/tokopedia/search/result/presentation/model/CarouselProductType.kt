package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.analytics.SearchTracking

sealed class CarouselProductType(val hasThreeDots: Boolean) {
    abstract fun getDataLayerList(isOrganicAds: Boolean, componentId: String): String
}

class BroadMatchProduct(
        hasThreeDots: Boolean = true,
): CarouselProductType(hasThreeDots) {
    override fun getDataLayerList(isOrganicAds: Boolean, componentId: String): String {
        return SearchTracking.getBroadMatchListName(isOrganicAds, componentId)
    }
}

class DynamicCarouselProduct(
    val type: String,
    val inspirationCarouselProduct: InspirationCarouselDataView.Option.Product,
): CarouselProductType(false) {
    override fun getDataLayerList(isOrganicAds: Boolean, componentId: String): String {
        return "/search - carousel dynamic"
    }
}