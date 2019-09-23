package com.tokopedia.searchbar.util

import com.tokopedia.track.TrackApp

/**
 * Created by Ade Fulki on 2019-09-17.
 * ade.hadian@tokopedia.com
 */

class NotifAnalytics {

    fun trackClickGimmickNotif() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_TOP_NAV,
                CATEGORY_TOP_NAV,
                ACTION_CLICK_NOTIF,
                ""
        )
    }

    fun trackImpressionOnGimmickNotif() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_VIEW_TOP_NAV,
                CATEGORY_TOP_NAV,
                ACTION_IMPRESSION_NOTIF,
                ""
        )
    }

    companion object {
        val EVENT_CLICK_TOP_NAV = "clickTopNav"
        val EVENT_VIEW_TOP_NAV = "viewTopNav"

        val CATEGORY_TOP_NAV = "top nav"

        val ACTION_IMPRESSION_NOTIF = "impression on notification icon - with red dot - nonlogin"
        val ACTION_CLICK_NOTIF = "click on notification icon - with red dot - nonlogin"
    }
}