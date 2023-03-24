package com.tokopedia.catalog_library.util

import com.tokopedia.catalog_library.util.CategoryKeys.Companion.CATALOG_LIBRARY_POPULAR_BRAND_PAGE
import com.tokopedia.track.builder.Tracker

object CatalogAnalyticsBrandPage {

    fun sendImpressOnLihatButtonEvent (eventLabel: String, businessUnit: String, currentSite: String, pagePath: String, sessionIris: String, userId: String) {
        Tracker.Builder()
            .setEvent("viewContentIris")
            .setEventAction("impress on lihat button")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_PAGE)
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
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_PAGE)
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
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_PAGE)
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

