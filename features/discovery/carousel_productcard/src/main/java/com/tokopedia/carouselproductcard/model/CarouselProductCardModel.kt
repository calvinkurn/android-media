package com.tokopedia.carouselproductcard.model

import com.tokopedia.carouselproductcard.CarouselProductCardListenerInfo
import com.tokopedia.productcard.v2.ProductCardModel

internal data class CarouselProductCardModel(
        val productCardModel: ProductCardModel,
        val carouselProductCardListenerInfo: CarouselProductCardListenerInfo
) {
    fun getOnItemClickListener() = carouselProductCardListenerInfo.onItemClickListener
    fun getOnItemAddToCartListener() = carouselProductCardListenerInfo.onItemAddToCartListener
    fun getOnItemImpressedListener() = carouselProductCardListenerInfo.onItemImpressedListener
    fun getOnItemLongClickListener() = carouselProductCardListenerInfo.onItemLongClickListener
    fun getOnWishlistItemClickListener() = carouselProductCardListenerInfo.onWishlistItemClickListener
}