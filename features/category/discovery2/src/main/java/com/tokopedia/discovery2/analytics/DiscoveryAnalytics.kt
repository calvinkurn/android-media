package com.tokopedia.discovery2.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.CLAIM_COUPON_CLICK
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.CLAIM_COUPON_IMPRESSION
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.CLICK_BACK_BUTTON_ACTION
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.CLICK_BUTTON_CLAIM_COUPON_ACTION
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.CLICK_SOCIAL_SHARE_ACTION
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.EVENT_CLICK
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.EVENT_CLICK_COUPON
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.EVENT_VIEW
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.KEY_ECOMMERCE
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.KEY_EVENT
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.KEY_EVENT_ACTION
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.KEY_EVENT_CATEGORY
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.KEY_EVENT_LABEL
import com.tokopedia.discovery2.analytics.DiscoveryAnalyticsConstants.Companion.NONE_OTHER
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

class DiscoveryAnalytics {

    companion object {
        val pageType: String = ""
        val pagePath: String = ""

        //20
        @JvmStatic
        fun trackBackClick() {
            val tracker = TrackApp.getInstance().gtm
            val map = DataLayer.mapOf(
                    KEY_EVENT, EVENT_CLICK,
                    KEY_EVENT_CATEGORY, "discovery page - $pageType - $pagePath",
                    KEY_EVENT_ACTION, CLICK_BACK_BUTTON_ACTION,
                    KEY_EVENT_LABEL, "")
            tracker.sendEnhanceEcommerceEvent(map)
        }

        //21
        @JvmStatic
        fun trackShareClick() {
            val tracker = TrackApp.getInstance().gtm
            val map = DataLayer.mapOf(
                    KEY_EVENT, EVENT_CLICK,
                    KEY_EVENT_CATEGORY, "discovery page - $pageType - $pagePath",
                    KEY_EVENT_ACTION, CLICK_SOCIAL_SHARE_ACTION,
                    KEY_EVENT_LABEL, "")
            tracker.sendEnhanceEcommerceEvent(map)
        }

        //55
        @JvmStatic
        fun trackEventImpressionCoupon(trackingQueue: TrackingQueue, coupons: ArrayList<DataItem>?) {
            if (!coupons.isNullOrEmpty()) {
                val list = ArrayList<Map<String, Any>>()
                for (coupon in coupons) {
                    val map = HashMap<String, Any>()
                    coupon.let {
                        map["id"] = it.id.toString()
                        map["name"] = "${it.minUsageLabel} - p(x) - promo list - mini coupon"
                        map["creative"] = it.minUsageLabel.toString()
                        map["creative_url"] = it.imageUrlMobile ?: NONE_OTHER
                        map["position"] = coupons.indexOf(it) + 1
                        map["promo_id"] = it.promoId.toString()
                        map["promo_code"] = it.couponCode.toString()
                    }
                    list.add(map)
                }

                val ecommerce: MutableMap<String, Any>? = DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                        "promotions", list))

                val map = DataLayer.mapOf(
                        KEY_EVENT, EVENT_VIEW,
                        KEY_EVENT_CATEGORY, "discovery page - $pageType - $pagePath",
                        KEY_EVENT_ACTION, CLAIM_COUPON_IMPRESSION,
                        KEY_EVENT_LABEL, "",
                        KEY_ECOMMERCE, ecommerce)
                trackingQueue.putEETracking(map as HashMap<String, Any>)
            }
        }

        //56
        @JvmStatic
        fun trackClickClaimCoupon(couponName: String, promoCode: String) {
            val tracker = TrackApp.getInstance().gtm
            val map = DataLayer.mapOf(
                    KEY_EVENT, EVENT_CLICK,
                    KEY_EVENT_CATEGORY, "discovery page - $pageType - $pagePath",
                    KEY_EVENT_ACTION, CLICK_BUTTON_CLAIM_COUPON_ACTION,
                    KEY_EVENT_LABEL, "$couponName - $promoCode")
            tracker.sendEnhanceEcommerceEvent(map)
        }

        //57
        @JvmStatic
        fun trackEventClickCoupon(trackingQueue: TrackingQueue, coupon: DataItem, position: Int) {
            val tracker = TrackApp.getInstance().gtm
            val list = ArrayList<Map<String, Any>>()
            coupon.let {
                list.add(DataLayer.mapOf(
                        "id", it.id.toString(),
                        "name", "${it.minUsageLabel} - p(x) - promo list - mini coupon",
                        "creative", it.minUsageLabel.toString(),
                        "creative_url", it.imageUrlMobile ?: NONE_OTHER,
                        "position", position.toString(),
                        "promo_id", it.promoId.toString(),
                        "promo_code", it.couponCode.toString()
                ))
            }

            val promotions: MutableMap<String, Any>? = DataLayer.mapOf(
                    "promotions", list)

            val map = DataLayer.mapOf(
                    KEY_EVENT, EVENT_CLICK_COUPON,
                    KEY_EVENT_CATEGORY, "discovery page - $pageType - $pagePath",
                    KEY_EVENT_ACTION, CLAIM_COUPON_CLICK,
                    KEY_EVENT_LABEL, "${coupon.name} - ${coupon.couponCode}",
                    KEY_ECOMMERCE, promotions)
            tracker.sendEnhanceEcommerceEvent(map)
        }
    }
}