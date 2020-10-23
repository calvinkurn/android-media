package com.tokopedia.searchbar.navigation_component.icons

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.searchbar.R

internal interface IconConfigItem { fun get(disableRouteManager: Boolean = false, onClick: ()-> Unit = {}): IconToolbar }

object IconList {
    const val ID_MESSAGE = IconUnify.MESSAGE
    const val ID_NOTIFICATION = IconUnify.BELL
    const val ID_CART = IconUnify.CART
    const val ID_NAV_GLOBAL = IconUnify.MENU_HAMBURGER
    const val ID_WISHLIST = IconUnify.HEART
    const val ID_SHARE = IconUnify.SHARE_MOBILE

    const val ID_NAV_LOTTIE_WISHLIST = 91

    //Image icon
    internal object MessageIcon: IconConfigItem {
        override fun get(disableRouteManager: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_MESSAGE,
                    applink = ApplinkConst.INBOX,
                    disableRouteManager = disableRouteManager
            ) {
                onClick.invoke()
            }
        }
    }

    internal object NotificationIcon: IconConfigItem {
        override fun get(disableRouteManager: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_NOTIFICATION,
                    applink = ApplinkConst.NOTIFICATION,
                    disableRouteManager = disableRouteManager
            ) {
                onClick.invoke()
            }
        }
    }

    internal object CartIcon: IconConfigItem {
        override fun get(disableRouteManager: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_CART,
                    applink = ApplinkConst.CART,
                    disableRouteManager = disableRouteManager
            ) {
                onClick.invoke()
            }
        }
    }

    internal object NavGlobalIcon: IconConfigItem {
        override fun get(disableRouteManager: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_NAV_GLOBAL,
                    applink = "",
                    disableRouteManager = disableRouteManager
            ) {
                onClick.invoke()
            }
        }
    }

    internal object WishlistIcon: IconConfigItem {
        override fun get(disableRouteManager: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_WISHLIST,
                    applink = ApplinkConst.NEW_WISHLIST,
                    disableRouteManager = disableRouteManager
            ) {
                onClick.invoke()
            }
        }
    }

    internal object ShareIcon: IconConfigItem {
        override fun get(disableRouteManager: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_SHARE,
                    applink = "",
                    disableRouteManager = disableRouteManager
            ) {
                onClick.invoke()
            }
        }
    }

    //Lottie icon
    internal object LottieWishlistIcon: IconConfigItem {
        override fun get(disableRouteManager: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_NAV_LOTTIE_WISHLIST,
                    imageRes = R.raw.toolbar_lottie_wishlist,
                    applink = "",
                    iconType = IconToolbar.TYPE_LOTTIE,
                    disableRouteManager = disableRouteManager
            ) {
                onClick.invoke()
            }
        }
    }
}