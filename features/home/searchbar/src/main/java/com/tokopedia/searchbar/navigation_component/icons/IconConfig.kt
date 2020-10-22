package com.tokopedia.searchbar.navigation_component.icons

data class IconConfig(val iconList: List<IconToolbar>)

data class IconToolbar(val id: Int, val imageRes: Int, val applink: String, var badgeCounter: Int = 0, val iconType: Int = TYPE_IMAGE, val onIconClicked: ()->Unit) {
    companion object {
        val TYPE_IMAGE = 0
        val TYPE_LOTTIE = 1
    }
}