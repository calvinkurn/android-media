package com.tokopedia.home.analytics.v2

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object LoginWidgetTracking: BaseTrackerConst() {
    fun sendLoginClick() {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = Category.HOMEPAGE,
            eventAction = "click login button on login widget",
            eventLabel = Value.EMPTY
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, "41946")

        getTracker().sendGeneralEvent(trackerBuilder.build())
    }
}
