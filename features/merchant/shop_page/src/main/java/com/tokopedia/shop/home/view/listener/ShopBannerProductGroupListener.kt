package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselProductCard
import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerVerticalBanner

interface ShopBannerProductGroupListener {
    fun onBannerProductGroupMainBannerClick(mainBanner: ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data)
    fun onBannerProductGroupProductClick(selectedProduct: ShopHomeProductCarouselProductCard)
    fun onBannerProductGroupVerticalBannerClick(shopHomeProductCarouselVerticalBanner: ShopHomeProductCarouselVerticalBannerVerticalBanner)
    fun onBannerProductGroupViewAllClick(ctaLink: String)
}
