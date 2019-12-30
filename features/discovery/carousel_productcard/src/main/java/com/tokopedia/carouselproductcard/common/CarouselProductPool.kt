package com.tokopedia.carouselproductcard.common

import com.tokopedia.carouselproductcard.CarouselProductCardAdapter

open class CarouselProductPool {
    internal var carouselAdapters: HashMap<String, CarouselProductCardAdapter> = hashMapOf()
    internal var carouselAdaptersPositions: HashMap<String, Int> = hashMapOf()

    fun release() {
        carouselAdapters.clear()
        carouselAdaptersPositions.clear()
    }
}