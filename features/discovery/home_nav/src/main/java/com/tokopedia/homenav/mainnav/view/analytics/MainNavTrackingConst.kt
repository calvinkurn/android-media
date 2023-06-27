package com.tokopedia.homenav.mainnav.view.analytics

import com.tokopedia.applink.internal.ApplinkConsInternalNavigation

internal object MainNavTrackingConst {
    const val CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer"
    const val GLOBAL_MENU = "global menu"
    const val KEY_PAGE_SOURCE = "pageSource"
    const val FORMAT_PAGE_SOURCE = "%s.null.null.null"

    private const val PAGE_SOURCE_HOME = "Homepage"
    private const val PAGE_SOURCE_ACCOUNT = "Account page"
    private const val PAGE_SOURCE_UOH = "Order History"
    private const val PAGE_SOURCE_WISHLIST = "Wishlist"
    private const val PAGE_SOURCE_DISCOVERY = "Discovery page"
    private const val PAGE_SOURCE_PDP = "PDP"
    private const val PAGE_SOURCE_SRP = "Search Result Page"
    private const val PAGE_SOURCE_SHOP = "ShopPage"
    private const val PAGE_SOURCE_FEED = "Feed"
    private const val PAGE_SOURCE_CART = "Cart"
    private const val PAGE_SOURCE_NOTIF = "Notification Page"
    private const val PAGE_SOURCE_THANK_YOU = "Thank you"
    private const val PAGE_SOURCE_SOS = "Serbu Official Store"

    fun String.asTrackingPageSource(): String {
        return when(this) {
            ApplinkConsInternalNavigation.SOURCE_HOME -> PAGE_SOURCE_HOME
            ApplinkConsInternalNavigation.SOURCE_ACCOUNT -> PAGE_SOURCE_ACCOUNT
            ApplinkConsInternalNavigation.SOURCE_HOME_UOH -> PAGE_SOURCE_UOH
            ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST,
            ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_V2,
            ApplinkConsInternalNavigation.SOURCE_HOME_WISHLIST_COLLECTION -> PAGE_SOURCE_WISHLIST
            ApplinkConsInternalNavigation.SOURCE_HOME_SOS -> PAGE_SOURCE_SOS
            else -> this
        }
    }
}
