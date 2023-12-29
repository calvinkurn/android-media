package com.tokopedia.notifications.common

import android.content.Context
import android.text.TextUtils
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.PayloadExtra
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import timber.log.Timber

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
    const val PUSH_RENDERED = "pushRendered"
    const val DEVICE_NOTIFICATION_OFF = "device_notification_off"
    const val PUSH_CLICKED = "pushClicked"
    const val PUSH_DISMISSED = "pushDismissed"
    const val PUSH_CANCELLED = "pushCancelled"
    const val PUSH_DELETED = "pushDeleted"
    const val PUSH_EXPIRED = "pushExpired"
    const val INAPP_RECEIVED = "inappReceived"
    const val INAPP_CLICKED = "inappClicked"
    const val INAPP_DISMISSED = "inappDismissed"
    const val INAPP_CANCELLED = "inappCancelled"
    const val INAPP_DELIVERED = "inappDelivered"
    const val INAPP_PREREAD = "inappPreread"
    const val INAPP_READ = "inappRead"
    const val INAPP_EXPIRED = "inappExpired"

    private const val EVENT_NAME = "event"
    private const val EVENT_TIME = "event_time"
    private const val EVENT_MESSAGE_ID = "message_id"
    private const val CAMPAIGN_ID = "campaign_id"
    private const val NOTIFICATION_ID = "notification_id"
    private const val SOURCE = "source"
    private const val PARENT_ID = "parent_id"
    private const val PUSH_TYPE = "push_type"
    private const val IS_SILENT = "is_silent"
    private const val CLICKED_ELEMENT_ID = "clicked_element_id"
    private const val INAPP_TYPE = "inapp_type"
    private const val LABEL = "eventlabel"
    private const val SHOP_ID = "shop_id"
    private const val CAMPAIGN_NAME = "campaign_name"
    private const val JOURNEY_ID = "journey_id"
    private const val JOURNEY_NAME = "journey_name"
    private const val SESSION_ID = "session_id_cm"

    private const val AMPLIFICATION = "amplification"


    private const val IRIS_ANALYTICS_APP_SITE_OPEN = "appSiteOpen"
    private const val IRIS_ANALYTICS_EVENT_KEY = "event"


    fun trackCmINAppEvent(context: Context?,  cmInApp: CMInApp?,
                          eventName: String?,  elementId: String?){
        cmInApp?.let {
            context?.let { context ->
                if(eventName!= null){
                    if(elementId!= null) {
                        sendInAppEvent(context.applicationContext, eventName, cmInApp, elementId)
                    }else {
                        sendInAppEvent(context.applicationContext, eventName, cmInApp)
                    }
                }
            }
        }

    }

    fun sendFirstScreenEvent(context: Context?){
        context?.let {
            val map: MutableMap<String, Any> = mutableMapOf(
                IRIS_ANALYTICS_EVENT_KEY to IRIS_ANALYTICS_APP_SITE_OPEN
            )
            IrisAnalytics.Companion.getInstance(context.applicationContext).saveEvent(map)
        }
    }


    fun sendPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel) {
        if (baseNotificationModel.isTest) return
        val irisAnalytics = IrisAnalytics.getInstance(context)
        val values = addBaseValues(context, eventName, baseNotificationModel)
        if (baseNotificationModel.isAmplification) {
            values[LABEL] = AMPLIFICATION
        }
        checkEventAndAddShopId(eventName,values,baseNotificationModel.shopId)
        trackEvent(context, irisAnalytics, values)
    }

    fun sendPushEvent(context: Context, eventName: String, baseNotificationModel: BaseNotificationModel, elementID: String?) {
        if (baseNotificationModel.isTest)
            return
        val irisAnalytics = IrisAnalytics.Companion.getInstance(context.applicationContext)
        val values = addBaseValues(context, eventName, baseNotificationModel)
        if (baseNotificationModel.isAmplification) {
            values[LABEL] = AMPLIFICATION
        }
        if (elementID != null) {
            values[CLICKED_ELEMENT_ID] = elementID
        }
        checkEventAndAddShopId(eventName,values,baseNotificationModel.shopId)
        trackEvent(context, irisAnalytics, values)
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
        addTrackingExtras(eventName, baseNotificationModel.payloadExtra, values)
        return values

    }

    fun sendInAppEvent(context: Context, eventName: String, cmInApp: CMInApp) {
        if (cmInApp.isTest)
            return
        val irisAnalytics = IrisAnalytics.Companion.getInstance(context.applicationContext)
        val values = addBaseValues(context, eventName, cmInApp)
        checkEventAndAddShopId(eventName, values, cmInApp.shopId)
        trackEvent(context, irisAnalytics, values)

    }

    fun sendInAppEvent(context: Context, eventName: String, cmInApp: CMInApp, elementID: String?) {
        if (cmInApp.isTest)
            return
        val irisAnalytics = IrisAnalytics.Companion.getInstance(context.applicationContext)
        val values = addBaseValues(context, eventName, cmInApp)
        elementID?.let {
            values[CLICKED_ELEMENT_ID] = elementID
        }
        checkEventAndAddShopId(eventName, values, cmInApp.shopId)
        trackEvent(context, irisAnalytics, values)
    }

    fun sendAmplificationInAppEvent(context: Context, eventName: String, cmInApp: CMInApp) {
        if (cmInApp.isTest) return
        val irisAnalytics = IrisAnalytics.getInstance(context)
        trackEvent(context, irisAnalytics, addBaseValues(context, eventName, cmInApp).apply {
            put(LABEL, AMPLIFICATION)
        })
    }

    private fun trackEvent(context: Context, irisAnalytics: Iris, values: HashMap<String, Any>) {
        logTimber(values)
        if (CMNotificationUtils.isNetworkAvailable(context))
            irisAnalytics.sendEvent(values)
        else irisAnalytics.saveEvent(values)
    }

    private fun logTimber(values: HashMap<String, Any>) {
        val push = "_push'"
        val inapp = "_inapp'"
        if (values.containsKey(CAMPAIGN_ID) && TextUtils.isEmpty(values[CAMPAIGN_ID].toString()))
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                mapOf("type" to "validation",
                    "reason" to "no_campaignId".plus(if(values.containsKey(PUSH_TYPE)) push else inapp),
                    "data" to values.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
        else if (!values.containsKey(CAMPAIGN_ID))
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                mapOf("type" to "validation",
                    "reason" to "campaignId_removed".plus(if(values.containsKey(PUSH_TYPE)) push else inapp),
                    "data" to values.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
        else if (values.containsKey(PARENT_ID) && TextUtils.isEmpty(values[PARENT_ID].toString()))
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                mapOf("type" to "validation",
                    "reason" to "no_parentId".plus(if(values.containsKey(PUSH_TYPE)) push else inapp),
                    "data" to values.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
        else if (!values.containsKey(PARENT_ID))
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                mapOf("type" to "validation",
                    "reason" to "parentId_removed".plus(if(values.containsKey(PUSH_TYPE)) push else inapp),
                    "data" to values.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
        else if (values.containsKey(NOTIFICATION_ID) && TextUtils.isEmpty(values[NOTIFICATION_ID].toString()))
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                mapOf("type" to "validation",
                    "reason" to "no_notificationId".plus(if(values.containsKey(PUSH_TYPE)) push else inapp),
                    "data" to values.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
        else if (!values.containsKey(NOTIFICATION_ID))
            ServerLogger.log(Priority.P2, "CM_VALIDATION",
                mapOf("type" to "validation",
                    "reason" to "notificationId_removed".plus(if(values.containsKey(PUSH_TYPE)) push else inapp),
                    "data" to values.toString().take(CMConstant.TimberTags.MAX_LIMIT)))
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

        addTrackingExtras(eventName, cmInApp.payloadExtra, values)
        return values

    }


    private fun addTrackingExtras(eventName: String,
                                  payloadExtra: PayloadExtra?,
                                  values: HashMap<String, Any>){
        payloadExtra?.let {
            it.campaignName?.let {cmpName ->
                values[CAMPAIGN_NAME] = cmpName
            }

            it.journeyId?.let {journeyId ->
                values[JOURNEY_ID] = journeyId
            }

            it.journeyName?.let { journeyName ->
                values[JOURNEY_NAME] = journeyName
            }
            it.sessionId?.let { sessionId ->
                setSessionId(eventName, values, sessionId)
            }
        }
    }

    private fun setSessionId(eventName : String,
                             values : HashMap<String, Any>,
                             sessionId : String){

        val allowedEvents = listOf(
            INAPP_RECEIVED,
            INAPP_CLICKED,
            INAPP_DISMISSED,
            PUSH_RECEIVED,
            PUSH_CLICKED,
            PUSH_RENDERED,
            PUSH_DISMISSED,
            PUSH_EXPIRED,
            DEVICE_NOTIFICATION_OFF
        )

        if (eventName in allowedEvents) {
            values[SESSION_ID] = sessionId
        }
    }

    private fun checkEventAndAddShopId(
        eventName: String,
        values: HashMap<String, Any>,
        shopId: String?
    ) {

        val allowedEvents = listOf(
            INAPP_RECEIVED,
            INAPP_CLICKED,
            INAPP_DISMISSED,
            PUSH_RECEIVED,
            PUSH_CLICKED,
            PUSH_RENDERED,
            PUSH_DISMISSED,
            PUSH_CANCELLED,
            PUSH_EXPIRED
        )

        if (!shopId.isNullOrBlank() && (eventName in allowedEvents)) {
            values[SHOP_ID] = shopId
        }
    }

}

object CMEvents {

    private val TAG = CMEvents::class.java.simpleName

    @JvmStatic
    fun postGAEvent(event: String, category: String, action: String, label: String) {
        Timber.d("$TAG-$event&$category&$action&$label")
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
            event, category, action, label))
    }
}
