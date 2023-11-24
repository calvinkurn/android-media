package com.tokopedia.logisticseller.ui.confirmshipping.data

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by irpan on 01/09/23.
 */
object ConfirmShippingAnalytics {

    private const val CLICK_SOM = "clickSOM"
    private const val CATEGORY_SOM = "som"
    private const val CLICK_KONFIRMASI = "click konfirmasi"

    fun eventClickKonfirmasi(isSuccess: Boolean) {
        var success = "success"
        if (!isSuccess) success = "failed"
        sendEventCategoryActionLabel(CLICK_SOM, CATEGORY_SOM, CLICK_KONFIRMASI, success)
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
