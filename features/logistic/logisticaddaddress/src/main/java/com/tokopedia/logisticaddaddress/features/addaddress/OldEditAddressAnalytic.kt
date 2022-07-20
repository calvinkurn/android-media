package com.tokopedia.logisticaddaddress.features.addaddress

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object OldEditAddressAnalytics : BaseTrackerConst() {
    private const val BUSINESS_UNIT_LOGISTIC = "logistic"
    private const val EVENT_LOGISTIC_IRIS = "viewLogisticIris"
    private const val EVENT_CLICK_LOGISTIC = "clickLogistic"
    private const val CATEGORY_EDIT_ADDRESS_OLD = "edit address page old"
    private const val ACTION_VIEW_OLD_EDIT_ADDRESS = "view edit address page old"
    private const val ACTION_CLICK_SAVE_EDIT = "click button simpan - edit address old"
    private const val LABEL_SUCCESS = "success"
    private const val LABEL_NOT_SUCCESS = "not success"

    fun sendViewEditAddressPageOldEvent() {
        sendEventActionLabel(
            EVENT_LOGISTIC_IRIS,
            ACTION_VIEW_OLD_EDIT_ADDRESS,
            ""
        )
    }


    fun sendClickButtonSimpanEditAddressOldEvent(success: Boolean) {
        sendEventActionLabel(
            EVENT_CLICK_LOGISTIC,
            ACTION_CLICK_SAVE_EDIT,
            if (success) LABEL_SUCCESS else LABEL_NOT_SUCCESS
        )
    }

    private fun sendEventActionLabel(
        event: String,
        eventAction: String, eventLabel: String
    ) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(event)
                .appendEventCategory(CATEGORY_EDIT_ADDRESS_OLD)
                .appendEventAction(eventAction)
                .appendEventLabel(eventLabel)
                .appendBusinessUnit(BUSINESS_UNIT_LOGISTIC)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
        )
    }
}