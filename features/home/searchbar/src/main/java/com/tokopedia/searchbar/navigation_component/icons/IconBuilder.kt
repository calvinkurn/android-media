package com.tokopedia.searchbar.navigation_component.icons

class IconBuilder {
    private val listIcon = mutableListOf<IconToolbar>()

    internal fun build(): IconConfig {
        return IconConfig(listIcon)
    }

    //image icon
    fun addIcon(iconId: Int, disableRouteManager: Boolean = false, onClick: ()->Unit): IconBuilder {
        when(iconId) {
            //image
            IconList.ID_MESSAGE -> listIcon.add(IconList.MessageIcon.get(disableRouteManager, onClick))
            IconList.ID_WISHLIST -> listIcon.add(IconList.WishlistIcon.get(disableRouteManager, onClick))
            IconList.ID_SHARE -> listIcon.add(IconList.ShareIcon.get(disableRouteManager, onClick))
            IconList.ID_CART -> listIcon.add(IconList.CartIcon.get(disableRouteManager, onClick))
            IconList.ID_NOTIFICATION -> listIcon.add(IconList.NotificationIcon.get(disableRouteManager, onClick))
            IconList.ID_NAV_GLOBAL -> listIcon.add(IconList.NavGlobalIcon.get(disableRouteManager, onClick))

            //lottiee
            IconList.ID_NAV_LOTTIE_WISHLIST -> listIcon.add(IconList.LottieWishlistIcon.get(disableRouteManager, onClick))
        }
        return this
    }
}

internal data class IconConfig(val iconList: MutableList<IconToolbar>)

internal data class IconToolbar(val id: Int, val imageRes: Int? = null, val applink: String, var badgeCounter: Int = 0, var disableRouteManager: Boolean = false, val iconType: Int = TYPE_IMAGE, val onIconClicked: ()->Unit) {
    companion object {
        val TYPE_IMAGE = 0
        val TYPE_LOTTIE = 1
    }
}