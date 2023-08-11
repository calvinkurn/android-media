package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.Product
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel

interface ShopHomeProductCarouselListener {
    fun onProductCarouselMainBannerClick(mainBanner: ShopHomeProductCarouselUiModel.Tab.ComponentList.Data)
    fun onProductCarouselProductClick(selectedProduct: Product)
    fun onProductCarouselVerticalBannerClick(verticalBanner: Product)
}
