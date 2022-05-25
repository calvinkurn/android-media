package com.tokopedia.promocheckout.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

class PromoCheckoutAnalytics {

    val KEY_EVENT = "event"
    val KEY_EVENT_CATEGORY = "eventCategory"
    val KEY_EVENT_ACTION = "eventAction"
    val KEY_EVENT_LABEL = "eventLabel"
    val KEY_EVENT_ECOMMERCE = "ecommerce"
    val KEY_EVENT_ECOMMERCE_PROMOCLICK = "promoClick"
    val KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION = "promotions"
    val KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION_ID = "id"
    val KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION_NAME = "name"
    val KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION_CREATIVE = "creative"
    val KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION_POSITION = "position"


    val EVENT_CATEGORY_VALUE = "cart"
    val EVENT_VALUE = "promoClick"
    val EVENT_ACTION_VALUE_CLICK_CATALOG = "click catalog"
    val EVENT_ACTION_VALUE_TUKAR_BUTTON = "click tukar button"
    val EVENT_ACTION_VALUE_TUKAR_POPUP_BUTTON = "click tukar button popup"
    val EVENT_ACTION_VALUE_CLICK_BATAL_POPUP = "click batal popup"

    val KEY_EVENT_PROFILE_VALUE = "clickProfile"
    val KEY_EVENT_CATEGORY_PROFILE_VALUE = "phone number verification"
    val KEY_EVENT_ACTION_PROFILE_VALUE = "click on button verifikasi"
    val KEY_EVENT_ACTION_PROFILE_VALUE_BATAL = "click on button batal"

    private val KEY_CATEGORY_DIGITAL_LAST_SEEN_BANNER = "digital - banner ticker"
    private val KEY_CATEGORY_DIGITAL_MY_COUPON = "digital - coupon ticker"
    private val KEY_CATEGORY_DIGITAL_COUPON_DETAIL = "kupon milik saya - coupon detail"
    private val KEY_ACTION_DIGITAL_CLICK_BANNER = "click banner ticker"
    private val KEY_ACTION_DIGITAL_CLICK_COUPON = "click coupon ticker"
    private val KEY_ACTION_DIGITAL_CLICK_COUPON_DETAIL = "click gunakan"
    private val KEY_EVENT_DIGITAL_CLICK_COUPON = "clickCoupon"
    private val KEY_EVENT_DIGITAL_CLICK_BANNER = "clickBanner"
    private val KEY_USER_ID = "userId"


    companion object {
        val promoCheckoutAnalytics: PromoCheckoutAnalytics by lazy { PromoCheckoutAnalytics() }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }


    fun clickTukarButton(promocode: String?) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, EVENT_ACTION_VALUE_TUKAR_BUTTON,
                KEY_EVENT_LABEL, promocode
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun clickTukarPopUp(promocode: String?) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, EVENT_ACTION_VALUE_TUKAR_POPUP_BUTTON,
                KEY_EVENT_LABEL, promocode
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun clickBatalPopUp(promocode: String?) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, EVENT_ACTION_VALUE_CLICK_BATAL_POPUP,
                KEY_EVENT_LABEL, promocode
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun clickCatalog(promocode: String?, promoname: String, banner_id: Int?, creativename: String, positionIndex: Int) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_VALUE,
                KEY_EVENT_CATEGORY, EVENT_CATEGORY_VALUE,
                KEY_EVENT_ACTION, EVENT_ACTION_VALUE_CLICK_CATALOG,
                KEY_EVENT_LABEL, promocode,
                KEY_EVENT_ECOMMERCE, DataLayer.mapOf(
                KEY_EVENT_ECOMMERCE_PROMOCLICK, DataLayer.mapOf(
                KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION, DataLayer.listOf(DataLayer.mapOf(
                KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION_ID, banner_id,
                KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION_NAME, promoname,
                KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION_CREATIVE, creativename,
                KEY_EVENT_ECOMMERCE_PROMOCLICK_PROMOTION_POSITION, positionIndex)))
        ))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun clickVerifikasai() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_EVENT_PROFILE_VALUE,
                KEY_EVENT_CATEGORY, KEY_EVENT_CATEGORY_PROFILE_VALUE,
                KEY_EVENT_ACTION, KEY_EVENT_ACTION_PROFILE_VALUE)
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun clickCancelVerifikasi() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, KEY_EVENT_PROFILE_VALUE,
                KEY_EVENT_CATEGORY, KEY_EVENT_CATEGORY_PROFILE_VALUE,
                KEY_EVENT_ACTION, KEY_EVENT_ACTION_PROFILE_VALUE_BATAL)
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun clickDigitalLastSeenPromo(promocode: String?, userId: String) {
        val map = TrackAppUtils.gtmData(
                KEY_EVENT_DIGITAL_CLICK_BANNER,
                KEY_CATEGORY_DIGITAL_LAST_SEEN_BANNER,
                KEY_ACTION_DIGITAL_CLICK_BANNER,
                promocode)
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

    fun clickDigitalMyPromo(category: String, operator: String, userId: String) {
        val map = TrackAppUtils.gtmData(
                KEY_EVENT_DIGITAL_CLICK_COUPON,
                KEY_CATEGORY_DIGITAL_MY_COUPON,
                KEY_ACTION_DIGITAL_CLICK_COUPON,
                "$category - $operator")
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

    fun clickUseDigitalMyPromo(couponName: String, userId: String) {
        val map = TrackAppUtils.gtmData(
                KEY_EVENT_DIGITAL_CLICK_COUPON,
                KEY_CATEGORY_DIGITAL_COUPON_DETAIL,
                KEY_ACTION_DIGITAL_CLICK_COUPON_DETAIL,
                couponName)
        map[KEY_USER_ID] = userId
        getTracker().sendGeneralEvent(map)
    }

}
