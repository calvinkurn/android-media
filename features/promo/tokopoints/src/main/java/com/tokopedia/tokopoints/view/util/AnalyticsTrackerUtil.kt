package com.tokopedia.tokopoints.view.util

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.topads.sdk.domain.model.FreeOngkir
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

object AnalyticsTrackerUtil {


    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendScreenEvent(context: Activity?, screenName: String?) {
        getTracker().sendScreenAuthenticated(screenName)
    }

    @JvmStatic
    fun sendEvent(context: Context?, event: String?, category: String?,
                  action: String?, label: String?) {
        getTracker().sendGeneralEvent(TrackAppUtils.gtmData(event, category, action, label))
    }

    @JvmStatic
    fun sendEvent(userId: String, event: String, category: String,
                  action: String, label: String, businessUnit: String, currentSite: String) {
        val eventDataLayer = HashMap<String, Any>()
        eventDataLayer[EventKeys.EVENT] = event
        eventDataLayer[EventKeys.EVENT_CATEGORY] = category
        eventDataLayer[EventKeys.EVENT_ACTION] = action
        eventDataLayer[EventKeys.EVENT_LABEL] = label
        eventDataLayer[EventKeys.EVENT_BUSINESSUNIT] = businessUnit
        eventDataLayer[EventKeys.EVENT_CURRENTSITE] = currentSite
        eventDataLayer[EcommerceKeys.USERID] = userId

        getTracker().sendGeneralEvent(eventDataLayer)
    }

    @JvmStatic
    fun sendEvent(event: String, category: String,
                  action: String, label: String, businessUnit: String, currentSite: String) {
        val eventDataLayer = Bundle()
        eventDataLayer.putString(EventKeys.EVENT, event)
        eventDataLayer.putString(EventKeys.EVENT_CATEGORY, category)
        eventDataLayer.putString(EventKeys.EVENT_ACTION, action)
        eventDataLayer.putString(EventKeys.EVENT_LABEL, label)
        eventDataLayer.putString(EventKeys.EVENT_BUSINESSUNIT, businessUnit)
        eventDataLayer.putString(EventKeys.EVENT_CURRENTSITE, currentSite)
        getTracker().sendEnhanceEcommerceEvent(event, eventDataLayer)
    }

    fun sendECommerceEvent(context: Context?, event: String, category: String,
                           action: String, label: String, ecommerce: HashMap<String, Map<String, List<Map<String, String?>>>>) {
        val map = HashMap<String, Any>()
        map[EventKeys.EVENT] = event
        map[EventKeys.EVENT_CATEGORY] = category
        map[EventKeys.EVENT_ACTION] = action
        map[EventKeys.EVENT_LABEL] = label
        map[EventKeys.ECOMMERCE] = ecommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    @JvmStatic
    fun sendECommerceEvent(event: String, category: String,
                           action: String, label: String, ecommerce: HashMap<String, Any>) {
        val map = HashMap<String, Any>()
        map[EventKeys.EVENT] = event
        map[EventKeys.EVENT_CATEGORY] = category
        map[EventKeys.EVENT_ACTION] = action
        map[EventKeys.EVENT_LABEL] = label
        map[EventKeys.ECOMMERCE] = DataLayer.mapOf("promoView", ecommerce)
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    @JvmStatic
    fun sendECommerceEvent(event: String, category: String,
                           action: String, label: String, businessUnit: String, currentSite: String, ecommerce: HashMap<String, Any>) {
        val map = HashMap<String, Any>()
        map[EventKeys.EVENT] = event
        map[EventKeys.EVENT_CATEGORY] = category
        map[EventKeys.EVENT_ACTION] = action
        map[EventKeys.EVENT_LABEL] = label
        map[EventKeys.EVENT_BUSINESSUNIT] = businessUnit
        map[EventKeys.EVENT_CURRENTSITE] = currentSite
        map[EventKeys.ECOMMERCE] = DataLayer.mapOf("promoView", ecommerce)
        getTracker().sendEnhanceEcommerceEvent(map)
    }


    @JvmStatic
    fun sendECommerceEventBanner(event: String, category: String,
                                 action: String, label: String, ecommerce: HashMap<String, Any>) {
        val map = HashMap<String, Any>()
        map[EventKeys.EVENT] = event
        map[EventKeys.EVENT_CATEGORY] = category
        map[EventKeys.EVENT_ACTION] = action
        map[EventKeys.EVENT_LABEL] = label
        map[EventKeys.ECOMMERCE] = DataLayer.mapOf("promoClick", ecommerce)

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun clickProductRecomItem(productId: String,
                              productPositionIndex: Int,
                              recommendationType: String,
                              isTopads: Boolean,
                              isFreeOngkir: Boolean,
                              productBrand: String,
                              itemCategory: String,
                              productName: String,
                              productVariant: String,
                              productPrice: String,
    ) {
        val map = mutableMapOf<String, Any>()
        val price = CurrencyFormatHelper?.convertRupiahToInt(productPrice)
        map[EventKeys.EVENT] = EventKeys.EVENT_CLICK_RECOM
        map[EventKeys.EVENT_CATEGORY] = CategoryKeys.EVENT_CATEGORY_RECOM
        map[EventKeys.EVENT_ACTION] = ActionKeys.CLICK_RECOM_ACTION
        map[EventKeys.EVENT_LABEL] = ""
        map[EventKeys.EVENT_BUSINESSUNIT]= EcommerceKeys.BUSINESSUNIT
        map[EventKeys.EVENT_CURRENTSITE]=EcommerceKeys.CURRENTSITE
        map[EcommerceKeys.CURRENCY_CODE] = EcommerceKeys.IDR
        map[EcommerceKeys.ITEM_LIST] = getItemList(recommendationType,isTopads)
        map[EventKeys.ECOMMERCE] = DataLayer.mapOf(
            EcommerceKeys.CLICK, DataLayer.mapOf(EcommerceKeys.ACTION_FIELD,
                DataLayer.mapOf(EcommerceKeys.LIST,EcommerceKeys.NONE),
                EcommerceKeys.PRODUCTS, DataLayer.listOf(getItemsMapList(productId,
                    productPositionIndex,
                    recommendationType,
                    isTopads,
                    isFreeOngkir,
                    productBrand,
                    itemCategory,
                    productName,
                    productVariant,
                    price)
                )
            ))
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    private fun getItemsMapList(productId: String,
                                productPositionIndex: Int,
                                recommendationType: String,
                                isTopads: Boolean,
                                isFreeOngkir: Boolean,
                                productBrand: String,
                                itemCategory: String,
                                productName: String,
                                productVariant: String,
                                productPrice: Int): Map<String, Any> {

        val itemsMap = HashMap<String, Any>()
        itemsMap[EcommerceKeys.INDEX] = productPositionIndex + 1
        itemsMap[EcommerceKeys.ITEM_BRAND] = productBrand
        itemsMap[EcommerceKeys.ITEM_CATEGORY] = itemCategory
        itemsMap[EcommerceKeys.ITEM_ID] = productId
        itemsMap[EcommerceKeys.ITEM_NAME] = productName
        itemsMap[EcommerceKeys.ITEM_VARIANT] = productVariant
        itemsMap[EcommerceKeys.PRICE] = productPrice
        itemsMap[EcommerceKeys.DIMENSION39] = getItemList(recommendationType,isTopads)

        var list = ""
        if (isFreeOngkir){
            list = EcommerceKeys.FREEONGKIR
        }
        itemsMap[EcommerceKeys.DIMENSION73] = list
        return itemsMap
    }

    fun impressionProductRecomItem(productId: String,
                                   productPositionIndex: Int,
                                   recommendationType: String,
                                   isTopads: Boolean,
                                   isFreeOngkir: Boolean,
                                   productBrand: String,
                                   itemCategory: String,
                                   productName: String,
                                   productVariant: String,
                                   productPrice: String,
                                   trackingQueue: TrackingQueue) {
        val map = mutableMapOf<String, Any>()
        val price = CurrencyFormatHelper.convertRupiahToInt(productPrice)
        map[EventKeys.EVENT] = EventKeys.EVENT_VIEW_RECOM
        map[EventKeys.EVENT_CATEGORY] = CategoryKeys.EVENT_CATEGORY_RECOM
        map[EventKeys.EVENT_ACTION] = ActionKeys.IMPRESSION_RECOM_ACTION
        map[EventKeys.EVENT_LABEL] = ""
        map[EventKeys.EVENT_BUSINESSUNIT] = EcommerceKeys.BUSINESSUNIT
        map[EventKeys.EVENT_CURRENTSITE] = EcommerceKeys.CURRENTSITE
        map[EcommerceKeys.CURRENCY_CODE] = EcommerceKeys.IDR
        map[EcommerceKeys.ITEM_LIST] = getItemList(recommendationType,isTopads)
        map[EventKeys.ECOMMERCE] = DataLayer.mapOf(
            EcommerceKeys.ITEMS,
            DataLayer.listOf(getItemsMapList(productId,
                productPositionIndex,
                recommendationType,
                isTopads,
                isFreeOngkir,
                productBrand,
                itemCategory,
                productName,
                productVariant,
                price)
            )
        )
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    private fun getItemList(recommendationType: String, isTopads: Boolean): String {
        var list: String = String.format(
            EcommerceKeys.ITEM_LIST_V2,
            recommendationType
        )
        if (isTopads) {
            list += EcommerceKeys.VALUE_PRODUCT_TOPADS
        }
        return list
    }

    interface EventKeys {
        companion object {
            const val EVENT = "event"
            const val EVENT_CATEGORY = "eventCategory"
            const val EVENT_ACTION = "eventAction"
            const val EVENT_LABEL = "eventLabel"
            const val ECOMMERCE = "ecommerce"
            const val EVENT_TOKOPOINT = "eventTokopoint"
            const val EVENT_TOKOPOINT_IRIS = "viewTokopointIris"
            const val EVENT_VIEW_TOKOPOINT = "eventViewTokopoint"
            const val EVENT_CLICK_COUPON = "clickCoupon"
            const val EVENT_VIEW_COUPON = "viewCoupon"
            const val EVENT_VIEW_PROMO = "promoView"
            const val EVENT_CLICK_PROMO = "promoClick"
            const val BACK_ARROW_LABEL = "back arrow detail kupon"
            const val KEY_EVENT_PROFILE_VALUE = "clickProfile"
            const val EVENT_BUSINESSUNIT = "businessUnit"
            const val EVENT_CURRENTSITE = "currentSite"
            const val EVENT_MVC = "mvc - {{x}} - {{shop_name}}"
            const val EVENT_MVC_SECTION = "mvc section"
            const val EVENT_VIEW_RECOM = "productView"
            const val EVENT_CLICK_RECOM = "productClick"

        }
    }

    interface CategoryKeys {
        companion object {
            const val TOKOPOINTS = "tokopoints"
            const val TOKOPOINTS_PENUKARAN_POINT = "tokopoints-penukaran point"
            const val PENUKARAN_POINT_DETAIL = "penukaran point - coupon detail"
            const val PENUKARAN_POINT = "penukaran point"
            const val TOKOPOINTS_KUPON_SAYA = "tokopoints-kupon saya"
            const val KUPON_MILIK_SAYA = "kupon milik saya"
            const val KUPON_MILIK_SAYA_DETAIL = "kupon milik saya - coupon detail"
            const val POPUP_KONFIRMASI = "pop up konfirmasi tukar points"
            const val POPUP_PENUKARAN_BERHASIL = "pop up penukaran berhasil"
            const val POPUP_PENUKARAN_POINT_TIDAK = "pop up point tidak cukup"
            const val POPUP_KUOTA_HABIS = "pop up kuota penukaran habis"
            const val POPUP_VERIFIED = "pop up belum verified"
            const val POPUP_KONFIRMASI_GUNAKAN_KUPON = "pop up konfirmasi gunakan kupon"
            const val POPUP_KIRIM_KUPON = "pop up kirim kupon"
            const val KEY_EVENT_CATEGORY_PROFILE_VALUE = "phone number verification"
            const val KUPON_TOKO = "kupon toko"
            const val EVENT_CATEGORY_RECOM = "my rewards page"
        }
    }

    interface ActionKeys {
        companion object {
            const val CLICK_MEMBERSHIP = "click lihat status membership"
            const val CLICK_STATUS_MEMBERSHIP = "click status membership"
            const val CLICK_BACK_ARROW = "click back arrow"
            const val CLICK_GUNAKAN = "click gunakan"
            const val CLICK_TUKAR = "click tukar"
            const val CLICK_BATAL = "click batal"
            const val CLICK_NANTI_SAJA = "click nanti saja"
            const val CLICK_OK = "click ok"
            const val CLICK_BELANJA = "click belanja"
            const val CLICK_INCOMPLETE_PROFILE = "click lengkapi profil"
            const val CLICK_LIHAT_KUPON = "click lihat kupon"
            const val VIEW_COUPON = "view coupon detail"
            const val VIEW_MY_COUPON_DETAIL = "view my coupon detail"
            const val VIEW_MY_COUPON = "view my coupon"
            const val CLICK_COUPON = "click coupon"
            const val CLICK_DYNAMIC_ICON = "click dynamic icon"
            const val VIEW_DYNAMIC_CAT = "view dynamic category"
            const val VIEW_DYNAMIC_ICON = "view dynamic icon"
            const val SWIPE_COUPON = "swipe untuk lihat code"
            const val COPY_CODE = "salin kode swipe"
            const val CLICK_MEM_BOTTOM = "click footer status membership"
            const val CLICK_SELL_ALL_COUPON = "click kupon milik saya"
            const val CLICK_KIRIM_SEKARANG = "click kirim sekarang"
            const val VIEW_REDEEM_SUCCESS = "view redeem success"
            const val CLICK_TICKER = "click ticker"
            const val CLICK_LIHAT_SEMUA = "click lihat semua"
            const val CLICK_SEE_ALL_EXPLORE_CATALOG = "click lihat semua coupon catalog"
            const val CLICK_SEE_ALL_EXPLORE_BANNER = "click lihat semua on banner"
            const val CLICK_SEE_ALL_COUPON = "click lihat semua kupon saya"
            const val CLICK_CTA_COUPON = "click cta on coupon section"
            const val CLICK_CATALOG_HOME = "click coupon on catalog"
            const val VIEW_BANNERS_ON_HOME_TOKOPOINTS = "view banner on home tokopoints"
            const val CLICK_BANNERS_ON_HOME_TOKOPOINTS = "click banner on home tokopoints"
            const val CLICK_COUPON_ON_CATALOG = "view coupon on catalog"
            const val KEY_EVENT_ACTION_PROFILE_VALUE = "click on button verifikasi"
            const val KEY_EVENT_ACTION_PROFILE_VALUE_BATAL = "click on button batal"
            const val KEY_EVENT_CLICK_DYNAMICITEM = "click reward section"
            const val CLICK_USERSAVING_ENTRYPOINT = "click user saving page"
            const val VIEW_MVC_COUPON_ON_REWARDS = "impression-mvc section"
            const val VIEW_MVC_COUPON = "impression-mvc"
            const val CLICK_COUPON_TITLE = "click coupon title"
            const val CLICK_SHOP_NAME = "click shop name"
            const val CLICK_PRODUCT_CARD = "click product card"
            const val VIEW_HOMEPAGE = "view homepage"

            const val IMPRESSION_RECOM_ACTION = "impression - products"
            const val CLICK_RECOM_ACTION = "click - products"

        }
    }

    interface EcommerceKeys {
        companion object {
            const val POSITION = "position"
            const val NAME = "name"
            const val CREATIVE = "creative"
            const val USERID = "userId"
            const val PROMOTIONS = "promotions"
            const val BUSINESSUNIT = " buyer growth platform"
            const val CURRENTSITE = " tokopediamarketplace"
            const val ITEM_LIST = "item_list"
            const val ITEMS = "impressions"
            const val CURRENCY_CODE = "currencyCode"
            const val IDR = "IDR"
            const val ACTION_FIELD = "actionField"
            const val CLICK = "click"
            const val LIST = "list"
            const val PRODUCTS = "products"
            const val NONE = "none / other"
            const val ITEM_LIST_V2 = "/rewards page - rekomendasi untuk anda - %s"
            const val VALUE_PRODUCT_TOPADS = " - product topads"
            const val DIMENSION39 = "dimension39"
            const val DIMENSION73 = "dimension73"
            const val INDEX  = "index"
            const val ITEM_BRAND= "item_brand"
            const val ITEM_CATEGORY = "item_category"
            const val ITEM_ID = "item_id"
            const val ITEM_NAME= "item_name"
            const val ITEM_VARIANT="item_variant"
            const val PRICE = "price"
            const val FREEONGKIR = "bebas ongkir"
        }
    }

    interface ScreenKeys {
        companion object {
            const val MERCHANT_COUPONLIST_SCREEN_NAME = "/tokopoints/merchant-coupon"
            const val MY_COUPON_LISTING_SCREEN_NAME = "/tokopoints/kupon-saya"
            const val COUPON_CATALOG_SCREEN_NAME = "/tokopoints/tukar-point/detail"
            const val CATALOG_LISTING_SCREEN_NAME = "/tokopoints/tukar-point"
            const val COUPON_DETAIL_SCREEN_NAME = "/tokopoints/kupon-saya/detail"
            const val HOME_PAGE_SCREEN_NAME = "/tokopoints"
        }
    }
}