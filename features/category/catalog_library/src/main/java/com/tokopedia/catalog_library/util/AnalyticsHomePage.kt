package com.tokopedia.catalog_library.util

import com.tokopedia.track.builder.Tracker
import org.json.JSONArray

object AnalyticsHomePage {

    fun sendImpressionOnSpecialCategoriesEvent(
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        promotions: JSONArray,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_ITEM)
            .setEventAction(ActionKeys.IMPRESSION_ON_SPECIAL_CATEGORIES)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_SPECIAL_CATEGORIES)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.PROMOTIONS, promotions)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendImpressionOnRelevantCatalogsEvent(
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        promotions: JSONArray,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_ITEM)
            .setEventAction(ActionKeys.IMPRESSION_ON_RELEVANT_CATALOGS)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_RELEVANT_CATALOGS)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.PROMOTIONS, promotions)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendImpressionOnPopularBrandsEvent(
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        promotions: JSONArray,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_ITEM)
            .setEventAction(ActionKeys.IMPRESSION_ON_POPULAR_BRANDS)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_POPULAR_BRANDS)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.PROMOTIONS, promotions)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendImpressionOnCatalogListEvent(
        businessUnit: String,
        currentSite: String,
        itemList: String,
        items: JSONArray,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_ITEM_LIST)
            .setEventAction(ActionKeys.IMPRESSION_ON_CATALOG_LIST)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_CATALOG_LIST)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.ITEM_LIST, itemList)
            .setCustomProperty(EventKeys.ITEMS, items)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCategoryOnSpecialCategoriesEvent(
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
            .setEventAction(ActionKeys.CLICK_CATEGORY_ON_SPECIAL_CATEGORIES)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_CATEGORY_ON_SPECIAL_CATEGORIES)
            .setBusinessUnit(businessUnit)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCatalogOnRelevantCatalogsEvent(
        eventLabel: String,
        businessUnit: String,
        catalogId: String,
        currentSite: String,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATALOG_ON_RELEVANT_CATALOGS)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_CATALOG_ON_RELEVANT_CATALOGS)
            .setBusinessUnit(businessUnit)
            .setCustomProperty(EventKeys.CATALOG_ID, catalogId)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickBrandOnPopularBrandsEvent(
        eventLabel: String,
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_BRAND_ON_POPULAR_BRANDS)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_BRAND_ON_POPULAR_BRANDS)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCatalogOnCatalogListEvent(
        eventLabel: String,
        businessUnit: String,
        catalogId: String,
        currentSite: String,
        itemList: String,
        items: JSONArray,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.SELECT_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATALOG_ON_CATALOG_LIST)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_CATALOG_ON_CATALOG_LIST)
            .setBusinessUnit(businessUnit)
            .setCustomProperty(EventKeys.CATALOG_ID, catalogId)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.ITEM_LIST, itemList)
            .setCustomProperty(EventKeys.ITEMS, items)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickLihatSemuaOnSpecialCategoriesEvent(
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_LIHAT_SEMUA_ON_SPECIAL_CATEGORIES)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("")
            .setCustomProperty(
                EventKeys.TRACKER_ID,
                TrackerId.CLICK_LIHAT_SEMUA_ON_SPECIAL_CATEGORIES
            )
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickLihatSemuaOnPopularBrandsEvent(
        businessUnit: String,
        currentSite: String,
        pagePath: String,
        sessionIris: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_LIHAT_SEMUA_ON_POPULAR_BRANDS)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_LIHAT_SEMUA_ON_POPULAR_BRANDS)
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, pagePath)
            .setCustomProperty(EventKeys.SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }
}
