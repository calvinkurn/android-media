package com.tokopedia.purchase_platform.common.analytics

import android.app.Activity
import android.os.Bundle
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import java.util.*

/**
 * @author anggaprasetiyo on 18/05/18.
 */
abstract class TransactionAnalytics {

    fun sendScreenName(activity: Activity?, screenName: String) {
        if (activity == null) {
            return
        }
        val customDimension: MutableMap<String, String> = HashMap()
        customDimension[KEY_SESSION_IRIS] = IrisSession(activity).getSessionId()
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, customDimension)
    }

    fun sendScreenName(activity: Activity, screenName: String, customDimension: MutableMap<String, String>) {
        customDimension[KEY_SESSION_IRIS] = IrisSession(activity).getSessionId()
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, customDimension)
    }

    protected fun sendGeneralEvent(event: String, eventCategory: String,
                                   eventAction: String, eventLabel: String = "") {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(event, eventCategory, eventAction, eventLabel)
        )
    }

    protected fun sendGeneralEvent(eventData: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventData)
    }

    @Deprecated("This method is not optimized for kotlin", ReplaceWith("sendGeneralEvent()"))
    protected fun sendEventCategoryActionLabel(event: String, eventCategory: String,
                                               eventAction: String, eventLabel: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                TrackAppUtils.gtmData(event, eventCategory, eventAction, eventLabel)
        )
    }

    @Deprecated("This method is not optimized for kotlin", ReplaceWith("sendGeneralEvent()"))
    protected fun sendEventCategoryAction(event: String, eventCategory: String,
                                          eventAction: String) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    protected fun sendEnhancedEcommerce(dataLayer: Map<String, Any?>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(dataLayer)
    }

    protected fun sendEnhancedEcommerce(eventName: String, bundle: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, bundle)
    }

    protected fun getGtmData(event: String, eventCategory: String,
                             eventAction: String, eventLabel: String): MutableMap<String, Any> {
        return TrackAppUtils.gtmData(event, eventCategory, eventAction, eventLabel)
    }
}