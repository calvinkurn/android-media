package com.tokopedia.feed_shop.shop.view

import com.tokopedia.shop.common.view.model.ShopPageFabConfig

interface InterfaceShopPageFab {
    fun shouldShowShopPageFab(): Boolean
    fun getShopPageFabConfig(): ShopPageFabConfig?
}