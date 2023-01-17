package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCarousel.TYPE_INSPIRATION_CAROUSEL_KEYWORD
import com.tokopedia.search.result.product.broadmatch.BroadMatchTracking.getBroadMatchListName
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

sealed class CarouselProductType(val hasThreeDots: Boolean) {
    abstract fun getDataLayerList(isOrganicAds: Boolean, componentId: String): String

    companion object {

        fun of(
            type: String,
            option: InspirationCarouselDataView.Option,
            product: InspirationCarouselDataView.Option.Product,
        ): CarouselProductType =
            if (type == TYPE_INSPIRATION_CAROUSEL_KEYWORD)
                BroadMatchProduct(false)
            else
                DynamicCarouselProduct(option.inspirationCarouselType, product)
    }
}

class BroadMatchProduct(
    hasThreeDots: Boolean = true,
) : CarouselProductType(hasThreeDots) {
    override fun getDataLayerList(isOrganicAds: Boolean, componentId: String): String {
        return getBroadMatchListName(isOrganicAds, componentId)
    }
}

class DynamicCarouselProduct(
    val type: String,
    val inspirationCarouselProduct: InspirationCarouselDataView.Option.Product,
) : CarouselProductType(false) {
    override fun getDataLayerList(isOrganicAds: Boolean, componentId: String): String {
        return "/search - carousel dynamic"
    }
}
