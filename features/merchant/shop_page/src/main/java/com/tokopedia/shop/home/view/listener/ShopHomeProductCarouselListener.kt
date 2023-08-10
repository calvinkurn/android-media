package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel

interface ShopHomeProductCarouselListener {
    fun onMainBannerClick(mainBanner: ShopHomeProductCarouselUiModel.Tab.Component)
    fun onProductClick(selectedProduct: ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild)
}
