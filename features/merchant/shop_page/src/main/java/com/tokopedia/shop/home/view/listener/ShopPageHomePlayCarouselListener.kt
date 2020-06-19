package com.tokopedia.shop.home.view.listener

import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.shop.home.view.model.ShopHomePlayCarouselUiModel

interface ShopPageHomePlayCarouselListener{
    fun onPlayBannerCarouselRefresh(shopHomePlayCarouselUiModel: ShopHomePlayCarouselUiModel, position: Int)
    fun onReminderClick(playBannerCarouselItemDataModel: PlayBannerCarouselItemDataModel, position: Int)
}