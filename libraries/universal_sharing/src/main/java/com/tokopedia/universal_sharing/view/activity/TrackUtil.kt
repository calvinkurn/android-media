package com.tokopedia.universal_sharing.view.activity

import com.tokopedia.track.builder.Tracker

object TrackUtil {

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4293
    // Tracker ID: 47771
    fun sendImpressionPageEvent(uriString: String?, pageType: String) {
        if (uriString.isNullOrEmpty()) return
        Tracker.Builder()
            .setEvent("viewCommunicationIris")
            .setEventAction("impression - 404 Page")
            .setEventCategory("404 Page")
            .setEventLabel("$pageType - $uriString")
            .setCustomProperty("trackerId", "47771")
            .setBusinessUnit("sharingexperience")
            .setCurrentSite("tokopediamarketplace")s
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
            .setEventLabel(" - $uriString")
            .setCustomProperty("trackerId", "47772")
            .setBusinessUnit("sharingexperience")
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty("pagePath", uriString)
            .setCustomProperty("pageType", "")
            .build()
            .send()
    }

}