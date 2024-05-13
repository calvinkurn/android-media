package com.tokopedia.homenav.mainnav.view.analytics

import com.tokopedia.searchbar.navigation_component.NavSource

internal object MainNavTrackingConst {
    const val GLOBAL_MENU = "global menu"

    // page Source section
    const val KEY_PAGE_SOURCE = "pageSource"
    private const val FORMAT_PAGE_SOURCE = "%s.%s.null.null"

    private const val PAGE_SOURCE_ACCOUNT = "account page"
    private const val PAGE_SOURCE_AFFILIATE = "affiliate page"
    private const val PAGE_SOURCE_UOH = "transaction"
    private const val PAGE_SOURCE_CART = "cart"
    private const val PAGE_SOURCE_CATALOG = "catalog page"
    private const val PAGE_SOURCE_CLP = "category page"
    private const val PAGE_SOURCE_DISCOVERY = "discopage"
    private const val PAGE_SOURCE_DT = "dilayani page"
    private const val PAGE_SOURCE_FEED = "feed"
    private const val PAGE_SOURCE_FEED_PROFILE = "feed profile"
    private const val PAGE_SOURCE_HOME = "homepage"
    private const val PAGE_SOURCE_MVC = "merchant voucher"
    private const val PAGE_SOURCE_NOTIF = "notification"
    private const val PAGE_SOURCE_PDP = "pdp"
    private const val PAGE_SOURCE_SHOP = "shoppage"
    private const val PAGE_SOURCE_SRP = "search result"
    private const val PAGE_SOURCE_SRP_UNIVERSAL = "srp universal"
    private const val PAGE_SOURCE_THANK_YOU = "thankyou page"
    private const val PAGE_SOURCE_TOKOFOOD = "food"
    private const val PAGE_SOURCE_TOKONOW = "now"
    private const val PAGE_SOURCE_WISHLIST = "wishlist"

    private const val PAGE_SOURCE_PATH_SOS = "sos"
    // end of page source section

    fun NavSource.asTrackingPageSource(pageSourcePath: String = ""): String {
        val parsedPath = if(this == NavSource.SOS)
            PAGE_SOURCE_PATH_SOS
        else pageSourcePath.ifEmpty { "null" }

        val pageSource = when(this) {
            NavSource.ACCOUNT -> PAGE_SOURCE_ACCOUNT
            NavSource.AFFILIATE -> PAGE_SOURCE_AFFILIATE
            NavSource.CART -> PAGE_SOURCE_CART
            NavSource.CATALOG -> PAGE_SOURCE_CATALOG
            NavSource.CLP -> PAGE_SOURCE_CLP
            NavSource.DISCOVERY,
            NavSource.SOS -> PAGE_SOURCE_DISCOVERY
            NavSource.DT -> PAGE_SOURCE_DT
            NavSource.FEED -> PAGE_SOURCE_FEED
            NavSource.HOME -> PAGE_SOURCE_HOME
            NavSource.HOME_WISHLIST,
            NavSource.WISHLIST -> PAGE_SOURCE_WISHLIST
            NavSource.HOME_UOH,
            NavSource.UOH -> PAGE_SOURCE_UOH
            NavSource.MVC -> PAGE_SOURCE_MVC
            NavSource.NOTIFICATION -> PAGE_SOURCE_NOTIF
            NavSource.PDP -> PAGE_SOURCE_PDP
            NavSource.SHOP -> PAGE_SOURCE_SHOP
            NavSource.SRP -> PAGE_SOURCE_SRP
            NavSource.SRP_UNIVERSAL -> PAGE_SOURCE_SRP_UNIVERSAL
            NavSource.THANKYOU -> PAGE_SOURCE_THANK_YOU
            NavSource.TOKOFOOD -> PAGE_SOURCE_TOKOFOOD
            NavSource.TOKONOW -> PAGE_SOURCE_TOKONOW
            NavSource.USER_PROFILE -> PAGE_SOURCE_FEED_PROFILE
            else -> "null"
        }
        return FORMAT_PAGE_SOURCE.format(pageSource, parsedPath)
    }
}
