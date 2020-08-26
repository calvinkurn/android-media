package com.tokopedia.play_common.widget.playBannerCarousel.event

import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel

interface PlayBannerCarouselViewEventListener{
    fun onItemClick(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onItemOverlayClick(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onItemImpress(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onOverlayImageBannerClick(dataModel: PlayBannerCarouselOverlayImageDataModel)
    fun onOverlayImageBannerImpress(dataModel: PlayBannerCarouselOverlayImageDataModel)
    fun onReminderClick(dataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onSeeMoreClick(dataModel: PlayBannerCarouselDataModel)
    fun onSeeMoreBannerClick(dataModel: PlayBannerCarouselBannerDataModel, position: Int)
    fun onRefreshView(dataModel: PlayBannerCarouselDataModel)
}