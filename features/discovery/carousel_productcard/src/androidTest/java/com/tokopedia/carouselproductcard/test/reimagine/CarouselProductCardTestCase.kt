package com.tokopedia.carouselproductcard.test.reimagine

import com.tokopedia.carouselproductcard.reimagine.viewallcard.CarouselProductCardViewAllCardModel
import com.tokopedia.productcard.reimagine.ProductCardModel

data class CarouselProductCardTestCase(
    val productCardModelList: List<ProductCardModel>,
    val viewAllCard: CarouselProductCardViewAllCardModel? = null
)
