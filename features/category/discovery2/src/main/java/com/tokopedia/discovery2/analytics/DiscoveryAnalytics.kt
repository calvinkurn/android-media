package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery2.ClaimCouponConstant
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue

class DiscoveryAnalytics(pageType: String = "",
                         pagePath: String = "",
                         val trackingQueue: TrackingQueue) {

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
    fun trackImpressionIconDynamicComponent(headerName: String, icons: List<DataItem>) {
        val map = createGeneralImpressionEvent(eventAction = "$IMPRESSION_ICON_DYNAMIC_COMPONENT - $headerName")
        if (icons.isNotEmpty()) {
            val headerPosition = icons[0].positionForParentItem + 1
            val list = ArrayList<Map<String, Any>>()
            var index = 0
            for (coupon in icons) {
                val hashMap = HashMap<String, Any>()
                coupon.let {
                    hashMap[KEY_ID] = it.dynamicComponentId.toString()
                    hashMap[KEY_NAME] = "$eventDiscoveryCategory - $VALUE_CATEGORY_ICON - $headerName - $headerPosition"
                    hashMap[KEY_CREATIVE] = it.name ?: ""
                    hashMap[KEY_POSITION] = ++index
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
    fun trackClickIconDynamicComponent(iconPosition: Int, icon: DataItem) {
        val headerName = icon.title
        val map = createGeneralClickEvent(eventName = EVENT_PROMO_CLICK, eventAction = "$CLICK_ICON_DYNAMIC_COMPONENT - $headerName")
        val list = ArrayList<Map<String, Any>>()
        icon.let {
            list.add(mapOf(
                    KEY_ID to it.dynamicComponentId.toString(),
                    KEY_NAME to "$eventDiscoveryCategory - $VALUE_CATEGORY_ICON - $headerName - ${icon.positionForParentItem + 1}",
                    KEY_CREATIVE to (it.name ?: EMPTY_STRING),
                    KEY_POSITION to iconPosition + 1
            ))
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
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
    fun trackEventImpressionCoupon(componentsItems: ArrayList<ComponentsItem>) {
        if (!componentsItems.isNullOrEmpty()) {
            val list = ArrayList<Map<String, Any>>()
            if (!componentsItems.isNullOrEmpty()) {
                for (coupon in componentsItems) {
                    val data: ArrayList<DataItem> = ArrayList()
                    coupon.data?.let {
                        data.addAll(it)
                    }
                    val map = HashMap<String, Any>()
                    data[0].let {
                        map[KEY_ID] = it.dynamicComponentId.toString()
                        map[KEY_CREATIVE_URL] = if (coupon.properties?.columns?.equals(ClaimCouponConstant.DOUBLE_COLUMNS) == true)
                            it.smallImageUrlMobile
                                    ?: NONE_OTHER else it.imageUrlMobile ?: NONE_OTHER
                        map[KEY_POSITION] = componentsItems.indexOf(coupon) + 1
                        map[KEY_PROMO_ID] = it.promoId.toString()
                        map[KEY_PROMO_CODE] = it.slug.toString()
                    }
                    list.add(map)
                }
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
    fun trackClickClaimCoupon(couponName: String?, promoCode: String?) {
        val map = createGeneralClickEvent(eventAction = CLICK_BUTTON_CLAIM_COUPON_ACTION, eventLabel = "$couponName - $promoCode")
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    //57
    fun trackEventClickCoupon(coupon: DataItem?, position: Int, isDouble: Boolean) {
        val list = ArrayList<Map<String, Any>>()
        coupon?.let {
            list.add(mapOf(
                    KEY_ID to it.dynamicComponentId.toString(),
                    KEY_CREATIVE_URL to if (isDouble) it.smallImageUrlMobile
                            ?: NONE_OTHER else it.imageUrlMobile ?: NONE_OTHER,
                    KEY_POSITION to position.toString(),
                    KEY_PROMO_ID to it.promoId.toString(),
                    KEY_PROMO_CODE to it.slug.toString()
            ))
        }
        val promotions: Map<String, ArrayList<Map<String, Any>>> = mapOf(
                KEY_PROMOTIONS to list)
        val map = createGeneralClickEvent(eventName = EVENT_CLICK_COUPON, eventAction = CLAIM_COUPON_CLICK,
                eventLabel = "${coupon?.name} - ${coupon?.couponCode}")
        map[KEY_E_COMMERCE] = promotions
        getTracker().sendEnhanceEcommerceEvent(map)
    }
}