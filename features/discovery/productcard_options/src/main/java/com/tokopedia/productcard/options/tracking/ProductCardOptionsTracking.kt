package com.tokopedia.productcard.options.tracking

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*

internal object ProductCardOptionsTracking {

    fun eventClickSeeSimilarProduct(event: String, screenName: String, keyword: String, productId: String) {
        val eventTrackingMap: MutableMap<String, Any> = mutableMapOf()

        eventTrackingMap[EVENT] = event
        eventTrackingMap[EVENT_CATEGORY] = screenName.toLowerCase()
        eventTrackingMap[EVENT_ACTION] = Action.CLICK_SIMILAR_BUTTON
        eventTrackingMap[EVENT_LABEL] = String.format(Label.KEYWORD_PRODUCT_ID, keyword, productId)

        TrackApp.getInstance().gtm.sendGeneralEvent(eventTrackingMap)
    }
}