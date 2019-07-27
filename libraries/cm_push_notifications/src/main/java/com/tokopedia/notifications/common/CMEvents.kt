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

    private const val EVENT_NAME = "event"
    private const val EVENT_TIME = "event_time"
    private const val CAMPAIGN_ID = "campaign_id"
    private const val NOTIFICATION_ID = "notification_id"
    private const val SOURCE = "source"
    private const val PARENT_ID = "parent_id"
    private const val PUSH_TYPE = "push_type"
    private const val IS_SILENT = "is_silent"

    fun sendPushReceiveEvent(context: Context, baseNotificationModel: BaseNotificationModel) {
        val values = HashMap<String, Any>()
        val irisAnalytics = IrisAnalytics(context)
        if (irisAnalytics != null) {
            values[EVENT_NAME] = PUSH_RECEIVED
            values[EVENT_TIME] = CMNotificationUtils.currentLocalTimeStamp
            values[CAMPAIGN_ID] = baseNotificationModel.campaignId.toString()
            values[NOTIFICATION_ID] = baseNotificationModel.notificationId.toString()
            values[SOURCE] = CMNotificationUtils.getApplicationName(context)
            values[PARENT_ID] = baseNotificationModel.parentId.toString()
            values[PUSH_TYPE] = baseNotificationModel.type.let { baseNotificationModel.type } ?: ""
            values[IS_SILENT] = CMConstant.NotificationType.SILENT_PUSH == baseNotificationModel.type

        }
        irisAnalytics.sendEvent(values)

    }

    fun sendPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel, pushType: String) {
        val values = HashMap<String, Any>()
        val irisAnalytics = IrisAnalytics(context)
        if (irisAnalytics != null) {
            values[EVENT_NAME] = eventName
            values[EVENT_TIME] = CMNotificationUtils.currentLocalTimeStamp
            values[CAMPAIGN_ID] = baseNotificationModel.campaignId.toString()
            values[NOTIFICATION_ID] = baseNotificationModel.notificationId.toString()
            values[SOURCE] = CMNotificationUtils.getApplicationName(context)
            values[PARENT_ID] = baseNotificationModel.parentId.toString()
            values[PUSH_TYPE] = pushType
            values[IS_SILENT] = false
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
