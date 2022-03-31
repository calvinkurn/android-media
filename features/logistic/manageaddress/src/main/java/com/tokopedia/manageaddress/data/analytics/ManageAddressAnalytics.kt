package com.tokopedia.manageaddress.data.analytics

import android.app.Activity
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object ManageAddressAnalytics : BaseTrackerConst(){

    private const val EVENT_CLICK_LOGISTIC = "clickLogistic"
    private const val ACTION_UBAH_ALAMAT = "click button ubah alamat"
    private const val CATEGORY_LIST_ADDRESS = "address list page"
    private const val BUSINESS_UNIT_LOGISTIC = "logistic"

    @JvmStatic
    fun sendScreenName(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun sendClickButtonUbahAlamatEvent() {
        sendEventCategoryActionLabel(EVENT_CLICK_LOGISTIC, CATEGORY_LIST_ADDRESS, ACTION_UBAH_ALAMAT, "")
    }

    private fun sendEventCategoryActionLabel(event: String, eventCategory: String,
                                             eventAction: String, eventLabel: String) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
            .appendEvent(event)
            .appendEventCategory(eventCategory)
            .appendEventAction(eventAction)
            .appendEventLabel(eventLabel)
            .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .build())
    }
}