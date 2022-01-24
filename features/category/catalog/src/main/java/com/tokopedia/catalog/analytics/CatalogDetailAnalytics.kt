package com.tokopedia.catalog.analytics

import com.tokopedia.catalog.analytics.CatalogDetailAnalytics.KEYS.Companion.CATALOG_URL_KEY
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.model.util.CatalogUtil
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

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

    fun sendSharingExperienceEvent(event: String,
                  action: String, category: String, label: String, catalogId : String,
                                   pageSource : String = "",userId : String) {
        HashMap<String,Any>().apply {
            put(EventKeys.KEY_EVENT,event)
            put(EventKeys.KEY_EVENT_ACTION,action)
            put(EventKeys.KEY_EVENT_CATEGORY,category)
            put(EventKeys.KEY_EVENT_LABEL,label)
            put(EventKeys.KEY_BUSINESS_UNIT,EventKeys.SHARING_EXPERIENCE_BUSINESS_UNIT_VALUE)
            put(EventKeys.KEY_CATALOG_ID,catalogId)
            put(EventKeys.KEY_CURRENT_SITE,EventKeys.CURRENT_SITE_VALUE)
            if(pageSource.isNotBlank()){
                put(EventKeys.KEY_PAGE_SOURCE,pageSource)
            }
            put(EventKeys.KEY_USER_ID,userId)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    fun trackEventImpressionProductCard(catalogId : String, catalogUrl : String, userId : String ,
                                        item : CatalogProductItem, position : String,
                                        searchFilterMap : HashMap<String,String>?){
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[KEYS.BRAND] = KEYS.NONE_OTHER
        productMap[KEYS.CATEGORY] = item.categoryId.toString()
        productMap[KEYS.ID] = item.id
        productMap[KEYS.LIST] = getCatalogTrackingUrl(catalogUrl)
        productMap[KEYS.NAME] = item.name
        productMap[KEYS.DIMENSION61] = CatalogUtil.getSortFilterAnalytics(searchFilterMap)
        productMap[KEYS.POSITION] = position
        productMap[KEYS.PRICE] = CurrencyFormatHelper.convertRupiahToInt(CurrencyFormatHelper.convertRupiahToInt(item.priceString).toString()).toString()
        productMap[KEYS.VARIANT] = KEYS.NONE_OTHER
        list.add(productMap)

        val eCommerce = mapOf(
                KEYS.CURRENCY_CODE to KEYS.IDR,
                KEYS.IMPRESSION to list)
        val map = HashMap<String,Any>()
        map[EventKeys.KEY_EVENT] = EventKeys.EVENT_NAME_PRODUCT_VIEW
        map[EventKeys.KEY_EVENT_CATEGORY] = CategoryKeys.PAGE_EVENT_CATEGORY
        map[EventKeys.KEY_EVENT_ACTION] = ActionKeys.IMPRESSION_PRODUCT
        map[EventKeys.KEY_EVENT_LABEL] = catalogId
        map[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        map[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        map[EventKeys.KEY_ECOMMERCE] = eCommerce
        map[EventKeys.KEY_PRODUCT_ID] = item.id
        map[EventKeys.KEY_USER_ID] = userId

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackProductCardClick(catalogId : String,  catalogUrl : String, userId : String ,
                              item : CatalogProductItem, position : String ,
                              searchFilterMap : HashMap<String,String>?) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[KEYS.BRAND] = KEYS.NONE_OTHER
        productMap[KEYS.CATEGORY] = item.categoryId.toString()
        productMap[KEYS.ID] = item.id
        productMap[KEYS.LIST] = getCatalogTrackingUrl(catalogUrl)
        productMap[KEYS.NAME] = item.name
        productMap[KEYS.DIMENSION61] = CatalogUtil.getSortFilterAnalytics(searchFilterMap)
        productMap[KEYS.POSITION] = position
        productMap[KEYS.PRICE] = CurrencyFormatHelper.convertRupiahToInt(CurrencyFormatHelper.convertRupiahToInt(item.priceString).toString()).toString()
        productMap[KEYS.VARIANT] = KEYS.NONE_OTHER
        list.add(productMap)


        val eCommerce = mapOf(
                KEYS.CLICK to mapOf(
                        KEYS.ACTION_FIELD to mapOf(
                                KEYS.LIST to getCatalogTrackingUrl(catalogUrl)
                        ),
                        KEYS.PRODUCTS to list
                )
        )
        val map = HashMap<String,Any>()
        map[EventKeys.KEY_EVENT] = EventKeys.EVENT_NAME_PRODUCT_CLICK
        map[EventKeys.KEY_EVENT_CATEGORY] = CategoryKeys.PAGE_EVENT_CATEGORY
        map[EventKeys.KEY_EVENT_ACTION] = ActionKeys.CLICK_PRODUCT
        map[EventKeys.KEY_EVENT_LABEL] = catalogId
        map[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        map[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        map[EventKeys.KEY_ECOMMERCE] = eCommerce
        map[EventKeys.KEY_PRODUCT_ID] = item.id
        map[EventKeys.KEY_USER_ID] = userId
        map[KEYS.CAMPAIGN_CODE] = ""

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
            const val KEY_CATALOG_ID = "catalogId"
            const val KEY_PAGE_SOURCE = "pageSource"

            const val KEY_BUSINESS_UNIT = "businessUnit"
            const val KEY_CURRENT_SITE = "currentSite"

            const val BUSINESS_UNIT_VALUE= "Physical Goods"
            const val SHARING_EXPERIENCE_BUSINESS_UNIT_VALUE = "sharingexperience"
            const val CURRENT_SITE_VALUE = "tokopediamarketplace"

            const val EVENT_CATEGORY = "catalog page"

            const val EVENT_NAME_PRODUCT_CLICK = "productClick"
            const val EVENT_NAME_CATALOG_CLICK = "clickCatalog"
            const val EVENT_NAME_VIEW_CATALOG_IRIS = "viewCatalogIris"
            const val EVENT_NAME_PRODUCT_VIEW = "productView"
        }
    }

    interface CategoryKeys {
        companion object {
            const val TOP_NAV_CATALOG = "top nav - catalog page"
            const val PAGE_EVENT_CATEGORY = "catalog page"
        }
    }

    interface ActionKeys {
        companion object {
            const val CLICK_ACCESS_PHOTO_FILES = "click - access photo media and files"
            const val CLICK_CHANNEL_SBS_SCREENSHOT = "click - channel share bottom sheet - screenshot"
            const val CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET = "click - close screenshot share bottom sheet"
            const val VIEW_SCREENSHOT_SHARE_BOTTOM_SHEET = "view - screenshot share bottom sheet"
            const val VIEW_ON_SHARING_CHANNEL = "view on sharing channel"
            const val CLICK_SHARING_CHANNEL = "click - sharing channel"
            const val CLICK_CLOSE_SHARE_BOTTOM_SHEET = "click - close share bottom sheet"
            const val CLICK_SHARE_BUTTON = "click - share button"
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
            const val CLICK_VIDEO_WIDGET = "click video widget"
            const val CLICK_COMPARISION_CATALOG = "click compared catalog on perbandingan preview"
            const val CLICK_SHARE = "click share"
            const val CLICK_BACK_BUTTON = "click back button"
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
            const val IMPRESSION = "impressions"
            const val PRODUCTS = "products"
            const val CLICK = "click"
            const val ACTION_FIELD = "actionField"
            const val CURRENCY_CODE = "currencyCode"
            const val CAMPAIGN_CODE = "campaignCode"

            const val IDR = "IDR"

            const val CATALOG_URL_KEY = "/catalog/"
        }
    }

    private fun getCatalogTrackingUrl(catalogUrl : String?) : String {
        if (!catalogUrl.isNullOrEmpty()){
            catalogUrl.split(CATALOG_URL_KEY).last().let {
                return "${CATALOG_URL_KEY}$it"
            }
        }
        return ""
    }
}