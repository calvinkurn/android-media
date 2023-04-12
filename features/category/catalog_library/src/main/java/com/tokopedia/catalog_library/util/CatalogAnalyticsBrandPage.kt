package com.tokopedia.catalog_library.util

import android.os.Bundle
import com.tokopedia.catalog_library.model.raw.CatalogBrandsPopularResponse
import com.tokopedia.catalog_library.util.CategoryKeys.Companion.CATALOG_LIBRARY_POPULAR_BRAND_PAGE
import com.tokopedia.catalog_library.util.EventKeys.Companion.PROMOTIONS
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker

object CatalogAnalyticsBrandPage {

    fun sendImpressOnLihatButtonEvent(eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent("viewContentIris")
            .setEventAction(ActionKeys.IMPRESS_LIHAT_SEMUA_POPULAR_PAGE)
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37368")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_POPULAR_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendItemImpression(
        userId: String,
        position: Int,
        item: CatalogBrandsPopularResponse.CatalogGetBrandPopular.Brands

    ) {
        val event = EventKeys.VIEW_ITEM
        val arrayListBundle = arrayListOf<Bundle>()
        val bundle = Bundle()
        item.catalogs.forEachIndexed { index, catalog ->
            arrayListBundle.add(
                Bundle().apply {
                    putString(EventKeys.ITEM_ID, catalog.id)
                    putString(EventKeys.CREATIVE_SLOT, (index + 1).toString())
                    putString(EventKeys.ITEM_NAME, item.name)
                    putString(EventKeys.CREATIVE_NAME, catalog.name)
                }
            )
        }
        bundle.putString(EventKeys.KEY_EVENT, event)
        bundle.putString(EventKeys.KEY_EVENT_CATEGORY, CATALOG_LIBRARY_POPULAR_BRAND_PAGE)
        bundle.putString(EventKeys.KEY_EVENT_ACTION, ActionKeys.IMPRESSION_ON_CATALOG)
        bundle.putString(EventKeys.KEY_EVENT_LABEL, "${item.name} - ${item.id} - position: $position")
        bundle.putString(EventKeys.KEY_BUSINESS_UNIT, EventKeys.BUSINESS_UNIT_VALUE)
        bundle.putString(EventKeys.KEY_CURRENT_SITE, EventKeys.CURRENT_SITE_VALUE)
        bundle.putString(EventKeys.KEY_USER_ID, userId)
        bundle.putString(EventKeys.TRACKER_ID, "37371")
        bundle.putParcelableArrayList(PROMOTIONS, arrayListBundle)

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(event, bundle)
    }

    fun sendClickOnLihatButtonEvent(trackerId: String, eventAction: String, eventLabel: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction(eventAction)
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, trackerId)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_POPULAR_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickOnCatalogEvent(eventLabel: String, catalogId: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.CLICK_CONTENT)
            .setEventAction("click on catalog")
            .setEventCategory(CATALOG_LIBRARY_POPULAR_BRAND_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(EventKeys.TRACKER_ID, "37372")
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty(EventKeys.CATALOG_ID, catalogId)
            .setCustomProperty(EventKeys.PAGE_PATH, CatalogLibraryConstant.APP_LINK_POPULAR_BRANDS)
            .setUserId(userId)
            .build()
            .send()
    }
}
