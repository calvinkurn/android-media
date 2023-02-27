package com.tokopedia.catalog_library.util

import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import org.json.JSONArray

object AnalyticsCategoryLandingPage {

    private fun getIrisSessionId(): String {
        return TrackApp.getInstance().gtm.irisSessionId
    }

    fun sendImpressionOnTopCatalogsInCategoryEvent(
        eventLabel: String,
        categoryId: String,
        promotions: JSONArray,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_ITEM)
            .setEventAction(ActionKeys.IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                EventKeys.TRACKER_ID,
                TrackerId.IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY
            )
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(
                EventKeys.PAGE_PATH,
                "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryId"
            )
            .setCustomProperty(EventKeys.PROMOTIONS, promotions)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendImpressionOnMostViralCatalogInCategoryEvent(
        eventLabel: String,
        catalogId: String,
        categoryId: String,
        pagePath: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_CONTENT_IRIS)
            .setEventAction(ActionKeys.IMPRESSION_ON_MOST_VIRAL_CATALOG_IN_CATEGORY)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                EventKeys.TRACKER_ID,
                TrackerId.IMPRESSION_ON_MOST_VIRAL_CATALOG_IN_CATEGORY
            )
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATALOG_ID, catalogId)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(
                EventKeys.PAGE_PATH,
                "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryId"
            )
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendImpressionOnCatalogListInCategoryEvent(
        eventLabel: String,
        categoryId: String,
        itemList: String,
        items: JSONArray,
        pagePath: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_ITEM_LIST)
            .setEventAction(ActionKeys.IMPRESSION_ON_CATALOG_LIST_IN_CATEGORY)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                EventKeys.TRACKER_ID,
                TrackerId.IMPRESSION_ON_CATALOG_LIST_IN_CATEGORY
            )
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.ITEM_LIST, itemList)
            .setCustomProperty(EventKeys.ITEMS, items)
            .setCustomProperty(
                EventKeys.PAGE_PATH,
                "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryId"
            )
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCatalogOnTopCatalogsInCategoryEvent(
        eventLabel: String,
        catalogId: String,
        categoryId: String,
        pagePath: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATALOG_ON_TOP_5_CATALOGS_IN_CATEGORY)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                EventKeys.TRACKER_ID,
                TrackerId.CLICK_CATALOG_ON_TOP_5_CATALOGS_IN_CATEGORY
            )
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATALOG_ID, catalogId)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(
                EventKeys.PAGE_PATH,
                "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryId"
            )
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickOnMostViralCatalogInCategoryEvent(
        eventLabel: String,
        catalogId: String,
        categoryId: String,
        pagePath: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATALOG_ON_MOST_VIRAL_CATALOG_IN_CATEGORY)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                EventKeys.TRACKER_ID,
                TrackerId.CLICK_CATALOG_ON_MOST_VIRAL_CATALOG_IN_CATEGORY
            )
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATALOG_ID, catalogId)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(
                EventKeys.PAGE_PATH,
                "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryId"
            )
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickOnCatalogListInCategoryEvent(
        eventLabel: String,
        catalogId: String,
        categoryId: String,
        itemList: String,
        items: JSONArray,
        pagePath: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.SELECT_CONTENT)
            .setEventAction(ActionKeys.CLICK_ON_CATALOG_LIST_IN_CATEGORY)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_ON_CATALOG_LIST_IN_CATEGORY)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATALOG_ID, catalogId)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.ITEM_LIST, itemList)
            .setCustomProperty(EventKeys.ITEMS, items)
            .setCustomProperty(
                EventKeys.PAGE_PATH,
                "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryId"
            )
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }
}
