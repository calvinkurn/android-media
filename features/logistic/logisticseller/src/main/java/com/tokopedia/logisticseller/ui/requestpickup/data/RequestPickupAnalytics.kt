package com.tokopedia.logisticseller.ui.requestpickup.data

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by irpan on 01/09/23.
 */
object RequestPickupAnalytics {

    private const val CLICK_SOM = "clickSOM"
    private const val CATEGORY_SOM = "som"
    private const val CLICK_REQUEST_PICKUP_POPUP = "click request pickup popup"

    fun eventClickRequestPickupPopup() {
        sendEventCategoryAction(CLICK_SOM, CATEGORY_SOM, CLICK_REQUEST_PICKUP_POPUP)
    }

    private fun sendEventCategoryAction(
        event: String,
        eventCategory: String,
        eventAction: String
    ) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    private fun sendEventCategoryActionLabel(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                event,
                eventCategory,
                eventAction,
                eventLabel
            )
        )
    }
}
