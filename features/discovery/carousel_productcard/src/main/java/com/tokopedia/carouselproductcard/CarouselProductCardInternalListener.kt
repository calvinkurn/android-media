package com.tokopedia.carouselproductcard

import com.tokopedia.productcard.ProductCardLifecycleObserver

internal interface CarouselProductCardInternalListener {

    val productCardLifecycleObserver: ProductCardLifecycleObserver?
}