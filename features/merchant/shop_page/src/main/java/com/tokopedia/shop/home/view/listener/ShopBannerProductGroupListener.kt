package com.tokopedia.shop.home.view.listener

import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.banner_product_group.BannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.VerticalBannerItemType

interface ShopBannerProductGroupListener {
    fun onBannerProductGroupMainBannerClick(mainBanner: BannerProductGroupUiModel.Tab.ComponentList.Data)
    fun onBannerProductGroupProductClick(selectedProduct: ProductItemType)
    fun onBannerProductGroupVerticalBannerClick(shopHomeProductCarouselVerticalBannerItemType: VerticalBannerItemType)
    fun onBannerProductGroupViewAllClick(ctaLink: String)
}
