package com.tokopedia.play_common.widget.playBannerCarousel.model

import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeFactory

data class PlayBannerCarouselAddStoryDataModel(
        val profileUrl: String,
        val backgroundUrl: String,
        val content: String,
        val applink: String
): BasePlayBannerCarouselModel{
    override fun type(typeFactory: PlayBannerCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }

}