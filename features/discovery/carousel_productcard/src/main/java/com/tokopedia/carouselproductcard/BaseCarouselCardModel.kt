package com.tokopedia.carouselproductcard

interface BaseCarouselCardModel{
    fun areContentsTheSame(newItem: BaseCarouselCardModel): Boolean
    fun areItemsTheSame(newItem: BaseCarouselCardModel): Boolean
}