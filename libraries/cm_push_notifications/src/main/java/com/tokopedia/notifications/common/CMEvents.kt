package com.tokopedia.notifications.common

import android.content.Context
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.iris.IrisAnalytics
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

}

object IrisAnalyticsEvents {
    private const val PUSH_RECEIVED = "pushReceived"
    const val PUSH_CLICKED = "pushClicked"
    const val PUSH_DISMISSED = "pushDismissed"

    fun sendPushReceiveEvent(context: Context, baseNotificationModel: BaseNotificationModel) {
        val values = HashMap<String, Any>()
        val irisAnalytics = IrisAnalytics(context)
        if (irisAnalytics != null) {
            values["event"] = PUSH_RECEIVED
            values["event_time"] = CMNotificationUtils.currentLocalTimeStamp
            values["campaign_id"] = baseNotificationModel.campaignId.toString()
            values["notification_id"] = baseNotificationModel.notificationId.toString()
            values["source"] = CMNotificationUtils.getApplicationName(context)
            values["is_silent"] = CMConstant.NotificationType.SILENT_PUSH == baseNotificationModel.type
            values["parent_id"] = baseNotificationModel.parentId.toString()
            values["push_type"] = baseNotificationModel.type.let { baseNotificationModel.type }
                    ?: ""

        }
        irisAnalytics.sendEvent(values)

    }

    fun sendPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel, pushType:String) {
        val values = HashMap<String, Any>()
        val irisAnalytics = IrisAnalytics(context)
        if (irisAnalytics != null) {
            values["event"] = eventName
            values["event_time"] = CMNotificationUtils.currentLocalTimeStamp
            values["campaign_id"] = baseNotificationModel.campaignId.toString()
            values["notification_id"] = baseNotificationModel.notificationId.toString()
            values["source"] = CMNotificationUtils.getApplicationName(context)
            values["parent_id"] = baseNotificationModel.parentId.toString()
            values["push_type"] =pushType
            values["is_silent"] = false
        }
        irisAnalytics.saveEvent(values)

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
