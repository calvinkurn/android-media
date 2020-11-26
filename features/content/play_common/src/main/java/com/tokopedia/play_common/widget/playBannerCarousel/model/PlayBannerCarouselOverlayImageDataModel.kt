package com.tokopedia.play_common.widget.playBannerCarousel.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.BasePlayBannerCarouselModel
import com.tokopedia.play_common.widget.playBannerCarousel.typeFactory.PlayBannerCarouselTypeFactory

class PlayBannerCarouselOverlayImageDataModel(
        val applink: String,
        val imageUrl: String
) : BasePlayBannerCarouselModel, ImpressHolder(){
    override fun type(typeFactory: PlayBannerCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getId(): Any {
        return applink
    }

    override fun equalsWith(other: BasePlayBannerCarouselModel): Boolean {
        return other is PlayBannerCarouselOverlayImageDataModel && imageUrl == other.imageUrl
    }
}