package com.tokopedia.notifcenter.analytics

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker
import javax.inject.Inject

/**
 * @author by nisie on 23/01/19.
 */
class NotifCenterAnalytics @Inject constructor(private val analyticTracker: AnalyticTracker) {
    val EVENT_CLICK_NOTIF_CENTER = "clickNotifCenter"
    val CATEGORY_NOTIF_CENTER = "notif center"

    fun trackClickList() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                "click on notif list",
                ""
        )
    }

    fun trackClickFilter(filterName: String) {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                "click on filter request",
                filterName
        )
    }

    fun trackScrollToBottom() {
        analyticTracker.sendEventTracking(
                EVENT_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                "scroll to bottom",
                ""
        )
    }


}