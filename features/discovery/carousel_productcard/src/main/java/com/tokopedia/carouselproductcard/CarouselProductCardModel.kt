package com.tokopedia.carouselproductcard

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig

internal data class CarouselProductCardModel(
        val productCardModel: ProductCardModel,
        val carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
        val blankSpaceConfig: BlankSpaceConfig = BlankSpaceConfig()
) {
    fun getOnItemClickListener() = carouselProductCardListenerInfo.onItemClickListener
    fun getOnItemAddToCartListener() = carouselProductCardListenerInfo.onItemAddToCartListener
    fun getOnItemImpressedListener() = carouselProductCardListenerInfo.onItemImpressedListener
    fun getOnItemThreeDotsClickListener() = carouselProductCardListenerInfo.onItemThreeDotsClickListener
}