package com.tokopedia.tokopoints.view.util

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil.ScreenKeys.Companion.HOME_PAGE_SCREEN_NAME
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import kotlin.collections.HashMap

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
    fun sendEvent(event: String, category: String,
                  action: String, label: String, businessUnit: String, currentSite: String) {
        val eventDataLayer = Bundle()
        eventDataLayer.putString(EventKeys.EVENT, event)
        eventDataLayer.putString(EventKeys.EVENT_CATEGORY, category)
        eventDataLayer.putString(EventKeys.EVENT_ACTION, action)
        eventDataLayer.putString(EventKeys.EVENT_LABEL, label)
        eventDataLayer.putString(EventKeys.EVENT_BUSINESSUNIT, businessUnit)
        eventDataLayer.putString(EventKeys.EVENT_CURRENTSITE, currentSite)
        getTracker().sendEnhanceEcommerceEvent(HOME_PAGE_SCREEN_NAME, eventDataLayer)
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

    interface EventKeys {
        companion object {
            const val EVENT = "event"
            const val EVENT_CATEGORY = "eventCategory"
            const val EVENT_ACTION = "eventAction"
            const val EVENT_LABEL = "eventLabel"
            const val ECOMMERCE = "ecommerce"
            const val EVENT_TOKOPOINT = "eventTokopoint"
            const val EVENT_LUCKY_EGG = "luckyEggClick"
            const val EVENT_VIEW_TOKOPOINT = "eventViewTokopoint"
            const val EVENT_CLICK_COUPON = "clickCoupon"
            const val EVENT_VIEW_COUPON = "viewCoupon"
            const val EVENT_VIEW_PROMO = "promoView"
            const val EVENT_CLICK_PROMO = "promoClick"
            const val TOKOPOINTS_LABEL = "tokopoints"
            const val TOKOPOINTS_ON_BOARDING_LABEL = "tokopoints on boarding"
            const val TOKOPOINTS_LUCKY_EGG_CLOSE_LABEL = "close cara mendapatkan lucky egg"
            const val BACK_ARROW_LABEL = "back arrow detail kupon"
            const val KEY_EVENT_PROFILE_VALUE = "clickProfile"
            const val EVENT_BUSINESSUNIT = "businessUnit"
            const val EVENT_CURRENTSITE = "currentSite"
        }
    }

    interface CategoryKeys {
        companion object {
            const val HOMEPAGE = "homepage-tokopoints"
            const val TOKOPOINTS = "tokopoints"
            const val TOKOPOINTS_EGG = "tokopoints-egg"
            const val TOKOPOINTS_PENUKARAN_POINT = "tokopoints-penukaran point"
            const val PENUKARAN_POINT_DETAIL = "penukaran point - coupon detail"
            const val PENUKARAN_POINT = "penukaran point"
            const val TOKOPOINTS_KUPON_SAYA = "tokopoints-kupon saya"
            const val KUPON_MILIK_SAYA = "kupon milik saya"
            const val KUPON_MILIK_SAYA_DETAIL = "kupon milik saya - coupon detail"
            const val RIWAYAT_TOKOPOINTS = "riwayat tokopoints"
            const val POPUP_KONFIRMASI = "pop up konfirmasi tukar points"
            const val POPUP_PENUKARAN_BERHASIL = "pop up penukaran berhasil"
            const val POPUP_PENUKARAN_POINT_TIDAK = "pop up point tidak cukup"
            const val POPUP_KUOTA_HABIS = "pop up kuota penukaran habis"
            const val POPUP_VERIFIED = "pop up belum verified"
            const val POPUP_KONFIRMASI_GUNAKAN_KUPON = "pop up konfirmasi gunakan kupon"
            const val POPUP_KIRIM_KUPON = "pop up kirim kupon"
            const val POPUP_TERIMA_HADIAH = "pop up terima hadiah kupon"
            const val KEY_EVENT_CATEGORY_PROFILE_VALUE = "phone number verification"
        }
    }

    interface ActionKeys {
        companion object {
            const val CLICK_POINT = "click point & tier status"
            const val CLICK_CEK = "click cek tokopoints"
            const val CLICK_MEMBERSHIP = "click lihat status membership"
            const val CLICK_STATUS_MEMBERSHIP = "click status membership"
            const val CLICK_POINT_SAYA = "click points saya"
            const val CLICK_LOYALTY_SAYA = "click loyalty saya"
            const val VIEW_TICKER = "view ticker"
            const val CLICK_LIHAT_SEMUA = "click lihat semua"
            const val CLICK_BANTUAN = "click bantuan tokopoints"
            const val CLICK_EGG = "click floating lucky egg"
            const val CLICK_EGG_EMPTY = "click raih points dan kumpulkan loyalty"
            const val CLICK_EGG_BELI = "click beli - untuk dapat lucky egg"
            const val CLICK_EGG_BAYAR = "click bayar - untuk dapat lucky egg"
            const val CLICK_EGG_PESAWAT = "click pesawat - untuk dapat lucky egg"
            const val CLICK_EGG_KARETA = "click kereta - untuk dapat lucky egg"
            const val CLICK_CLOSE_BUTTON = "click close button"
            const val CLICK_KETENTUAN = "click ketentuan"
            const val CLICK_CARA_PAKAI = "click cara pakai"
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
            const val CLICK_PENUKARAN = "click penukaran point"
            const val CLICK_EXPLORE = "click explore"
            const val CLICK_KUPON_SAYA = "click kupon saya"
            const val CLICK_DYNAMIC_CAT = "click dynamic category"
            const val CLICK_DYNAMIC_ICON = "click dynamic icon"
            const val VIEW_DYNAMIC_CAT = "view dynamic category"
            const val VIEW_DYNAMIC_ICON = "view dynamic icon"
            const val CLICK_FLOATING_LUCKY = "click floating lucky egg"
            const val CLICK_FILTER = "click filter"
            const val PILIH_FILTER = "pilih filter"
            const val CLICK_SAVE_FILTER = "click simpan filter"
            const val SWIPE_COUPON = "swipe untuk lihat code"
            const val COPY_CODE = "salin kode swipe"
            const val CLICK_MEM_BOTTOM = "click footer status membership"
            const val CLICK_SELL_ALL_COUPON = "click kupon milik saya"
            const val CLICK_PENUKARAN_POINTS = "click penukaran point"
            const val CLICK_COUPON_SAYA = "click coupon saya"
            const val CLICK_KIRIM_SEKARANG = "click kirim sekarang"
            const val CLICK_OK_ON_SUCCESS = "click ok on success"
            const val CLICK_OK_ON_FAILED = "click ok on failed"
            const val CLICK_GUNAKAN_KUPON = "click gunakan kupon"
            const val VIEW_REDEEM_SUCCESS = "view redeem success"
            const val CLICK_COUNTER_KUPON_SAYA = "click counter kupon saya"
            const val CLICK_TICKER = "click ticker"
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
        }
    }

    interface EcommerceKeys {
        companion object {
            const val POSITION = "position"
            const val NAME = "name"
            const val CREATIVE = "creative"
            const val PROMOTIONS = "promotions"
            const val BUSINESSUNIT = " buyer growth platform"
            const val CURRENTSITE = " tokopediamarketplace"

        }
    }

    interface ScreenKeys {
        companion object {
            const val MY_COUPON_LISTING_SCREEN_NAME = "/tokopoints/kupon-saya"
            const val COUPON_CATALOG_SCREEN_NAME = "/tokopoints/tukar-point/detail"
            const val CATALOG_LISTING_SCREEN_NAME = "/tokopoints/tukar-point"
            const val COUPON_DETAIL_SCREEN_NAME = "/tokopoints/kupon-saya/detail"
            const val HOME_PAGE_SCREEN_NAME = "/tokopoints"
        }
    }
}