package com.tokopedia.catalog.analytics

import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

object CatalogDetailAnalytics {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendEvent(event: String, category: String,
                  action: String, label: String, userId : String) {
        HashMap<String,Any>().apply {
            put(EventKeys.KEY_EVENT,event)
            put(EventKeys.KEY_EVENT_CATEGORY,category)
            put(EventKeys.KEY_EVENT_ACTION,action)
            put(EventKeys.KEY_EVENT_LABEL,label)
            put(EventKeys.KEY_USER_ID,userId)
            put(EventKeys.KEY_BUSINESS_UNIT,EventKeys.BUSINESS_UNIT_VALUE)
            put(EventKeys.KEY_CURRENT_SITE,EventKeys.CURRENT_SITE_VALUE)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }


    fun sendECommerceEvent(event: String, category: String,
                           action: String, label: String,
                           ecommerce: HashMap<String, Map<String, List<Map<String, String?>>>>,
                           productId : String, userId: String
    ) {
        val map = HashMap<String, Any>()
        map[EventKeys.KEY_EVENT] = event
        map[EventKeys.KEY_EVENT_CATEGORY] = category
        map[EventKeys.KEY_EVENT_ACTION] = action
        map[EventKeys.KEY_EVENT_LABEL] = label
        map[EventKeys.KEY_ECOMMERCE] = ecommerce
        map[EventKeys.KEY_PRODUCT_ID] = productId
        map[EventKeys.KEY_USER_ID] = userId
        map[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        map[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    @JvmStatic
    fun sendECommerceEvent(event: String, category: String,
                           action: String, label: String,
                           ecommerce: Map<String, List<Map<String, String?>>>,
                           productId : String, userId: String
    ) {
        val map = HashMap<String, Any>()
        map[EventKeys.KEY_EVENT] = event
        map[EventKeys.KEY_EVENT_CATEGORY] = category
        map[EventKeys.KEY_EVENT_ACTION] = action
        map[EventKeys.KEY_EVENT_LABEL] = label
        map[EventKeys.KEY_ECOMMERCE] = ecommerce
        map[EventKeys.KEY_PRODUCT_ID] = productId
        map[EventKeys.KEY_USER_ID] = userId
        map[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        map[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    interface EventKeys {
        companion object {
            const val KEY_EVENT = "event"
            const val KEY_EVENT_CATEGORY = "eventCategory"
            const val KEY_EVENT_ACTION = "eventAction"
            const val KEY_EVENT_LABEL = "eventLabel"
            const val KEY_PRODUCT_ID = "productId"
            const val KEY_ECOMMERCE = "ecommerce"
            const val KEY_USER_ID = "userId"

            const val KEY_BUSINESS_UNIT = "businessUnit"
            const val KEY_CURRENT_SITE = "currentSite"

            const val BUSINESS_UNIT_VALUE= "Physical Goods"
            const val CURRENT_SITE_VALUE = "tokopediamarketplace"

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
            const val ACTION_ADD_WISHLIST = "add wishlist"
            const val ACTION_REMOVE_WISHLIST = "remove wishlist"

            const val CLICK_MORE_DESCRIPTION = "click lihat selengkapnya deskripsi"
            const val CLICK_MORE_SPECIFICATIONS = "click lihat selengkapnya spesifikasi"
            const val CLICK_TAB_SPECIFICATIONS = "click tab spesifikasi lihat selengkapnya"
            const val CLICK_TAB_DESCRIPTION = "click tab deskripsi lihat selengkapnya"
        }
    }

    interface KEYS {
        companion object {
            const val NONE_OTHER = "none / other"
            const val BRAND = "brand"
            const val CATEGORY = "category"
            const val DIMENSION61 = "dimensions61"
            const val ID = "id"
            const val LIST = "list"
            const val NAME = "name"
            const val POSITION = "position"
            const val PRICE = "price"
            const val VARIANT = "variant"
            const val IMPRESSION = "impression"
            const val PRODUCTS = "products"
            const val CLICK = "click"

        }
    }
}