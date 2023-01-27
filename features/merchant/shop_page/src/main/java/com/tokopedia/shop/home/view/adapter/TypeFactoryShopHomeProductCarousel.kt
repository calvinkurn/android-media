package com.tokopedia.shop.home.view.adapter

import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

interface TypeFactoryShopHomeProductCarousel {
    fun type(shopHomeProductViewModel: ShopHomeProductUiModel): Int
}
