package com.tokopedia.sellerhome.analytic

/**
 * Created By @ilhamsuaib on 11/03/20
 */
 
object NavigationTracking {

    fun sendClickNotificationEvent() {
        val event = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_NAVIGATION_DRAWER,
                category = TrackingConstant.TOP_NAV,
                action = TrackingConstant.CLICK_NOTIFICATION,
                label = ""
        )
        TrackingHelper.sendGeneralEvent(event)
    }

    fun sendClickBottomNavigationMenuEvent(action: String) {
        val event = TrackingHelper.createMap(
                event = TrackingConstant.CLICK_NAVIGATION_DRAWER,
                category = TrackingConstant.BOTTOM_NAV,
                action = action,
                label = ""
        )
        TrackingHelper.sendGeneralEvent(event)
    }
}