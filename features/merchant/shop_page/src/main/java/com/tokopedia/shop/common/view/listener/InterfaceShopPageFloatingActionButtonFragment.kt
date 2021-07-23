package com.tokopedia.shop.common.view.listener

import com.tokopedia.shop.common.view.model.ShopPageBottomEndFabConfig

interface InterfaceShopPageFloatingActionButtonFragment {
    fun shouldShowBottomEndFab(): Boolean
    fun getBottomEndFabConfig(): ShopPageBottomEndFabConfig?
}