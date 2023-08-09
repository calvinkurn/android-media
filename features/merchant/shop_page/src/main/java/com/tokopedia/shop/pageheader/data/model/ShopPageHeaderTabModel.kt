package com.tokopedia.shop.pageheader.data.model

import androidx.fragment.app.Fragment

data class ShopPageHeaderTabModel(
    val tabTitle: String = "",
    var tabIconInactive: Int = 0,
    val tabIconActive: Int = 0,
    val tabFragment: Fragment = Fragment(),
    val iconUrl: String = "",
    val iconActiveUrl: String = "",
    val isFocus: Boolean = false,
    val isDefault: Boolean = false,
    val chipsWording: String = "",
    val shareWording: String = "",
    val tabPathUrl: String = ""
)
