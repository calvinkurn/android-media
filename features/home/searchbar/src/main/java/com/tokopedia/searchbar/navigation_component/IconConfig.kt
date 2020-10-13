package com.tokopedia.searchbar.navigation_component

import android.graphics.drawable.Drawable

data class IconConfig(val iconList: List<IconToolbar>)

data class IconToolbar(val id: Int, val imageRes: Int, val applink: String, var badgeCounter: Int = 0)