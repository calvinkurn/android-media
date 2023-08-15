package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselProductCardVerticalBanner
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselVerticalBannerVerticalBanner

interface ShopHomeProductCarouselListener {
    fun onProductCarouselMainBannerClick(mainBanner: ShopHomeProductCarouselUiModel.Tab.ComponentList.Data)
    fun onProductCarouselProductClick(selectedProduct: ShopHomeProductCarouselProductCardVerticalBanner)
    fun onProductCarouselVerticalBannerClick(shopHomeProductCarouselVerticalBanner: ShopHomeProductCarouselVerticalBannerVerticalBanner)
    fun onProductCarouselChevronViewAllClick(ctaLink: String)
}
