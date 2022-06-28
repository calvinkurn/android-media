package com.tokopedia.shop.pageheader.data.model

import androidx.fragment.app.Fragment

data class ShopPageTabModel(
        val tabTitle: String = "",
        var tabIconInactive: Int = 0,
        val tabIconActive: Int = 0,
        val tabFragment: Fragment = Fragment(),
        var iconUrl: String = "",
        var iconActiveUrl: String = "",
)