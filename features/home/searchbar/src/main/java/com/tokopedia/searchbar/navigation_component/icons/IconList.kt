package com.tokopedia.searchbar.navigation_component.icons

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.searchbar.R

internal interface IconConfigItem { fun get(onClick: ()-> Unit = {}): IconToolbar }

object IconList {
    const val ID_MESSAGE = 0
    const val ID_NOTIFICATION = 1
    const val ID_CART = 2
    const val ID_NAV_GLOBAL = 3

    const val ID_NAV_LOTTIE_WISHLIST = 91

    object MessageIcon: IconConfigItem {
        override fun get(onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_MESSAGE,
                    imageRes = R.drawable.ic_home_nav_message_light,
                    applink = ApplinkConst.INBOX
            ) {
                onClick.invoke()
            }
        }
    }

    object NotificationIcon: IconConfigItem {
        override fun get(onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_NOTIFICATION,
                    imageRes = R.drawable.ic_home_nav_notification_light,
                    applink = ApplinkConst.NOTIFICATION
            ) {
                onClick.invoke()
            }
        }
    }

    object CartIcon: IconConfigItem {
        override fun get(onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_CART,
                    imageRes = R.drawable.ic_home_nav_cart_light,
                    applink = ApplinkConst.CART
            ) {
                onClick.invoke()
            }
        }
    }

    object NavGlobalIcon: IconConfigItem {
        override fun get(onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_NAV_GLOBAL,
                    imageRes = R.drawable.ic_home_nav_global_light,
                    applink = ""
            ) {
                onClick.invoke()
            }
        }
    }

    object LottieWishlistIcon: IconConfigItem {
        override fun get(onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_NAV_LOTTIE_WISHLIST,
                    imageRes = R.raw.toolbar_lottie_wishlist,
                    applink = "",
                    iconType = IconToolbar.TYPE_LOTTIE
            ) {
                onClick.invoke()
            }
        }
    }
}