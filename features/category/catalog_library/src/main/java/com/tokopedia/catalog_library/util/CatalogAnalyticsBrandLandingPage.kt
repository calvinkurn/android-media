package com.tokopedia.catalog_library.util

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.util.CategoryKeys.Companion.CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.trackingoptimizer.TrackingQueue

object CatalogAnalyticsBrandLandingPage {

    fun sendImpressionOnCatalogListEvent(
        trackingQueue: TrackingQueue?,
        brandName: String,
        brandId: String,
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
            EventKeys.KEY_EVENT, EventKeys.VIEW_ITEM_LIST,
            EventKeys.KEY_EVENT_CATEGORY, CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE,
            EventKeys.KEY_EVENT_ACTION, ActionKeys.IMPRESSION_ON_CATALOG,
            EventKeys.KEY_EVENT_LABEL, "brand page: $brandName - $brandId - category tab: {category-name/$categoryName} - {category-id/${product.categoryID}}",
            EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE,
            EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE,
            EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_CATALOG_LIST_BRAND,
            EventKeys.ITEM_LIST, "",
            EventKeys.KEY_ECOMMERCE,
            DataLayer.mapOf(
                EventKeys.CURRENCY_CODE,
                EventKeys.IDR,
                EventKeys.IMPRESSIONS,
                list
            ),
            TrackerConstant.USERID, userId
        ) as HashMap<String, Any>
        trackingQueue?.putEETracking(dataLayer)
    }

    fun sendOnTabClickEvent(
        trackingQueue: TrackingQueue?,
        brandName: String,
        brandId: String,
        categoryName: String,
        categoryId: String,
        position: Int,
        userId: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val itemMap = HashMap<String, Any>()

        itemMap[EventKeys.CREATIVE_NAME] = categoryName
        itemMap[EventKeys.CREATIVE_SLOT] = position
        itemMap[EventKeys.ITEM_ID] = categoryId
        itemMap[EventKeys.ITEM_NAME] = "category tab"
        list.add(itemMap)

        val dataLayer = DataLayer.mapOf(
            EventKeys.KEY_EVENT, EventKeys.SELECT_CONTENT,
            EventKeys.KEY_EVENT_CATEGORY, CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE,
            EventKeys.KEY_EVENT_ACTION, "click on category tab",
            EventKeys.KEY_EVENT_LABEL, "ea click on category tab - brand page: $brandName - $brandId - category tab: {category-name/$categoryName} - {category-id/$categoryId}",
            EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE,
            EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE,
            EventKeys.TRACKER_ID, "37374",
            EventKeys.CATEGORY_ID, categoryId,
            EventKeys.ITEM_LIST, "",
            EventKeys.KEY_ECOMMERCE,
            DataLayer.mapOf(
                EventKeys.CURRENCY_CODE,
                EventKeys.IDR,
                EventKeys.IMPRESSIONS,
                list
            ),
            TrackerConstant.USERID, userId
        ) as HashMap<String, Any>
        trackingQueue?.putEETracking(dataLayer)
    }

    fun sendClickExpandBottomSheetButtonEvent(eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction("click expand bottom sheet button")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37378")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCloseBottomSheetButtonEvent(eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction("click close bottom sheet button")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37379")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCollapseExpandOnBottomSheetEvent(eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction("click collapse expand on bottom sheet")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37380")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCategoryOnBottomSheetEvent(eventLabel: String, categoryId: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATEGORY_ON_BS)
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37383")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }
}
