package com.tokopedia.navigation.presentation.model

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.FragmentConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.HomeInternalRouter
import com.tokopedia.navigation.presentation.activity.NewMainParentActivity
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavItemId
import com.tokopedia.navigation_common.ui.DiscoId

private const val DISCOVERY_APPLINK = "discovery_applink"
private const val DISCOVERY_APPLINK_TARGET = "tokopedia://discovery/sos"
private const val DISCOVERY_PAGE_SOURCE = "discovery_page_source"
private const val DISCOVERY_END_POINT = "end_point"
private const val DISCOVERY_SOS_END_POINT = "sos"

private const val PARAM_ACTIVITY_WISHLIST_COLLECTION = "activity_wishlist_collection"
private const val WISHLIST_COLLECTION_PAGE = "WishlistCollectionFragment"

private const val UOH_SOURCE_FILTER_KEY = "source_filter"
private const val PARAM_ACTIVITY_ORDER_HISTORY = "activity_order_history"
private const val UOH_PAGE = "UohListFragment"

private const val PARAM_HOME = "home"



typealias FragmentCreator = FragmentManager.(AppCompatActivity, DiscoId) -> Fragment

val BottomNavHomeType = BottomNavBarItemType("home")
val BottomNavFeedType = BottomNavBarItemType("feed")
val BottomNavDiscoType = BottomNavBarItemType("discopage")
val BottomNavWishlistType = BottomNavBarItemType("wishlist")
val BottomNavTransactionType = BottomNavBarItemType("transaction")

val BottomNavHomeId = BottomNavItemId(BottomNavHomeType)
val BottomNavFeedId = BottomNavItemId(BottomNavFeedType)
val BottomNavOfficialStoreId = BottomNavItemId(BottomNavDiscoType, DiscoId(DISCOVERY_SOS_END_POINT))
val BottomNavWishlistId = BottomNavItemId(BottomNavWishlistType)
val BottomNavTransactionId = BottomNavItemId(BottomNavTransactionType)

val HomeFragmentCreator: FragmentCreator = { activity, _ ->
    val intent = activity.intent
    HomeInternalRouter.getHomeFragment(intent.getBooleanExtra(NewMainParentActivity.SCROLL_RECOMMEND_LIST, false))
}
val FeedFragmentCreator: FragmentCreator = { activity, _ ->
    RouteManager.instantiateFragment(activity, FragmentConst.FEED_PLUS_CONTAINER_FRAGMENT, activity.intent.extras)
}
val DiscoFragmentCreator: FragmentCreator = { activity, discoId ->
    val extras = activity.intent.extras ?: Bundle()
    with (extras) {
        putString(DISCOVERY_APPLINK, DISCOVERY_APPLINK_TARGET)
        putString(DISCOVERY_PAGE_SOURCE, PARAM_HOME)
        putString(DISCOVERY_END_POINT, discoId.value)
    }
    RouteManager.instantiateFragment(activity, FragmentConst.DISCOVERY_FRAGMENT, extras)
}
val WishlistFragmentCreator: FragmentCreator = { activity, _ ->
    val extras = activity.intent.extras ?: Bundle()
    with (extras) {
        putString(PARAM_ACTIVITY_WISHLIST_COLLECTION, PARAM_HOME)
        putString(WISHLIST_COLLECTION_PAGE, activity::class.java.simpleName)
    }
    RouteManager.instantiateFragment(activity, FragmentConst.WISHLIST_COLLECTION_FRAGMENT, extras)
}
val TransactionFragmentCreator: FragmentCreator = { activity, _ ->
    val extras = activity.intent.extras ?: Bundle()
    with (extras) {
        putString(UOH_SOURCE_FILTER_KEY, "")
        putString(PARAM_ACTIVITY_ORDER_HISTORY, PARAM_HOME)
        putString(UOH_PAGE, activity::class.java.simpleName)
    }
    RouteManager.instantiateFragment(activity, FragmentConst.UOH_LIST_FRAGMENT, extras)
}

val supportedMainFragments = mapOf(
    BottomNavHomeType to HomeFragmentCreator,
    BottomNavFeedType to FeedFragmentCreator,
    BottomNavDiscoType to DiscoFragmentCreator,
    BottomNavWishlistType to WishlistFragmentCreator,
    BottomNavTransactionType to TransactionFragmentCreator,
)
