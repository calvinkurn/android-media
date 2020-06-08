package com.tokopedia.play_common.widget.playBannerCarousel.event

import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel

interface PlayBannerCarouselViewEventListener{
    fun onItemClick(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onItemImpress(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onOverlayImageBannerClick(dataModel: PlayBannerCarouselOverlayImageDataModel)
    fun onPromoBadgeClick(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onSeeMoreClick(dataModel: PlayBannerCarouselBannerDataModel, position: Int)
    fun onRefreshView(dataModel: PlayBannerCarouselDataModel)
}