package com.tokopedia.catalog.analytics

import android.app.Activity
import android.content.Context
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

object CatalogDetailAnalytics {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendScreenEvent(context: Activity?, screenName: String?) {
        getTracker().sendScreenAuthenticated(screenName)
    }

    fun sendEvent(context: Context?, event: String?, category: String?,
                  action: String?, label: String?) {
        getTracker().sendGeneralEvent(TrackAppUtils.gtmData(event, category, action, label))
    }


    fun sendECommerceEvent(context: Context?, event: String, category: String,
                           action: String, label: String,
                           ecommerce: HashMap<String, Map<String, List<Map<String, String?>>>>,
                           productId : String
    ) {
        val map = HashMap<String, Any>()
        map[EventKeys.KEY_EVENT] = event
        map[EventKeys.KEY_EVENT_CATEGORY] = category
        map[EventKeys.KEY_EVENT_ACTION] = action
        map[EventKeys.KEY_EVENT_LABEL] = label
        map[EventKeys.KEY_ECOMMERCE] = ecommerce
        map[EventKeys.KEY_PRODUCT_ID] = productId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    @JvmStatic
    fun sendECommerceEvent(context: Context?, event: String, category: String,
                           action: String, label: String,
                           ecommerce: Map<String, List<Map<String, String?>>>,
                           productId : String
    ) {
        val map = HashMap<String, Any>()
        map[EventKeys.KEY_EVENT] = event
        map[EventKeys.KEY_EVENT_CATEGORY] = category
        map[EventKeys.KEY_EVENT_ACTION] = action
        map[EventKeys.KEY_EVENT_LABEL] = label
        map[EventKeys.KEY_ECOMMERCE] = ecommerce
        map[EventKeys.KEY_PRODUCT_ID] = productId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    interface EventKeys {
        companion object {
            const val KEY_EVENT = "event"
            const val KEY_EVENT_CATEGORY = "eventCategory"
            const val KEY_EVENT_ACTION = "eventAction"
            const val KEY_EVENT_LABEL = "eventLabel"
            const val KEY_EVENT_BUSINESSUNIT = "businessUnit"
            const val KEY_EVENT_CURRENTSITE = "currentSite"
            const val KEY_PRODUCT_ID = "productId"
            const val KEY_ECOMMERCE = "ecommerce"

            const val EVENT_CATEGORY = "catalog page"

            const val EVENT_NAME_PRODUCT_CLICK = "productClick"
            const val EVENT_NAME_CATALOG_CLICK = "clickCatalog"
            const val EVENT_NAME_PRODUCT_VIEW = "productView"
        }
    }

    interface CategoryKeys {
        companion object {
            const val PAGE_EVENT_CATEGORY = "catalog page"
        }
    }

    interface ActionKeys {
        companion object {
            const val CLICK_CATALOG_IMAGE = "click catalog image"
            const val DRAG_IMAGE_KNOB = "drag the knob"
            const val CLICK_DYNAMIC_FILTER = "click filter"
            const val CLICK_QUICK_FILTER = "click quick filter"
            const val CLICK_PRODUCT = "click product"
            const val IMPRESSION_PRODUCT = "impression product"
            const val CLICK_THREE_DOTS = "click on 3 dots"
            const val CLICK_MORE_SPECIFICATIONS = "click lihat selengkapnya"
            const val CLICK_TAB_SPECIFICATIONS = "click tab spesifikasi lihat selengkapnya"
            const val CLICK_TAB_DESCRIPTION = "click tab deskripsi lihat selengkapnya"
        }
    }

    interface ScreenNameKeys {
        companion object {
            const val CATALOG_PAGE = "Catalog page"
            const val PRODUCT_LIST = "Catalog page - Product list"
            const val DESCRIPTION = "Catalog page - Bottom sheet 'Deskripsi'"
            const val SPECIFICATIONS = "Catalog page - Bottom sheet 'Spesifikasi'"
        }
    }
}