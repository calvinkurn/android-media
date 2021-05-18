package com.tokopedia.shop.pageheader.data.model

import androidx.fragment.app.Fragment

data class ShopPageTabModel(
        val tabTitle: String,
        var tabIconInactive: Int,
        val tabIconActive: Int,
        val tabFragment: Fragment
)