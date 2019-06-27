package com.tokopedia.notifications.common

import android.content.Context
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author lalit.singh
 */

object PersistentEvent {

    const val EVENT_VIEW_NOTIFICATION = "viewNotifications"
    const val EVENT_ACTION_PUSH_RECEIVED = "push received"

    const val EVENT_ACTION_LOGO_CLICK = "click tokopedia logo"

    const val EVENT = "openPushNotification"
    const val EVENT_LABEL = "-"

    const val EVENT_CATEGORY = "cm_persistent_push"
    const val EVENT_ACTION_CANCELED = "click close button"

    const val EVENT_ACTION_PERISTENT_CLICK = "Click_Action_Persistent"
}

object IrisAnalyticsEvents {
    private const val PUSH_RECEIVED = "push_received"
    const val PUSH_CLICKED = "push_clicked"
    const val PUSH_DISMISSED = "push_dismissed"

    @JvmStatic
    fun sendPushReceiveEvent(context: Context, baseNotificationModel: BaseNotificationModel) {

    }

    @JvmStatic
    fun sendPushEvent(context: Context, eventName: String, campaignID: String, notificationID: String) {
    }
}

object CMEvents {

    private val TAG = CMEvents::class.java.simpleName

    @JvmStatic
    fun postGAEvent(event: String, category: String, action: String, label: String) {
        CommonUtils.dumper("$TAG-$event&$category&$action&$label")
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, category, action, label))
    }
}
