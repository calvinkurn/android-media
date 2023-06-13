package com.tokopedia.shop.common.view.listener

import com.tokopedia.shop.common.view.model.ShopPageFabConfig

interface InterfaceShopPageFab {
    fun shouldShowShopPageFab(): Boolean
    fun getShopPageFabConfig(): ShopPageFabConfig?
}
