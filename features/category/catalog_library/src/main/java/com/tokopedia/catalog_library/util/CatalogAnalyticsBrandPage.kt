package com.tokopedia.catalog_library.util

import com.tokopedia.track.builder.Tracker

object CatalogAnalyticsBrandPage {

    fun sendImpressOnLihatButtonEvent (eventLabel: String, businessUnit: String, currentSite: String, pagePath: String, sessionIris: String, userId: String) {
        Tracker.Builder()
            .setEvent("viewContentIris")
            .setEventAction("impress on lihat button")
            .setEventCategory("catalog library - popular brand page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37368")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty("pagePath", pagePath)
            .setCustomProperty("sessionIris", sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickOnLihatButtonEvent (trackerId : String, eventAction: String, eventLabel: String ,userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(eventAction)
            .setEventCategory("catalog library - popular brand page")
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, trackerId)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_POPULAR_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickOnCatalogEvent (eventLabel: String, catalogId: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction("click on catalog")
            .setEventCategory("catalog library - popular brand page")
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37372")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.CATALOG_ID, catalogId)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_POPULAR_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }
}

