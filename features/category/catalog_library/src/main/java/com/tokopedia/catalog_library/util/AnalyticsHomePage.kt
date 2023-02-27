package com.tokopedia.catalog_library.util

import android.os.Bundle
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.util.EventKeys.Companion.CREATIVE_NAME
import com.tokopedia.catalog_library.util.EventKeys.Companion.CREATIVE_NAME_RELEVANT_VALUE
import com.tokopedia.catalog_library.util.EventKeys.Companion.CREATIVE_NAME_SPECIAL_VALUE
import com.tokopedia.catalog_library.util.EventKeys.Companion.CREATIVE_SLOT
import com.tokopedia.catalog_library.util.EventKeys.Companion.INDEX
import com.tokopedia.catalog_library.util.EventKeys.Companion.ITEM_BRAND
import com.tokopedia.catalog_library.util.EventKeys.Companion.ITEM_CATEGORY
import com.tokopedia.catalog_library.util.EventKeys.Companion.ITEM_ID
import com.tokopedia.catalog_library.util.EventKeys.Companion.ITEM_NAME
import com.tokopedia.catalog_library.util.EventKeys.Companion.PRICE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.interfaces.Analytics

object AnalyticsHomePage {

    private fun getIrisSessionId(): String {
        return TrackApp.getInstance().gtm.irisSessionId
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendImpressionOnSpecialCategoriesEvent(
        creativeSlot: Int,
        itemId: String,
        itemName: String,
        userId: String
    ) {
        val listBundle = Bundle().apply {
            putString(ITEM_ID, itemId)
            putString(ITEM_NAME, itemName)
            putString(CREATIVE_SLOT, creativeSlot.toString())
            putString(CREATIVE_NAME, CREATIVE_NAME_SPECIAL_VALUE)
        }
        val bundle = Bundle().apply {
            putString(EventKeys.KEY_EVENT, EventKeys.VIEW_ITEM)
            putString(EventKeys.KEY_EVENT_CATEGORY, CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            putString(EventKeys.KEY_EVENT_ACTION, ActionKeys.IMPRESSION_ON_SPECIAL_CATEGORIES)
            putString(EventKeys.KEY_EVENT_LABEL, "")
            putString(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_SPECIAL_CATEGORIES)
            putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
            putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
            putString(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
            putString(EventKeys.SESSION_IRIS, getIrisSessionId())
            putParcelableArrayList(EventKeys.PROMOTIONS, arrayListOf(listBundle))
            putString(EventKeys.KEY_USER_ID, userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EventKeys.PROMO_VIEW, bundle)
    }

    fun sendImpressionOnRelevantCatalogsEvent(
        creativeSlot: Int,
        itemId: String,
        itemName: String,
        userId: String
    ) {
        val listBundle = Bundle().apply {
            putString(ITEM_ID, itemId)
            putString(ITEM_NAME, itemName)
            putString(CREATIVE_SLOT, creativeSlot.toString())
            putString(CREATIVE_NAME, CREATIVE_NAME_RELEVANT_VALUE)
        }
        val bundle = Bundle().apply {
            putString(EventKeys.KEY_EVENT, EventKeys.VIEW_ITEM)
            putString(EventKeys.KEY_EVENT_CATEGORY, CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            putString(EventKeys.KEY_EVENT_ACTION, ActionKeys.IMPRESSION_ON_RELEVANT_CATALOGS)
            putString(EventKeys.KEY_EVENT_LABEL, "")
            putString(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_RELEVANT_CATALOGS)
            putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
            putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
            putString(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
            putString(EventKeys.SESSION_IRIS, getIrisSessionId())
            putParcelableArrayList(EventKeys.PROMOTIONS, arrayListOf(listBundle))
            putString(EventKeys.KEY_USER_ID, userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EventKeys.PROMO_VIEW, bundle)
    }

    fun sendImpressionOnPopularBrandsEvent(
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.VIEW_ITEM)
            .setEventAction(ActionKeys.IMPRESSION_ON_POPULAR_BRANDS)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_POPULAR_BRANDS)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
//            .setCustomProperty(EventKeys.PROMOTIONS, promotions)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendImpressionOnCatalogListEvent(
        product: CatalogListResponse.CatalogGetList.CatalogsProduct,
        position: Int,
        userId: String
    ) {
        val listBundle = Bundle().apply {
            putString(INDEX, position.toString())
            putString(ITEM_NAME, product.name)
            putString(PRICE, product.marketPrice.toString())
            putString(ITEM_NAME, product.name)
            putString(ITEM_BRAND, product.brand)
            putString(ITEM_CATEGORY, product.categoryID)
        }
        val bundle = Bundle().apply {
            putString(EventKeys.KEY_EVENT, EventKeys.VIEW_ITEM_LIST)
            putString(EventKeys.KEY_EVENT_CATEGORY, CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            putString(EventKeys.KEY_EVENT_ACTION, ActionKeys.IMPRESSION_ON_CATALOG_LIST)
            putString(EventKeys.KEY_EVENT_LABEL, "")
            putString(EventKeys.TRACKER_ID, TrackerId.IMPRESSION_ON_CATALOG_LIST)
            putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
            putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
            putString(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
            putString(EventKeys.SESSION_IRIS, getIrisSessionId())
            putString(EventKeys.ITEM_LIST, "")
            putParcelableArrayList(EventKeys.ITEMS, arrayListOf(listBundle))
            putString(EventKeys.KEY_USER_ID, userId)
        }
        getTracker().sendEnhanceEcommerceEvent(EventKeys.PRODUCT_VIEW, bundle)
    }

    /**
     * event Label = {category-name} - {category-id}
     */
    fun sendClickCategoryOnSpecialCategoriesEvent(
        categoryName: String,
        categoryId: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATEGORY_ON_SPECIAL_CATEGORIES)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("$categoryName-$categoryId")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_CATEGORY_ON_SPECIAL_CATEGORIES)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * event Label = {catalog-name} - {catalog-id} - {position}
     */
    fun sendClickCatalogOnRelevantCatalogsEvent(
        catalogName: String,
        position: Int,
        catalogId: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATALOG_ON_RELEVANT_CATALOGS)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("$catalogName-$catalogId-$position")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_CATALOG_ON_RELEVANT_CATALOGS)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATALOG_ID, catalogId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * event Label = {brand-name} - {brand-id} - {position}
     */
    fun sendClickBrandOnPopularBrandsEvent(
        eventLabel: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_BRAND_ON_POPULAR_BRANDS)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_BRAND_ON_POPULAR_BRANDS)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * event Label = {catalog-name} - {catalog-id}
     */
    fun sendClickCatalogOnCatalogListEvent(
        product: CatalogListResponse.CatalogGetList.CatalogsProduct,
        position: Int,
        userId: String
    ) {
        val listBundle = Bundle().apply {
            putString(INDEX, position.toString())
            putString(ITEM_NAME, product.name)
            putString(PRICE, product.marketPrice.toString())
            putString(ITEM_NAME, product.name)
            putString(ITEM_BRAND, product.brand)
            putString(ITEM_CATEGORY, product.categoryID)
        }
        val bundle = Bundle().apply {
            putString(EventKeys.KEY_EVENT, EventKeys.SELECT_CONTENT)
            putString(EventKeys.KEY_EVENT_CATEGORY, CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            putString(EventKeys.KEY_EVENT_ACTION, ActionKeys.CLICK_CATALOG_ON_CATALOG_LIST)
            putString(EventKeys.KEY_EVENT_LABEL, "")
            putString(EventKeys.TRACKER_ID, TrackerId.CLICK_CATALOG_ON_CATALOG_LIST)
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

    fun sendClickLihatSemuaOnSpecialCategoriesEvent(
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
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickLihatSemuaOnPopularBrandsEvent(
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_LIHAT_SEMUA_ON_POPULAR_BRANDS)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_LANDING_PAGE)
            .setEventLabel("")
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_LIHAT_SEMUA_ON_POPULAR_BRANDS)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_HOME)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId())
            .setUserId(userId)
            .build()
            .send()
    }
}
