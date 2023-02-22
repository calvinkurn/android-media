package com.tokopedia.searchbar.navigation_component.analytics

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.searchbar.navigation_component.NavConstant
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.CATEGORY_TOP_NAV
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.DEFAULT_BUSINESS_UNIT
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.DEFAULT_CURRENT_SITE
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.DEFAULT_EMPTY
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.EVENT_CLICK_TOP_NAV
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.FIELD_PAGE_SOURCE
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.FIELD_PAGE_SOURCE_VALUE_FORMAT
import com.tokopedia.searchbar.navigation_component.analytics.TrackingConst.VALUE_RED_DOT
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object NavToolbarTracking : BaseTrackerConst() {
    private const val ACTION_CLICK_ON_TOP_NAV = "click %s nav%s"
    fun clickNavToolbarComponent(pageName: String, componentName: String, userId: String, keyword: String = "", counter: Int = 0) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = EVENT_CLICK_TOP_NAV,
            eventCategory = CATEGORY_TOP_NAV,
            eventAction = if ((counter.isMoreThanZero() || counter == NavConstant.ICON_COUNTER_NONE_TYPE) && componentName != IconList.NAME_NOTIFICATION) {
                String.format(ACTION_CLICK_ON_TOP_NAV, componentName, VALUE_RED_DOT)
            } else {
                String.format(ACTION_CLICK_ON_TOP_NAV, componentName, DEFAULT_EMPTY)
            },
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
