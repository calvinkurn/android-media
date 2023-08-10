package com.tokopedia.shop.home.view.listener

import androidx.fragment.app.Fragment
import com.tokopedia.shop.home.view.model.Product
import com.tokopedia.shop.home.view.model.ShopHomeProductCarouselUiModel

interface ShopHomeProductCarouselListener {
    fun onProductCarouselMainBannerClick(mainBanner: ShopHomeProductCarouselUiModel.Tab.Component.ComponentChild)
    fun onProductCarouselProductClick(selectedProduct: Product)

    val fragment: Fragment

    val currentShopId: String
}
