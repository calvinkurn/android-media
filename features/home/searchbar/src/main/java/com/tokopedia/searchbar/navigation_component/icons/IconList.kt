package com.tokopedia.searchbar.navigation_component.icons

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.navigation_component.IconToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_CART
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_MESSAGE
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NAV_GLOBAL
import com.tokopedia.searchbar.navigation_component.icons.IconList.ID_NOTIFICATION

interface IconConfigItem { fun get(): IconToolbar }

object IconList {
    const val ID_MESSAGE = 0
    const val ID_NOTIFICATION = 1
    const val ID_CART = 2
    const val ID_NAV_GLOBAL = 3

    object MessageIcon: IconConfigItem {
        override fun get(): IconToolbar {
            return IconToolbar(
                    id = ID_MESSAGE,
                    imageRes = R.drawable.ic_home_nav_message_light,
                    applink = ApplinkConst.INBOX
            )
        }
    }

    object NotificationIcon: IconConfigItem {
        override fun get(): IconToolbar {
            return IconToolbar(
                    id = ID_NOTIFICATION,
                    imageRes = R.drawable.ic_home_nav_notification_light,
                    applink = ApplinkConst.NOTIFICATION
            )
        }
    }

    object CartIcon: IconConfigItem {
        override fun get(): IconToolbar {
            return IconToolbar(
                    id = ID_CART,
                    imageRes = R.drawable.ic_home_nav_cart_light,
                    applink = ApplinkConst.CART
            )
        }
    }

    object NavGlobalIcon: IconConfigItem {
        override fun get(): IconToolbar {
            return IconToolbar(
                    id = ID_NAV_GLOBAL,
                    imageRes = R.drawable.ic_home_nav_global_light,
                    applink = ""
            )
        }
    }
}