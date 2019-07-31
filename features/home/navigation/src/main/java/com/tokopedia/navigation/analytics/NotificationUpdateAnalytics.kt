package com.tokopedia.navigation.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author : Steven 03/05/19
 */
class NotificationUpdateAnalytics @Inject constructor() {

    companion object {

        val SCREEN_NAME: String = "notif center"
        val EVENT_CATEGORY_NOTIF_CENTER: String = "notif center"
        val EVENT_NAME_CLICK_NOTIF_CENTER: String = "clickNotifCenter"

        val EVENT_ACTION_CLICK_NEWEST_INFO: String = "click on info terbaru"
        val EVENT_ACTION_CLICK_NOTIF_LIST: String = "click on notif list"
        val EVENT_ACTION_CLICK_FILTER_REQ: String = "click on filter request"
        val EVENT_ACTION_SCROLL_TO_BOTTOM: String = "scroll to bottom"
        val EVENT_ACTION_MARK_ALL_AS_READ: String = "mark all as read"

    }

    // #NC1
    fun trackClickNewestInfo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_NEWEST_INFO,
                ""
        ))
    }

    // #NC2
    fun trackClickNotifList(templateKey: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_NOTIF_LIST,
                templateKey
        ))
    }
    // #NC3
    fun trackClickFilterRequest(filter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_CLICK_FILTER_REQ,
                filter
        ))
    }
    // #NC4
    fun trackScrollBottom(notifSize: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_SCROLL_TO_BOTTOM,
                notifSize
        ))
    }
    // #NC5
    fun trackMarkAllAsRead(markAllReadCounter: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_NAME_CLICK_NOTIF_CENTER,
                EVENT_CATEGORY_NOTIF_CENTER,
                EVENT_ACTION_MARK_ALL_AS_READ,
                markAllReadCounter
        ))
    }
}