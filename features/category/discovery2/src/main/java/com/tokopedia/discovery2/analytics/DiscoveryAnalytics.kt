package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.quickcouponresponse.ClickCouponData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set

class DiscoveryAnalytics(val pageType: String = EMPTY_STRING,
                         val pagePath: String = EMPTY_STRING,
                         val trackingQueue: TrackingQueue) {

    private var eventDiscoveryCategory: String = "$VALUE_DISCOVERY_PAGE - $pageType - $pagePath"
    private var productCardImpressionLabel: String = EMPTY_STRING
    private var productCardItemList: String = EMPTY_STRING

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun createGeneralEvent(eventName: String = EVENT_CLICK_DISCOVERY, eventAction: String,
                                   eventLabel: String = EMPTY_STRING): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_EVENT to eventName,
                KEY_EVENT_CATEGORY to eventDiscoveryCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)
    }

    private fun getTargetingType(action: String): String {
        when (action) {
            ACTION_APPLINK -> return ACTION_APPLINK_VALUE
            ACTION_CODE -> return ACTION_CODE_VALUE
            ACTION_LOCAL_CALENDAR -> return ACTION_LOCAL_CALENDAR_VALUE
            ACTION_NOTIFIER -> return ACTION_NOTIFIER_VALUE
        }
        return EMPTY_STRING
    }

    fun trackBannerImpression(banners: List<DataItem>) {
        if (banners.isNotEmpty()) {
            val componentName = banners[0].parentComponentName ?: EMPTY_STRING
            val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
                    eventAction = IMPRESSION_DYNAMIC_BANNER, eventLabel = componentName)
            val list = ArrayList<Map<String, Any>>()
            var index = 0
            for (banner in banners) {
                val hashMap = HashMap<String, Any>()
                banner.let {
                    hashMap[KEY_ID] = it.id ?: 0
                    hashMap[KEY_NAME] = "$eventDiscoveryCategory - ${banner.positionForParentItem + 1} - ${getTargetingType(banner.action ?: EMPTY_STRING)} - $componentName"
                    hashMap[KEY_CREATIVE] = it.name ?: EMPTY_STRING
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

    fun trackBannerClick(banner: DataItem, bannerPosition: Int) {
        val componentName = banner.parentComponentName ?: EMPTY_STRING
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_DYNAMIC_BANNER, eventLabel = componentName)
        val list = ArrayList<Map<String, Any>>()
        banner.let {
            list.add(mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_NAME to "$eventDiscoveryCategory - ${banner.positionForParentItem + 1} - ${getTargetingType(banner.action ?: EMPTY_STRING)} - $componentName",
                    KEY_CREATIVE to it.persona.toString(),
                    KEY_POSITION to bannerPosition + 1
            ))
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_ATTRIBUTION] = banner.attribution ?: EMPTY_STRING
        map[KEY_AFFINITY_LABEL] = banner.name ?: EMPTY_STRING
        map[KEY_CATEGORY_ID] = banner.category ?: EMPTY_STRING
        map[KEY_SHOP_ID] = banner.shopId ?: EMPTY_STRING
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackCategoryNavigationImpression(componentsItems: ArrayList<ComponentsItem>) {
        if (componentsItems.isNotEmpty()) {
            val list = ArrayList<Map<String, Any>>()
            for (coupon in componentsItems) {
                var headerPosition = 0
                val data: ArrayList<DataItem> = ArrayList()
                coupon.data?.let {
                    data.addAll(it)
                    headerPosition = coupon.data?.firstOrNull()?.positionForParentItem ?: 0 + 1
                }
                val map = HashMap<String, Any>()
                data[0].let {
                    map[KEY_ID] = it.id.toString()
                    map[KEY_CREATIVE] = (it.name ?: EMPTY_STRING)
                    map[KEY_NAME] = "/$pagePath - $pageType - $headerPosition - $SUB_CATEGORY_NAVIGATION"
                    map[KEY_POSITION] = componentsItems.indexOf(coupon) + 1
                }
                list.add(map)
            }

            val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                    EVENT_PROMO_VIEW to mapOf(
                            KEY_PROMOTIONS to list))
            val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = IMPRESSION_CATEGORY_NAVIGATION)
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    fun trackCategoryNavigationClick(categoryItem: DataItem?, position: Int) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_CATEGORY_NAVIGATION, eventLabel = "${categoryItem?.dynamicComponentId} - ${categoryItem?.name}")
        val list = ArrayList<Map<String, Any>>()
        categoryItem?.let {
            list.add(mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_NAME to "/$pagePath - $pageType - ${it.positionForParentItem + 1} - $SUB_CATEGORY_NAVIGATION",
                    KEY_CREATIVE to (it.name ?: EMPTY_STRING),
                    KEY_POSITION to position + 1
            ))
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackClickVideo(videoUrl: String, videoName: String, videoPlayedTime: String) {
        val map = createGeneralEvent(eventAction = "$CLICK_VIDEO - $videoUrl", eventLabel = "$videoName - $videoPlayedTime")
        getTracker().sendGeneralEvent(map)
    }

    fun trackTabsClick(tabName: String) {
        val map = createGeneralEvent(eventAction = CLICK_TAB, eventLabel = tabName)
        getTracker().sendGeneralEvent(map)
    }

    fun trackBackClick() {
        val map = createGeneralEvent(eventAction = CLICK_BACK_BUTTON_ACTION)
        getTracker().sendGeneralEvent(map)
    }

    fun trackShareClick() {
        val map = createGeneralEvent(eventAction = CLICK_SOCIAL_SHARE_ACTION)
        getTracker().sendGeneralEvent(map)
    }

    fun trackLihatSemuaClick(headerName: String?) {
        val map = createGeneralEvent(eventAction = CLICK_VIEW_ALL, eventLabel = headerName
                ?: EMPTY_STRING)
        getTracker().sendGeneralEvent(map)
    }

    fun trackImpressionIconDynamicComponent(headerName: String, icons: List<DataItem>) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
                eventAction = "$IMPRESSION_ICON_DYNAMIC_COMPONENT - $headerName")
        if (icons.isNotEmpty()) {
            val headerPosition = icons[0].positionForParentItem + 1
            val list = ArrayList<Map<String, Any>>()
            var index = 0
            for (icon in icons) {
                val hashMap = HashMap<String, Any>()
                icon.let {
                    hashMap[KEY_ID] = it.dynamicComponentId.toString()
                    hashMap[KEY_NAME] = "$eventDiscoveryCategory - $VALUE_CATEGORY_ICON - $headerName - $headerPosition"
                    hashMap[KEY_CREATIVE] = it.name ?: EMPTY_STRING
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

    fun trackClickIconDynamicComponent(iconPosition: Int, icon: DataItem) {
        val headerName = icon.title
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = "$CLICK_ICON_DYNAMIC_COMPONENT - $headerName")
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

    fun trackClickSeeAllBanner() {
        val map = createGeneralEvent(eventAction = BANNER_CAROUSEL_SEE_ALL_CLICK)
        getTracker().sendGeneralEvent(map)
    }

    fun trackClickCustomTopChat() {
        val map = createGeneralEvent(eventAction = CUSTOM_TOP_CHAT_CLICK)
        getTracker().sendGeneralEvent(map)
    }

    fun trackClickChipsFilter(filterName: String) {
        val map = createGeneralEvent(eventAction = CHIPS_FILTER_CLICK, eventLabel = filterName)
        getTracker().sendGeneralEvent(map)
    }


    fun trackTimerSprintSale() {
        val map = createGeneralEvent(eventAction = TIMER_SPRINT_SALE_CLICK)
        getTracker().sendGeneralEvent(map)
    }

    fun trackEventImpressionProductCard(componentsItems: ArrayList<ComponentsItem>, isLogin: Boolean) {
        val login = if (isLogin) "login" else "nonlogin"
        val list = ArrayList<Map<String, Any>>()
        if (componentsItems.isNotEmpty()) {
            var index = 0
            for (product in componentsItems) {
                val data: ArrayList<DataItem> = ArrayList()
                product.data?.let {
                    data.addAll(it)
                }
                val map = HashMap<String, Any>()
                data[0].let {
                    productCardImpressionLabel = "$login ${it.typeProductCard}"
                    productCardItemList = "/$pagePath - $pageType - ${it.positionForParentItem.plus(1)} - $login - ${it.typeProductCard} - - ${if (it.isTopads == true) TOPADS else NON_TOPADS}"
                    map[KEY_NAME] = it.name.toString()
                    map[KEY_ID] = it.productId.toString()
                    map[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
                    map[KEY_BRAND] = NONE_OTHER
                    map[KEY_ITEM_CATEGORY] = NONE_OTHER
                    map[KEY_VARIANT] = NONE_OTHER
                    map[KEY_POSITION] = ++index
                    map[LIST] = productCardItemList
                    map[DIMENSION83] = if (it.freeOngkir?.isActive == true) BEBAS_ONGKIR else NONE_OTHER
                }
                list.add(map)
            }

            val eCommerce = mapOf(
                    CURRENCY_CODE to IDR,
                    KEY_IMPRESSIONS to list)
            val map = createGeneralEvent(eventName = EVENT_PRODUCT_VIEW,
                    eventAction = PRODUCT_LIST_IMPRESSION, eventLabel = productCardImpressionLabel)
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    fun trackProductCardClick(data: DataItem?, isLogin: Boolean, position: Int) {
        val login = if (isLogin) "login" else "nonlogin"
        val list = ArrayList<Map<String, Any>>()
        val listMap = HashMap<String, Any>()
        data?.let {
            productCardItemList = "/$pagePath - $pageType - ${it.positionForParentItem.plus(1)} - $login - ${it.typeProductCard} - - ${if (it.isTopads == true) TOPADS else NON_TOPADS}"
            listMap[KEY_NAME] = it.name.toString()
            listMap[KEY_ID] = it.productId.toString()
            listMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
            listMap[KEY_BRAND] = NONE_OTHER
            listMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            listMap[KEY_VARIANT] = NONE_OTHER
            listMap[KEY_POSITION] = position + 1
            listMap[LIST] = productCardItemList
            listMap[DIMENSION83] = if (it.freeOngkir?.isActive == true) BEBAS_ONGKIR else NONE_OTHER
        }
        list.add(listMap)

        val eCommerce = mapOf(
                CLICK to mapOf(
                        ACTION_FIELD to mapOf(
                                LIST to productCardItemList
                        ),
                        PRODUCTS to list
                )
        )
        val map = createGeneralEvent(eventName = EVENT_PRODUCT_CLICK, eventAction = CLICK_PRODUCT_LIST, eventLabel = productCardImpressionLabel)
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
        productCardImpressionLabel = EMPTY_STRING
        productCardItemList = EMPTY_STRING

    }

    fun trackEventImpressionCoupon(componentsItems: ArrayList<ComponentsItem>) {
        if (componentsItems.isNotEmpty()) {
            val list = ArrayList<Map<String, Any>>()
            for (coupon in componentsItems) {
                val data: ArrayList<DataItem> = ArrayList()
                coupon.data?.let {
                    data.addAll(it)
                }
                val map = HashMap<String, Any>()
                data[0].let {
                    map[KEY_ID] = it.id.toString()
                    map[KEY_CREATIVE_URL] = if (coupon.properties?.columns?.equals(DOUBLE_COLUMNS) == true)
                        it.smallImageUrlMobile
                                ?: NONE_OTHER else it.imageUrlMobile ?: NONE_OTHER
                    map[KEY_POSITION] = componentsItems.indexOf(coupon) + 1
                    map[KEY_PROMO_ID] = it.promoId.toString()
                    map[KEY_PROMO_CODE] = it.slug.toString()
                }
                list.add(map)
            }

            val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                    EVENT_PROMO_VIEW to mapOf(
                            KEY_PROMOTIONS to list))
            val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = CLAIM_COUPON_IMPRESSION)
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    fun trackClickClaimCoupon(couponName: String?, promoCode: String?) {
        val map = createGeneralEvent(eventAction = CLICK_BUTTON_CLAIM_COUPON_ACTION, eventLabel = "$couponName - $promoCode")
        getTracker().sendGeneralEvent(map)
    }

    fun trackEventClickCoupon(coupon: DataItem?, position: Int, isDouble: Boolean) {
        val list = ArrayList<Map<String, Any>>()
        coupon?.let {
            list.add(mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_CREATIVE_URL to if (isDouble) it.smallImageUrlMobile
                            ?: NONE_OTHER else it.imageUrlMobile ?: NONE_OTHER,
                    KEY_POSITION to (position + 1).toString(),
                    KEY_PROMO_ID to it.promoId.toString(),
                    KEY_PROMO_CODE to it.slug.toString()
            ))
        }
        val promotions: Map<String, ArrayList<Map<String, Any>>> = mapOf(
                KEY_PROMOTIONS to list)
        val map = createGeneralEvent(eventName = EVENT_CLICK_COUPON, eventAction = CLAIM_COUPON_CLICK,
                eventLabel = "${coupon?.name} - ${coupon?.couponCode}")
        map[KEY_E_COMMERCE] = promotions
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackEventImpressionTopAdsShop(dataItem: DataItem?) {
        val list = ArrayList<Map<String, Any>>()
        dataItem?.let {
            val map = HashMap<String, Any>()
            map[KEY_ID] = it.shopId ?: EMPTY_STRING
            map[KEY_NAME] = "/discovery/${pagePath} - topads - headline"
            map[KEY_CREATIVE] = it.shopName ?: EMPTY_STRING
            map[KEY_POSITION] = it.positionForParentItem + 1
            list.add(map)
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = TOP_ADS_HEADLINE_IMPRESSION, eventLabel = dataItem?.imageUrlMobile
                ?: EMPTY_STRING)
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun trackClickTopAdsShop(shop: DataItem) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = TOP_ADS_HEADLINE_SHOP, eventLabel = shop.imageUrlMobile
                ?: EMPTY_STRING)
        val list = ArrayList<Map<String, Any>>()
        shop.let {
            list.add(mapOf(
                    KEY_ID to it.shopId.toString(),
                    KEY_NAME to "/discovery/${pagePath} - topads - headline shop",
                    KEY_CREATIVE to (it.shopName ?: EMPTY_STRING),
                    KEY_POSITION to shop.positionForParentItem + 1
            ))
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackClickTopAdsProducts(item: DataItem) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = TOP_ADS_HEADLINE_PRODUCT)
        val list = ArrayList<Map<String, Any>>()
        item.let {
            list.add(mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_NAME to "/discovery/${pagePath} - topads - headline product",
                    KEY_CREATIVE to (it.name ?: EMPTY_STRING),
                    KEY_POSITION to item.positionForParentItem + 1
            ))
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackQuickCouponImpression(clickCouponData: ClickCouponData) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
                eventAction = if (clickCouponData.couponApplied == true) IMPRESSION_MINI_COUPON_CANCEL else IMPRESSION_MINI_COUPON_USE,
                eventLabel = "${clickCouponData.codePromo} - ${clickCouponData.realCode}")
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_ID to clickCouponData.componentID,
                KEY_NAME to "/tokopoints/penukaran points - ${clickCouponData.componentPosition} - promo list - mini coupon",
                KEY_CREATIVE to (clickCouponData.componentName ?: EMPTY_STRING),
                KEY_POSITION to clickCouponData.componentPosition
        ))

        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun trackQuickCouponClick(clickCouponData: ClickCouponData) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK,
                eventAction = CLICK_MINI_COUPON_DETAIL,
                eventLabel = "${clickCouponData.codePromo ?: EMPTY_STRING} - ${clickCouponData.realCode ?: EMPTY_STRING}")
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_ID to clickCouponData.componentID,
                KEY_NAME to "/tokopoints/penukaran points - ${clickCouponData.componentPosition} - promo list - mini coupon",
                KEY_CREATIVE to (clickCouponData.componentName ?: EMPTY_STRING),
                KEY_POSITION to clickCouponData.componentPosition
        ))

        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun trackQuickCouponApply(clickCouponData: ClickCouponData) {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CLICK_ON_MINI_COUPON_USE,
                eventLabel = "${clickCouponData.codePromo} - ${clickCouponData.realCode}")
        getTracker().sendGeneralEvent(map)
    }

    fun trackQuickCouponPhoneVerified() {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CLICK_PHONE_VERIFIED,
                eventLabel = EMPTY_STRING)
        getTracker().sendGeneralEvent(map)
    }

    fun trackQuickCouponPhoneVerifyCancel() {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CLICK_PHONE_CLOSED,
                eventLabel = EMPTY_STRING)
        getTracker().sendGeneralEvent(map)
    }
}