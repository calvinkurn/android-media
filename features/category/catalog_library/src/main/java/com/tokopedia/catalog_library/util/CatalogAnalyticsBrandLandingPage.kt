package com.tokopedia.catalog_library.util

import com.tokopedia.catalog_library.util.CategoryKeys.Companion.CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE
import com.tokopedia.track.builder.Tracker

object CatalogAnalyticsBrandLandingPage {

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3502
    // Tracker ID: 37373
    fun sendImpressOnCategoryTabEvent (eventLabel: String, promotions: String, userId: String) {
        Tracker.Builder()
            .setEvent("view_item")
            .setEventAction("impress on category tab")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37373")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setCustomProperty("promotions", promotions)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3502
    // Tracker ID: 37374
    fun sendClickOnCategoryTabEvent (eventLabel: String, categoryId: String, promotions: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.SELECT_CONTENT)
            .setEventAction("click on category tab")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37374")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setCustomProperty("promotions", promotions)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3502
    // Tracker ID: 37375
    fun sendImpressCatalogEvent (eventLabel: String, categoryId: String, itemList: String, items: String, userId: String) {
        Tracker.Builder()
            .setEvent("view_item_list")
            .setEventAction("impress catalog")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37375")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty("item_list", itemList)
            .setCustomProperty("items", items)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3502
    // Tracker ID: 37376
    fun sendClickCatalogEvent (eventLabel: String, catalogId: String, categoryId: String, itemList: String, items: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.SELECT_CONTENT)
            .setEventAction("click catalog")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37376")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty("catalogId", catalogId)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty("item_list", itemList)
            .setCustomProperty("items", items)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3502
    // Tracker ID: 37378
    fun sendClickExpandBottomSheetButtonEvent (eventLabel: String, businessUnit: String, currentSite: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction("click expand bottom sheet button")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37378")
            .setBusinessUnit(businessUnit)
            .setCurrentSite(currentSite)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCloseBottomSheetButtonEvent (eventLabel: String, businessUnit: String, currentSite: String, userId: String) {
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

    fun sendClickCollapseExpandOnBottomSheetEvent (eventLabel: String, businessUnit: String, currentSite: String, userId: String) {
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

    fun sendImpressCategoryOnBottomSheetEvent (eventLabel: String, itemList: String, items: String, userId: String) {
        Tracker.Builder()
            .setEvent("view_item_list")
            .setEventAction("impress category on bottom sheet")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37382")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty("item_list", itemList)
            .setCustomProperty("items", items)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCategoryOnBottomSheetEvent (eventLabel: String, categoryId: String, userId: String) {
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

