package com.tokopedia.oldcatalog.analytics

import android.os.Bundle
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics.ActionKeys.Companion.KATALOG_PiILIHAN_UNTUKMU
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics.EventKeys.Companion.EVENT_PROMO_VIEW
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics.EventKeys.Companion.KEY_ECOMMERCE
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics.EventKeys.Companion.KEY_PROMOTIONS
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics.KEYS.Companion.CATALOG_URL_KEY
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics.TrackerId.Companion.CLICK_CATALOG_ENTRY_POINT
import com.tokopedia.oldcatalog.model.raw.CatalogProductItem
import com.tokopedia.oldcatalog.model.util.CatalogConstant
import com.tokopedia.oldcatalog.model.util.CatalogUtil
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

object CatalogDetailAnalytics {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendEvent(event: String, category: String,
                  action: String, label: String, userId : String, catalogId: String, trackerId: String?="") {
        HashMap<String,Any>().apply {
            put(EventKeys.KEY_EVENT,event)
            put(EventKeys.KEY_EVENT_CATEGORY,category)
            put(EventKeys.KEY_EVENT_ACTION,action)
            put(EventKeys.KEY_EVENT_LABEL,label)
            put(EventKeys.KEY_USER_ID,userId)
            put(EventKeys.KEY_CATALOG_ID,catalogId)
            put(EventKeys.KEY_BUSINESS_UNIT,EventKeys.BUSINESS_UNIT_VALUE)
            put(EventKeys.KEY_CURRENT_SITE,EventKeys.CURRENT_SITE_VALUE)
            if(!trackerId.isNullOrEmpty()){
                put(EventKeys.KEY_TRACKER_ID, trackerId)
            }
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
            if(userId.isNotBlank()){
                put(EventKeys.KEY_USER_ID,userId)
            }else {
                put(EventKeys.KEY_USER_ID,"0")
            }
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    fun trackEventImpressionProductCard(catalogName : String, catalogId : String, catalogUrl : String, userId : String ,
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
        map[EventKeys.KEY_EVENT_LABEL] = "$catalogName - $catalogId"
        map[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        map[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        map[EventKeys.KEY_ECOMMERCE] = eCommerce
        map[EventKeys.KEY_PRODUCT_ID] = item.id
        map[EventKeys.KEY_USER_ID] = userId
        map[EventKeys.KEY_CATALOG_ID] = catalogId

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackProductCardClick(catalogName : String, catalogId : String,  catalogUrl : String, userId : String ,
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
        map[EventKeys.KEY_EVENT_LABEL] = "$catalogName - $catalogId"
        map[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        map[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        map[EventKeys.KEY_ECOMMERCE] = eCommerce
        map[EventKeys.KEY_PRODUCT_ID] = item.id
        map[EventKeys.KEY_USER_ID] = userId
        map[EventKeys.KEY_CATALOG_ID] = catalogId
        map[KEYS.CAMPAIGN_CODE] = ""

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackProductCardClick(catalogName : String, catalogId : String,  catalogUrl : String, userId : String ,
                              item : com.tokopedia.catalog.domain.model.CatalogProductItem, position : String ,
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
        map[EventKeys.KEY_EVENT_LABEL] = "$catalogName - $catalogId"
        map[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        map[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        map[EventKeys.KEY_ECOMMERCE] = eCommerce
        map[EventKeys.KEY_PRODUCT_ID] = item.id
        map[EventKeys.KEY_USER_ID] = userId
        map[EventKeys.KEY_CATALOG_ID] = catalogId
        map[KEYS.CAMPAIGN_CODE] = ""

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun sendPromotionEvent (
        event: String,
        action: String,
        category: String,
        eventLabel: String,
        catalogId: String,
        position: Int,
        userId: String,
        impressedCatalogId : String,
        impressedCatalogName : String
    ){
        val bundle = Bundle()
        val itemBundle = Bundle().apply {
            putString(EventKeys.KEY_ITEM_ID,impressedCatalogId)
            putString(EventKeys.KEY_CREATIVE_NAME,impressedCatalogName)
            putString(EventKeys.KEY_CREATIVE_SLOT, (position + 1).toString())
            putString(EventKeys.KEY_ITEM_NAME,KATALOG_PiILIHAN_UNTUKMU)
        }
        bundle.putString(EventKeys.KEY_EVENT , event)
        bundle.putString(EventKeys.KEY_EVENT_CATEGORY,category)
        bundle.putString(EventKeys.KEY_EVENT_ACTION,action)
        bundle.putString(EventKeys.KEY_EVENT_LABEL,eventLabel)
        bundle.putString(EventKeys.KEY_CATALOG_ID,catalogId)
        bundle.putString(EventKeys.KEY_BUSINESS_UNIT,EventKeys.BUSINESS_UNIT_VALUE_CATALOG)
        bundle.putString(EventKeys.KEY_CURRENT_SITE,EventKeys.CURRENT_SITE_VALUE)
        bundle.putString(EventKeys.KEY_USER_ID,userId)
        bundle.putParcelableArrayList(KEY_PROMOTIONS, arrayListOf(itemBundle))

        getTracker().sendEnhanceEcommerceEvent(event,bundle)
    }

    fun sendImpressionEventInQueue (
        trackingQueue : TrackingQueue,
        event: String,
        action: String,
        category: String,
        eventLabel: String,
        catalogId: String,
        position: Int,
        userId: String,
        impressedCatalogId : String,
        impressedCatalogName : String,
        impressedItemName : String,
        impressionUniqueKey : String,
    ){

        val list = ArrayList<Map<String, Any>>()
        val promotionMap = HashMap<String, Any>()

        promotionMap[EventKeys.KEY_ITEM_ID] = impressedCatalogId
        promotionMap[EventKeys.KEY_CREATIVE_NAME] = impressedCatalogName
        promotionMap[EventKeys.KEY_CREATIVE_SLOT] =  (position + 1).toString()
        promotionMap[EventKeys.KEY_ITEM_NAME] = impressedItemName
        list.add(promotionMap)
        val eventModel = EventModel(event,category,action,eventLabel)
        eventModel.key = impressionUniqueKey
        val customDimensionMap = HashMap<String, Any>()
        customDimensionMap[EventKeys.KEY_CATALOG_ID] = catalogId
        customDimensionMap[EventKeys.KEY_BUSINESS_UNIT] = EventKeys.BUSINESS_UNIT_VALUE
        customDimensionMap[EventKeys.KEY_CURRENT_SITE] = EventKeys.CURRENT_SITE_VALUE
        customDimensionMap[EventKeys.KEY_USER_ID] = userId

        trackingQueue.putEETracking(eventModel, hashMapOf (
            KEY_ECOMMERCE to hashMapOf(
                EVENT_PROMO_VIEW to hashMapOf(
                    KEY_PROMOTIONS to  list)
            )
        ), customDimensionMap)
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
            const val KEY_TRACKER_ID = "trackerId"

            const val KEY_PROMOTIONS = "promotions"
            const val BUSINESS_UNIT_VALUE= "Physical Goods"
            const val BUSINESS_UNIT_VALUE_CATALOG= "catalog"
            const val SHARING_EXPERIENCE_BUSINESS_UNIT_VALUE = "sharingexperience"
            const val CURRENT_SITE_VALUE = "tokopediamarketplace"

            const val EVENT_CATEGORY = "catalog page"

            const val EVENT_NAME_PRODUCT_CLICK = "productClick"
            const val EVENT_NAME_CATALOG_CLICK = "clickCatalog"
            const val EVENT_NAME_VIEW_CATALOG_IRIS = "viewCatalogIris"
            const val EVENT_NAME_PRODUCT_VIEW = "productView"
            const val EVENT_NAME_CLICK_PG = "clickPG"
            const val EVENT_PROMO_VIEW = "promoView"
            const val EVENT_VIEW_PG_IRIS = "viewPGIris"

            const val KEY_CREATIVE_NAME = "creative_name"
            const val KEY_CREATIVE_SLOT = "creative_slot"
            const val KEY_ITEM_ID = "item_id"
            const val KEY_ITEM_NAME = "item_name"

            const val EVENT_SELECT_CONTENT = "select_content"

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

            const val CLICK_MORE_DESCRIPTION = "click deskripsi lihat selengkapnya"
            const val CLICK_MORE_SPECIFICATIONS = "click spesifikasi lihat selengkapnya"
            const val CLICK_TAB_SPECIFICATIONS = "click tab spesifikasi lihat selengkapnya"
            const val CLICK_TAB_DESCRIPTION = "click tab deskripsi lihat selengkapnya"
            const val CLICK_VIDEO_WIDGET = "click video widget"
            const val CLICK_COMPARISION_CATALOG = "click compared catalog on perbandingan preview"
            const val CLICK_SHARE = "click share"
            const val CLICK_BACK_BUTTON = "click back button"
            const val CLICK_SELENGKAPNYA_ON_REVIEW = "click selengkapnya on review"
            const val CLICK_ON_LIHAT_SEMUA_REVIEW = "click lihat semua review"
            const val CLICK_IMAGE_ON_LIST_REVIEW = "click image on list review"
            const val CLICK_IMAGE_ON_REVIEW = "click image on review"
            const val CLICK_CLOSE_ON_IMAGE_REVIEW = "click close on image review"
            const val CLICK_GANTI_PERBANDINGAN = "click ganti perbandingan - perbandingan produk"
            const val CLICK_SEARCH_BAR_PERBANDINGAN_PRODUK = "click search bar - perbandingan produk"
            const val CLICK_BANDINGKAN_PERBANDINGAN_PRODUK= "click bandingkan - perbandingan produk"
            const val CLICK_NEXT_CATALOG_PAGE_PERBANDINGAN_PRODUK = "click next catalog page - perbandingan produk"
            const val CLICK_DROP_UP_BUTTON_PERBANDINGAN_PRODUK = "click drop up button - perbandingan produk"
            const val CLICK_DROP_DOWN_BUTTON_PERBANDINGAN_PRODUK = "click drop down button - perbandingan produk"

            const val KATALOG_PiILIHAN_UNTUKMU = "katalog pilihan untukmu"
            const val CLICK_KATALOG_PILIHAN_UNTUKMU = "click katalog pilihan untukmu"
            const val IMPRESSION_KATALOG_PILIHAN_UNTUKMU = "impression katalog pilihan untukmu"

            const val IMAGE_WIDGET_IMPRESSION = "impression image"
            const val IMAGE_WIDGET_IMPRESSION_ITEM_NAME = "image banner impression"

            const val SPECIFICATION_WIDGET_IMPRESSION = "impression specification"
            const val SPECIFICATION_WIDGET_IMPRESSION_ITEM_NAME = "specification banner impression"

            const val VIDEO_WIDGET_IMPRESSION = "impression video widget"
            const val VIDEO_WIDGET_IMPRESSION_ITEM_NAME = "video banner impression"

            const val DESCRIPTION_WIDGET_IMPRESSION = "impression description"
            const val COMPARISON_WIDGET_IMPRESSION = "impression comparison widget"
            const val REVIEW_WIDGET_IMPRESSION = "impression review widget"

            const val CLICK_FLOATING_BUTTON_PRODUCT = "click floating action button to product list"
            const val CLICK_FLOATING_BUTTON_LAST_SCROLL = "click floating action button to last scroll position"

            const val IMPRESS_CATALOG_ENTRY_POINT = "impress catalog library entry point"
            const val CLICK_CATALOG_ENTRY_POINT = "click catalog library entry point"
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

    interface TrackerId{
        companion object {
            const val OPEN_BOTTOMSHEET = "27182"
            const val CLICK_SEARCH_BAR = "27183"
            const val CLICK_BANDINGAN = "27184"
            const val CLICK_NEXT_CATALOG_PAGE = "28893"
            const val CLICK_DROP_UP_BUTTON = "35721"
            const val CLICK_DROP_DOWN_BUTTON = "35722"
            const val IMPRESS_CATALOG_ENTRY_POINT = "42951"
            const val CLICK_CATALOG_ENTRY_POINT = "42952"

        }
    }

    fun getCatalogTrackingUrl(catalogUrl : String?) : String {
        if (!catalogUrl.isNullOrEmpty()){
            catalogUrl.split(CATALOG_URL_KEY).last().let {
                return "${CATALOG_URL_KEY}$it"
            }
        }
        return ""
    }

    fun sendWidgetTracking(userId: String, catalogId: String, catalogName: String,actionName: String, trackerId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.EVENT_VIEW_PG_IRIS)
            .setEventAction(actionName)
            .setEventCategory(CategoryKeys.PAGE_EVENT_CATEGORY)
            .setEventLabel("$catalogName - $catalogId")
            .setCustomProperty(EventKeys.KEY_TRACKER_ID, trackerId)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.KEY_CATALOG_ID, catalogId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty("pagePath", "tokopedia://catalog/$catalogId")
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCatalogLibraryEntryPointEvent (catalogId: String, catalogName: String, userId: String) {
        Tracker.Builder()
            .setEvent(EventKeys.EVENT_NAME_CLICK_PG)
            .setEventAction(ActionKeys.CLICK_CATALOG_ENTRY_POINT)
            .setEventCategory(CategoryKeys.PAGE_EVENT_CATEGORY)
            .setEventLabel("$catalogName - $catalogId")
            .setCustomProperty(EventKeys.KEY_TRACKER_ID, CLICK_CATALOG_ENTRY_POINT)
            .setBusinessUnit(EventKeys.BUSINESS_UNIT_VALUE)
            .setCustomProperty(EventKeys.KEY_CATALOG_ID, catalogId)
            .setCurrentSite(EventKeys.CURRENT_SITE_VALUE)
            .setCustomProperty("pagePath", "tokopedia://catalog/$catalogId")
            .setUserId(userId)
            .build()
            .send()
    }
}
