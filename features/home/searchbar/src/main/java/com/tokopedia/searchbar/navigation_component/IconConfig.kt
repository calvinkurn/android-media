package com.tokopedia.searchbar.navigation_component

data class IconConfig(val iconList: List<IconToolbar>)

data class IconToolbar(val id: Int, val imageResLight: Int, val imageResDark: Int, val applink: String, var badgeCounter: Int = 0)