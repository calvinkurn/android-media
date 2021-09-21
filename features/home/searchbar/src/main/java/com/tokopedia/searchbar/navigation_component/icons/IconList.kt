package com.tokopedia.searchbar.navigation_component.icons

import android.net.Uri
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.searchbar.R

internal interface IconConfigItem { fun get(
        pageSource: String = "",
        disableRouteManager: Boolean = false,
        disableDefaultGtmTracker: Boolean,
        onClick: ()-> Unit = {}): IconToolbar }

object IconList {
    val ID_INBOX = R.drawable.ic_searchbar_new_inbox

    const val ID_MESSAGE = IconUnify.MESSAGE
    const val ID_NOTIFICATION = IconUnify.BELL
    const val ID_CART = IconUnify.CART
    const val ID_NAV_GLOBAL = IconUnify.MENU_HAMBURGER
    const val ID_WISHLIST = IconUnify.HEART
    const val ID_SHARE = IconUnify.SHARE_MOBILE
    const val ID_SETTING = IconUnify.SETTING
    const val ID_SEARCH = IconUnify.SEARCH

    const val NAME_MESSAGE = "Inbox"
    const val NAME_NOTIFICATION = "Notif"
    const val NAME_CART = "Cart"
    const val NAME_NAV_GLOBAL = "Global Menu"
    const val NAME_WISHLIST = "Wishlist"
    const val NAME_SHARE = "Share"
    const val NAME_SETTING = "Setting"
    const val NAME_BACK_BUTTON = "Back Button"
    const val NAME_SEARCH_BAR = "Search Bar"
    const val NAME_SEARCH = "Search"

    const val ID_NAV_LOTTIE_WISHLIST = 91
    const val ID_NAV_ANIMATED_WISHLIST = 92

    const val SOURCE_INBOX = "review inbox"

    //Image icon
    internal object MessageIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_MESSAGE,
                    applink = getInboxApplink(),
                    disableRouteManager = disableRouteManager,
                    nonLoginApplink = ApplinkConst.LOGIN,
                    name = NAME_MESSAGE,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object InboxIcon: IconConfigItem {
        override fun get(
                pageSource: String, disableRouteManager: Boolean,
                disableDefaultGtmTracker: Boolean, onClick: ()-> Unit
        ): IconToolbar {
            return IconToolbar(
                    id = ID_INBOX,
                    imageRes = ID_INBOX,
                    applink = getInboxApplink(),
                    disableRouteManager = disableRouteManager,
                    nonLoginApplink = ApplinkConst.LOGIN,
                    name = NAME_MESSAGE,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object NotificationIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_NOTIFICATION,
                    applink = ApplinkConst.NOTIFICATION,
                    disableRouteManager = disableRouteManager,
                    nonLoginApplink = ApplinkConst.LOGIN,
                    name = NAME_NOTIFICATION,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object CartIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_CART,
                    applink = ApplinkConst.CART,
                    disableRouteManager = disableRouteManager,
                    nonLoginApplink = ApplinkConst.LOGIN,
                    name = NAME_CART,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object NavGlobalIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_NAV_GLOBAL,
                    applink = ApplinkConst.HOME_NAVIGATION,
                    disableRouteManager = disableRouteManager,
                    name = NAME_NAV_GLOBAL,
                    bundle = Bundle().run {
                        this.putString(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE, pageSource)
                        this
                    },
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object WishlistIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_WISHLIST,
                    applink = ApplinkConst.NEW_WISHLIST,
                    disableRouteManager = disableRouteManager,
                    nonLoginApplink = ApplinkConst.LOGIN,
                    name = NAME_WISHLIST,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object ShareIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_SHARE,
                    applink = "",
                    disableRouteManager = disableRouteManager,
                    name = NAME_SHARE,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object SettingIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_SETTING,
                    applink = ApplinkConstInternalGlobal.GENERAL_SETTING,
                    disableRouteManager = disableRouteManager,
                    name = NAME_SETTING,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object SearchGlobalIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_SEARCH,
                    applink = ApplinkConstInternalMarketplace.CHAT_SEARCH,
                    disableRouteManager = disableRouteManager,
                    name = NAME_SEARCH,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    //Lottie icon
    internal object LottieWishlistIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_NAV_LOTTIE_WISHLIST,
                    imageRes = R.raw.toolbar_lottie_wishlist,
                    applink = "",
                    iconType = IconToolbar.TYPE_LOTTIE,
                    disableRouteManager = disableRouteManager,
                    nonLoginApplink = ApplinkConst.LOGIN,
                    name = NAME_WISHLIST,
                    paddingEndRes = R.dimen.lottie_wishlist_padding_end,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    // Animated Vector Drawable Icon
    internal object AnimatedWishlistIcon: IconConfigItem {
        override fun get(pageSource: String, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: ()-> Unit): IconToolbar {
            return IconToolbar(
                    id = ID_WISHLIST,
                    imageRes = R.drawable.unify_wishlist_avd_new,
                    applink = "",
                    iconType = IconToolbar.TYPE_ANIMATED,
                    disableRouteManager = disableRouteManager,
                    nonLoginApplink = ApplinkConst.LOGIN,
                    name = NAME_WISHLIST,
                    paddingEndRes = R.dimen.lottie_wishlist_padding_end,
                    disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    private fun getInboxApplink(): String {
        return Uri.parse(ApplinkConst.INBOX).buildUpon().appendQueryParameter(ApplinkConst.Inbox.PARAM_SOURCE, SOURCE_INBOX).build().toString()
    }

}