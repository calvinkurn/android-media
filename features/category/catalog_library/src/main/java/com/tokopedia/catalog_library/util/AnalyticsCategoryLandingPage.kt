package com.tokopedia.catalog_library.util

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import org.json.JSONArray

object AnalyticsCategoryLandingPage {

    private fun getIrisSessionId(): String {
        return TrackApp.getInstance().gtm.irisSessionId
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendImpressionOnTopCatalogsInCategoryEvent(
        categoryId: String,
        categoryName: String,
        promotions: JSONArray,
        userId: String
    ) {
//        val listBundle = Bundle().apply {
//            putString(EventKeys.ITEM_ID, itemId)
//            putString(EventKeys.ITEM_NAME, itemName)
//            putString(EventKeys.CREATIVE_SLOT, creativeSlot.toString())
//            putString(EventKeys.CREATIVE_NAME, EventKeys.CREATIVE_NAME_SPECIAL_VALUE)
//        }
//        val bundle = Bundle().apply {
//            putString(EventKeys.KEY_EVENT, EventKeys.VIEW_ITEM)
//            putString(EventKeys.KEY_EVENT_CATEGORY, CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
//            putString(EventKeys.KEY_EVENT_ACTION, ActionKeys.IMPRESSION_ON_SPECIAL_CATEGORIES)
//            putString(EventKeys.KEY_EVENT_LABEL, "")
//            putString(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_SPECIAL_CATEGORIES)
//            putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
//            putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
//            putString(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
//            putString(EventKeys.SESSION_IRIS, AnalyticsHomePage.getIrisSessionId())
//            putParcelableArrayList(EventKeys.PROMOTIONS, arrayListOf(listBundle))
//            putString(EventKeys.KEY_USER_ID, userId)
//        }
//
//        getTracker().sendEnhanceEcommerceEvent(EventKeys.PROMO_VIEW, bundle)

//        Tracker.Builder()
//            .setEvent(EventKeys.VIEW_ITEM)
//            .setEventAction(ActionKeys.IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY)
//            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_PAGE)
//            .setEventLabel(eventLabel)
//            .setCustomProperty(
//                EventKeys.TRACKER_ID,
//                TrackerId.IMPRESSION_ON_TOP_5_CATALOGS_IN_CATEGORY
//            )
//            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
//            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
//            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
//            .setCustomProperty(
//                EventKeys.PAGE_PATH,
//                "${CatalogLibraryConstant.APP_LINK_KATEGORI}/$categoryId"
//            )
//            .setCustomProperty(EventKeys.PROMOTIONS, promotions)
//            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
//            .setUserId(userId)
//            .build()
//            .send()
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
        trackingQueue: TrackingQueue,
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
            "category: $categoryName - ${product.categoryID} - catalog: ${product.name} - ${product.id} - $position",
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
        trackingQueue.putEETracking(dataLayer)
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
        categoryName: String,
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
            putString(EventKeys.KEY_EVENT_CATEGORY, CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            putString(EventKeys.KEY_EVENT_ACTION, ActionKeys.CLICK_ON_CATALOG_LIST_IN_CATEGORY)
            putString(
                EventKeys.KEY_EVENT_LABEL,
                "category: $categoryName - ${product.categoryID} - catalog: ${product.name} - ${product.id}"
            )
            putString(EventKeys.TRACKER_ID, TrackerId.CLICK_ON_CATALOG_LIST_IN_CATEGORY)
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
