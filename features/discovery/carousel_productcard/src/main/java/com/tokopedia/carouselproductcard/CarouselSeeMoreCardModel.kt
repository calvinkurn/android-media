package com.tokopedia.carouselproductcard

import com.tokopedia.carouselproductcard.typeFactory.CarouselProductCardTypeFactory

internal data class CarouselSeeMoreCardModel(
        var carouselProductCardListenerInfo: CarouselProductCardListenerInfo
) : BaseCarouselCardModel{
    override fun areContentsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselSeeMoreCardModel
    }

    override fun areItemsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselSeeMoreCardModel
    }

    override fun type(typeFactory: CarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getOnSeeMoreClickListener() = carouselProductCardListenerInfo.onSeeMoreClickListener
}