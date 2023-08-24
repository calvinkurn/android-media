package com.tokopedia.shop.home.util

import androidx.fragment.app.Fragment

interface ShopBannerProductGroupWidgetTabDependencyProvider {
    val currentShopId: String
    val fragment: Fragment
}
