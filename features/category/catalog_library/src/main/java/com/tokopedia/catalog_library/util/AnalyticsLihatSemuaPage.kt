package com.tokopedia.catalog_library.util

import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel

object AnalyticsLihatSemuaPage {
    private fun getIrisSessionId(): String {
        return TrackApp.getInstance().gtm.irisSessionId
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendImpressionOnCategoryListEvent(
        trackingQueue: TrackingQueue,
        parentCategoryName: String,
        parentCategoryId: String,
        categoryName: String,
        categoryId: String,
        isGrid: Boolean,
        isAsc: Boolean,
        position: Int,
        userId: String
    ) {
        val order = if (isAsc) "ascending order" else "descending order"
        val view = if (isGrid) "grid view" else "list view"

        val list = ArrayList<Map<String, Any>>()
        val promotionMap = HashMap<String, Any>()

        promotionMap[EventKeys.ITEM_ID] = categoryId
        promotionMap[EventKeys.DIMENSION61] = "$view - $order"
        promotionMap[EventKeys.ITEM_NAME] = categoryName
        promotionMap[EventKeys.CREATIVE_SLOT] = (position).toString()
        promotionMap[EventKeys.CREATIVE_NAME] = EventKeys.CREATIVE_NAME_CATEGORY_LIST_VALUE
        list.add(promotionMap)
        val eventModel = EventModel(
            EventKeys.PROMO_VIEW,
            CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA,
            ActionKeys.IMPRESSION_ON_CATEGORY_LIST,
            ""
        )
        eventModel.key = "${ActionKeys.IMPRESSION_ON_CATEGORY_LIST}-$parentCategoryId-$position"
        val customDimensionMap = HashMap<String, Any>()
        customDimensionMap[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        customDimensionMap[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        customDimensionMap[EventKeys.KEY_USER_ID] = userId
        customDimensionMap[EventKeys.KEY_EVENT_LABEL] =
            ("L1 name: $parentCategoryName - L1 ID: $parentCategoryId - sort & filter: $view - $order")
        customDimensionMap[EventKeys.TRACKER_ID] = TrackerId.IMPRESSION_ON_CATEGORY_LIST
        customDimensionMap[EventKeys.PAGE_PATH] = CatalogLibraryConstant.APP_LINK_HOME
        customDimensionMap[EventKeys.SESSION_IRIS] = getIrisSessionId()

        trackingQueue.putEETracking(
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

    fun sendClickAscendingDescendingSortEvent(
        eventLabel: String,
        userId: String
    ) {
        Tracker.Builder().setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_ASCENDING_DESCENDING_SORT)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_ASCENDING_DESCENDING_SORT)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_KATEGORI)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId()).setUserId(userId).build()
            .send()
    }

    fun sendClickGridListViewEvent(
        eventLabel: String,
        userId: String
    ) {
        Tracker.Builder().setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_GRID_LIST_VIEW)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_GRID_LIST_VIEW)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_KATEGORI)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId()).setUserId(userId).build()
            .send()
    }

    fun sendClickCategoryOnCategoryListEvent(
        eventLabel: String,
        categoryId: String,
        userId: String
    ) {
        Tracker.Builder().setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_CATEGORY_ON_CATEGORY_LIST)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_CATEGORY_ON_CATEGORY_LIST)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.CATEGORY_ID, categoryId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_KATEGORI)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId()).setUserId(userId).build()
            .send()
    }

    fun sendClickDropUpButtonEvent(
        eventLabel: String,
        userId: String
    ) {
        Tracker.Builder().setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(ActionKeys.CLICK_DROP_UP_BUTTON)
            .setEventCategory(CategoryKeys.CATALOG_LIBRARY_CATEGORY_LIHAT_SEMUHA)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, TrackerId.CLICK_DROP_UP_BUTTON)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_KATEGORI)
            .setCustomProperty(EventKeys.SESSION_IRIS, getIrisSessionId()).setUserId(userId).build()
            .send()
    }
}
