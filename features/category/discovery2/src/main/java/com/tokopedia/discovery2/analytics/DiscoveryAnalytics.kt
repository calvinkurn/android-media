package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue

class DiscoveryAnalytics(pageType: String = "",
                         pagePath: String = "") {

    private var eventDiscoveryCategory: String = "$VALUE_DISCOVERY_PAGE - $pageType - $pagePath"

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun createGeneralClickEvent(eventName: String = EVENT_CLICK_DISCOVERY, eventAction: String,
                                        eventLabel: String = EMPTY_STRING): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_EVENT to eventName,
                KEY_EVENT_CATEGORY to eventDiscoveryCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)
    }

    private fun createGeneralImpressionEvent(eventName: String = EVENT_PROMO_VIEW, eventAction: String,
                                             eventLabel: String = EMPTY_STRING): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_EVENT to eventName,
                KEY_EVENT_CATEGORY to eventDiscoveryCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)
    }

    //20
    fun trackBackClick() {
        val map = createGeneralClickEvent(eventAction = CLICK_BACK_BUTTON_ACTION)
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    //21
    fun trackShareClick() {
        val map = createGeneralClickEvent(eventAction = CLICK_SOCIAL_SHARE_ACTION)
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    //41 done
    fun trackImpressionIconDynamicComponent(headerName: String, headerPosition: Int, icons: List<DataItem>, trackingQueue: TrackingQueue) {
        val map = createGeneralImpressionEvent(eventAction = "$IMPRESSION_ICON_DYNAMIC_COMPONENT - $headerName")
        if (icons.isNotEmpty()) {
            val list = ArrayList<Map<String, Any>>()
            for (coupon in icons) {
                val hashMap = HashMap<String, Any>()
                coupon.let {
                    hashMap[KEY_ID] = it.id.toString()
                    hashMap[KEY_NAME] = "$eventDiscoveryCategory - $VALUE_CATEGORY_ICON - $headerName - $headerPosition"
                    hashMap[KEY_CREATIVE] = it.categoryLabel
                    hashMap[KEY_POSITION] = icons.indexOf(it) + 1
                }
                list.add(hashMap)
            }
            val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                    EVENT_PROMO_VIEW to mapOf(
                            KEY_PROMOTIONS to list))
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    //42  done
    fun trackClickIconDynamicComponent(iconPosition: Int, icon: DataItem, trackingQueue: TrackingQueue) {
        val headerName = icon.title
        val map = createGeneralClickEvent(eventName = EVENT_PROMO_CLICK, eventAction = "$CLICK_ICON_DYNAMIC_COMPONENT - $headerName")
        val list = ArrayList<Map<String, Any>>()
        icon.let {
            list.add(mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_NAME to "$eventDiscoveryCategory - $VALUE_CATEGORY_ICON - $headerName - ${icon.positionForParentItem}",
                    KEY_CREATIVE to (it.name ?: EMPTY_STRING),
                    KEY_CREATIVE_URL to (it.imageUrlMobile ?: NONE_OTHER),
                    KEY_POSITION to iconPosition + 1
            ))
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    //43 wont do
    fun trackClickSeeAllBannerCarousel() {
        val map = createGeneralClickEvent(eventAction = BANNER_CAROUSEL_LIHAT_SEMUA_CLICK)
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    //48 done
    fun trackClickCustomTopChat() {
        val map = createGeneralClickEvent(eventAction = CUSTOM_TOP_CHAT_CLICK)
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    //49 done
    fun trackClickChipsFilter(filterName: String) {
        val map = createGeneralClickEvent(eventAction = CHIPS_FILTER_CLICK, eventLabel = filterName)
        getTracker().sendEnhanceEcommerceEvent(map)
    }


    //55
    fun trackEventImpressionCoupon(trackingQueue: TrackingQueue, coupons: ArrayList<DataItem>?) {
        if (!coupons.isNullOrEmpty()) {
            val list = ArrayList<Map<String, Any>>()
            for (coupon in coupons) {
                val map = HashMap<String, Any>()
                coupon.let {
                    map[KEY_ID] = it.id.toString()
                    map[KEY_NAME] = "${it.minUsageLabel} - p(x) - promo list - mini coupon"
                    map[KEY_CREATIVE] = it.minUsageLabel.toString()
                    map[KEY_CREATIVE_URL] = it.imageUrlMobile ?: NONE_OTHER
                    map[KEY_POSITION] = coupons.indexOf(it) + 1
                    map[KEY_PROMO_ID] = it.promoId.toString()
                    map[KEY_PROMO_CODE] = it.couponCode.toString()
                }
                list.add(map)
            }

            val ecommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                    EVENT_PROMO_VIEW to mapOf(
                            KEY_PROMOTIONS to list))
            val map = createGeneralImpressionEvent(eventAction = CLAIM_COUPON_IMPRESSION)
            map[KEY_E_COMMERCE] = ecommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    //56
    fun trackClickClaimCoupon(couponName: String, promoCode: String) {
        val map = createGeneralClickEvent(eventAction = CLICK_BUTTON_CLAIM_COUPON_ACTION, eventLabel = "$couponName - $promoCode")
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    //57
    fun trackEventClickCoupon(trackingQueue: TrackingQueue, coupon: DataItem, position: Int) {
        val list = ArrayList<Map<String, Any>>()
        coupon.let {
            list.add(mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_NAME to "${it.minUsageLabel} - p(x) - promo list - mini coupon",
                    KEY_CREATIVE to it.minUsageLabel.toString(),
                    KEY_CREATIVE_URL to (it.imageUrlMobile ?: NONE_OTHER),
                    KEY_POSITION to position.toString(),
                    KEY_PROMO_ID to it.promoId.toString(),
                    KEY_PROMO_CODE to it.couponCode.toString()
            ))
        }
        val promotions: Map<String, ArrayList<Map<String, Any>>> = mapOf(
                KEY_PROMOTIONS to list)
        val map = createGeneralClickEvent(eventName = EVENT_CLICK_COUPON, eventAction = CLAIM_COUPON_CLICK,
                eventLabel = "${coupon.name} - ${coupon.couponCode}")
        map[KEY_E_COMMERCE] = promotions
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }
}