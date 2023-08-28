package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.banner_product_group.ProductItemType
import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.VerticalBannerItemType

interface ShopBannerProductGroupListener {
    fun onBannerProductGroupMainBannerClick(mainBanner: ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data)
    fun onBannerProductGroupProductClick(selectedProduct: ProductItemType)
    fun onBannerProductGroupVerticalBannerClick(shopHomeProductCarouselVerticalBannerItemType: VerticalBannerItemType)
    fun onBannerProductGroupViewAllClick(ctaLink: String)
}
