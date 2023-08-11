package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.ProductCard
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel
import com.tokopedia.shop.home.view.model.VerticalBanner

interface ShopHomeProductCarouselListener {
    fun onProductCarouselMainBannerClick(mainBanner: ShopHomeProductCarouselUiModel.Tab.ComponentList.Data)
    fun onProductCarouselProductClick(selectedProduct: ProductCard)
    fun onProductCarouselVerticalBannerClick(verticalBanner: VerticalBanner)
    fun onProductCarouselChevronViewAllClick(ctaLink: String)
}
