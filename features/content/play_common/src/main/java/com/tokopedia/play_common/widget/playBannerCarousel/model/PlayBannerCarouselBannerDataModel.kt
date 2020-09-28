package com.tokopedia.play_common.widget.playBannerCarousel.model

import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeFactory

class PlayBannerCarouselBannerDataModel(
        val applink: String = "",
        val imageUrl: String = ""
) : BasePlayBannerCarouselModel {
    override fun type(typeFactory: PlayBannerCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getId(): Any {
        return applink
    }

    override fun equalsWith(other: BasePlayBannerCarouselModel): Boolean {
        return other is PlayBannerCarouselBannerDataModel && other.applink == applink && other.imageUrl == imageUrl
    }
}