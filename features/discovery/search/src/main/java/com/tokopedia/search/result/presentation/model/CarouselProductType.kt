package com.tokopedia.search.result.presentation.model

sealed class CarouselProductType(val hasThreeDots: Boolean) {
    abstract val dataLayerList: String
}

class BroadMatchProduct(val isOrganicAds: Boolean): CarouselProductType(true) {
    override val dataLayerList = "/search - broad match - ${if (isOrganicAds) "organic ads" else "organic"}"
}

class DynamicCarouselProduct(val type: String): CarouselProductType(false) {
    override val dataLayerList = "/search - carousel"
}