package com.tokopedia.homenav.mainnav.view.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object MainNavAnalytics: BaseTrackerConst() {
    private const val CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer"
    private const val GLOBAL_MENU = "global menu"
    private const val BUSINESS_UNIT_LIST = "business_unit_list"

    fun onClickBusinessUnitItem(title: String, userId: String){
        getTracker().sendGeneralEvent(BaseTrackerBuilder()
                .appendEvent(CLICK_NAVIGATION_DRAWER)
                .appendEventCategory(GLOBAL_MENU)
                .appendEventAction(Action.CLICK_ON.format(BUSINESS_UNIT_LIST))
                .appendEventLabel(title)
                .appendUserId(userId)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build())
    }
}