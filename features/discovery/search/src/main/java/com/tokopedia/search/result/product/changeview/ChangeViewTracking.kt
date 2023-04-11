package com.tokopedia.search.result.product.changeview

import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object ChangeViewTracking {

    private const val GRID_MENU = "grid menu"
    private const val CLICK_CHANGE_VIEW = "click - "

    @JvmStatic
    fun eventSearchResultChangeView(viewTypeName: String, screenName: String) {
        val eventAction = CLICK_CHANGE_VIEW + viewTypeName
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                SearchEventTracking.Event.SEARCH_RESULT,
                GRID_MENU,
                eventAction,
                screenName,
            )
        )
    }
}
