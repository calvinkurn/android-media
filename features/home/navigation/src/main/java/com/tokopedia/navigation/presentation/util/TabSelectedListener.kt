package com.tokopedia.navigation.presentation.util

import com.tokopedia.navigation.GlobalNavAnalytics
import com.tokopedia.navigation.presentation.model.BottomNavFeedId
import com.tokopedia.navigation.presentation.model.BottomNavHomeId
import com.tokopedia.navigation.presentation.model.BottomNavOfficialStoreId
import com.tokopedia.navigation.presentation.model.BottomNavMePageId
import com.tokopedia.navigation.presentation.model.BottomNavTransactionId
import com.tokopedia.navigation.presentation.model.BottomNavWishlistId
import com.tokopedia.navigation.util.MainParentServerLogger
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

fun interface TabSelectedListener {
    
    fun onSelected(model: BottomNavBarUiModel)
}

fun createTabSelectedListener(
    processor: TabSelectedListener,
) = createTabSelectedListener(processor, true, { it })

fun createTabSelectedListener(
    processor: TabSelectedListener,
    initialCondition: Boolean,
    conditionToProcess: (Boolean) -> (Boolean),
    onProcessed: (Boolean) -> (Boolean) = { it },
): TabSelectedListener {
    var _condition = initialCondition
    return TabSelectedListener { model ->
        val condition = conditionToProcess(_condition)
        if (condition) processor.onSelected(model)
        _condition = onProcessed(_condition)
    }
}

class GlobalNavAnalyticsProcessor @Inject constructor(
    private val globalNavAnalytics: GlobalNavAnalytics,
    private val userSession: UserSessionInterface,
) : TabSelectedListener {

    override fun onSelected(model: BottomNavBarUiModel) {
        val pageName = when (model.uniqueId) {
            BottomNavHomeId -> "/"
            BottomNavOfficialStoreId -> "OS Homepage"
            BottomNavFeedId -> "Feed"
            BottomNavTransactionId -> "transaction page"
            BottomNavWishlistId -> "wishlist page"
            BottomNavMePageId -> "" //TODO("Tracker GTM for me page")
            else -> ""
        }

        val pageTitle = model.title

        globalNavAnalytics.eventBottomNavigationDrawer(
            pageName,
            pageTitle,
            userSession.userId
        )
    }
}

fun EmbraceNavAnalyticsProcessor() = TabSelectedListener {
    MainParentServerLogger.sendEmbraceBreadCrumb(it.title)
}

class VisitFeedProcessor @Inject constructor(
    private val globalNavAnalytics: GlobalNavAnalytics,
    private val userSession: UserSessionInterface,
) : TabSelectedListener {

    override fun onSelected(model: BottomNavBarUiModel) {
        globalNavAnalytics.userVisitsFeed(
            userSession.isLoggedIn().toString(),
            userSession.getUserId()
        )
    }
}
