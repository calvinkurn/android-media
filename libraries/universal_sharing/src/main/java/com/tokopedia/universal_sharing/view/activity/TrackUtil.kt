package com.tokopedia.universal_sharing.view.activity

import android.net.Uri
import com.tokopedia.track.builder.Tracker

object TrackUtil {

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/4293
    // Tracker ID: 47771
    fun sendImpressionPageEvent(uri: Uri?) {
        val res = getUriStringAndPath(uri) ?: return
        val uriString = res.first
        val pageType = res.second
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
    fun sendClickUpdateAppEvent(uri: Uri?) {
        val res = getUriStringAndPath(uri) ?: return
        val uriString = res.first
        val path = res.second
        Tracker.Builder()
            .setEvent("clickCommunication")
            .setEventAction("click - update app")
            .setEventCategory("404 Page")
            .setEventLabel("$path - $uriString")
            .setCustomProperty("trackerId", "47772")
            .setBusinessUnit("sharingexperience")
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty("pagePath", uriString)
            .setCustomProperty("pageType", path)
            .build()
            .send()
    }

    private fun getUriStringAndPath(uri: Uri?): Pair<String, String>? {
        if (uri == null) return null
        val uriString = uri.toString()
        if (uriString.isEmpty()) return null
        var path = uri.path ?: ""
        // getting first path only
        if (path.startsWith("/")) {
            path = path.replaceFirst("/", "")
        }
        path = path.substringBefore("/")
        return (uriString to path)
    }

}