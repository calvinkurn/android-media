package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.data.AdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Level
import com.tokopedia.discovery2.data.quickcouponresponse.ClickCouponData
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlin.collections.set

class DiscoveryAnalytics(val pageType: String = EMPTY_STRING,
                         val pagePath: String = EMPTY_STRING,
                         val pageIdentifier: String = EMPTY_STRING,
                         val campaignCode : String = EMPTY_STRING,
                         val sourceIdentifier: String = EMPTY_STRING,
                         val trackingQueue: TrackingQueue) {

    private var eventDiscoveryCategory: String = "$VALUE_DISCOVERY_PAGE - $pageType - ${removeDashPageIdentifier(pageIdentifier)}"
    private var productCardImpressionLabel: String = EMPTY_STRING
    private var productCardItemList: String = EMPTY_STRING
    private var viewedProductsSet: MutableSet<String> = HashSet()

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
                    hashMap[KEY_NAME] = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${banner.positionForParentItem + 1} - - $componentName"
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
                    KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${banner.positionForParentItem + 1} - - $componentName",
                    KEY_CREATIVE to it.name.toString(),
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
        map[KEY_CAMPAIGN_CODE] = campaignCode
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
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_CATEGORY_NAVIGATION, eventLabel = "${categoryItem?.id} - ${categoryItem?.name}")
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

    fun trackBackClick() {
        val map = createGeneralEvent(eventAction = CLICK_BACK_BUTTON_ACTION)
        getTracker().sendGeneralEvent(map)
    }

    fun trackShareClick() {
        val map = createGeneralEvent(eventAction = CLICK_SOCIAL_SHARE_ACTION)
        getTracker().sendGeneralEvent(map)
    }

    fun trackSearchClick() {
        val eventCategory = "$TOP_NAV - $VALUE_DISCOVERY_PAGE - $pageType - ${removeDashPageIdentifier(pageIdentifier)}"
        val map: MutableMap<String, Any> = mutableMapOf(
                KEY_EVENT to CLICK_TOP_NAV,
                KEY_EVENT_CATEGORY to eventCategory,
                KEY_EVENT_ACTION to CLICK_SEARCH_BOX,
                KEY_EVENT_LABEL to "")
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
        map[KEY_CAMPAIGN_CODE] = campaignCode
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

    fun trackClickQuickFilter(filterName: String, componentName: String?) {
        val map = createGeneralEvent(eventAction = QUICK_FILTER_CLICK, eventLabel = "$componentName - $filterName")
        getTracker().sendGeneralEvent(map)
    }

    fun trackClickDetailedFilter(componentName: String?) {
        val map = createGeneralEvent(eventAction = DETAILED_FILTER_CLICK, eventLabel = componentName ?: "")
        getTracker().sendGeneralEvent(map)
    }

    fun trackClickApplyFilter(mapParameters: Map<String, String>) {
        var label = ""
        for (map in mapParameters) {
            label = "$label&${map.key}-${map.value}"
        }
        val map = createGeneralEvent(eventAction = APPLY_FILTER_CLICK, eventLabel = label.removePrefix("&"))
        getTracker().sendGeneralEvent(map)
    }

    fun trackTimerSprintSale() {
        val map = createGeneralEvent(eventAction = TIMER_SPRINT_SALE_CLICK)
        getTracker().sendGeneralEvent(map)
    }

    private fun trackEventImpressionProductCard(componentsItems: ComponentsItem, isLogin: Boolean) {
        val tabName = getTabValue(componentsItems)
        val login = if (isLogin) LOGIN else NON_LOGIN
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        componentsItems.data?.get(0)?.let {
            val productTypeName = getProductName(it.typeProductCard)
            productCardImpressionLabel = "$login - $productTypeName"
            productCardItemList = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${it.positionForParentItem.plus(1)} - $login - $productTypeName - - ${if (it.isTopads == true) TOPADS else NON_TOPADS} - $tabName"
            productMap[KEY_NAME] = it.name.toString()
            productMap[KEY_ID] = it.productId.toString()
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[KEY_POSITION] = componentsItems.position + 1
            productMap[LIST] = productCardItemList
            productMap[DIMENSION83] = if (it.freeOngkir?.isActive == true) BEBAS_ONGKIR else NONE_OTHER
            addSourceData(productMap)
            if (productTypeName == PRODUCT_SPRINT_SALE || productTypeName == PRODUCT_SPRINT_SALE_CAROUSEL) {
                productMap[DIMENSION96] = " - ${if (it.notifyMeCount.toIntOrZero() > 0) it.notifyMeCount else " "} - ${if (it.pdpView.toIntOrZero() > 0) it.pdpView else 0} - " +
                        "${if (it.campaignSoldCount.toIntOrZero() > 0) it.pdpView else 0} $SOLD - ${if (it.customStock.toIntOrZero() > 0) it.customStock else 0} $LEFT - - $tabName - $NOTIFY_ME ${getNotificationStatus(componentsItems)}"
            }

        }
        list.add(productMap)

        val eCommerce = mapOf(
                CURRENCY_CODE to IDR,
                KEY_IMPRESSIONS to list)
        val map = createGeneralEvent(eventName = EVENT_PRODUCT_VIEW,
                eventAction = PRODUCT_LIST_IMPRESSION, eventLabel = productCardImpressionLabel)
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
        productCardImpressionLabel = EMPTY_STRING
        productCardItemList = EMPTY_STRING
    }

    fun viewProductsList(componentsItems: ComponentsItem, isLogin: Boolean) {
        if (!componentsItems.data.isNullOrEmpty()) {
            val productID = componentsItems.data!![0].productId
            if (!productID.isNullOrEmpty()) {
                if (viewedProductsSet.add(productID)) {
                    trackEventImpressionProductCard(componentsItems, isLogin)
                }
            }
        }
    }

    private fun getProductName(productType: String?): String {
        return when (productType) {
            PRODUCT_CARD_REVAMP_ITEM -> PRODUCT_CARD_REVAMP
            PRODUCT_CARD_CAROUSEL_ITEM -> PRODUCT_CARD_CAROUSEL
            PRODUCT_SPRINT_SALE_ITEM -> PRODUCT_SPRINT_SALE
            PRODUCT_SPRINT_SALE_CAROUSEL_ITEM -> PRODUCT_SPRINT_SALE_CAROUSEL
            else -> EMPTY_STRING
        }
    }

    fun clearProductViewIds() {
        viewedProductsSet.clear()
    }

    fun trackProductCardClick(componentsItems: ComponentsItem, isLogin: Boolean) {
        if (!componentsItems.data.isNullOrEmpty()) {
            val tabName = getTabValue(componentsItems)
            val login = if (isLogin) LOGIN else NON_LOGIN
            val list = ArrayList<Map<String, Any>>()
            val listMap = HashMap<String, Any>()
            componentsItems.data?.get(0)?.let {
                val productTypeName = getProductName(it.typeProductCard)
                productCardImpressionLabel = "$login - $productTypeName"
                productCardItemList = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${it.positionForParentItem.plus(1)} - $login - $productTypeName - - ${if (it.isTopads == true) TOPADS else NON_TOPADS} - $tabName"
                listMap[KEY_NAME] = it.name.toString()
                listMap[KEY_ID] = it.productId.toString()
                listMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
                listMap[KEY_BRAND] = NONE_OTHER
                listMap[KEY_ITEM_CATEGORY] = NONE_OTHER
                listMap[KEY_VARIANT] = NONE_OTHER
                listMap[KEY_POSITION] = componentsItems.position + 1
                listMap[LIST] = productCardItemList
                listMap[DIMENSION83] = if (it.freeOngkir?.isActive == true) BEBAS_ONGKIR else NONE_OTHER
                addSourceData(listMap)
                if (productTypeName == PRODUCT_SPRINT_SALE || productTypeName == PRODUCT_SPRINT_SALE_CAROUSEL) {
                    listMap[DIMENSION96] = " - ${if (it.notifyMeCount.toIntOrZero() > 0) it.notifyMeCount else " "} - ${if (it.pdpView.toIntOrZero() > 0) it.pdpView else 0} - " +
                            "${if (it.campaignSoldCount.toIntOrZero() > 0) it.pdpView else 0} $SOLD - ${if (it.customStock.toIntOrZero() > 0) it.customStock else 0} $LEFT - - $tabName - $NOTIFY_ME ${getNotificationStatus(componentsItems)}"
                }
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
            map[KEY_CAMPAIGN_CODE] = campaignCode
            map[KEY_E_COMMERCE] = eCommerce
            getTracker().sendEnhanceEcommerceEvent(map)
            productCardImpressionLabel = EMPTY_STRING
            productCardItemList = EMPTY_STRING
        }
    }

    private fun getTabValue(componentsItems: ComponentsItem): String {
        val parentProductContainer = getComponent(componentsItems.parentComponentId, pageIdentifier)
        parentProductContainer?.let {
            val tabComponent = getComponent(parentProductContainer.parentComponentId, pageIdentifier)
            tabComponent?.let {
                if (!it.data.isNullOrEmpty()) {
                    return it.data?.get(0)?.name ?: EMPTY_STRING
                }
            }
        }
        return EMPTY_STRING
    }


    private fun getNotificationStatus(componentsItems: ComponentsItem): String {
        val parentProductContainer = getComponent(componentsItems.parentComponentId, pageIdentifier)
        parentProductContainer?.let {
            return if (it.properties?.buttonNotification == true) NOTIFY_ON else NOTIFY_OFF
        }
        return NOTIFY_ON
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
        getTracker().sendGeneralEvent(map)
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

    fun trackOpenScreen(screenName: String, additionalInfo: AdditionalInfo?, userLoggedIn: Boolean) {
        if (screenName.isNotEmpty()) {
            val map = getTrackingMapOpenScreen(screenName, additionalInfo, userLoggedIn)
            TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, map)
        }
    }

    private fun getTrackingMapOpenScreen(pageIdentifier: String, additionalInfo: AdditionalInfo?, userLoggedIn: Boolean): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[KEY_EVENT] = OPEN_SCREEN
        map[EVENT_NAME] = OPEN_SCREEN
        map[SCREEN_NAME] = "${DISCOVERY_PATH}${removeDashPageIdentifier(pageIdentifier)}"
        map[IS_LOGGED_IN_STATUS] = userLoggedIn.toString()
        map[DISCOVERY_NAME] = pageIdentifier
        map[DISCOVERY_SLUG] = pageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = DISCOVERY
        map[CATEGORY] = EMPTY_STRING
        map[CATEGORY_ID] = EMPTY_STRING
        map[SUB_CATEGORY] = EMPTY_STRING
        map[SUB_CATEGORY_ID] = EMPTY_STRING

        additionalInfo?.category?.levels?.let { categoryListLevels ->
            addCategoryData(categoryListLevels)?.let {
                map.putAll(it)
            }
        }
        return map
    }

    private fun addCategoryData(categoryListLevelsInfo: List<Level>?): MutableMap<String, String>? {
        val categoryMap = mutableMapOf<String, String>()

        if (!categoryListLevelsInfo.isNullOrEmpty()) {
            categoryListLevelsInfo.forEach { levelData ->
                when (levelData.level) {
                    CATEGORY_LEVEL_1 -> {
                        categoryMap[CATEGORY] = levelData.name ?: EMPTY_STRING
                        categoryMap[CATEGORY_ID] = levelData.categoryId?.toString() ?: EMPTY_STRING
                    }
                    CATEGORY_LEVEL_2 -> {
                        categoryMap[SUB_CATEGORY] = levelData.name ?: EMPTY_STRING
                        categoryMap[SUB_CATEGORY_ID] = levelData.categoryId?.toString()
                                ?: EMPTY_STRING
                    }
                }
            }
        }
        return categoryMap
    }

    private fun removeDashPageIdentifier(identifier: String): String {
        if (identifier.isNotEmpty()) {
            return identifier.replace("-", " ")
        }
        return EMPTY_STRING
    }

    fun trackTabsClick(id: String, parentPosition: Int, dataItem: DataItem, tabPosition1: Int) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_TAB, eventLabel = dataItem.name
                ?: "")
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_ID to id,
                KEY_NAME to "/$pagePath - $pageType - ${parentPosition + 1} - - $MEGA_TAB_COMPONENT",
                KEY_CREATIVE to (dataItem.name ?: EMPTY_STRING),
                KEY_POSITION to tabPosition1 + 1
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }


    fun trackCarouselBannerImpression(banners: List<DataItem>) {
        if (banners.isNotEmpty()) {
            val componentName = banners[0].parentComponentName ?: EMPTY_STRING
            val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
                    eventAction = IMPRESSION_DYNAMIC_BANNER, eventLabel = componentName)
            val list = ArrayList<Map<String, Any>>()
            for ((index, banner) in banners.withIndex()) {
                val hashMap = HashMap<String, Any>()
                    hashMap[KEY_ID] = if (banner.id == null) DEFAULT_ID else if (banner.id!!.isNotEmpty()) banner.id!! else DEFAULT_ID
                    hashMap[KEY_NAME] = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${banner.positionForParentItem + 1} - - $componentName"
                    hashMap[KEY_CREATIVE] = banner.name ?: EMPTY_STRING
                    hashMap[KEY_POSITION] = index + 1
                    hashMap[KEY_PROMO_ID] = banner.trackingFields?.promoId ?: EMPTY_STRING
                    hashMap[PROMO_CODE] = banner.trackingFields?.promoCode ?: EMPTY_STRING
                list.add(hashMap)
            }
            val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                    EVENT_PROMO_VIEW to mapOf(
                            KEY_PROMOTIONS to list))
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    fun trackCarouselBannerClick(banner: DataItem, bannerPosition: Int) {
        val componentName = banner.parentComponentName ?: EMPTY_STRING
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_DYNAMIC_BANNER, eventLabel = "$componentName - ${banner.name} - ${banner.imageClickUrl}")
        val list = ArrayList<Map<String, Any>>()
            list.add(mapOf(
                    KEY_ID to if (banner.id == null) DEFAULT_ID else if (banner.id!!.isNotEmpty()) banner.id!! else DEFAULT_ID,
                    KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${banner.positionForParentItem + 1} - - $componentName",
                    KEY_CREATIVE to (banner.name ?: EMPTY_STRING),
                    KEY_POSITION to bannerPosition + 1,
                    KEY_PROMO_ID to (banner.trackingFields?.promoId ?: EMPTY_STRING),
                    PROMO_CODE to (banner.trackingFields?.promoCode ?: EMPTY_STRING)
            ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_ATTRIBUTION] = banner.trackingFields?.bu ?: EMPTY_STRING
        map[KEY_AFFINITY_LABEL] = banner.name ?: EMPTY_STRING
        map[KEY_CATEGORY_ID] = banner.trackingFields?.categoryId ?: EMPTY_STRING
        map[KEY_SHOP_ID] = banner.shopId ?: EMPTY_STRING
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackBannerCarouselLihat() {
        val map = createGeneralEvent(eventAction = CLICK_VIEW_ALL_BANNER_CAROUSEL, eventLabel = EMPTY_STRING)
        getTracker().sendGeneralEvent(map)
    }
    private fun addSourceData(productMap: HashMap<String, Any>): HashMap<String, Any> {
        if (sourceIdentifier.isEmpty()) return productMap

        productMap[DIMENSION90] = sourceIdentifier
        return productMap
    }

    fun trackEventImpressionTopAdsShop(componentDataItem: ComponentsItem, cpmData: CpmData) {
        val list = ArrayList<Map<String, Any>>()
        val listMap = HashMap<String, Any>()
        listMap[KEY_ID] = "${cpmData.id}_${cpmData.cpm.cpmShop.id}"
        listMap[KEY_NAME] = "/$pagePath - $pageType - 1 - - $CPM_SHOP_PAGE_COMPONENT"
        listMap[KEY_CREATIVE] = componentDataItem.name ?: EMPTY_STRING
        listMap[KEY_POSITION] = 1
        list.add(listMap)
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = TOP_ADS_HEADLINE_IMPRESSION, eventLabel = EMPTY_STRING)
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun trackClickTopAdsShop(componentDataItem: ComponentsItem, cpmData: CpmData) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CPM_SHOP_CLICK, eventLabel = "${cpmData.id}-${cpmData.cpm.cpmShop.id}")
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_ID to "${cpmData.id}_${cpmData.cpm.cpmShop.id}",
                KEY_NAME to "/$pagePath - $pageType - 1 - - $CPM_SHOP_PAGE_COMPONENT",
                KEY_CREATIVE to (componentDataItem.name ?: EMPTY_STRING),
                KEY_POSITION to 1
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun trackTopAdsProductImpression(componentDataItem: ComponentsItem, cpmData: CpmData, componentPosition: Int, productPosition: Int, userLoggedIn: Boolean) {
        val login = if (userLoggedIn) LOGIN else NON_LOGIN
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        val cpmProductList = cpmData.cpm.cpmShop.products

        if (cpmProductList.isNotEmpty() && cpmProductList.size > productPosition) {
            val productData = cpmData.cpm.cpmShop.products[productPosition]
            productMap[KEY_NAME] = productData.name
            productMap[KEY_ID] = productData.id
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(productData.priceFormat
                    ?: "")
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[KEY_POSITION] = productPosition + 1
            productMap[dimension140] = sourceIdentifier
            productMap[LIST] = "/$pagePath - $pageType - ${componentPosition + 1} - $login - ${componentDataItem.name} - ${cpmData.id}"
            list.add(productMap)
        }

        val eCommerce = mapOf(
                CURRENCY_CODE to IDR,
                KEY_IMPRESSIONS to list)
        val map = createGeneralEvent(eventName = EVENT_PRODUCT_VIEW,
                eventAction = CPM_PRODUCT_LIST_IMPRESSION, eventLabel = EMPTY_STRING)
        map[KEY_CAMPAIGN_CODE] = campaignCode
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun trackClickTopAdsProducts(componentDataItem: ComponentsItem, cpmData: CpmData, componentPosition: Int, productPosition: Int, userLoggedIn: Boolean) {
        val login = if (userLoggedIn) LOGIN else NON_LOGIN
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        var productID = ""
        val cpmProductList = cpmData.cpm.cpmShop.products
        productCardItemList = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${componentPosition + 1} - $login - ${componentDataItem.name} - ${cpmData.id}"

        if (cpmProductList.isNotEmpty() && cpmProductList.size > productPosition) {
            val productData = cpmData.cpm.cpmShop.products[productPosition]
            productID = productData.id
            productMap[KEY_NAME] = productData.name
            productMap[KEY_ID] = productData.id
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(productData.priceFormat
                    ?: "")
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[KEY_POSITION] = productPosition + 1
            productMap[dimension140] = sourceIdentifier
            productMap[LIST] = "/$pagePath - $pageType - ${componentPosition + 1} - $login - ${componentDataItem.name} - ${cpmData.id}"
            list.add(productMap)
        }

        val eCommerce = mapOf(
                CLICK to mapOf(
                        ACTION_FIELD to mapOf(
                                LIST to productCardItemList
                        ),
                        PRODUCTS to list
                )
        )
        val map = createGeneralEvent(eventName = EVENT_PRODUCT_CLICK, eventAction = CLICK_PRODUCT_LIST, eventLabel = "${cpmData.id}-${cpmData.cpm.cpmShop.id}-$productID")
        map[KEY_CAMPAIGN_CODE] = campaignCode
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
        productCardItemList = EMPTY_STRING
    }

    fun trackHeaderSeeAllClick(isLogin: Boolean, componentsItems: ComponentsItem) {
        val loginValue = if (isLogin) LOGIN else NON_LOGIN
        val tabValue = getTabValue(componentsItems)
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CLICK_VIEW_ALL_HEADER,
                eventLabel = "$loginValue - ${componentsItems.name} - ${componentsItems.title} - $tabValue")
        getTracker().sendGeneralEvent(map)
    }
}