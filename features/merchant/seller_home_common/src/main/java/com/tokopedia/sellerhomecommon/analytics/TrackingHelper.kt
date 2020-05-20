package com.tokopedia.sellerhomecommon.analytics

import com.tokopedia.track.TrackApp

/**
 * Created By @ilhamsuaib on 19/05/20
 */

object TrackingHelper {

    fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                TrackingConstant.EVENT to event,
                TrackingConstant.EVENT_CATEGORY to category,
                TrackingConstant.EVENT_ACTION to action,
                TrackingConstant.EVENT_LABEL to label
        )
    }

    fun sendGeneralEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun sendEnhanceEcommerceEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }
}