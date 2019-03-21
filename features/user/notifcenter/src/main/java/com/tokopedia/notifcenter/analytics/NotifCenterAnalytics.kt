package com.tokopedia.notifcenter.analytics

import javax.inject.Inject
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * @author by nisie on 23/01/19.
 */
class NotifCenterAnalytics @Inject constructor() {
    val EVENT_CLICK_NOTIF_CENTER = "clickNotifCenter"
    val CATEGORY_NOTIF_CENTER = "notif center"

    fun trackClickList() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                "click on notif list",
                ""
        ))
    }

    fun trackClickFilter(filterName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                "click on filter request",
                filterName
        ))
    }

    fun trackScrollToBottom() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_NOTIF_CENTER,
                CATEGORY_NOTIF_CENTER,
                "scroll to bottom",
                ""
        ))
    }


}