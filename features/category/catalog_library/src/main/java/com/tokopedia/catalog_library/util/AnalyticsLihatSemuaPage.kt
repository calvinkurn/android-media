package com.tokopedia.catalog_library.util

import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import org.json.JSONArray

object AnalyticsLihatSemuaPage {

    private fun getIrisSessionId(): String {
        return TrackApp.getInstance().gtm.irisSessionId
    }

    fun sendImpressionOnCategoryListEvent(
        eventLabel: String,
        promotions: JSONArray,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_ITEM)
            .setEventAction(ActionKeys.IMPRESSION_ON_CATEGORY_LIST)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_CATEGORY_LIST)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_KATEGORI)
            .setCustomProperty(EventKeys.PROMOTIONS, promotions)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickAscendingDescendingSortEvent(
        eventLabel: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_ASCENDING_DESCENDING_SORT)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_ASCENDING_DESCENDING_SORT)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_KATEGORI)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickGridListViewEvent(
        eventLabel: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_GRID_LIST_VIEW)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_GRID_LIST_VIEW)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_KATEGORI)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCategoryOnCategoryListEvent(
        eventLabel: String,
        categoryId: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATEGORY_ON_CATEGORY_LIST)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_CATEGORY_ON_CATEGORY_LIST)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_KATEGORI)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickDropUpButtonEvent(
        eventLabel: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_DROP_UP_BUTTON)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_DROP_UP_BUTTON)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_KATEGORI)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }
}
