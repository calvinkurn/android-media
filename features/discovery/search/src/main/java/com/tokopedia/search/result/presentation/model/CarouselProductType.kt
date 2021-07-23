package com.tokopedia.search.result.presentation.model

sealed class CarouselProductType(val hasThreeDots: Boolean) {
    abstract fun getDataLayerList(isOrganicAds: Boolean): String
}

class BroadMatchProduct(
        val isOrganicAds: Boolean,
        hasThreeDots: Boolean = true,
): CarouselProductType(hasThreeDots) {
    override fun getDataLayerList(isOrganicAds: Boolean): String {
        return "/search - broad match - ${if (isOrganicAds) "organic ads" else "organic"}"
    }
}

class DynamicCarouselProduct(val type: String): CarouselProductType(false) {
    override fun getDataLayerList(isOrganicAds: Boolean): String {
        return "/search - carousel dynamic"
    }
}