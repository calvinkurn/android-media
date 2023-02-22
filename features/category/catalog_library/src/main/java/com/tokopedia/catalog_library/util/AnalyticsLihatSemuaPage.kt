package com.tokopedia.catalog_library.util

import com.tokopedia.track.builder.Tracker
import org.json.JSONArray

object AnalyticsLihatSemuaPage {

    fun sendImpressionOnCategoryListEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        promotions: JSONArray,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_ITEM)
            .setEventAction(ActionKeys.IMPRESSION_ON_CATEGORY_LIST)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_CATEGORY_LIST)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.PROMOTIONS, promotions)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickAscendingDescendingSortEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_ASCENDING_DESCENDING_SORT)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_ASCENDING_DESCENDING_SORT)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickGridListViewEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_GRID_LIST_VIEW)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_GRID_LIST_VIEW)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCategoryOnCategoryListEvent(
        eventLabel: String,
        businessUnit: String,
        categoryId: String,
        currentSite: String,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATEGORY_ON_CATEGORY_LIST)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_CATEGORY_ON_CATEGORY_LIST)
            .setBusinessUnit(businessUnit)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickDropUpButtonEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_DROP_UP_BUTTON)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_DROP_UP_BUTTON)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }
}
