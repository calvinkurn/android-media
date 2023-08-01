package com.tokopedia.promogamification.common.floating

import com.tokopedia.promogamification.common.applink.CoreGamificationEventTracking
import com.tokopedia.promogamification.common.constants.TrackerConstants
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSession

class FloatingEggTracker(val userSession: UserSession) {

    fun trackingEggImpression(idToken: Int, name: String) {
        val map = TrackAppUtils.gtmData(
            CoreGamificationEventTracking.Event.VIEW_LUCKY_EGG,
            CoreGamificationEventTracking.Category.CLICK_LUCKY_EGG,
            CoreGamificationEventTracking.Action.IMPRESSION_LUCKY_EGG,
            idToken.toString() + "_" + name
        )
        map[TrackerConstants.BUSINESS_UNIT_KEY] = TrackerConstants.BUSINESS_UNIT_VALUE
        map[TrackerConstants.CURRENT_SITE_KEY] = TrackerConstants.CURRENT_SITE_VALUE
        map[TrackerConstants.USER_ID_KEY] = userSession.userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackingEggClick(idToken: Int, name: String) {
        val map = TrackAppUtils.gtmData(
            CoreGamificationEventTracking.Event.CLICK_LUCKY_EGG,
            CoreGamificationEventTracking.Category.CLICK_LUCKY_EGG,
            CoreGamificationEventTracking.Action.CLICK_LUCKY_EGG,
            idToken.toString() + "_" + name
        )
        map[TrackerConstants.BUSINESS_UNIT_KEY] = TrackerConstants.BUSINESS_UNIT_VALUE
        map[TrackerConstants.CURRENT_SITE_KEY] = TrackerConstants.CURRENT_SITE_VALUE
        map[TrackerConstants.USER_ID_KEY] = userSession.userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackingEggClickCLose(idToken: Int, name: String) {
        val map = TrackAppUtils.gtmData(
            CoreGamificationEventTracking.Event.CLICK_LUCKY_EGG,
            CoreGamificationEventTracking.Category.CLICK_LUCKY_EGG,
            CoreGamificationEventTracking.Action.CLICK_CLOSE_LUCKY_EGG,
            idToken.toString() + "_" + name
        )
        map[TrackerConstants.BUSINESS_UNIT_KEY] = TrackerConstants.BUSINESS_UNIT_VALUE
        map[TrackerConstants.CURRENT_SITE_KEY] = TrackerConstants.CURRENT_SITE_VALUE
        map[TrackerConstants.USER_ID_KEY] = userSession.userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun trackingEggHide(idToken: Int, name: String, isMinimized: Boolean) {
        var label = "_hide"
        if (!isMinimized) {
            label = "_show"
        }
        val map = TrackAppUtils.gtmData(
            CoreGamificationEventTracking.Event.CLICK_LUCKY_EGG,
            CoreGamificationEventTracking.Category.CLICK_LUCKY_EGG,
            CoreGamificationEventTracking.Action.HIDE_LUCKY_EGG,
            idToken.toString() + "_" + name + label
        )
        map[TrackerConstants.BUSINESS_UNIT_KEY] = TrackerConstants.BUSINESS_UNIT_VALUE
        map[TrackerConstants.CURRENT_SITE_KEY] = TrackerConstants.CURRENT_SITE_VALUE
        map[TrackerConstants.USER_ID_KEY] = userSession.userId
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

}