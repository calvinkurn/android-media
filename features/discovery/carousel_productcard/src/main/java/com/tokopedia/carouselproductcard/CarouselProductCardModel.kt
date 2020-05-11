package com.tokopedia.carouselproductcard

import com.tokopedia.productcard.ProductCardModel

internal data class CarouselProductCardModel(
        val productCardModel: ProductCardModel,
        var carouselProductCardListenerInfo: CarouselProductCardListenerInfo
) {
    fun getOnItemClickListener() = carouselProductCardListenerInfo.onItemClickListener
    fun getOnItemAddToCartListener() = carouselProductCardListenerInfo.onItemAddToCartListener
    fun getOnItemImpressedListener() = carouselProductCardListenerInfo.onItemImpressedListener
    fun getOnItemThreeDotsClickListener() = carouselProductCardListenerInfo.onItemThreeDotsClickListener
}