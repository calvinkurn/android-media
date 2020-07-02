package com.tokopedia.carouselproductcard

import com.tokopedia.carouselproductcard.typeFactory.CarouselProductCardTypeFactory

internal interface BaseCarouselCardModel{
    fun areContentsTheSame(newItem: BaseCarouselCardModel): Boolean
    fun areItemsTheSame(newItem: BaseCarouselCardModel): Boolean
    fun type(typeFactory: CarouselProductCardTypeFactory):Int
}