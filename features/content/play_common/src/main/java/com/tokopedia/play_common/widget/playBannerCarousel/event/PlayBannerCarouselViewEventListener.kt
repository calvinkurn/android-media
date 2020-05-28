package com.tokopedia.play_common.widget.playBannerCarousel.event

import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel

interface PlayBannerCarouselViewEventListener{
    fun onItemClick(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onItemImpress(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onPromoBadgeClick(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onSeeMoreClick(applink: String)
}