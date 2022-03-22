package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.Utils.Companion.getParentPosition
import com.tokopedia.discovery2.data.AdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Level
import com.tokopedia.discovery2.data.quickcouponresponse.ClickCouponData
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_ECOMMERCE
import com.tokopedia.quest_widget.tracker.Tracker
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlin.collections.set

open class DiscoveryAnalytics(pageType: String = DISCOVERY_DEFAULT_PAGE_TYPE,
                              pagePath: String = EMPTY_STRING,
                              pageIdentifier: String = EMPTY_STRING,
                              campaignCode: String = EMPTY_STRING,
                              sourceIdentifier: String = EMPTY_STRING,
                              private val userSession:UserSessionInterface,
                              trackingQueue: TrackingQueue) : BaseDiscoveryAnalytics(pageType, pagePath, pageIdentifier, campaignCode, sourceIdentifier, trackingQueue) {

    private var eventDiscoveryCategory: String = VALUE_DISCOVERY_PAGE
    private var removedDashPageIdentifier: String = removeDashPageIdentifier(pageIdentifier)
    private var productCardImpressionLabel: String = EMPTY_STRING
    private var productCardItemList: String = EMPTY_STRING
    private var viewedProductsSet: MutableSet<String> = HashSet()
    private var viewedCalendarSet: MutableSet<String> = HashSet()
    private var viewedAnchorTabsSet: MutableSet<String> = HashSet()

    private fun createGeneralEvent(eventName: String = EVENT_CLICK_DISCOVERY, eventAction: String,
                                   eventLabel: String = EMPTY_STRING): MutableMap<String, Any> {
        return mutableMapOf(
                KEY_EVENT to eventName,
                KEY_EVENT_CATEGORY to eventDiscoveryCategory,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_LABEL to eventLabel)
    }

    override fun trackBannerImpression(banners: List<DataItem>, componentPosition: Int?, userID: String?) {
        if (banners.isNotEmpty()) {
            banners.forEachIndexed { index, banner ->
                val componentName = banner.parentComponentName ?: EMPTY_STRING
                val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
                        eventAction = IMPRESSION_DYNAMIC_BANNER)
                map[PAGE_TYPE] = pageType
                map[PAGE_PATH] = removedDashPageIdentifier
                val list = ArrayList<Map<String, Any>>()
                val hashMap = HashMap<String, Any>()
                banner.let {
                    hashMap[KEY_ID] = it.id ?: 0
                    hashMap[KEY_NAME] = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${banner.positionForParentItem + 1} - - - ${componentName}${if (banner.action == ACTION_NOTIFIER) "-$NOTIFIER" else ""}"
                    hashMap[KEY_CREATIVE] = it.creativeName ?: EMPTY_STRING
                    hashMap[KEY_POSITION] = componentPosition?.plus(1)?:index+1
                }
                list.add(hashMap)
                val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                        EVENT_PROMO_VIEW to mapOf(
                                KEY_PROMOTIONS to list))
                map[KEY_E_COMMERCE] = eCommerce
                map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
                map[BUSINESS_UNIT] = HOME_BROWSE
                map[USER_ID] = userID ?: EMPTY_STRING
                trackingQueue.putEETracking(map as HashMap<String, Any>)
            }
        }
    }

    override fun trackBannerClick(banner: DataItem, bannerPosition: Int, userID: String?) {
        val componentName = banner.parentComponentName ?: EMPTY_STRING
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_DYNAMIC_BANNER, eventLabel = "${componentName}${if (banner.action == ACTION_NOTIFIER) "-$NOTIFIER" else ""}${if (!banner.name.isNullOrEmpty()) " - ${banner.name}" else " - "}${if (!banner.applinks.isNullOrEmpty()) " - ${banner.applinks}" else " - "}")
        val list = ArrayList<Map<String, Any>>()
        banner.let {
            list.add(mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${banner.positionForParentItem + 1} - - - ${componentName}${if (banner.action == ACTION_NOTIFIER) "-$NOTIFIER" else ""}",
                    KEY_CREATIVE to it.creativeName.toString(),
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
        map[KEY_CAMPAIGN_CODE] = "${if (banner.campaignCode.isNullOrEmpty()) campaignCode else banner.campaignCode}"
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[USER_ID] = userID ?: EMPTY_STRING
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackBrandRecommendationImpression(items: List<ComponentsItem>, componentPosition: Int, componentID: String) {
        if (items.isNotEmpty()) {
            items.forEachIndexed { index, brandItem ->
                if(!brandItem.data.isNullOrEmpty()){
                    brandItem.data?.firstOrNull()?.let {
                        val componentName = it.parentComponentName ?: EMPTY_STRING
                        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
                                eventAction = IMPRESSION_DYNAMIC_BANNER, eventLabel = "${componentName}${if (it.action == ACTION_NOTIFIER) "-$NOTIFIER" else ""}")
                        map[PAGE_TYPE] = pageType
                        map[PAGE_PATH] = removedDashPageIdentifier
                        val list = ArrayList<Map<String, Any>>()
                        val hashMap = HashMap<String, Any>()
                        it.let {
                            hashMap[KEY_ID] = it.id ?: 0
                            hashMap[KEY_NAME] = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${it.positionForParentItem + 1} - - ${componentName}${if (it.action == ACTION_NOTIFIER) "-$NOTIFIER" else ""}"
                            hashMap[KEY_CREATIVE] = it.creativeName ?: EMPTY_STRING
                            hashMap[KEY_POSITION] = index + 1
                            hashMap[KEY_PROMO_ID] = componentID
                        }
                        list.add(hashMap)
                        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                                EVENT_PROMO_VIEW to mapOf(
                                        KEY_PROMOTIONS to list))
                        map[KEY_E_COMMERCE] = eCommerce
                        trackingQueue.putEETracking(map as HashMap<String, Any>)
                    }
                }
            }
        }
    }


    override fun trackBrandRecommendationClick(banner: DataItem, bannerPosition: Int, compID : String) {
        val componentName = banner.parentComponentName ?: EMPTY_STRING
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_DYNAMIC_BANNER,
                eventLabel = "${componentName}${if (!banner.name.isNullOrEmpty()) " - ${banner.name}" else " - "}${if (!banner.applinks.isNullOrEmpty()) " - ${banner.applinks}" else " - "}")
        val list = ArrayList<Map<String, Any>>()
        banner.let {
            list.add(mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${banner.positionForParentItem + 1} - - ${componentName}",
                KEY_CREATIVE to "${it.creativeName ?: EMPTY_STRING}${if(!banner.shopId.isNullOrEmpty()){" - ${banner.shopId}"} else EMPTY_STRING}",
                    KEY_POSITION to bannerPosition + 1,
                    KEY_PROMO_ID to compID
            ))
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[KEY_ATTRIBUTION] = banner.attribution ?: EMPTY_STRING
        map[KEY_AFFINITY_LABEL] = banner.name ?: EMPTY_STRING
        map[KEY_CATEGORY_ID] = banner.category ?: EMPTY_STRING
        map[KEY_SHOP_ID] = banner.shopId ?: EMPTY_STRING
        map[KEY_CAMPAIGN_CODE] = "${if (banner.campaignCode.isNullOrEmpty()) campaignCode else banner.campaignCode}"
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackCategoryNavigationImpression(componentsItems: ArrayList<ComponentsItem>) {
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
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }


    override fun trackPlayWidgetClick(componentsItem: ComponentsItem, userID: String?, channelId: String, destinationURL: String, shopId: String, widgetPosition: Int, channelPositionInList: Int, isAutoPlay: Boolean) {
        val list = ArrayList<Map<String, Any>>()
        val creativeName = componentsItem.data?.firstOrNull()?.creativeName?: EMPTY_STRING
        list.add(mapOf(
                KEY_ID to "0_${if (shopId.isEmpty()) 0 else shopId}_$channelId",
                KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${widgetPosition + 1} - - - ${componentsItem.name}-$CHANNEL",
                KEY_CREATIVE to " - $creativeName - $isAutoPlay",
                KEY_POSITION to "$channelPositionInList - "
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK, eventAction = CLICK_DYNAMIC_BANNER,
            "${componentsItem.name ?: EMPTY_STRING} - $creativeName - $destinationURL"
        )
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackPlayWidgetBannerClick(componentsItem: ComponentsItem, userID: String?, widgetPosition: Int) {
        val creativeName = componentsItem.data?.firstOrNull()?.creativeName?: EMPTY_STRING
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY, eventAction = CLICK_OTHER_CONTENT, "${
            componentsItem.name
                    ?: EMPTY_STRING
        } - $creativeName - ${widgetPosition + 1}")
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        getTracker().sendGeneralEvent(map as HashMap<String, Any>)
    }

    override fun trackPlayWidgetReminderClick(componentsItem: ComponentsItem, userID: String?, widgetPosition: Int, channelPositionInList: Int, channelId: String, isRemindMe: Boolean) {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = if (isRemindMe) CLICK_REMIND_ME else CLICK_CANCEL_REMIND_ME,
                "${componentsItem.name?: EMPTY_STRING} - $channelId - $channelPositionInList - ")
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removeDashPageIdentifier(pageIdentifier)
        map[USER_ID] = userID ?: EMPTY_STRING
        getTracker().sendGeneralEvent(map as HashMap<String, Any>)
    }

    override fun trackPlayWidgetLihatSemuaClick(componentsItem: ComponentsItem, userID: String?, widgetPosition: Int) {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY, eventAction = CLICK_VIEW_ALL, "${
            componentsItem.name
                    ?: EMPTY_STRING
        } - ${widgetPosition + 1}")
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        getTracker().sendGeneralEvent(map as HashMap<String, Any>)

    }

    override fun trackPlayWidgetOverLayClick(componentsItem: ComponentsItem, userID: String?, widgetPosition: Int, channelPositionInList: Int, destinationURL: String) {
        val list = ArrayList<Map<String, Any>>()
        val creativeName = componentsItem.data?.firstOrNull()?.creativeName?: EMPTY_STRING
        list.add(mapOf(
                KEY_ID to componentsItem.id,
                KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${widgetPosition + 1} - - - ${componentsItem.name}-$BANNER",
                KEY_CREATIVE to creativeName,
                KEY_POSITION to "$channelPositionInList - "
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_DYNAMIC_BANNER, "${
            componentsItem.name
                    ?: EMPTY_STRING
        } - $creativeName - $destinationURL")
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackPlayWidgetOverLayImpression(componentsItem: ComponentsItem, userID: String?, widgetPosition: Int, channelPositionInList: Int, destinationURL: String) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_ID to componentsItem.id,
                KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${widgetPosition + 1} - - - ${componentsItem.name}-$BANNER",
                KEY_CREATIVE to (componentsItem.data?.firstOrNull()?.creativeName?: EMPTY_STRING),
                KEY_POSITION to "$channelPositionInList - "
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = IMPRESSION_DYNAMIC_BANNER, componentsItem.name
                ?: EMPTY_STRING)
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }


    override fun trackPlayWidgetImpression(componentsItem: ComponentsItem, userID: String?, channelId: String, shopId: String, widgetPosition: Int, channelPositionInList: Int, isAutoPlay: Boolean) {
        val list = ArrayList<Map<String, Any>>()
        val creativeName = componentsItem.data?.firstOrNull()?.creativeName?: EMPTY_STRING
        list.add(mapOf(
                KEY_ID to "0_${if (shopId.isEmpty()) 0 else shopId}_$channelId",
                KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${widgetPosition + 1} - - - ${componentsItem.name}-$CHANNEL",
                KEY_CREATIVE to " - $creativeName - $isAutoPlay",
                KEY_POSITION to "$channelPositionInList - "
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = IMPRESSION_DYNAMIC_BANNER, componentsItem.name
                ?: EMPTY_STRING)
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }


    override fun trackTDNBannerImpression(componentsItem: ComponentsItem, userID: String?, positionInPage: Int, adID: String, shopId: String) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_ID to "${adID}_$shopId",
                KEY_NAME to "$pagePath - $pageType - ${positionInPage + 1} - $TDN_BANNER_COMPONENT",
                KEY_CREATIVE to (componentsItem.data?.firstOrNull()?.creativeName ?: EMPTY_STRING),
                KEY_POSITION to "1"
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = IMPRESSION_TDN_BANNER, EMPTY_STRING)
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackTDNBannerClick(componentsItem: ComponentsItem, userID: String?, positionInPage: Int, adID: String, shopId: String) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_ID to "${adID}_$shopId",
                KEY_NAME to "$pagePath - $pageType - ${positionInPage + 1} - $TDN_BANNER_COMPONENT",
                KEY_CREATIVE to (componentsItem.data?.firstOrNull()?.creativeName ?: EMPTY_STRING),
                KEY_POSITION to "1"
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_TDN_BANNER, eventLabel ="$adID - $shopId" )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackCategoryNavigationClick(categoryItem: DataItem?, position: Int) {
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
        map[PAGE_TYPE] = pageType
        map[PAGE_TYPE] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackClickVideo(videoUrl: String, videoName: String, videoPlayedTime: String) {
        val map = createGeneralEvent(eventAction = CLICK_VIDEO,
                eventLabel = "$videoName - $videoUrl - $videoPlayedTime")
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackBackClick() {
        val map = createGeneralEvent(eventAction = CLICK_BACK_BUTTON_ACTION)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackShareClick() {
        val map = createGeneralEvent(eventAction = CLICK_SOCIAL_SHARE_ACTION)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackSearchClick() {
        val map: MutableMap<String, Any> = mutableMapOf(
                KEY_EVENT to CLICK_TOP_NAV,
                KEY_EVENT_ACTION to CLICK_SEARCH_BOX,
                KEY_EVENT_CATEGORY to TOP_NAV,
                KEY_EVENT_LABEL to "",
                BUSINESS_UNIT to HOME_BROWSE,
                CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
                PAGE_PATH to removedDashPageIdentifier,
                PAGE_SOURCE to PAGE_SOURCE_TOP_NAV,
                PAGE_TYPE to pageType)
        getTracker().sendGeneralEvent(map)
    }


    override fun trackGlobalNavBarClick(buttonName: String, userID: String?) {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to CLICK_TOP_NAV,
            KEY_EVENT_ACTION to "click $buttonName nav",
            KEY_EVENT_CATEGORY to TOP_NAV,
            KEY_EVENT_LABEL to "",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            USER_ID to (userID ?: ""),
            PAGE_SOURCE to PAGE_SOURCE_TOP_NAV,
            PAGE_TYPE to pageType
        )
        getTracker().sendGeneralEvent(map)
    }

    override fun trackLihatSemuaClick(dataItem: DataItem?) {
        val map = createGeneralEvent(eventAction = CLICK_VIEW_ALL, eventLabel = dataItem?.title
                ?: EMPTY_STRING)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        getTracker().sendGeneralEvent(map)
    }

    override fun trackMerchantVoucherLihatSemuaClick(dataItem: DataItem?) {
        val map = createGeneralEvent(eventAction = CLICK_MV_MULTIPLE_LIHAT, eventLabel = dataItem?.title
                ?: EMPTY_STRING)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        getTracker().sendGeneralEvent(map)
    }

    override fun trackImpressionIconDynamicComponent(headerName: String, icons: List<DataItem>) {
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
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    override fun trackClickIconDynamicComponent(iconPosition: Int, icon: DataItem) {
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
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackClickSeeAllBanner() {
        val map = createGeneralEvent(eventAction = BANNER_CAROUSEL_SEE_ALL_CLICK)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackClickCustomTopChat() {
        val map = createGeneralEvent(eventAction = CUSTOM_TOP_CHAT_CLICK)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackClickChipsFilter(filterName: String) {
        val map = createGeneralEvent(eventAction = CHIPS_FILTER_CLICK, eventLabel = filterName)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackClickQuickFilter(filterName: String, componentName: String?, value: String, isFilterSelected: Boolean) {
        val map = createGeneralEvent(eventAction = QUICK_FILTER_CLICK, eventLabel = "$componentName - $filterName")
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackClickDetailedFilter(componentName: String?) {
        val map = createGeneralEvent(eventAction = DETAILED_FILTER_CLICK, eventLabel = componentName
                ?: "")
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackClickApplyFilter(mapParameters: Map<String, String>) {
        var label = ""
        for (map in mapParameters) {
            label = "$label&${map.key}-${map.value}"
        }
        val map = createGeneralEvent(eventAction = APPLY_FILTER_CLICK, eventLabel = label.removePrefix("&"))
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackTimerSprintSale() {
        val map = createGeneralEvent(eventAction = TIMER_SPRINT_SALE_CLICK)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    private fun trackEventImpressionProductCard(componentsItems: ComponentsItem, isLogin: Boolean) {
        val login = if (isLogin) LOGIN else NON_LOGIN
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        componentsItems.data?.get(0)?.let {
            val productTypeName = getProductName(it.typeProductCard)
            productCardImpressionLabel = "$login - $productTypeName"
            productCardItemList = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${getParentPosition(componentsItems)+1} - $login - $productTypeName - - ${if (it.isTopads == true) TOPADS else NON_TOPADS} - ${if (it.creativeName.isNullOrEmpty()) "" else it.creativeName} - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName}"
            productMap[KEY_NAME] = it.name.toString()
            productMap[KEY_ID] = it.productId.toString()
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[KEY_POSITION] = componentsItems.position + 1
            productMap[LIST] = productCardItemList
            productMap[DIMENSION83] = getProductDime83(it)
            addSourceData(productMap)
            productMap[DIMENSION96] = " - ${if (it.notifyMeCount.toIntOrZero() > 0) it.notifyMeCount else " "} - ${if (it.pdpView.toIntOrZero() > 0) it.pdpView else 0} - " +
                    "${if (it.campaignSoldCount.toIntOrZero() > 0) it.campaignSoldCount else 0} $SOLD - ${if (it.customStock.toIntOrZero() > 0) it.customStock else 0} $LEFT - - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName} - ${getLabelCampaign(it)} - $NOTIFY_ME ${getNotificationStatus(componentsItems)}"

        }
        list.add(productMap)

        val eCommerce = mapOf(
                CURRENCY_CODE to IDR,
                KEY_IMPRESSIONS to list)
        val map = createGeneralEvent(eventName = EVENT_PRODUCT_VIEW,
                eventAction = PRODUCT_LIST_IMPRESSION)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
        productCardImpressionLabel = EMPTY_STRING
        productCardItemList = EMPTY_STRING
    }

    private fun getLabelCampaign(it: DataItem) =
            it.labelsGroupList?.filter { labelItem -> labelItem.position == KEY_CAMPAIGN_LABEL }?.let { list ->
                if (list.isNotEmpty())
                    list[0].title
                else
                    ""
            } ?: ""

    private fun getProductDime83(dataItem: DataItem): String {
        if (dataItem.freeOngkir?.isActive == true) {
            for (labelGroup in dataItem.labelsGroupList ?: arrayListOf()) {
                if (labelGroup.position == Constant.LABEL_FULFILLMENT) {
                    return BEBAS_ONGKIR_EXTRA
                }
            }
            return BEBAS_ONGKIR
        } else {
            return NONE_OTHER
        }
    }

    override fun trackEventProductATC(componentsItems: ComponentsItem, userID: String?) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        var productTypeName = ""
        componentsItems.data?.firstOrNull()?.let {
            productTypeName = getProductName(it.typeProductCard)
            productMap[KEY_NAME] = it.name.toString()
            productMap[KEY_ID] = it.productId.toString()
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_CATEGORY] = NONE_OTHER
            productMap[KEY_ATC_CATEGORY_ID] = NONE_OTHER
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[DIMENSION38] = ""
            productMap[DIMENSION83] = getProductDime83(it)
            productMap[DIMENSION84] = ""
            addSourceData(productMap)
            productMap[DIMENSION96] = " - ${if (it.notifyMeCount.toIntOrZero() > 0) it.notifyMeCount else " "} - ${if (it.pdpView.toIntOrZero() > 0) it.pdpView else 0} - " +
                    "${if (it.campaignSoldCount.toIntOrZero() > 0) it.campaignSoldCount else 0} $SOLD - ${if (it.customStock.toIntOrZero() > 0) it.customStock else 0} $LEFT - - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName} - ${getLabelCampaign(it)} - $NOTIFY_ME ${getNotificationStatus(componentsItems)}"
            productMap[KEY_QUANTITY] = it.quantity
            productMap[KEY_ATC_SHOP_ID] = ""
            productMap[KEY_SHOP_NAME] = it.shopName?:""
            productMap[KEY_SHOP_TYPE] = ""
        }
        list.add(productMap)
        val productsMap = mapOf(PRODUCTS to list)
        val eCommerce = mapOf(
            CURRENCY_CODE to IDR,
            KEY_ADD to productsMap)
        val map = createGeneralEvent(eventName = EVENT_PRODUCT_ATC,
            eventAction = PRODUCT_ATC_ACTION, eventLabel = productTypeName)
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = (userID?: "")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun viewProductsList(componentsItems: ComponentsItem, isLogin: Boolean) {
        if (!componentsItems.data.isNullOrEmpty()) {
            val productID = componentsItems.data!![0].productId
            if (!productID.isNullOrEmpty()) {
                if (viewedProductsSet.add(productID)) {
                    trackEventImpressionProductCard(componentsItems, isLogin)
                }
            }
        }
    }
    override fun viewAnchorTabs(componentsItems: ComponentsItem) {
        if (!componentsItems.data.isNullOrEmpty() && componentsItems.id.isNotEmpty()) {
//                send analytics if not already sent
            if (viewedAnchorTabsSet.add(componentsItems.id)) {
                trackAnchorTabImpression(componentsItems)
            }
        }
    }

    private fun getProductName(productType: String?): String {
        return when (productType) {
            PRODUCT_CARD_REVAMP_ITEM, MASTER_PRODUCT_CARD_ITEM_LIST -> PRODUCT_CARD_REVAMP
            PRODUCT_CARD_CAROUSEL_ITEM -> PRODUCT_CARD_CAROUSEL
            PRODUCT_SPRINT_SALE_ITEM -> PRODUCT_SPRINT_SALE
            PRODUCT_SPRINT_SALE_CAROUSEL_ITEM -> PRODUCT_SPRINT_SALE_CAROUSEL
            else -> EMPTY_STRING
        }
    }

    override fun clearProductViewIds(isRefresh: Boolean) {
        viewedProductsSet.clear()
        if(isRefresh) {
            viewedCalendarSet.clear()
            viewedAnchorTabsSet.clear()
        }
    }

    override fun trackProductCardClick(componentsItems: ComponentsItem, isLogin: Boolean) {
        if (!componentsItems.data.isNullOrEmpty()) {
            val login = if (isLogin) LOGIN else NON_LOGIN
            val list = ArrayList<Map<String, Any>>()
            val listMap = HashMap<String, Any>()
            componentsItems.data?.get(0)?.let {
                val productTypeName = getProductName(it.typeProductCard)
                productCardImpressionLabel = "$login - $productTypeName"
                productCardItemList = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${getParentPosition(componentsItems)+1} - $login - $productTypeName - - ${if (it.isTopads == true) TOPADS else NON_TOPADS} - ${if (it.creativeName.isNullOrEmpty()) "" else it.creativeName} - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName}"
                listMap[KEY_NAME] = it.name.toString()
                listMap[KEY_ID] = it.productId.toString()
                listMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
                listMap[KEY_BRAND] = NONE_OTHER
                listMap[KEY_ITEM_CATEGORY] = NONE_OTHER
                listMap[KEY_VARIANT] = NONE_OTHER
                listMap[KEY_POSITION] = componentsItems.position + 1
                listMap[LIST] = productCardItemList
                listMap[DIMENSION83] = getProductDime83(it)
                addSourceData(listMap)
                listMap[DIMENSION96] = " - ${if (it.notifyMeCount.toIntOrZero() > 0) it.notifyMeCount else " "} - ${if (it.pdpView.toIntOrZero() > 0) it.pdpView else 0} - " +
                        "${if (it.campaignSoldCount.toIntOrZero() > 0) it.campaignSoldCount else 0} $SOLD - ${if (it.customStock.toIntOrZero() > 0) it.customStock else 0} $LEFT - - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName} - ${getLabelCampaign(it)} - $NOTIFY_ME ${getNotificationStatus(componentsItems)}"

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
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[KEY_E_COMMERCE] = eCommerce
            getTracker().sendEnhanceEcommerceEvent(map)
            productCardImpressionLabel = EMPTY_STRING
            productCardItemList = EMPTY_STRING
        }
    }

    override fun trackNotifyClick(componentsItems: ComponentsItem, isLogin: Boolean, userID: String?) {
        val productItem = componentsItems.data?.firstOrNull()
        val map: MutableMap<String, Any> = mutableMapOf(
                KEY_EVENT to EVENT_CLICK_DISCOVERY,
                KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
                KEY_EVENT_ACTION to "${
                    productItem?.notifyMe?.let {
                        if (it) PRODUCT_NOTIFY_CANCEL_CLICK else PRODUCT_NOTIFY_CLICK
                    }
                }",
                KEY_EVENT_LABEL to "${productItem?.productId ?: ""} - ${if (isLogin) LOGIN else NON_LOGIN} - ${getProductComponentName(componentsItems.name)} - - ${if (productItem?.tabName.isNullOrEmpty()) "" else formatTabName(productItem!!.tabName)}",
                CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
                USER_ID to (userID ?: ""),
                BUSINESS_UNIT to HOME_BROWSE,
                PAGE_TYPE to pageType,
                PAGE_PATH to removedDashPageIdentifier
        )
        getTracker().sendGeneralEvent(map)
    }

    private fun formatTabName(tabName: String?): String {
        return tabName?.replace("\n", "") ?: ""
    }

    private fun getProductComponentName(componentName: String?): String {
        return when (componentName) {
            ComponentNames.ProductCardRevampItem.componentName -> ComponentNames.ProductCardRevamp.componentName
            ComponentNames.ProductCardCarouselItem.componentName -> ComponentNames.ProductCardCarousel.componentName
            ComponentNames.ProductCardSprintSaleItem.componentName -> ComponentNames.ProductCardSprintSale.componentName
            ComponentNames.ProductCardSprintSaleCarouselItem.componentName -> ComponentNames.ProductCardSprintSaleCarousel.componentName
            else -> ""
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
            return if (componentsItems.data?.firstOrNull()?.notifyMe != null) NOTIFY_ON else NOTIFY_OFF
        }
        return NOTIFY_ON
    }

    override fun trackEventImpressionCoupon(componentsItems: ArrayList<ComponentsItem>) {
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
                    map[KEY_NAME] = CLAIM_COUPON_ITEM_NAME
                }
                list.add(map)
            }

            val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                    EVENT_PROMO_VIEW to mapOf(
                            KEY_PROMOTIONS to list))
            val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = CLAIM_COUPON_IMPRESSION)
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    override fun trackClickClaimCoupon(couponName: String?, promoCode: String?) {
        val map = createGeneralEvent(eventAction = CLICK_BUTTON_CLAIM_COUPON_ACTION, eventLabel = "$couponName - $promoCode")
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackEventClickCoupon(coupon: DataItem?, position: Int, isDouble: Boolean) {
        val list = ArrayList<Map<String, Any>>()
        coupon?.let {
            list.add(mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_CREATIVE_URL to if (isDouble) it.smallImageUrlMobile
                            ?: NONE_OTHER else it.imageUrlMobile ?: NONE_OTHER,
                    KEY_POSITION to (position + 1).toString(),
                    KEY_PROMO_ID to it.promoId.toString(),
                    KEY_PROMO_CODE to it.slug.toString(),
                    KEY_NAME to CLAIM_COUPON_ITEM_NAME
            ))
        }
        val promotions: Map<String, ArrayList<Map<String, Any>>> = mapOf(
                KEY_PROMOTIONS to list)
        val map = createGeneralEvent(eventName = EVENT_CLICK_COUPON, eventAction = CLAIM_COUPON_CLICK,
                eventLabel = "${coupon?.name} - ${coupon?.couponCode}")
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = promotions
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackQuickCouponImpression(clickCouponData: ClickCouponData) {
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
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackQuickCouponClick(clickCouponData: ClickCouponData) {
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
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendGeneralEvent(map)
    }

    override fun trackQuickCouponApply(clickCouponData: ClickCouponData) {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CLICK_ON_MINI_COUPON_USE,
                eventLabel = "${clickCouponData.codePromo} - ${clickCouponData.realCode}")
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackQuickCouponPhoneVerified() {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CLICK_PHONE_VERIFIED,
                eventLabel = EMPTY_STRING)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackQuickCouponPhoneVerifyCancel() {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CLICK_PHONE_CLOSED,
                eventLabel = EMPTY_STRING)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackOpenScreen(screenName: String, additionalInfo: AdditionalInfo?, userLoggedIn: Boolean) {
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
        map[PAGE_SOURCE] = sourceIdentifier
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

    override fun trackTabsClick(id: String, parentPosition: Int, dataItem: DataItem, tabPosition1: Int) {
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
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }


    override fun trackCarouselBannerImpression(banners: List<DataItem>) {
        if (banners.isNotEmpty()) {
            val componentName = banners[0].parentComponentName ?: EMPTY_STRING
            val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
                    eventAction = IMPRESSION_DYNAMIC_BANNER)
            val list = ArrayList<Map<String, Any>>()
            for ((index, banner) in banners.withIndex()) {
                val hashMap = HashMap<String, Any>()
                hashMap[KEY_ID] = if (banner.id == null) DEFAULT_ID else if (banner.id!!.isNotEmpty()) banner.id!! else DEFAULT_ID
                hashMap[KEY_NAME] = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${banner.positionForParentItem + 1} - - - $componentName"
                hashMap[KEY_CREATIVE] = banner.creativeName ?: EMPTY_STRING
                hashMap[KEY_POSITION] = index + 1
                hashMap[KEY_PROMO_ID] = banner.trackingFields?.promoId ?: EMPTY_STRING
                hashMap[PROMO_CODE] = banner.trackingFields?.promoCode ?: EMPTY_STRING
                list.add(hashMap)
            }
            val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                    EVENT_PROMO_VIEW to mapOf(
                            KEY_PROMOTIONS to list))
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    override fun trackCarouselBannerClick(banner: DataItem, bannerPosition: Int) {
        val componentName = banner.parentComponentName ?: EMPTY_STRING
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_DYNAMIC_BANNER, eventLabel = "$componentName - ${banner.name} - ${banner.imageClickUrl}")
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_ID to if (banner.id == null) DEFAULT_ID else if (banner.id!!.isNotEmpty()) banner.id!! else DEFAULT_ID,
                KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${banner.positionForParentItem + 1} - - - $componentName",
                KEY_CREATIVE to (banner.creativeName ?: EMPTY_STRING),
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
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackBannerCarouselLihat() {
        val map = createGeneralEvent(eventAction = CLICK_VIEW_ALL_BANNER_CAROUSEL, eventLabel = EMPTY_STRING)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    private fun addSourceData(productMap: HashMap<String, Any>): HashMap<String, Any> {
        if (sourceIdentifier.isEmpty()) return productMap

        productMap[DIMENSION90] = sourceIdentifier
        return productMap
    }

    override fun trackEventImpressionTopAdsShop(componentDataItem: ComponentsItem, cpmData: CpmData) {
        val list = ArrayList<Map<String, Any>>()
        val listMap = HashMap<String, Any>()
        listMap[KEY_ID] = "${cpmData.id}_${cpmData.cpm.cpmShop.id}"
        listMap[KEY_NAME] = "/$pagePath - $pageType - 1 - - $CPM_SHOP_PAGE_COMPONENT"
        listMap[KEY_CREATIVE] = componentDataItem.creativeName ?: EMPTY_STRING
        listMap[KEY_POSITION] = 1
        list.add(listMap)
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = TOP_ADS_HEADLINE_IMPRESSION, eventLabel = EMPTY_STRING)
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackClickTopAdsShop(componentDataItem: ComponentsItem, cpmData: CpmData) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CPM_SHOP_CLICK, eventLabel = "${cpmData.id}-${cpmData.cpm.cpmShop.id}")
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_ID to "${cpmData.id}_${cpmData.cpm.cpmShop.id}",
                KEY_NAME to "/$pagePath - $pageType - 1 - - $CPM_SHOP_PAGE_COMPONENT",
                KEY_CREATIVE to (componentDataItem.creativeName ?: EMPTY_STRING),
                KEY_POSITION to 1
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackTopAdsProductImpression(componentDataItem: ComponentsItem, cpmData: CpmData, componentPosition: Int, productPosition: Int, userLoggedIn: Boolean) {
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
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackClickTopAdsProducts(componentDataItem: ComponentsItem, cpmData: CpmData, componentPosition: Int, productPosition: Int, userLoggedIn: Boolean) {
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
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
        productCardItemList = EMPTY_STRING
    }

    override fun trackHeaderSeeAllClick(isLogin: Boolean, componentsItems: ComponentsItem) {
        val loginValue = if (isLogin) LOGIN else NON_LOGIN
        val unifyTabValue = getTabValue(componentsItems)
        val creativeName = componentsItems.creativeName
        val headerValue = componentsItems.data?.firstOrNull()?.title ?: ""
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CLICK_VIEW_ALL_HEADER,
                eventLabel = "$loginValue - ${componentsItems.name} - $headerValue - $creativeName - $unifyTabValue")
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackCategoryTreeCloseClick(userLoggedIn: Boolean) {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CLOSE_CATEGORY_TREE,
                eventLabel = "")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = if (userLoggedIn) LOGIN else NON_LOGIN
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackCategoryTreeDropDownClick(userLoggedIn: Boolean) {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CATEGORY_TREE_ARROW,
                eventLabel = "")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = if (userLoggedIn) LOGIN else NON_LOGIN
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackCategoryOptionClick(userLoggedIn: Boolean, childCatID: String, applink: String?,
                                          catDepth: Int, childCatName: String) {
        val map = createGeneralEvent(eventName = EVENT_CLICK_DISCOVERY,
                eventAction = CATEGORY_TREE_OPTION_SELECTED,
                eventLabel = "$childCatID - $childCatName")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = if (userLoggedIn) LOGIN else NON_LOGIN
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackBottomNavBarClick(buttonName: String, userID: String?) {
        val map: MutableMap<String, Any> = mutableMapOf(
                KEY_EVENT to CLICK_NAV_DRAWER,
                KEY_EVENT_CATEGORY to eventDiscoveryCategory,
                KEY_EVENT_ACTION to "click $buttonName nav",
                KEY_EVENT_LABEL to "",
                CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
                USER_ID to (userID ?: ""),
                BUSINESS_UNIT to HOME_BROWSE,
                PAGE_TYPE to pageType,
                PAGE_PATH to removedDashPageIdentifier)
        getTracker().sendGeneralEvent(map)
    }

    override fun getHostSource(): String {
        return Constant.ChooseAddressGTMSSource.HOST_SOURCE
    }

    override fun getHostTrackingSource(): String {
        return Constant.ChooseAddressGTMSSource.HOST_TRACKING_SOURCE
    }

    override fun onTopAdsProductItemListener(position: Int, product: Product, cpmData: CpmData, components: ComponentsItem, userLoggedIn: Boolean) {
        val cpmProductPosition = position - 1

        if (cpmProductPosition >= 0) {
            if (position == 1) {
                trackEventImpressionTopAdsShop(components, cpmData)
                trackTopAdsProductImpression(components, cpmData, components.position, cpmProductPosition, userLoggedIn)
            } else {
                trackTopAdsProductImpression(components, cpmData, components.position, cpmProductPosition, userLoggedIn)
            }
        }
    }

    override fun onTopAdsHeadlineAdsClick(position: Int, applink: String?, cpmData: CpmData, components: ComponentsItem, userLoggedIn: Boolean) {
        val cpmProductPosition = position - 1
        if (position == 0) {
            trackClickTopAdsShop(components, cpmData)
        } else if (cpmProductPosition >= 0) {
            trackClickTopAdsProducts(components, cpmData, components.position, cpmProductPosition, userLoggedIn)
        }
    }

    override fun trackSingleMerchantVoucherImpression(components: ComponentsItem,shopId: String, userID: String?, positionInPage:Int,couponName: String?) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
            KEY_ID to "${components.id}_$shopId",
            KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${positionInPage + 1} - $MV_SINGLE_COMPONENT",
            KEY_CREATIVE to "${couponName ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING}",
            KEY_POSITION to 1
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = IMPRESSION_MV_SINGLE, shopId)
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackMerchantVoucherMultipleImpression(components: ComponentsItem, userID: String?, position:Int){
        val horizontalPosition:Int
        val componentName:String
        val action:String
        when(components.name) {
            ComponentNames.MerchantVoucherListItem.componentName -> {
                horizontalPosition = components.position + 1
                componentName = MV_LIST_COMPONENT
                action = IMPRESSION_MV_LIST
            }
            else -> {
                horizontalPosition = position + 1
                componentName = MV_MULTIPLE_COMPONENT
                action = IMPRESSION_MV_MULTIPLE
            }
        }
        val dataItem = components.data?.firstOrNull()
        val shopId  = dataItem?.shopInfo?.id?:""
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
            KEY_ID to "${components.parentComponentId}_$shopId",
            KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${components.parentComponentPosition + 1} - $componentName",
            KEY_CREATIVE to "${dataItem?.title ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING}",
            KEY_POSITION to horizontalPosition
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = action, shopId)
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackSingleMerchantVoucherClick(components: ComponentsItem, shopId: String, userID: String?, positionInPage:Int,couponName: String?) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
            KEY_ID to "${components.id}_$shopId",
            KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${positionInPage + 1} - $MV_SINGLE_COMPONENT",
            KEY_CREATIVE to "${couponName ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING}",
            KEY_POSITION to 1
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_MV_SINGLE, shopId)
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackMerchantVoucherMultipleShopClicks(components: ComponentsItem, userID: String?, position:Int){
        val shopInfo = components.data?.firstOrNull()?.shopInfo
        val shopId  = shopInfo?.id?:""
        val shopName = shopInfo?.name?:""
        val horizontalPosition:Int
        val componentName:String
        val action:String
        when(components.name) {
            ComponentNames.MerchantVoucherListItem.componentName -> {
                horizontalPosition = components.position + 1
                componentName = MV_LIST_COMPONENT
                action = CLICK_MV_LIST_SHOP
            }
            else -> {
                horizontalPosition = position + 1
                componentName = MV_MULTIPLE_COMPONENT
                action = CLICK_MV_MULTIPLE_SHOP
            }
        }
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
            KEY_ID to "${components.parentComponentId}_$shopId",
            KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${components.parentComponentPosition + 1} - $componentName",
            KEY_CREATIVE to "$SHOP_DETAIL - $shopName",
            KEY_POSITION to horizontalPosition
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = action, shopId)
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackMerchantVoucherMultipleVoucherDetailClicks(components: ComponentsItem, userID: String?, position:Int){
        val dataItem = components.data?.firstOrNull()
        val shopId  = dataItem?.shopInfo?.id ?: ""
        val list = ArrayList<Map<String, Any>>()
        val horizontalPosition:Int
        val componentName:String
        val action:String
        when(components.name) {
            ComponentNames.MerchantVoucherListItem.componentName -> {
                horizontalPosition = components.position+1
                componentName = MV_LIST_COMPONENT
                action = CLICK_MV_LIST_DETAIL
            }
            else -> {
                horizontalPosition = position + 1
                componentName = MV_MULTIPLE_COMPONENT
                action = CLICK_MV_MULTIPLE_DETAIL
            }
        }
        list.add(mapOf(
            KEY_ID to "${components.parentComponentId}_$shopId",
            KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${components.parentComponentPosition + 1} - $componentName",
            KEY_CREATIVE to "$VOUCHER_DETAIL - ${dataItem?.title?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING}",
            KEY_POSITION to horizontalPosition
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = action, shopId)
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackMerchantVoucherMultipleVoucherProductClicks(
        components: ComponentsItem,
        userID: String?,
        position: Int,
        productIndex: Int
    ) {
        val horizontalPosition:Int
        val componentName:String
        val action:String
        when(components.name) {
            ComponentNames.MerchantVoucherListItem.componentName -> {
                horizontalPosition = productIndex + 1
                componentName = MV_LIST_COMPONENT
                action = CLICK_MV_LIST_PRODUCT
            }
            else -> {
                horizontalPosition = productIndex + 1
                componentName = MV_MULTIPLE_COMPONENT
                action = CLICK_MV_MULTIPLE_PRODUCT
            }
        }

        if (!components.data.isNullOrEmpty()) {
            val shopId  = components.data?.firstOrNull()?.shopInfo?.id ?: ""
            var productId = ""
            var listItem = ""
            val login = if (userID.isNullOrEmpty()) NON_LOGIN else LOGIN
            val list = ArrayList<Map<String, Any>>()
            val listMap = HashMap<String, Any>()
            components.data?.get(0)?.let { dataItem ->
                if((dataItem.products?.size?:0) > productIndex && dataItem.products?.get(productIndex)!= null)
                dataItem.products[productIndex]?.let{
                    productId = it.id?:""
                    listItem = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${components.parentComponentPosition+1} - $login - $componentName - - - - "
                    listMap[KEY_NAME] = it.name?: ""
                    listMap[KEY_ID] = productId
//                    listMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
                    listMap[PRICE] = ""
                    listMap[KEY_BRAND] = NONE_OTHER
                    listMap[KEY_CATEGORY] = NONE_OTHER
                    listMap[KEY_VARIANT] = NONE_OTHER
                    listMap[KEY_POSITION] = horizontalPosition
                    listMap[DIMENSION83] = NONE_OTHER
                    addSourceData(listMap)
                }
            }
            list.add(listMap)
            val eCommerce = mapOf(
                CLICK to mapOf(
                    ACTION_FIELD to mapOf(
                        LIST to listItem
                    ),
                    PRODUCTS to list
                )
            )
            val map = createGeneralEvent(eventName = EVENT_PRODUCT_CLICK, eventAction = action, eventLabel = "$productId - $shopId")
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
            map[BUSINESS_UNIT] = HOME_BROWSE
            map[USER_ID] = userID ?: EMPTY_STRING
            map[KEY_E_COMMERCE] = eCommerce
            getTracker().sendEnhanceEcommerceEvent(map)
        }

    }

    override fun trackMerchantCouponDetailImpression(components: ComponentsItem, shopId: String, shopType:String, userID: String?, positionInPage:Int, couponName: String?){
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
            KEY_ID to "${components.id}_$shopId",
            KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${positionInPage + 1} - $MV_DETAIL_COMPONENT",
            KEY_CREATIVE to "${couponName ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING} - $shopType",
            KEY_POSITION to 1
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = IMPRESSION_MV_DETAIL, "$shopId - $shopType")
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackMerchantCouponVisitShopCTA(shopId: String, shopType:String){
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_ACTION to CLICK_MV_VISIT_SHOP,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_LABEL to "$shopId - $shopType",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_TYPE to pageType)
        getTracker().sendGeneralEvent(map)
    }

    override fun trackMerchantCouponCTASection(shopId: String, shopType:String, buttonDetail:String){
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_ACTION to CLICK_MV_CTA_SECTION,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_LABEL to "$shopId - $buttonDetail - $shopType",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_TYPE to pageType)
        getTracker().sendGeneralEvent(map)
    }
    override fun trackMerchantCouponCloseBottomSheet(shopId: String, shopType:String){
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_ACTION to CLICK_MV_CLOSE_BOTTOMSHEET,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_LABEL to "$shopId - $shopType",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_TYPE to pageType)
        getTracker().sendGeneralEvent(map)
    }

    override fun trackScrollDepth(screenScrollPercentage: Int, lastVisibleComponent: ComponentsItem?, isManualScroll : Boolean) {
        val map: MutableMap<String, Any> = mutableMapOf(
                KEY_EVENT to EVENT_CLICK_DISCOVERY,
                KEY_EVENT_ACTION to if (isManualScroll) SCROLL_DEPTH_RATE_MANUAL else SCROLL_DEPTH_RATE_AUTO,
                KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
                KEY_EVENT_LABEL to "$screenScrollPercentage%  - ${lastVisibleComponent?.name ?: ""} - ${lastVisibleComponent?.creativeName ?: ""}",
                BUSINESS_UNIT to HOME_BROWSE,
                CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
                PAGE_PATH to removedDashPageIdentifier,
                PAGE_TYPE to pageType)
        getTracker().sendGeneralEvent(map)
    }

    override fun trackUnifyShare(event : String, eventAction : String, userID: String?,
                                 eventLabel : String,) {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to event,
            KEY_EVENT_CATEGORY to eventDiscoveryCategory,
            KEY_EVENT_ACTION to eventAction,
            KEY_EVENT_LABEL to eventLabel,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            USER_ID to "${if (userID.isNullOrBlank()) 0 else userID}",
            BUSINESS_UNIT to SHARING_EXPERIENCE,
            PAGE_TYPE to pageType,
            PAGE_PATH to removedDashPageIdentifier
        )
        getTracker().sendGeneralEvent(map)
    }

    override fun trackScreenshotAccess(eventAction: String, eventLabel: String, userID: String?) {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_CATEGORY to eventDiscoveryCategory,
            KEY_EVENT_ACTION to eventAction,
            KEY_EVENT_LABEL to eventLabel,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            USER_ID to "${if (userID.isNullOrBlank()) 0 else userID}",
            BUSINESS_UNIT to SHARING_EXPERIENCE,
            PAGE_TYPE to pageType,
            PAGE_PATH to removedDashPageIdentifier
        )
        getTracker().sendGeneralEvent(map)
    }


    override fun viewCalendarsList(componentsItems: ComponentsItem, userID: String) {
        if (!componentsItems.data.isNullOrEmpty()) {
            val calendarID = "${componentsItems.position}_${componentsItems.parentComponentId}"
            if (viewedCalendarSet.add(calendarID)) {
                trackEventImpressionCalendar(componentsItems, userID)
            }
        }
    }
    
    private fun trackEventImpressionCalendar(componentsItems: ComponentsItem, userID: String) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
            KEY_NAME to "/discovery/${removedDashPageIdentifier} - ${pageType} - ${getParentPosition(componentsItems) + 1} - ${componentsItems.data?.firstOrNull()?.title} - ${componentsItems.name}",
            KEY_ID to "${componentsItems.position + 1}_${componentsItems.parentComponentId}",
            KEY_POSITION to "${componentsItems.position + 1}",
            KEY_CREATIVE to (componentsItems.data?.firstOrNull()?.creativeName ?: EMPTY_STRING)
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
            eventAction = CALENDAR_WIDGET_IMPRESSION, eventLabel = "${componentsItems.properties?.calendarLayout} layout - ${componentsItems.data?.firstOrNull()?.title}")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        map[USER_ID] = userID
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackEventClickCalendarWidget(componentsItems: ComponentsItem, userID: String) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
            KEY_NAME to "/discovery/${removedDashPageIdentifier} - ${pageType} - ${getParentPosition(componentsItems) + 1} - ${componentsItems.data?.firstOrNull()?.title} - ${componentsItems.name}",
            KEY_ID to "${componentsItems.position + 1}_${componentsItems.parentComponentId}",
            KEY_POSITION to "${componentsItems.position + 1}",
            KEY_CREATIVE to (componentsItems.data?.firstOrNull()?.creativeName ?: EMPTY_STRING)
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK,
            eventAction = CALENDAR_WIDGET_CLICK, eventLabel = "${componentsItems.properties?.calendarLayout} layout - p${componentsItems.parentComponentPosition + 1} - ${componentsItems.data?.firstOrNull()?.title}")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        map[USER_ID] = userID
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackEventClickCalendarCTA(componentsItems: ComponentsItem, userID: String) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
            KEY_NAME to "/discovery/${removedDashPageIdentifier} - ${pageType} - ${getParentPosition(componentsItems) + 1} - ${componentsItems.data?.firstOrNull()?.title} - ${componentsItems.name}",
            KEY_ID to "${componentsItems.position + 1}_${componentsItems.parentComponentId}",
            KEY_POSITION to "${componentsItems.position + 1}",
            KEY_CREATIVE to (componentsItems.data?.firstOrNull()?.creativeName ?: EMPTY_STRING)
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK,
            eventAction = CALENDAR_WIDGET_CTA_CLICK, eventLabel = "${componentsItems.properties?.calendarLayout} layout - p${componentsItems.parentComponentPosition + 1} - ${componentsItems.data?.firstOrNull()?.title}")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        map[USER_ID] = userID
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun clickQuestLihatButton(source: Int) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = QUEST_EVENT_CLICK
        map[Tracker.Constants.EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE - $CATEGORY_QUEST_WIDGET"
        map[Tracker.Constants.EVENT_ACTION] = CLICK_LIHAT_SEMUA
        map[Tracker.Constants.EVENT_LABEL] = DISCO_SLUG
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        getTracker().sendGeneralEvent(map)
    }

    override fun viewQuestWidget(source: Int, id: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = QUEST_EVENT_VIEW
        map[Tracker.Constants.EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE - $CATEGORY_QUEST_WIDGET"
        map[Tracker.Constants.EVENT_ACTION] = VIEW_QUEST_WIDGET
        map[Tracker.Constants.EVENT_LABEL] = "${DISCO_SLUG}_$id"
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        getTracker().sendGeneralEvent(map)
    }

    override fun clickQuestCard(source: Int, id: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = QUEST_EVENT_CLICK
        map[Tracker.Constants.EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE - $CATEGORY_QUEST_WIDGET"
        map[Tracker.Constants.EVENT_ACTION] = CLICK_QUEST_WIDGET
        map[Tracker.Constants.EVENT_LABEL] = "${DISCO_SLUG}_$id"
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        getTracker().sendGeneralEvent(map)
    }

    override fun slideQuestCard(source: Int, direction: String) {
        val map = mutableMapOf<String, Any>()
        map[Tracker.Constants.EVENT] = QUEST_EVENT_CLICK
        map[Tracker.Constants.EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE - $CATEGORY_QUEST_WIDGET"
        map[Tracker.Constants.EVENT_ACTION] = SLIDE_QUEST_WIDGET
        map[Tracker.Constants.EVENT_LABEL] = "${DISCO_SLUG}_$direction"
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        getTracker().sendGeneralEvent(map)

    }

    private fun trackAnchorTabImpression(components: ComponentsItem) {
        val map =
            createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = ANCHOR_TAB_IMPRESSION)
        val list = ArrayList<Map<String, Any>>()
        val functionalName =
            if (components.data?.firstOrNull()?.imageUrlMobile?.isNotEmpty() == true)
                ANCHOR_TYPE_ICON_TEXT else ANCHOR_TYPE_TEXT_ONLY
        list.add(mapOf(
            KEY_ID to components.parentComponentId,
            KEY_NAME to "/discovery/${removedDashPageIdentifier} - $pageType - ${getParentPosition(components)+1} - $functionalName - - $ANCHOR_TAB_COMPONENT",
            KEY_CREATIVE to (components.data?.firstOrNull()?.creativeName ?: EMPTY_STRING),
            KEY_POSITION to (components.position + 1)
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[USER_ID] = userSession.userId
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackAnchorTabClick(components: ComponentsItem) {
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = ANCHOR_TAB_CLICK)
        val list = ArrayList<Map<String, Any>>()
        val functionalName =
            if (components.data?.firstOrNull()?.imageUrlMobile?.isNotEmpty() == true)
                ANCHOR_TYPE_ICON_TEXT else ANCHOR_TYPE_TEXT_ONLY
        list.add(mapOf(
            KEY_ID to components.parentComponentId,
            KEY_NAME to "/discovery/${removedDashPageIdentifier} - $pageType - ${getParentPosition(components)+1} - $functionalName - - $ANCHOR_TAB_COMPONENT",
            KEY_CREATIVE to (components.data?.firstOrNull()?.creativeName ?: EMPTY_STRING),
            KEY_POSITION to (components.position + 1)
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[USER_ID] = userSession.userId
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackEventViewMyCouponList(componentsItems: ComponentsItem, userID: String) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_NAME to "/discovery/${removedDashPageIdentifier} - ${pageType} - ${getParentPosition(componentsItems) + 1} - - - ${componentsItems.parentComponentName}",
                KEY_ID to "${componentsItems.myCouponList?.firstOrNull()?.catalogID}",
                KEY_POSITION to "${componentsItems.position + 1}",
                KEY_CREATIVE to "${componentsItems.myCouponList?.firstOrNull()?.code ?: EMPTY_STRING} - "
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW,
                eventAction = ACTION_VIEW_MY_COUPON)
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        map[USER_ID] = userID
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackEventClickMyCouponList(componentsItems: ComponentsItem, userID: String) {
        val list = ArrayList<Map<String, Any>>()
        list.add(mapOf(
                KEY_NAME to "/discovery/${removedDashPageIdentifier} - ${pageType} - ${getParentPosition(componentsItems) + 1} - - - ${componentsItems.parentComponentName}",
                KEY_ID to "${componentsItems.myCouponList?.firstOrNull()?.catalogID}",
                KEY_POSITION to "${componentsItems.position + 1}",
                KEY_CREATIVE to "${componentsItems.myCouponList?.firstOrNull()?.code ?: EMPTY_STRING} - "
        ))
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK,
                eventAction = ACTION_CLICK_MY_COUPON, eventLabel = "${componentsItems.myCouponList?.firstOrNull()?.code} ")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        map[USER_ID] = userID
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackShopCardImpression(componentsItems: ComponentsItem) {
        val list = ArrayList<Map<String, Any>>()
        val shopMap = HashMap<String, Any>()
        componentsItems.data?.firstOrNull()?.let {
            shopMap[KEY_NAME] = "/discovery/${removedDashPageIdentifier} - ${pageType} - ${getParentPosition(componentsItems) + 1} - ${componentsItems.properties?.shopInfo ?: ""} - - $SHOP_CARD_BANNER"
            shopMap[KEY_ID] = "${componentsItems.parentComponentId}_${componentsItems.data?.firstOrNull()?.shopId}"
            shopMap[KEY_POSITION] = "${componentsItems.position + 1}"
            shopMap[KEY_CREATIVE] = (componentsItems.creativeName ?: EMPTY_STRING)
        }
        list.add(shopMap)
        val eventModel = EventModel(event = EVENT_PROMO_VIEW,action = ACTION_SHOP_CARD_VIEW, label = "", category = eventDiscoveryCategory)
        eventModel.key = "${componentsItems.creativeName}"
        val customDimensionMap = HashMap<String, Any>()
        customDimensionMap[PAGE_TYPE] = pageType
        customDimensionMap[PAGE_PATH] = removedDashPageIdentifier
        customDimensionMap[BUSINESS_UNIT] = HOME_BROWSE
        customDimensionMap[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        customDimensionMap[USER_ID] = userSession.userId

        trackingQueue.putEETracking(eventModel, hashMapOf (
                KEY_ECOMMERCE to hashMapOf(
                        EVENT_PROMO_VIEW to hashMapOf(
                                KEY_PROMOTIONS to  list)
                )
        ), customDimensionMap)
    }

    override fun trackEventClickShopCard(componentsItems: ComponentsItem) {
        val list = ArrayList<Map<String, Any>>()
        val shopMap = HashMap<String, Any>()
        componentsItems.data?.firstOrNull().let {
            shopMap[KEY_NAME] = "/discovery/${removedDashPageIdentifier} - ${pageType} - ${getParentPosition(componentsItems) + 1} - ${componentsItems.properties?.shopInfo ?: ""} - - $SHOP_CARD_BANNER"
            shopMap[KEY_ID] = "${componentsItems.parentComponentId}_${componentsItems.data?.firstOrNull()?.shopId}"
            shopMap[KEY_POSITION] = "${componentsItems.position + 1}"
            shopMap[KEY_CREATIVE] = (componentsItems.data?.firstOrNull()?.creativeName
                    ?: EMPTY_STRING)
            addSourceData(shopMap)
        }
        list.add(shopMap)

        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_CLICK to mapOf(
                        KEY_PROMOTIONS to list))
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK,
                eventAction = ACTION_SHOP_CARD_CLICK, eventLabel = "$SHOP_CARD_BANNER - - ${componentsItems.data?.firstOrNull()?.shopId}")
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = userSession.userId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackMixLeftBannerImpression(componentsItems: ComponentsItem){
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = IMPRESSION_DYNAMIC_BANNER
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        val list = ArrayList<Map<String, Any>>()
        val hashMap = HashMap<String, Any>()
        hashMap[KEY_ID] = componentsItems.parentComponentId
        hashMap[KEY_NAME] = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${getParentPosition(componentsItems)} - - - $MIX_LEFT_BANNER"
        hashMap[KEY_CREATIVE] = componentsItems.properties?.mixLeft?.creativeName ?: EMPTY_STRING
        hashMap[KEY_POSITION] = 1
        list.add(hashMap)
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[KEY_E_COMMERCE] = eCommerce
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[USER_ID] = userSession.userId
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun sendMixLeftBannerImpression(componentsItems: ComponentsItem) {
        val id = componentsItems.parentComponentId + MIX_LEFT_BANNER
        if (viewedProductsSet.add(id)) {
            trackMixLeftBannerImpression(componentsItems)
        }
    }

    override fun trackMixLeftBannerClick(componentsItems: ComponentsItem) {
        val applink = componentsItems.properties?.mixLeft?.applink ?: ""
        val creativeName = componentsItems.properties?.mixLeft?.creativeName ?: ""
        val map = createGeneralEvent(eventName = EVENT_PROMO_CLICK, eventAction = CLICK_DYNAMIC_BANNER, eventLabel = "$MIX_LEFT_BANNER - $creativeName - $applink")
        val list = ArrayList<Map<String, Any>>()
        list.add(
            mapOf(
                KEY_ID to componentsItems.parentComponentId,
                KEY_NAME to "/${removeDashPageIdentifier(pagePath)} - $pageType - ${getParentPosition(componentsItems)} - - - $MIX_LEFT_BANNER",
                KEY_CREATIVE to creativeName,
                KEY_POSITION to 1
            )
        )

        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list))
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[USER_ID] = userSession.userId
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }
}