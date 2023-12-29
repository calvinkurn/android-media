package com.tokopedia.searchbar.navigation_component.icons

import android.net.Uri
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.navigation_component.NavSource

internal interface IconConfigItem {
    fun get(
        pageSource: NavSource = NavSource.DEFAULT,
        pageSourcePath: String? = null,
        disableRouteManager: Boolean = false,
        disableDefaultGtmTracker: Boolean,
        onClick: () -> Unit = {}
    ): IconToolbar
}

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
    const val ID_INFORMATION = IconUnify.INFORMATION
    const val ID_BILL = IconUnify.BILL
    const val ID_LIST_TRANSACTION = IconUnify.LIST_TRANSACTION
    const val ID_NOTEBOOK = IconUnify.NOTEBOOK
    const val ID_SHARE_AB_TEST = IconUnify.SHARE_AFFILIATE
    const val NAME_MESSAGE = "Inbox"
    const val NAME_NOTIFICATION = "Notif"
    const val NAME_CART = "Cart"
    const val NAME_NAV_GLOBAL = "Global Menu"
    const val NAME_WISHLIST = "Wishlist"
    const val NAME_SHARE = "Share"
    const val NAME_SHARE_AB = "Share AB"
    const val NAME_SETTING = "Setting"
    const val NAME_BACK_BUTTON = "Back Button"
    const val NAME_SEARCH_BAR = "Search Bar"
    const val NAME_SEARCH = "Search"
    const val NAME_INFORMATION = "Information"
    const val NAME_BILL = "Bill"
    const val NAME_LIST_TRANSACTION = "List Transaction"
    const val NAME_NOTEBOOK = "Notebook"

    const val ID_NAV_LOTTIE_WISHLIST = 91
    const val ID_NAV_ANIMATED_WISHLIST = 92

    const val SOURCE_INBOX = "review inbox"

    // Image icon
    internal object MessageIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
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

    internal object InboxIcon : IconConfigItem {
        override fun get(
            pageSource: NavSource,
            pageSourcePath: String?,
            disableRouteManager: Boolean,
            disableDefaultGtmTracker: Boolean,
            onClick: () -> Unit
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

    internal object NotificationIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
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

    internal object CartIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
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

    internal object NavGlobalIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
            return IconToolbar(
                id = ID_NAV_GLOBAL,
                applink = ApplinkConst.HOME_NAVIGATION,
                disableRouteManager = disableRouteManager,
                name = NAME_NAV_GLOBAL,
                bundle = Bundle().apply {
                    putString(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE, pageSource.name)
                    pageSourcePath?.let {
                        putString(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE_PATH, it)
                    }
                },
                disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object WishlistIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
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

    internal object ShareIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
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

    internal object SettingIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
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

    internal object SearchGlobalIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
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

    internal object InformationGlobalIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
            return IconToolbar(
                id = ID_INFORMATION,
                applink = "",
                disableRouteManager = disableRouteManager,
                name = NAME_INFORMATION,
                disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }
    internal object BillGlobalIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
            return IconToolbar(
                id = ID_BILL,
                applink = "",
                disableRouteManager = disableRouteManager,
                name = NAME_BILL,
                disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object ListTransactionIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
            return IconToolbar(
                id = ID_LIST_TRANSACTION,
                applink = "",
                disableRouteManager = disableRouteManager,
                name = NAME_LIST_TRANSACTION,
                disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    internal object NotebookIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
            return IconToolbar(
                id = ID_NOTEBOOK,
                applink = "",
                disableRouteManager = disableRouteManager,
                name = NAME_NOTEBOOK,
                disableDefaultGtmTracker = disableDefaultGtmTracker
            ) {
                onClick.invoke()
            }
        }
    }

    // Lottie icon
    internal object LottieWishlistIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
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
    internal object AnimatedWishlistIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
            return IconToolbar(
                id = ID_WISHLIST,
                imageRes = com.tokopedia.unifycomponents.R.drawable.unify_wishlist_avd_new,
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

    internal object ShareAbTestIcon : IconConfigItem {
        override fun get(pageSource: NavSource, pageSourcePath: String?, disableRouteManager: Boolean, disableDefaultGtmTracker: Boolean, onClick: () -> Unit): IconToolbar {
            return IconToolbar(
                id = ID_SHARE_AB_TEST,
                applink = "",
                disableRouteManager = disableRouteManager,
                name = NAME_SHARE_AB,
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
