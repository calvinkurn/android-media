package com.tokopedia.searchbar.navigation_component.analytics

import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.CATEGORY_TOP_NAV
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.DEFAULT_BUSINESS_UNIT
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.DEFAULT_CURRENT_SITE
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.DEFAULT_EMPTY
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.EVENT_CLICK_NAVIGATION_DRAWER
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object NavToolbarTracking: BaseTrackerConst() {
    private const val ACTION_CLICK_ON_TOP_NAV = "click %s nav"

    fun clickNavToolbarComponent(pageName: String, componentName: String, userId: String, keyword: String = "") {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
                event = EVENT_CLICK_NAVIGATION_DRAWER,
                eventCategory = String.format(CATEGORY_TOP_NAV, pageName),
                eventAction = String.format(ACTION_CLICK_ON_TOP_NAV, componentName),
                eventLabel = keyword
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }
}