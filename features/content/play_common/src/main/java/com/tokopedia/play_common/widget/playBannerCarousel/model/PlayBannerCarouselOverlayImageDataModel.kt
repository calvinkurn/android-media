package com.tokopedia.play_common.widget.playBannerCarousel.model

import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeFactory

class PlayBannerCarouselOverlayImageDataModel(
        val applink: String,
        val weblink: String,
        val imageUrl: String
) : BasePlayBannerCarouselModel{
    override fun type(typeFactory: PlayBannerCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }
}