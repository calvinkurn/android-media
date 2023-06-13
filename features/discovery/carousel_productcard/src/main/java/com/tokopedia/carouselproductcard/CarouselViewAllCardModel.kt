package com.tokopedia.carouselproductcard

import com.tokopedia.carouselproductcard.typeFactory.CarouselProductCardTypeFactory
import com.tokopedia.viewallcard.ViewAllCard

internal data class CarouselViewAllCardModel(
    val data: CarouselViewAllCardData = CarouselViewAllCardData(),
    var carouselProductCardListenerInfo: CarouselProductCardListenerInfo,

): BaseCarouselCardModel {
    override fun areContentsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselViewAllCardModel
    }

    override fun areItemsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselViewAllCardModel
    }

    override fun type(typeFactory: CarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getOnViewAllCardClickListener() = carouselProductCardListenerInfo.onViewAllCardClickListener
}

data class CarouselViewAllCardData(
    val title: String = "",
    val description: String = "",
    val titleIsInteger: Boolean = false,
    val viewAllCardMode: Int = ViewAllCard.MODE_NORMAL,
    val ctaText: String = ""
)
