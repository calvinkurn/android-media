package com.tokopedia.navigation.presentation.model

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.FragmentConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.home.HomeInternalRouter
import com.tokopedia.navigation.presentation.activity.NewMainParentActivity
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavItemId
import com.tokopedia.navigation_common.ui.DiscoId

private const val DISCOVERY_APPLINK = "discovery_applink"
private const val DISCOVERY_APPLINK_TARGET = "tokopedia://discovery/sos"
private const val DISCOVERY_PAGE_SOURCE = "discovery_page_source"
private const val DISCOVERY_END_POINT = "end_point"
private const val DISCOVERY_SHOULD_SHOW_GLOBAL_NAV = "should_show_global_nav"
private const val DISCOVERY_ADDITIONAL_QUERY_PARAMS = "additional_query_params"
private const val DISCOVERY_SOS_END_POINT = "sos"

private const val PARAM_ACTIVITY_WISHLIST_COLLECTION = "activity_wishlist_collection"
private const val WISHLIST_COLLECTION_PAGE = "WishlistCollectionFragment"
private const val WISHLIST_SHOULD_SHOW_GLOBAL_NAV = "should_show_global_nav"

private const val UOH_SOURCE_FILTER_KEY = "source_filter"
private const val PARAM_ACTIVITY_ORDER_HISTORY = "activity_order_history"
private const val UOH_PAGE = "UohListFragment"
private const val UOH_SHOULD_SHOW_GLOBAL_NAV = "should_show_global_nav"

private const val PARAM_HOME = "home"

private const val KEY_DISCO_ID = "disco_id"
private const val KEY_SHOULD_SHOW_GLOBAL_NAV = "should_show_global_nav"
private const val KEY_QUERY_PARAMS = "query_params"

data class FragmentCreator(
    val requireLogin: Boolean,
    val create: FragmentManager.(AppCompatActivity, Bundle) -> Fragment
)

/**
 * Type
 */
val BottomNavHomeType = BottomNavBarItemType("home")
val BottomNavFeedType = BottomNavBarItemType("feed")
val BottomNavDiscoType = BottomNavBarItemType("discopage")
val BottomNavWishlistType = BottomNavBarItemType("wishlist")
val BottomNavTransactionType = BottomNavBarItemType("transaction")
val BottomNavMePageType = BottomNavBarItemType("me_page")

/**
 * Id
 */
val BottomNavHomeId = BottomNavItemId(BottomNavHomeType)
val BottomNavFeedId = BottomNavItemId(BottomNavFeedType)
val BottomNavOfficialStoreId = BottomNavItemId(BottomNavDiscoType, DiscoId(DISCOVERY_SOS_END_POINT))
val BottomNavWishlistId = BottomNavItemId(BottomNavWishlistType)
val BottomNavTransactionId = BottomNavItemId(BottomNavTransactionType)
val BottomNavMePageId = BottomNavItemId(BottomNavMePageType)

/**
 * Creator
 */
val HomeFragmentCreator = FragmentCreator(
    requireLogin = false
) { activity, args ->
    val intent = activity.intent
    HomeInternalRouter.getHomeFragment(
        intent.getBooleanExtra(NewMainParentActivity.SCROLL_RECOMMEND_LIST, false),
        args.shouldShowGlobalNav
    )
}

val FeedFragmentCreator = FragmentCreator(
    requireLogin = false
) { activity, _ ->
    RouteManager.instantiateFragment(activity, FragmentConst.FEED_PLUS_CONTAINER_FRAGMENT, activity.intent.extras)
}

val DiscoFragmentCreator = FragmentCreator(
    requireLogin = false
) { activity, args ->
    val extras = activity.intent.extras ?: Bundle()
    with (extras) {
        putString(DISCOVERY_APPLINK, DISCOVERY_APPLINK_TARGET)
        putString(DISCOVERY_PAGE_SOURCE, PARAM_HOME)
        putString(DISCOVERY_END_POINT, args.discoId.value)
        putBoolean(DISCOVERY_SHOULD_SHOW_GLOBAL_NAV, args.shouldShowGlobalNav)
        putString(DISCOVERY_ADDITIONAL_QUERY_PARAMS, args.queryParams)
    }
    RouteManager.instantiateFragment(activity, FragmentConst.DISCOVERY_FRAGMENT, extras)
}

val WishlistFragmentCreator = FragmentCreator(
    requireLogin = true
) { activity, args ->
    val extras = activity.intent.extras ?: Bundle()
    with (extras) {
        putString(PARAM_ACTIVITY_WISHLIST_COLLECTION, PARAM_HOME)
        putString(WISHLIST_COLLECTION_PAGE, activity::class.java.simpleName)
        putBoolean(WISHLIST_SHOULD_SHOW_GLOBAL_NAV, args.shouldShowGlobalNav)
    }
    RouteManager.instantiateFragment(activity, FragmentConst.WISHLIST_COLLECTION_FRAGMENT, extras)
}

val TransactionFragmentCreator = FragmentCreator(
    requireLogin = true
) { activity, args ->
    val extras = activity.intent.extras ?: Bundle()
    with (extras) {
        putString(UOH_SOURCE_FILTER_KEY, "")
        putString(PARAM_ACTIVITY_ORDER_HISTORY, PARAM_HOME)
        putString(UOH_PAGE, activity::class.java.simpleName)
        putBoolean(UOH_SHOULD_SHOW_GLOBAL_NAV, args.shouldShowGlobalNav)
    }
    RouteManager.instantiateFragment(activity, FragmentConst.UOH_LIST_FRAGMENT, extras)
}

val ProfileFragmentCreator = FragmentCreator(
    requireLogin = false
)  { activity, _ ->
    val extras = activity.intent.extras ?: Bundle()
    with (extras) {
        putString(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE, "HOME")
        putString(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE_PATH, "")
        putBoolean(ApplinkConsInternalNavigation.PARAM_ACT_AS_ACCOUNT_PAGE, true)
    }
    RouteManager.instantiateFragment(activity, FragmentConst.HOME_NAV_FRAGMENT, extras)
}

/**
 * Mapper
 */
val supportedMainFragments = mapOf(
    BottomNavHomeType to HomeFragmentCreator,
    BottomNavFeedType to FeedFragmentCreator,
    BottomNavDiscoType to DiscoFragmentCreator,
    BottomNavWishlistType to WishlistFragmentCreator,
    BottomNavTransactionType to TransactionFragmentCreator,
    BottomNavMePageType to ProfileFragmentCreator,
)

/**
 * Bundle Util
 */
internal fun Bundle.putDiscoId(discoId: DiscoId) {
    putString(KEY_DISCO_ID, discoId.value)
}

private val Bundle.discoId: DiscoId
    get() = DiscoId(getString(KEY_DISCO_ID, ""))

internal fun Bundle.putShouldShowGlobalNav(shouldShowGlobalNav: Boolean) {
    putBoolean(KEY_SHOULD_SHOW_GLOBAL_NAV, shouldShowGlobalNav)
}

private val Bundle.shouldShowGlobalNav: Boolean
    get() = getBoolean(KEY_SHOULD_SHOW_GLOBAL_NAV, true)

internal fun Bundle.putQueryParams(queryParams: String) {
    putString(KEY_QUERY_PARAMS, queryParams)
}

private val Bundle.queryParams: String
    get() = getString(KEY_QUERY_PARAMS, "")
