package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomePlayCarouselUiModel

interface ShopPageHomePlayCarouselListener{
    fun onPlayBannerCarouselRefresh(shopHomePlayCarouselUiModel: ShopHomePlayCarouselUiModel, position: Int)
}