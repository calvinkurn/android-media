package com.tokopedia.shop.pageheader.data.model

import androidx.fragment.app.Fragment

data class ShopPageTabModel(
        val tabTitle: String,
        val tabIconInactive: Int,
        val tabIconActive: Int,
        val tabFragment: Fragment
)