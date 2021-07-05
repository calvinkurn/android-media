package com.tokopedia.homenav.mainnav.view.analytics

import com.tokopedia.homenav.common.TrackingConst.CATEGORY_GLOBAL_MENU
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_BUSINESS_UNIT
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_CURRENT_SITE
import com.tokopedia.homenav.common.TrackingConst.EVENT_CLICK_NAVIGATION_DRAWER
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TrackingUserMenuSection: BaseTrackerConst() {
    private const val ACTION_CLICK_ON_USER_MENU = "click on user menu"

    fun clickOnUserMenu(menuTrackerName: String, userId: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
                event = EVENT_CLICK_NAVIGATION_DRAWER,
                eventCategory = CATEGORY_GLOBAL_MENU,
                eventAction = ACTION_CLICK_ON_USER_MENU,
                eventLabel = menuTrackerName
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }
}