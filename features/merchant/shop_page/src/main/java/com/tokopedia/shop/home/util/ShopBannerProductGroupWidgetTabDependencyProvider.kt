package com.tokopedia.shop.home.util

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle

interface ShopBannerProductGroupWidgetTabDependencyProvider {
    val currentShopId: String
    val productCarouselHostFragmentManager: FragmentManager
    val productCarouselHostLifecycle: Lifecycle
}
