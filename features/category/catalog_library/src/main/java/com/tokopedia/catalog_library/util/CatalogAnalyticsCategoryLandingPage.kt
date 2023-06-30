package com.tokopedia.catalog_library.util

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.util.EventKeys.Companion.OPEN_SCREEN
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel

object CatalogAnalyticsCategoryLandingPage {

    private fun getIrisSessionId(): String {
        return TrackApp.getInstance().gtm.irisSessionId
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun openScreenCategoryLandingPage(categoryName: String, isLoggedInStatus: String, categoryId: String, userId: String) {
        Tracker.Builder()
            .setEvent(OPEN_SCREEN)
            .setCustomProperty(
                EventKeys.SCREEN_NAME,
                "/catalog library - kategori - $categoryId - $categoryName"
            )
            .setCustomProperty(
                EventKeys.TRACKER_ID,
                TrackerId.OPEN_SCREEN_CLP
            )
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(
                EventKeys.PAGE_PATH,
                "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryId"
            )
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setCustomProperty(EventKeys.IS_LOGGED_IN_STATUS, isLoggedInStatus)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendImpressionOnTopCatalogsInCategoryEvent(
        trackingQueue: TrackingQueue?,
        categoryName: String,
        categoryId: String,
        catalogName: String,
        catalogId: String,
        position: Int,
        userId: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val promotionMap = HashMap<String, Any>()

        promotionMap[EventKeys.ITEM_ID] = catalogId
        promotionMap[EventKeys.ITEM_NAME] = "top 5 $categoryName terlaris di Toped"
        promotionMap[EventKeys.CREATIVE_SLOT] = (position).toString()
        promotionMap[EventKeys.CREATIVE_NAME] = catalogName
        list.add(promotionMap)
        val eventModel = EventModel(
            EventKeys.PROMO_VIEW,
            CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE,
            ActionKeys.IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY,
            ""
        )
        eventModel.key = "${ActionKeys.IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY}-$position"
        val customDimensionMap = HashMap<String, Any>()
        customDimensionMap[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        customDimensionMap[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        customDimensionMap[EventKeys.KEY_USER_ID] = userId
        customDimensionMap[EventKeys.KEY_EVENT_LABEL] = ("$categoryName - $categoryId")
        customDimensionMap[EventKeys.TRACKER_ID] =
            TrackerId.IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY
        customDimensionMap[EventKeys.PAGE_PATH] = CatalogLibraryConstant.APP_LINK_HOME
        customDimensionMap[EventKeys.SESSION_IRIS] = getIrisSessionId()

        trackingQueue?.putEETracking(
            eventModel,
            hashMapOf(
                EventKeys.KEY_ECOMMERCE to hashMapOf(
                    EventKeys.PROMO_VIEW to hashMapOf(
                        EventKeys.PROMOTIONS to list
                    )
                )
            ),
            customDimensionMap
        )
    }

    /**
     * event-label category: {category-name} - {category-id} - catalog: {catalog-name} - {catalog-id}
     */
    fun sendImpressionOnMostViralCatalogInCategoryEvent(
        categoryName: String,
        categoryId: String,
        catalogName: String,
        catalogId: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_CONTENT_IRIS)
            .setEventAction(ActionKeys.IMPRESSION_ON_MOST_VIRAL_CATALOG_IN_CATEGORY)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
            .setEventLabel("category: $categoryName - $categoryId - catalog: $catalogName - $catalogId")
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

    fun sendImpressionOnCatalogListEvent(
        trackingQueue: TrackingQueue?,
        categoryName: String,
        product: CatalogListResponse.CatalogGetList.CatalogsProduct,
        position: Int,
        userId: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val itemMap = HashMap<String, Any>()

        itemMap[EventKeys.INDEX] = position
        itemMap[EventKeys.ITEM_NAME] = product.name ?: ""
        itemMap[EventKeys.ITEM_BRAND] = product.brand ?: ""
        itemMap[EventKeys.ITEM_CATEGORY] = product.categoryID ?: ""
        itemMap[EventKeys.PRICE] = product.marketPrice ?: ""
        list.add(itemMap)

        val dataLayer = DataLayer.mapOf(
            EventKeys.KEY_EVENT,
            EventKeys.PRODUCT_VIEW,
            EventKeys.KEY_EVENT_CATEGORY,
            CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE,
            EventKeys.KEY_EVENT_ACTION,
            ActionKeys.IMPRESSION_ON_CATALOG_LIST_IN_CATEGORY,
            EventKeys.KEY_EVENT_LABEL,
            "category: $categoryName - ${product.categoryID} ",
            EventKeys.KEY_BUSINESS_UNIT,
            EventKeys.BUSINESS_UNIT_VALUE,
            EventKeys.KEY_CURRENT_SITE,
            EventKeys.CURRENT_SITE_VALUE,
            EventKeys.TRACKER_ID,
            TrackerId.IMPRESSION_ON_CATALOG_LIST_IN_CATEGORY,
            EventKeys.ITEM_LIST,
            "",
            EventKeys.KEY_ECOMMERCE,
            DataLayer.mapOf(
                EventKeys.CURRENCY_CODE,
                EventKeys.IDR,
                EventKeys.IMPRESSIONS,
                list
            ),
            TrackerConstant.USERID,
            userId
        ) as HashMap<String, Any>
        trackingQueue?.putEETracking(dataLayer)
    }

    /**
     *event label category: {category-name} - {category-id} - catalog: {catalog-name} - {catalog-id} - {position}
     * */
    fun sendClickCatalogOnTopCatalogsInCategoryEvent(
        categoryName: String,
        categoryId: String,
        catalogName: String,
        catalogId: String,
        position: Int,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATALOG_ON_TOP_5_CATALOGS_IN_CATEGORY)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
            .setEventLabel("category: $categoryName - $categoryId - catalog: $catalogName - $catalogId -$position ")
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

    /**
     * event label category: {category-name} - {category-id} - catalog: {catalog-name} - {catalog-id}
     * */
    fun sendClickOnMostViralCatalogInCategoryEvent(
        categoryName: String,
        categoryId: String,
        catalogName: String,
        catalogId: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATALOG_ON_MOST_VIRAL_CATALOG_IN_CATEGORY)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
            .setEventLabel("category: $categoryName - $categoryId - catalog: $catalogName - $catalogId")
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

    fun sendClickCatalogOnCatalogListEvent(
        eventLabel: String,
        eventAction: String,
        eventCategory: String,
        trackerId: String,
        product: CatalogListResponse.CatalogGetList.CatalogsProduct,
        position: Int,
        userId: String
    ) {
        val listBundle = Bundle().apply {
            putString(EventKeys.INDEX, position.toString())
            putString(EventKeys.ITEM_NAME, product.name)
            putString(EventKeys.PRICE, product.marketPrice.toString())
            putString(EventKeys.ITEM_NAME, product.name)
            putString(EventKeys.ITEM_BRAND, product.brand)
            putString(EventKeys.ITEM_CATEGORY, product.categoryID)
        }
        val bundle = Bundle().apply {
            putString(EventKeys.KEY_EVENT, EventKeys.SELECT_CONTENT)
            putString(EventKeys.KEY_EVENT_CATEGORY, eventCategory)
            putString(EventKeys.KEY_EVENT_ACTION, eventAction)
            putString(
                EventKeys.KEY_EVENT_LABEL,
                eventLabel
            )
            putString(EventKeys.TRACKER_ID, trackerId)
            putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
            putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
            putString(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
            putString(EventKeys.SESSION_IRIS, getIrisSessionId())
            putString(EventKeys.ITEM_LIST, "")
            putParcelableArrayList(EventKeys.ITEMS, arrayListOf(listBundle))
            putString(EventKeys.KEY_USER_ID, userId)
        }
        getTracker().sendEnhanceEcommerceEvent(EventKeys.PRODUCT_CLICK, bundle)
    }
}
