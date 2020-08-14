package com.tokopedia.shop.home.view.listener

import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel
import com.tokopedia.shop.home.view.model.ShopHomePlayCarouselUiModel

interface ShopPageHomePlayCarouselListener{
    fun onPlayBannerCarouselRefresh(shopHomePlayCarouselUiModel: ShopHomePlayCarouselUiModel, position: Int)
    fun onReminderClick(playBannerCarouselItemDataModel: PlayBannerCarouselItemDataModel, position: Int)
    fun onPlayBannerSeeMoreClick(appLink: String)
    fun onPlayBannerImpressed(dataModel: PlayBannerCarouselItemDataModel, autoPlay: String, widgetId: String, widgetPosition: Int, position: Int)
    fun onPlayBannerClicked(dataModel: PlayBannerCarouselItemDataModel, autoPlay: String, widgetId: String, widgetPosition: Int, position: Int)
    fun onPlayLeftBannerImpressed(dataModel: PlayBannerCarouselOverlayImageDataModel, widgetId: String, position: Int)
    fun onPlayLeftBannerClicked(dataModel: PlayBannerCarouselOverlayImageDataModel, widgetId: String, position: Int)
}