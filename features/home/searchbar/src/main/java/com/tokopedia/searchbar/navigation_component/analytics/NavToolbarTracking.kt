package com.tokopedia.searchbar.navigation_component.analytics

import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.CATEGORY_TOP_NAV
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.DEFAULT_BUSINESS_UNIT
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.DEFAULT_CURRENT_SITE
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.EVENT_CLICK_TOP_NAV
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.FIELD_PAGE_SOURCE
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.FIELD_PAGE_SOURCE_VALUE_FORMAT
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object NavToolbarTracking : BaseTrackerConst() {
    private const val ACTION_CLICK_ON_TOP_NAV = "click %s nav"
    fun clickNavToolbarComponent(pageName: String, componentName: String, userId: String, keyword: String = "", counter: Int = 0) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = EVENT_CLICK_TOP_NAV,
            eventCategory = CATEGORY_TOP_NAV,
            eventAction = String.format(ACTION_CLICK_ON_TOP_NAV, componentName),
            eventLabel = keyword
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        trackingBuilder.appendCustomKeyValue(
            FIELD_PAGE_SOURCE,
            String.format(FIELD_PAGE_SOURCE_VALUE_FORMAT, pageName)
        )
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }
}
