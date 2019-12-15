package com.tokopedia.carouselproductcard.model

import com.tokopedia.carouselproductcard.CarouselProductCardListenerInfo
import com.tokopedia.productcard.v2.ProductCardModel

data class CarouselProductCardModel(
        val productCardModel: ProductCardModel,
        val carouselProductCardListenerInfo: CarouselProductCardListenerInfo
)