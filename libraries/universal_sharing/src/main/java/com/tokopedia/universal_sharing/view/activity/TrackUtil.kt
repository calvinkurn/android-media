package com.tokopedia.universal_sharing.view.activity

import com.tokopedia.track.builder.Tracker

object TrackUtil {
    const val EMPTY_TYPE = "0"
    const val UPDATE_TYPE = "1"

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4293
    // Tracker ID: 47771
    fun sendImpressionPageEvent(uriString: String?, isUpdateType: Boolean) {
        if (uriString.isNullOrEmpty()) return
        val pageType = if (isUpdateType) {
            UPDATE_TYPE
        } else {
            EMPTY_TYPE
        }
        Tracker.Builder()
            .setEvent("viewCommunicationIris")
            .setEventAction("impression - 404 Page")
            .setEventCategory("404 Page")
            .setEventLabel("$pageType - $uriString")
            .setCustomProperty("trackerId", "47771")
            .setBusinessUnit("sharingexperience")
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty("pagePath", uriString)
            .setCustomProperty("pageType", pageType)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4293
    // Tracker ID: 47772
    fun sendClickUpdateAppEvent(uriString: String?) {
        if (uriString.isNullOrEmpty()) return
        Tracker.Builder()
            .setEvent("clickCommunication")
            .setEventAction("click - update app")
            .setEventCategory("404 Page")
            .setEventLabel("$UPDATE_TYPE - $uriString")
            .setCustomProperty("trackerId", "47772")
            .setBusinessUnit("sharingexperience")
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty("pagePath", uriString)
            .setCustomProperty("pageType", UPDATE_TYPE)
            .build()
            .send()
    }

}