package com.tokopedia.notifications.common

import android.content.Context
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
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
    const val PUSH_RECEIVED = "pushReceived"
    const val PUSH_CLICKED = "pushClicked"
    const val PUSH_DISMISSED = "pushDismissed"
    const val PUSH_CANCELLED = "pushCancelled"
    const val INAPP_RECEIVED = "inappReceived"
    const val INAPP_CLICKED = "inappClicked"
    const val INAPP_DISMISSED = "inappDismissed"

    private const val EVENT_NAME = "event"
    private const val EVENT_TIME = "event_time"
    private const val EVENT_MESSAGE_ID = "message_id"
    private const val CAMPAIGN_ID = "campaign_id"
    private const val NOTIFICATION_ID = "notification_id"
    private const val SOURCE = "source"
    private const val PARENT_ID = "parent_id"
    private const val PUSH_TYPE = "push_type"
    private const val IS_SILENT = "is_silent"
    private const val CLICKED_ELEMENT_ID="clicked_element_id"
    private const val INAPP_TYPE="inapp_type"

    fun sendPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel) {
        val irisAnalytics = IrisAnalytics(context)
        if (irisAnalytics != null) {
            val values = addBaseValues(context, eventName, baseNotificationModel)
            irisAnalytics.saveEvent(values)
        }
    }

    fun sendPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel, elementID: String?) {
        val irisAnalytics = IrisAnalytics(context)
        if (irisAnalytics != null) {
            val values = addBaseValues(context, eventName, baseNotificationModel)
            if (elementID != null) {
                values[CLICKED_ELEMENT_ID] = elementID

            }
            irisAnalytics.saveEvent(values)
        }

    }

    private fun addBaseValues(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel): HashMap<String, Any> {
        val values = HashMap<String, Any>()

        values[EVENT_NAME] = eventName
        values[EVENT_TIME] = CMNotificationUtils.currentLocalTimeStamp
        values[CAMPAIGN_ID] = baseNotificationModel.campaignId.toString()
        values[NOTIFICATION_ID] = baseNotificationModel.notificationId.toString()
        values[SOURCE] = CMNotificationUtils.getApplicationName(context)
        values[PARENT_ID] = baseNotificationModel.parentId.toString()
        values[PUSH_TYPE] = baseNotificationModel.type.let { baseNotificationModel.type }
                ?: ""
        if (CMConstant.NotificationType.SILENT_PUSH != baseNotificationModel.type) {
            values[IS_SILENT] = false
        }
        values[EVENT_MESSAGE_ID] = baseNotificationModel.campaignUserToken?.let { it } ?: ""

        return values

    }

    fun sendPushEvent(context: Context, eventName: String, cmInApp: CMInApp) {
        val irisAnalytics = IrisAnalytics(context)
        if (irisAnalytics != null) {
            val values = addBaseValues(context, eventName, cmInApp)
            irisAnalytics.saveEvent(values)
        }
    }

    fun sendPushEvent(context: Context, eventName: String, cmInApp: CMInApp, elementID: String?) {
        val irisAnalytics = IrisAnalytics(context)
        if (irisAnalytics != null) {
            val values = addBaseValues(context, eventName, cmInApp)

            elementID?.let {
                values[CLICKED_ELEMENT_ID] = elementID
            }


            irisAnalytics.saveEvent(values)
        }

    }

    private fun addBaseValues(context: Context, eventName: String, cmInApp: CMInApp): HashMap<String, Any> {
        val values = HashMap<String, Any>()

        values[EVENT_NAME] = eventName
        values[EVENT_TIME] = CMNotificationUtils.currentLocalTimeStamp
        values[CAMPAIGN_ID] = cmInApp.campaignId.toString()
        values[NOTIFICATION_ID] = cmInApp.id.toString()
        values[SOURCE] = CMNotificationUtils.getApplicationName(context)
        values[PARENT_ID] = cmInApp.parentId
        values[IS_SILENT] = false
        values[INAPP_TYPE] = cmInApp.type.let { cmInApp.type } ?: ""
        values[EVENT_MESSAGE_ID] = cmInApp.campaignUserToken?.let { it } ?: ""

        return values

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
