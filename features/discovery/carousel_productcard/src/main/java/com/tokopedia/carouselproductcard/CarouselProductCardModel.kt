package com.tokopedia.carouselproductcard

import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.ProductCardModel

internal data class CarouselProductCardModel(
        val productCardModel: ProductCardModel,
        val carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
        val blankSpaceConfig: BlankSpaceConfig = BlankSpaceConfig(),
        val forcedHeight: Int = -1
) {
    fun getOnItemClickListener() = carouselProductCardListenerInfo.onItemClickListener
    fun getOnItemAddToCartListener() = carouselProductCardListenerInfo.onItemAddToCartListener
    fun getOnItemImpressedListener() = carouselProductCardListenerInfo.onItemImpressedListener
    fun getOnItemLongClickListener() = carouselProductCardListenerInfo.onItemLongClickListener
    fun getOnWishlistItemClickListener() = carouselProductCardListenerInfo.onWishlistItemClickListener
}