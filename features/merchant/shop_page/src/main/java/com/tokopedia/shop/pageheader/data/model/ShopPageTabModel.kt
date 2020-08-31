package com.tokopedia.shop.pageheader.data.model

import androidx.fragment.app.Fragment

data class ShopPageTabModel(
        val tabTitle: String,
        val tabIcon: Int,
        val tabFragment: Fragment
)