package com.tokopedia.play_common.widget.playBannerCarousel.typeFactory

interface BasePlayBannerCarouselModel{
    fun type(typeFactory: PlayBannerCarouselTypeFactory): Int
    fun getId(): Any
    fun equalsWith(other: BasePlayBannerCarouselModel): Boolean
}