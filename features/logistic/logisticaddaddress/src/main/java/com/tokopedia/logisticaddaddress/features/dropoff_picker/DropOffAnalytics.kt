package com.tokopedia.logisticaddaddress.features.dropoff_picker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

internal class DropOffAnalytics {

    private fun sendTracker(event: String, eventCategory: String, eventAction: String,
                        eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel))
    }


    private fun sendTracker(event: String, eventCategory: String, eventAction: String) {
        sendTracker(event, eventCategory, eventAction, "")
    }

    fun trackUserClickNantiSaja() {

    }

}