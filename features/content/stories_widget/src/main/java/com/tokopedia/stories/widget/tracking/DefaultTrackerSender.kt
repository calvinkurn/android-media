package com.tokopedia.stories.widget.tracking

import com.tokopedia.track.TrackApp

/**
 * Created by kenny.hadisaputra on 03/10/23
 */
class DefaultTrackerSender : StoriesWidgetTracker.Sender {

    private val impressionMap = mutableMapOf<String, MutableSet<String>>()

    override fun sendTracker(data: StoriesWidgetTracker.Data) = synchronized(this) {
        if (data == StoriesWidgetTracker.Data.Empty) return
        val ids = impressionMap[data.eventAction] ?: mutableSetOf()
        if (ids.contains(data.shopId) && data.isImpression) return
        ids.add(data.shopId)
        impressionMap[data.eventAction] = ids

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            data.eventName,
            data.bundle
        )
    }

    override fun reset() = synchronized(this) {
        impressionMap.clear()
    }
}
