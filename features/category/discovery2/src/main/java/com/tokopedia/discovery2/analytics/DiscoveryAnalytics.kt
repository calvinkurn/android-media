package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils.Companion.getParentPosition
import com.tokopedia.discovery2.analytics.merchantvoucher.MvcTrackingProperties
import com.tokopedia.discovery2.data.*
import com.tokopedia.discovery2.data.play.DiscoPlayWidgetType
import com.tokopedia.discovery2.data.producthighlight.DiscoveryOCSDataModel
import com.tokopedia.discovery2.data.quickcouponresponse.ClickCouponData
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_CURRENT_SITE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_ECOMMERCE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_TRACKER_ID
import com.tokopedia.quest_widget.tracker.Tracker
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import kotlin.collections.HashMap
import kotlin.collections.set

open class DiscoveryAnalytics(
    pageType: String = DISCOVERY_DEFAULT_PAGE_TYPE,
    pagePath: String = EMPTY_STRING,
    pageIdentifier: String = EMPTY_STRING,
    campaignCode: String = EMPTY_STRING,
    sourceIdentifier: String = EMPTY_STRING,
    private val userSession: UserSessionInterface,
    trackingQueue: TrackingQueue
) : BaseDiscoveryAnalytics(
    pageType,
    pagePath,
    pageIdentifier,
    campaignCode,
    sourceIdentifier,
    trackingQueue
) {

    private var eventDiscoveryCategory: String = VALUE_DISCOVERY_PAGE
    private var removedDashPageIdentifier: String = removeDashPageIdentifier(pageIdentifier)
    private var productCardImpressionLabel: String = EMPTY_STRING
    private var productCardItemList: String = EMPTY_STRING
    private var viewedProductsSet: MutableSet<String> = HashSet()
    private var viewedCalendarSet: MutableSet<String> = HashSet()
    private var viewedAnchorTabsSet: MutableSet<String> = HashSet()

    override fun trackBannerImpression(
        banners: List<DataItem>,
        componentPosition: Int?,
        userID: String?
    ) {
        if (banners.isNotEmpty()) {
            banners.forEachIndexed { index, banner ->
                val map = createGeneralEvent(
                    eventName = EVENT_PROMO_VIEW,
                    eventAction = IMPRESSION_DYNAMIC_BANNER,
                    shouldSendSourceAsDestination = true
                )
                map[TRACKER_ID] = "2704"
                map[PAGE_TYPE] = pageType
                map[PAGE_PATH] = removedDashPageIdentifier
                val list = ArrayList<Map<String, Any>>()
                val hashMap = HashMap<String, Any>()
                banner.let {
                    val bannerID = if (it.id.isNullOrEmpty()) 0 else it.id
                    hashMap[KEY_ID] = "${bannerID}_0"
                    hashMap[KEY_NAME] = it.gtmItemName.replace(
                        "#POSITION",
                        (banner.positionForParentItem + 1).toString()
                    )
                    hashMap[KEY_CREATIVE] = it.creativeName ?: EMPTY_STRING
                    hashMap[KEY_POSITION] = componentPosition?.plus(1) ?: (index + 1)
                }
                list.add(hashMap)
                val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                    EVENT_PROMO_VIEW to mapOf(
                        KEY_PROMOTIONS to list
                    )
                )
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
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_DYNAMIC_BANNER,
            eventLabel = "${componentName}${if (banner.action == ACTION_NOTIFIER) "-$NOTIFIER" else ""}${if (!banner.name.isNullOrEmpty()) " - ${banner.name}" else " - "}${if (!banner.applinks.isNullOrEmpty()) " - ${banner.applinks}" else " - "}",
            shouldSendSourceAsDestination = true
        )
        val list = ArrayList<Map<String, Any>>()
        banner.let {
            val bannerID = if (it.id.isNullOrEmpty()) 0 else it.id
            list.add(
                mapOf(
                    KEY_ID to "${bannerID}_0",
                    KEY_NAME to it.gtmItemName?.replace(
                        "#POSITION",
                        (banner.positionForParentItem + 1).toString()
                    ).toString(),
                    KEY_CREATIVE to it.creativeName.toString(),
                    KEY_POSITION to bannerPosition + 1
                )
            )
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[KEY_ATTRIBUTION] = banner.attribution ?: EMPTY_STRING
        map[KEY_AFFINITY_LABEL] = banner.name ?: EMPTY_STRING
        map[KEY_CATEGORY_ID] = banner.category ?: EMPTY_STRING
        map[KEY_SHOP_ID] = banner.shopId ?: EMPTY_STRING
        map[KEY_CAMPAIGN_CODE] =
            "${if (banner.campaignCode.isNullOrEmpty()) campaignCode else banner.campaignCode}"
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[TRACKER_ID] = "2705"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[USER_ID] = userID ?: EMPTY_STRING
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackQuickCouponImpression(clickCouponData: ClickCouponData) {
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = if (clickCouponData.couponApplied == true) IMPRESSION_MINI_COUPON_CANCEL else IMPRESSION_MINI_COUPON_USE,
            eventLabel = "${clickCouponData.codePromo} - ${clickCouponData.realCode}"
        )
        val list = ArrayList<Map<String, Any>>()
        list.add(
            mapOf(
                KEY_ID to clickCouponData.componentID,
                KEY_NAME to "/tokopoints/penukaran points - ${clickCouponData.componentPosition} - promo list - mini coupon",
                KEY_CREATIVE to (clickCouponData.componentName ?: EMPTY_STRING),
                KEY_POSITION to clickCouponData.componentPosition
            )
        )

        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackSupportingBrandImpression(components: List<ComponentsItem>) {
        if (components.isEmpty()) return

        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = EVENT_ACTION_SUPPORTING_BRAND_IMPRESSION,
            eventLabel = COMPONENT_SUPPORTING_BRAND,
            shouldSendSourceAsDestination = true
        )
        val list = ArrayList<Map<String, Any>>()
        for ((index, component) in components.withIndex()) {
            component.data?.firstOrNull()?.let { banner ->
                list.add(
                    mapOf(
                        KEY_ID to "${banner.offerId}_${banner.shopId}",
                        KEY_NAME to banner.gtmItemName.replace(
                            "#POSITION",
                            (getParentPosition(component) + 1).toString()
                        ),
                        KEY_CREATIVE to component.creativeName.orEmpty(),
                        KEY_POSITION to index + 1
                    )
                )
            }
        }

        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )

        map[TRACKER_ID] = TRACKER_ID_SUPPORTING_BRAND_IMPRESSION
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_EVENT_CATEGORY] = VALUE_DISCOVERY_PAGE
        map[USER_ID] = userSession.userId ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackSupportingBrandClick(component: ComponentsItem, actionType: String) {
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = EVENT_ACTION_SUPPORTING_BRAND_CLICK,
            eventLabel = "$COMPONENT_SUPPORTING_BRAND - $actionType",
            shouldSendSourceAsDestination = true
        )

        val list = ArrayList<Map<String, Any>>()
        component.data?.firstOrNull()?.let { banner ->
            list.add(
                mapOf(
                    KEY_ID to "${banner.offerId}_${banner.shopId}",
                    KEY_NAME to banner.gtmItemName.replace(
                        "#POSITION",
                        (getParentPosition(component) + 1).toString()
                    ),
                    KEY_CREATIVE to component.creativeName.orEmpty(),
                    KEY_POSITION to component.position + 1
                )
            )
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )

        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_EVENT_CATEGORY] = VALUE_DISCOVERY_PAGE
        map[TRACKER_ID] = TRACKER_ID_SUPPORTING_BRAND_CLICK
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[USER_ID] = userSession.userId ?: EMPTY_STRING
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackBrandRecommendationImpression(
        items: List<ComponentsItem>,
        componentPosition: Int,
        componentID: String
    ) {
        if (items.isNotEmpty()) {
            items.forEachIndexed { index, brandItem ->
                if (!brandItem.data.isNullOrEmpty()) {
                    brandItem.data?.firstOrNull()?.let {
                        val componentName = it.parentComponentName ?: EMPTY_STRING
                        val map = createGeneralEvent(
                            eventName = EVENT_PROMO_VIEW,
                            eventAction = IMPRESSION_DYNAMIC_BANNER,
                            eventLabel = "${componentName}${if (it.action == ACTION_NOTIFIER) "-$NOTIFIER" else ""}"
                        )
                        map[PAGE_TYPE] = pageType
                        map[PAGE_PATH] = removedDashPageIdentifier
                        val list = ArrayList<Map<String, Any>>()
                        val hashMap = HashMap<String, Any>()
                        it.let {
                            hashMap[KEY_ID] = it.id ?: 0
                            hashMap[KEY_NAME] = "/${
                                getDiscoPagePath(
                                    pagePath,
                                    removedDashPageIdentifier
                                )
                            } - $pageType - ${it.positionForParentItem + 1} - - ${componentName}${if (it.action == ACTION_NOTIFIER) "-$NOTIFIER" else ""}"
                            hashMap[KEY_CREATIVE] = it.creativeName ?: EMPTY_STRING
                            hashMap[KEY_POSITION] = index + 1
                            hashMap[KEY_PROMO_ID] = componentID
                        }
                        list.add(hashMap)
                        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> =
                            mapOf(
                                EVENT_PROMO_VIEW to mapOf(
                                    KEY_PROMOTIONS to list
                                )
                            )
                        map[KEY_E_COMMERCE] = eCommerce
                        trackingQueue.putEETracking(map as HashMap<String, Any>)
                    }
                }
            }
        }
    }

    override fun trackBrandRecommendationClick(
        banner: DataItem,
        bannerPosition: Int,
        compID: String
    ) {
        val componentName = banner.parentComponentName ?: EMPTY_STRING
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_DYNAMIC_BANNER,
            eventLabel = "${componentName}${if (!banner.name.isNullOrEmpty()) " - ${banner.name}" else " - "}${if (!banner.applinks.isNullOrEmpty()) " - ${banner.applinks}" else " - "}"
        )
        val list = ArrayList<Map<String, Any>>()
        banner.let {
            list.add(
                mapOf(
                    KEY_ID to it.id.toString(),
                    KEY_NAME to "/${
                        getDiscoPagePath(
                            pagePath,
                            removedDashPageIdentifier
                        )
                    } - $pageType - ${banner.positionForParentItem + 1} - - $componentName",
                    KEY_CREATIVE to "${it.creativeName ?: EMPTY_STRING}${
                        if (!banner.shopId.isNullOrEmpty()) {
                            " - ${banner.shopId}"
                        } else EMPTY_STRING
                    }",
                    KEY_POSITION to bannerPosition + 1,
                    KEY_PROMO_ID to compID
                )
            )
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[KEY_ATTRIBUTION] = banner.attribution ?: EMPTY_STRING
        map[KEY_AFFINITY_LABEL] = banner.name ?: EMPTY_STRING
        map[KEY_CATEGORY_ID] = banner.category ?: EMPTY_STRING
        map[KEY_SHOP_ID] = banner.shopId ?: EMPTY_STRING
        map[KEY_CAMPAIGN_CODE] =
            "${if (banner.campaignCode.isNullOrEmpty()) campaignCode else banner.campaignCode}"
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackPlayWidgetClick(
        dataItem: DataItem?,
        userID: String?,
        playModel: PlayWidgetChannelUiModel,
        widgetPosition: Int,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        val list = ArrayList<Map<String, Any>>()
        val creativeName = dataItem?.creativeName ?: EMPTY_STRING
        val componentName = dataItem?.let {
            getPlayComponentName(it.playWidgetType, it.mobileBanner)
        }
        val gtmItemName = dataItem?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to "0",
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/${removeDashPageIdentifier(pagePath)} - $pageType" +
                            " - ${widgetPosition + 1} - - - ${componentName.orEmpty()}"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (widgetPosition + 1).toString()
                        )
                    }),
                KEY_CREATIVE to creativeName,
                KEY_POSITION to channelPositionInList
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_DYNAMIC_BANNER,
            eventLabel = constructPlayEventLabel(
                playModel,
                channelPositionInList,
                widgetPosition,
                isAutoPlay
            ),
            shouldSendSourceAsDestination = true
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

    override fun trackPlayWidgetBannerClick(
        dataItem: DataItem?,
        userID: String?,
        widgetPosition: Int
    ) {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = CLICK_OTHER_CONTENT,
            eventLabel = constructSeeOthersPlayEventLabel(dataItem, widgetPosition),
            shouldSendSourceAsDestination = true
        )
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        getTracker().sendGeneralEvent(map as HashMap<String, Any>)
    }

    override fun trackPlayWidgetReminderClick(
        playModel: PlayWidgetChannelUiModel,
        userID: String?,
        widgetPosition: Int,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        isAutoPlay: Boolean
    ) {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = if (isRemindMe) CLICK_REMIND_ME else CLICK_CANCEL_REMIND_ME,
            eventLabel = constructPlayEventLabel(
                playModel,
                channelPositionInList,
                widgetPosition,
                isAutoPlay
            ),
            shouldSendSourceAsDestination = true
        )
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removeDashPageIdentifier(pageIdentifier)
        map[USER_ID] = userID ?: EMPTY_STRING
        getTracker().sendGeneralEvent(map as HashMap<String, Any>)
    }

    override fun trackPlayWidgetLihatSemuaClick(
        dataItem: DataItem?,
        userID: String?,
        widgetPosition: Int
    ) {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = CLICK_VIEW_ALL,
            eventLabel = constructSeeOthersPlayEventLabel(dataItem, widgetPosition),
            shouldSendSourceAsDestination = true
        )
        map[KEY_EVENT_CATEGORY] = "$VALUE_DISCOVERY_PAGE-$PLAY"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        getTracker().sendGeneralEvent(map as HashMap<String, Any>)
    }

    private fun constructSeeOthersPlayEventLabel(
        dataItem: DataItem?,
        widgetPosition: Int
    ): String {
        return dataItem?.let {
            "${getPlayComponentName(it.playWidgetType, it.mobileBanner)} - ${widgetPosition + 1}" +
                " - ${it.creativeName ?: EMPTY_STRING}"
        } ?: EMPTY_STRING
    }

    override fun trackPlayWidgetOverLayClick(
        componentsItem: ComponentsItem,
        userID: String?,
        widgetPosition: Int,
        channelPositionInList: Int,
        destinationURL: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val data = componentsItem.data?.firstOrNull()
        val creativeName = data?.creativeName ?: EMPTY_STRING
        val gtmItemName = data?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to componentsItem.id,
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/${removeDashPageIdentifier(pagePath)} - $pageType - ${widgetPosition + 1} - - - ${componentsItem.name}-$BANNER"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (widgetPosition + 1).toString()
                        )
                    }),
                KEY_CREATIVE to creativeName,
                KEY_POSITION to "$channelPositionInList - "
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_DYNAMIC_BANNER,
            "${
                componentsItem.name
                    ?: EMPTY_STRING
            } - $creativeName - $destinationURL"
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

    override fun trackPlayWidgetOverLayImpression(
        componentsItem: ComponentsItem,
        userID: String?,
        widgetPosition: Int,
        channelPositionInList: Int,
        destinationURL: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = componentsItem.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to componentsItem.id,
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/${removeDashPageIdentifier(pagePath)} - $pageType - ${widgetPosition + 1} - - - ${componentsItem.name}-$BANNER"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (widgetPosition + 1).toString()
                        )
                    }),
                KEY_CREATIVE to (componentsItem.data?.firstOrNull()?.creativeName ?: EMPTY_STRING),
                KEY_POSITION to "$channelPositionInList - "
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = IMPRESSION_DYNAMIC_BANNER,
            componentsItem.name
                ?: EMPTY_STRING
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

    override fun trackPlayWidgetImpression(
        dataItem: DataItem?,
        playModel: PlayWidgetChannelUiModel,
        userID: String?,
        widgetPosition: Int,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {
        val list = ArrayList<Map<String, Any>>()
        val creativeName = dataItem?.creativeName ?: EMPTY_STRING
        val componentName = dataItem?.let {
            getPlayComponentName(it.playWidgetType, it.mobileBanner)
        }
        val gtmItemName = dataItem?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to "0",
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/${removeDashPageIdentifier(pagePath)} - $pageType" +
                            " - ${widgetPosition + 1} - - - ${componentName.orEmpty()}"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (widgetPosition + 1).toString()
                        )
                    }),
                KEY_CREATIVE to creativeName,
                KEY_POSITION to channelPositionInList
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )

        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = IMPRESSION_DYNAMIC_BANNER,
            eventLabel = constructPlayEventLabel(
                playModel,
                channelPositionInList,
                widgetPosition,
                isAutoPlay
            ),
            shouldSendSourceAsDestination = true
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

    private fun constructPlayEventLabel(
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        widgetPosition: Int,
        isAutoPlay: Boolean
    ): String {
        val videoType = if (item.video.isLive) "live" else "vod"

        return "$sourceIdentifier - $videoType - ${item.partner.id} - ${item.channelId} - " +
            "$channelPositionInList - ${widgetPosition + 1} - is autoplay $isAutoPlay - ${item.recommendationType}"
    }

    private fun getPlayComponentName(widgetType: String?, mobileBanner: String?): String {
        return if (widgetType == DiscoPlayWidgetType.DISCO_PAGE_V2.name) {
            PLAY_CHANNEL_V2_TYPE
        } else if (!mobileBanner.isNullOrEmpty()) {
            PLAY_BANNER_TYPE
        } else {
            PLAY_CHANNEL_TYPE
        }
    }

    //    https://mynakama.tokopedia.com/datatracker/requestdetail/view/1559
    override fun trackTDNBannerImpression(
        componentsItem: ComponentsItem,
        userID: String?,
        positionInPage: Int,
        adID: String,
        shopId: String,
        itemPosition: Int
    ) {
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = componentsItem.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to "${adID}_$shopId",
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/${removeDashPageIdentifier(pagePath)} - $pageType - ${positionInPage + 1} - $TDN_BANNER_COMPONENT"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (positionInPage + 1).toString()
                        )
                    }),
                KEY_CREATIVE to (componentsItem.data?.firstOrNull()?.creativeName ?: EMPTY_STRING),
                KEY_POSITION to itemPosition.toString()
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = IMPRESSION_TDN_BANNER,
            EMPTY_STRING,
            shouldSendSourceAsDestination = true
        )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[TRACKER_ID] = "16357"
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    //    https://mynakama.tokopedia.com/datatracker/requestdetail/view/1559
    override fun trackTDNBannerClick(
        componentsItem: ComponentsItem,
        userID: String?,
        positionInPage: Int,
        adID: String,
        shopId: String,
        itemPosition: Int
    ) {
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = componentsItem.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to "${adID}_$shopId",
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/${removeDashPageIdentifier(pagePath)} - $pageType - ${positionInPage + 1} - $TDN_BANNER_COMPONENT"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (positionInPage + 1).toString()
                        )
                    }),
                KEY_CREATIVE to (componentsItem.data?.firstOrNull()?.creativeName ?: EMPTY_STRING),
                KEY_POSITION to itemPosition.toString()
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_TDN_BANNER,
            eventLabel = "$adID - $shopId",
            shouldSendSourceAsDestination = true
        )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[TRACKER_ID] = "16358"
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackClickVideo(videoUrl: String, videoName: String, videoPlayedTime: String) {
        val map = createGeneralEvent(
            eventAction = CLICK_VIDEO,
            eventLabel = "$videoName - $videoUrl - $videoPlayedTime"
        )
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
            KEY_EVENT_ACTION to CLICK_SEARCH_BAR_NAV,
            KEY_EVENT_CATEGORY to TOP_NAV,
            KEY_EVENT_LABEL to "",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_SOURCE to PAGE_SOURCE_TOP_NAV,
            PAGE_TYPE to pageType
        )
        getTracker().sendGeneralEvent(map)
    }

    override fun trackGlobalNavBarClick(buttonName: String, userID: String?) {
        if (buttonName == Constant.TOP_NAV_BUTTON.SEARCH_BAR) {
            trackSearchBarClick()
            return
        }
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

    private fun trackSearchBarClick() {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_ACTION to CLICK_SEARCH_BOX,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_LABEL to "",
            TRACKER_ID to "2712",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            USER_ID to (userSession.userId ?: ""),
            PAGE_DESTINATION to sourceIdentifier,
            PAGE_TYPE to pageType
        )
        getTracker().sendGeneralEvent(map)
    }

    override fun trackLihatSemuaClick(dataItem: DataItem?) {
        val map = createGeneralEvent(
            eventAction = CLICK_VIEW_ALL,
            eventLabel = dataItem?.title
                ?: EMPTY_STRING
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        getTracker().sendGeneralEvent(map)
    }

    override fun trackMerchantVoucherLihatSemuaClick(dataItem: DataItem?) {
        val map = createGeneralEvent(
            eventAction = CLICK_MV_MULTIPLE_LIHAT,
            eventLabel = dataItem?.title ?: EMPTY_STRING,
            shouldSendSourceAsDestination = true
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[TRACKER_ID] = "19679"
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        getTracker().sendGeneralEvent(map)
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

    override fun trackClickQuickFilter(
        filterName: String,
        componentName: String?,
        value: String,
        isFilterSelected: Boolean
    ) {
        val map = createGeneralEvent(
            eventAction = QUICK_FILTER_CLICK,
            eventLabel = "$componentName - $filterName"
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackClickDetailedFilter(componentName: String?) {
        val map = createGeneralEvent(
            eventAction = DETAILED_FILTER_CLICK,
            eventLabel = componentName
                ?: ""
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackClickApplyFilter(mapParameters: Map<String, String>) {
        var label = ""
        for (map in mapParameters) {
            label = "$label&${map.key}-${map.value}"
        }
        val map = createGeneralEvent(
            eventAction = APPLY_FILTER_CLICK,
            eventLabel = label.removePrefix("&")
        )
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

    private fun trackEventImpressionProductCard(
        componentsItems: ComponentsItem,
        isLogin: Boolean,
        isFulFillment: Boolean,
        warehouseId: Long
    ) {
        val login = if (isLogin) LOGIN else NON_LOGIN
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        componentsItems.data?.get(0)?.let {
            val productTypeName = getProductName(it.typeProductCard)
            productCardImpressionLabel = "$login - $productTypeName"
            productMap[KEY_NAME] = it.name.toString()
            productMap[KEY_ID] = it.productId.toString()
            productMap[PRICE] = convertRupiahToInt(it.price ?: "")
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[KEY_POSITION] = componentsItems.position + 1
            productMap[LIST] = it.gtmItemName.replace(
                "#POSITION",
                (getParentPosition(componentsItems) + 1).toString()
            )
            productMap[DIMENSION83] = getProductDime83(it)
            productMap[DIMENSION90] = sourceIdentifier
            productMap[DIMENSION96] = getValueForDimension96(it, componentsItems.parentComponentId)
            productMap[DIMENSION38] = ""
            productMap[DIMENSION84] = ""
            productMap[DIMENSION56] = warehouseId
            productMap[DIMENSION58] = isFulFillment.toString()
        }
        list.add(productMap)

        val eCommerce = mapOf(
            CURRENCY_CODE to IDR,
            KEY_IMPRESSIONS to list
        )
        val map = createGeneralEvent(
            eventName = EVENT_PRODUCT_VIEW,
            eventAction = PRODUCT_LIST_IMPRESSION,
            shouldSendSourceAsDestination = true
        )
        map[TRACKER_ID] = "2721"
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = (userSession.userId ?: "")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce

        trackingQueue.putEETracking(map as HashMap<String, Any>)
        productCardImpressionLabel = EMPTY_STRING
    }

    private fun getValueForDimension96(
        data: DataItem,
        parentComponentId: String?
    ): String {
        val notificationStatus = if (parentComponentId.isNullOrEmpty()) {
            NOTIFY_ON
        } else {
            getNotificationStatus(getComponent(parentComponentId, pageIdentifier), data)
        }

        return " - ${if (data.notifyMeCount.toIntOrZero() > 0) data.notifyMeCount else " "} " +
            "- ${data.pdpView.toIntOrZero()} - ${data.campaignSoldCount.toIntOrZero()} " +
            "$SOLD - ${data.customStock.toIntOrZero()} $LEFT - - ${data.tabName.orEmpty()} " +
            "- ${getLabelCampaign(data)} - $NOTIFY_ME $notificationStatus"
    }

    private fun getNotificationStatus(
        parentContainer: ComponentsItem?,
        dataItem: DataItem?
    ): String {
        parentContainer?.let {
            return if (dataItem?.notifyMe != null) NOTIFY_ON else NOTIFY_OFF
        }
        return NOTIFY_ON
    }

    private fun getLabelCampaign(it: DataItem) =
        it.labelsGroupList?.filter { labelItem -> labelItem.position == KEY_CAMPAIGN_LABEL }
            ?.let { list ->
                if (list.isNotEmpty()) {
                    list[0].title
                } else {
                    ""
                }
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

    override fun trackEventProductATCTokonow(componentsItems: ComponentsItem, cartId: String) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        val login = if (userSession.isLoggedIn) LOGIN else NON_LOGIN
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
            productMap[DIMENSION40] = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${
                getParentPosition(componentsItems) + 1
            } - $login - $productTypeName - - ${if (it.isTopads == true) TOPADS else NON_TOPADS} - ${if (it.creativeName.isNullOrEmpty()) "" else it.creativeName} - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName}"
            productMap[DIMENSION45] = cartId
            productMap[DIMENSION83] = getProductDime83(it)
            productMap[DIMENSION84] = ""
            productMap[DIMENSION90] = sourceIdentifier
            productMap[DIMENSION96] = getValueForDimension96(it, componentsItems.parentComponentId)
            productMap[KEY_QUANTITY] = it.quantity
            productMap[KEY_ATC_SHOP_ID] = it.shopId ?: ""
            productMap[KEY_SHOP_NAME] = it.shopName ?: ""
            productMap[KEY_SHOP_TYPE] = ""
        }
        list.add(productMap)
        val productsMap = mapOf(PRODUCTS to list)
        val eCommerce = mapOf(
            CURRENCY_CODE to IDR,
            KEY_ADD to productsMap
        )
        val map = createGeneralEvent(
            eventName = EVENT_PRODUCT_ATC,
            eventAction = PRODUCT_ATC_ACTION_TOKONOW,
            eventLabel = productTypeName,
            shouldSendSourceAsDestination = true
        )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = (userSession.userId ?: "")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[TRACKER_ID] = "21643"
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackEventProductATC(componentsItems: ComponentsItem, cartID: String) {
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
            productMap[KEY_QUANTITY] = it.quantity
            productMap[KEY_ATC_SHOP_ID] = it.shopId ?: ""
            productMap[KEY_SHOP_NAME] = it.shopName ?: ""
            productMap[KEY_SHOP_TYPE] = ""
            productMap[DIMENSION40] = it.gtmItemName.replace(
                "#POSITION", ((getParentPosition(componentsItems) + 1).toString()
                    ?: "")
            )
            productMap[DIMENSION45] = cartID
        }
        list.add(productMap)
        val productsMap = mapOf(PRODUCTS to list)
        val eCommerce = mapOf(
            CURRENCY_CODE to IDR,
            KEY_ADD to productsMap
        )
        val map = createGeneralEvent(
            eventName = EVENT_PRODUCT_ATC,
            eventAction = PRODUCT_ATC_ACTION,
            eventLabel = productTypeName
        )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = (userSession.userId ?: "")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_SOURCE] = sourceIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackEventProductBmGmATC(
        componentsItems: ComponentsItem,
        cartID: String
    ) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        componentsItems.data?.firstOrNull()?.let {
            productMap[KEY_ATC_CATEGORY_ID] = String.EMPTY
            productMap[DIMENSION40] = it.gtmItemName.replace(
                "#POSITION",
                (getParentPosition(componentsItems) + 1).toString()
            )
            productMap[DIMENSION45] = cartID
            productMap[DIMENSION90] = sourceIdentifier
            productMap[KEY_ITEM_BRAND] = NONE_OTHER
            productMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            productMap[KEY_ITEM_ID] = it.productId.toString()
            productMap[KEY_ITEM_NAME] = it.name.toString()
            productMap[KEY_ITEM_VARIANT] = NONE_OTHER
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price.orEmpty())
            productMap[KEY_QUANTITY] = it.quantity
            productMap[KEY_ATC_SHOP_ID] = it.shopId.orEmpty()
            productMap[KEY_SHOP_NAME] = it.shopName.orEmpty()
            productMap[KEY_SHOP_TYPE] = NONE_OTHER
        }
        list.add(productMap)
        val productsMap = mapOf(PRODUCTS to list)
        val eCommerce = mapOf(
            CURRENCY_CODE to IDR,
            KEY_ADD to productsMap
        )
        val map = createGeneralEvent(
            eventName = EVENT_PRODUCT_ATC,
            eventAction = PRODUCT_ATC_BUY_MORE_GET_MORE,
            eventLabel = "$COMPONENT_BMGM_NAME - ${componentsItems.data?.firstOrNull()?.quantity.orZero()} - false",
            shouldSendSourceAsDestination = true
        )
        map[TRACKER_ID] = TRACKER_ID_BMGM_ATC
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        map[USER_ID] = userSession.userId.orEmpty()
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackEventProductBmGmClickSeeMore(
        componentsItems: ComponentsItem
    ) {
        val header = componentsItems.getPropertyHeader()
        val map = createGeneralEvent(
            eventName = CLICK_HOMEPAGE_EVENT,
            eventAction = PRODUCT_CLICK_ON_BUY_MORE_GET_MORE,
            eventLabel = "$COMPONENT_BMGM_NAME - ${header?.shopId.orEmpty()} - ${componentsItems.creativeName} - ${header?.shopName.orEmpty()}",
            shouldSendSourceAsDestination = true
        )
        map[KEY_TRACKER_ID] = TRACKER_ID_BMGM_CLICK_SEE_MORE
        map[KEY_BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        getTracker().sendGeneralEvent(map)
    }

    override fun viewProductsList(
        componentsItems: ComponentsItem,
        isLogin: Boolean,
        isFulFillment: Boolean,
        warehouseId: Long
    ) {
        if (!componentsItems.data.isNullOrEmpty()) {
            val productID = componentsItems.data!![0].productId
            if (!productID.isNullOrEmpty()) {
                if (viewedProductsSet.add(productID)) {
                    trackEventImpressionProductCard(
                        componentsItems,
                        isLogin,
                        isFulFillment,
                        warehouseId
                    )
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
            PRODUCT_CARD_COLUMN_LIST -> PRODUCT_CARD_COLUMN_LIST
            ComponentNames.ProductCardSingleItem.componentName,
            ComponentNames.ProductCardSingleItemReimagine.componentName -> ComponentNames.ProductCardSingle.componentName

            else -> EMPTY_STRING
        }
    }

    override fun clearProductViewIds(isRefresh: Boolean) {
        viewedProductsSet.clear()
        if (isRefresh) {
            viewedCalendarSet.clear()
            viewedAnchorTabsSet.clear()
        }
    }

    override fun trackProductCardClick(
        componentsItems: ComponentsItem,
        isLogin: Boolean,
        isFulFillment: Boolean,
        warehouseId: Long
    ) {
        if (!componentsItems.data.isNullOrEmpty()) {
            val login = if (isLogin) LOGIN else NON_LOGIN
            val list = ArrayList<Map<String, Any>>()
            val listMap = HashMap<String, Any>()
            var productItemList = ""
            componentsItems.data?.get(0)?.let {
                val productTypeName = getProductName(it.typeProductCard)
                productItemList = it.gtmItemName.replace(
                    "#POSITION",
                    (getParentPosition(componentsItems) + 1).toString()
                )
                productCardImpressionLabel = "$login - $productTypeName"
                listMap[KEY_NAME] = it.name.toString()
                listMap[KEY_ID] = it.productId.toString()
                listMap[PRICE] = convertRupiahToInt(it.price ?: "")
                listMap[KEY_BRAND] = NONE_OTHER
                listMap[KEY_ITEM_CATEGORY] = NONE_OTHER
                listMap[KEY_VARIANT] = NONE_OTHER
                listMap[KEY_POSITION] = componentsItems.position + 1
                listMap[LIST] = productItemList
                listMap[DIMENSION83] = getProductDime83(it)
                listMap[DIMENSION90] = sourceIdentifier
                listMap[DIMENSION96] =
                    " - ${if (it.notifyMeCount.toIntOrZero() > 0) it.notifyMeCount else " "} - ${if (it.pdpView.toIntOrZero() > 0) it.pdpView else 0} - " +
                        "${if (it.campaignSoldCount.toIntOrZero() > 0) it.campaignSoldCount else 0} $SOLD - ${if (it.customStock.toIntOrZero() > 0) it.customStock else 0} $LEFT - - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName} - ${
                            getLabelCampaign(
                                it
                            )
                        } - $NOTIFY_ME ${getNotificationStatus(componentsItems)}"
                listMap[DIMENSION38] = ""
                listMap[DIMENSION84] = ""
                listMap[DIMENSION56] = warehouseId
                listMap[DIMENSION58] = isFulFillment.toString()
            }
            list.add(listMap)

            val eCommerce = mapOf(
                CLICK to mapOf(
                    ACTION_FIELD to mapOf(
                        LIST to productItemList
                    ),
                    PRODUCTS to list
                )
            )
            val map = createGeneralEvent(
                eventName = EVENT_PRODUCT_CLICK,
                eventAction = CLICK_PRODUCT_LIST,
                eventLabel = productCardImpressionLabel,
                shouldSendSourceAsDestination = true
            )
            map[TRACKER_ID] = "2722"
            map[KEY_CAMPAIGN_CODE] = campaignCode
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
            map[USER_ID] = (userSession.userId ?: "")
            map[BUSINESS_UNIT] = HOME_BROWSE
            map[KEY_E_COMMERCE] = eCommerce
            getTracker().sendEnhanceEcommerceEvent(map)
            productCardImpressionLabel = EMPTY_STRING
        }
    }

    override fun trackNotifyClick(
        componentsItems: ComponentsItem,
        isLogin: Boolean,
        userID: String?
    ) {
        val productItem = componentsItems.data?.firstOrNull()
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_ACTION to "${
                productItem?.notifyMe?.let {
                    if (it) PRODUCT_NOTIFY_CANCEL_CLICK else PRODUCT_NOTIFY_CLICK
                }
            }",
            KEY_EVENT_LABEL to "${productItem?.productId ?: ""} - ${if (isLogin) LOGIN else NON_LOGIN} - ${
                getProductComponentName(
                    componentsItems.name
                )
            } - - ${if (productItem?.tabName.isNullOrEmpty()) "" else formatTabName(productItem!!.tabName)}",
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            USER_ID to (userID ?: ""),
            BUSINESS_UNIT to HOME_BROWSE,
            PAGE_TYPE to pageType,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_SOURCE to sourceIdentifier
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
            ComponentNames.ShopOfferHeroBrandProductItem.componentName, ComponentNames.ShopOfferHeroBrandProductItemReimagine.componentName -> ComponentNames.ShopOfferHeroBrand.componentName
            else -> ""
        }
    }

    private fun getTabValue(componentsItems: ComponentsItem): String {
        val parentProductContainer = getComponent(componentsItems.parentComponentId, pageIdentifier)
        parentProductContainer?.let {
            val tabComponent =
                getComponent(parentProductContainer.parentComponentId, pageIdentifier)
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

    override fun trackQuickCouponClick(clickCouponData: ClickCouponData) {
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_MINI_COUPON_DETAIL,
            eventLabel = "${clickCouponData.codePromo ?: EMPTY_STRING} - ${clickCouponData.realCode ?: EMPTY_STRING}"
        )
        val list = ArrayList<Map<String, Any>>()
        list.add(
            mapOf(
                KEY_ID to clickCouponData.componentID,
                KEY_NAME to "/tokopoints/penukaran points - ${clickCouponData.componentPosition} - promo list - mini coupon",
                KEY_CREATIVE to (clickCouponData.componentName ?: EMPTY_STRING),
                KEY_POSITION to clickCouponData.componentPosition
            )
        )

        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendGeneralEvent(map)
    }

    override fun trackQuickCouponApply(clickCouponData: ClickCouponData) {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = CLICK_ON_MINI_COUPON_USE,
            eventLabel = "${clickCouponData.codePromo} - ${clickCouponData.realCode}"
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackQuickCouponPhoneVerified() {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = CLICK_PHONE_VERIFIED,
            eventLabel = EMPTY_STRING
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackQuickCouponPhoneVerifyCancel() {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = CLICK_PHONE_CLOSED,
            eventLabel = EMPTY_STRING
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackOpenScreen(
        screenName: String,
        additionalInfo: AdditionalInfo?,
        userLoggedIn: Boolean,
        paramsForOpenScreen: ParamsForOpenScreen
    ) {
        if (screenName.isNotEmpty()) {
            val map = getTrackingMapOpenScreen(
                screenName,
                additionalInfo,
                userLoggedIn,
                paramsForOpenScreen
            )
            TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, map)
        }
    }

    private fun getTrackingMapOpenScreen(
        pageIdentifier: String,
        additionalInfo: AdditionalInfo?,
        userLoggedIn: Boolean,
        paramsForOpenScreen: ParamsForOpenScreen
    ): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[KEY_EVENT] = OPEN_SCREEN
        map[EVENT_NAME] = OPEN_SCREEN
        map[SCREEN_NAME] = "${DISCOVERY_PATH}${removeDashPageIdentifier(pageIdentifier)}"
        map[IS_LOGGED_IN_STATUS] = userLoggedIn.toString()
        map[DISCOVERY_NAME] = pageIdentifier
        map[DISCOVERY_SLUG] = pageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[PAGE_DESTINATION] = sourceIdentifier
        map[CATEGORY] = EMPTY_STRING
        map[CATEGORY_ID] = EMPTY_STRING
        map[SUB_CATEGORY] = EMPTY_STRING
        map[SUB_CATEGORY_ID] = EMPTY_STRING
        map[CAMPAIGN_ID] = paramsForOpenScreen.campaignId
        map[VARIANT_ID] = paramsForOpenScreen.variantId
        map[SHOP_ID] = paramsForOpenScreen.shopID
        if (paramsForOpenScreen.affiliateChannelID.isNotEmpty()) {
            map[CHANNEL_ID] = paramsForOpenScreen.affiliateChannelID
        }
        map[TRACKER_ID] = TRACKER_ID_VALUE
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier

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

    private fun getDiscoPagePath(pagePath: String, removedDashPageIdentifier: String): String {
        var removeDashPagePath = removeDashPageIdentifier(pagePath)
        if (removeDashPagePath.isEmpty()) {
            removeDashPagePath =
                "$DISCOVERY/${if (removedDashPageIdentifier.isNotEmpty()) removedDashPageIdentifier else EMPTY_URL}"
        }
        return removeDashPagePath
    }

    private fun removeDashPageIdentifier(identifier: String): String {
        if (identifier.isNotEmpty()) {
            return identifier.replace("-", " ")
        }
        return EMPTY_STRING
    }

    override fun trackUnifyTabsClick(
        id: String,
        parentPosition: Int,
        dataItem: DataItem,
        tabPosition1: Int,
        eventAction: String
    ) {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = eventAction,
            eventLabel = dataItem.name
                ?: ""
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackTabsClick(
        id: String,
        parentPosition: Int,
        dataItem: DataItem,
        tabPosition1: Int,
        eventAction: String
    ) {
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = eventAction,
            eventLabel = dataItem.name
                ?: ""
        )
        val list = ArrayList<Map<String, Any>>()
        list.add(
            mapOf(
                KEY_ID to id,
                KEY_NAME to "/$pagePath - $pageType - ${parentPosition + 1} - - $MEGA_TAB_COMPONENT",
                KEY_CREATIVE to (dataItem.name ?: EMPTY_STRING),
                KEY_POSITION to tabPosition1 + 1
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackCarouselBannerImpression(banners: List<DataItem>, componentType: String) {
        if (banners.isNotEmpty()) {
            val map = createGeneralEvent(
                eventName = EVENT_PROMO_VIEW,
                eventAction = IMPRESSION_DYNAMIC_BANNER
            )
            val list = ArrayList<Map<String, Any>>()
            for ((index, banner) in banners.withIndex()) {
                val hashMap = HashMap<String, Any>()
                hashMap[KEY_ID] =
                    if (banner.id == null) DEFAULT_ID else if (banner.id!!.isNotEmpty()) banner.id!! else DEFAULT_ID
                hashMap[KEY_NAME] = banner.gtmItemName?.replace(
                    "#POSITION",
                    (banner.positionForParentItem + 1).toString()
                ).toString()
                hashMap[KEY_CREATIVE] = banner.creativeName ?: EMPTY_STRING
                hashMap[KEY_POSITION] = index + 1
                hashMap[KEY_PROMO_ID] = banner.trackingFields?.promoId ?: EMPTY_STRING
                hashMap[PROMO_CODE] = banner.trackingFields?.promoCode ?: EMPTY_STRING
                list.add(hashMap)
            }
            val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                EVENT_PROMO_VIEW to mapOf(
                    KEY_PROMOTIONS to list
                )
            )
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[KEY_E_COMMERCE] = eCommerce
            trackingQueue.putEETracking(map as HashMap<String, Any>)
        }
    }

    override fun trackCarouselBannerClick(
        banner: DataItem,
        bannerPosition: Int,
        componentType: String
    ) {
        val componentName = banner.parentComponentName ?: EMPTY_STRING
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_DYNAMIC_BANNER,
            eventLabel = "$componentName - ${banner.name} - ${banner.imageClickUrl}"
        )
        val list = ArrayList<Map<String, Any>>()
        list.add(
            mapOf(
                KEY_ID to if (banner.id == null) DEFAULT_ID else if (banner.id!!.isNotEmpty()) banner.id!! else DEFAULT_ID,
                KEY_NAME to banner.gtmItemName.replace(
                    "#POSITION",
                    (banner.positionForParentItem + 1).toString()
                ),
                KEY_CREATIVE to (banner.creativeName ?: EMPTY_STRING),
                KEY_POSITION to bannerPosition + 1,
                KEY_PROMO_ID to (banner.trackingFields?.promoId ?: EMPTY_STRING),
                PROMO_CODE to (banner.trackingFields?.promoCode ?: EMPTY_STRING)
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
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
        val map = createGeneralEvent(
            eventAction = CLICK_VIEW_ALL_BANNER_CAROUSEL,
            eventLabel = EMPTY_STRING
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    private fun addSourceData(productMap: HashMap<String, Any>): HashMap<String, Any> {
        if (sourceIdentifier.isEmpty()) return productMap

        productMap[DIMENSION90] = sourceIdentifier
        return productMap
    }

    override fun trackEventImpressionTopAdsShop(
        componentDataItem: ComponentsItem,
        cpmData: CpmData
    ) {
        val list = ArrayList<Map<String, Any>>()
        val listMap = HashMap<String, Any>()
        listMap[KEY_ID] = "${cpmData.id}_${cpmData.cpm.cpmShop.id}"
        listMap[KEY_NAME] = "/$pagePath - $pageType - 1 - - $CPM_SHOP_PAGE_COMPONENT"
        listMap[KEY_CREATIVE] = componentDataItem.creativeName ?: EMPTY_STRING
        listMap[KEY_POSITION] = 1
        list.add(listMap)
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = TOP_ADS_HEADLINE_IMPRESSION,
            eventLabel = EMPTY_STRING,
            isPageSourceRequired = false
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackClickTopAdsShop(componentDataItem: ComponentsItem, cpmData: CpmData) {
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CPM_SHOP_CLICK,
            eventLabel = "${cpmData.id}-${cpmData.cpm.cpmShop.id}",
            isPageSourceRequired = false
        )
        val list = ArrayList<Map<String, Any>>()
        list.add(
            mapOf(
                KEY_ID to "${cpmData.id}_${cpmData.cpm.cpmShop.id}",
                KEY_NAME to "/$pagePath - $pageType - 1 - - $CPM_SHOP_PAGE_COMPONENT",
                KEY_CREATIVE to (componentDataItem.creativeName ?: EMPTY_STRING),
                KEY_POSITION to 1
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackTopAdsProductImpression(
        componentDataItem: ComponentsItem,
        cpmData: CpmData,
        componentPosition: Int,
        productPosition: Int,
        userLoggedIn: Boolean
    ) {
        val login = if (userLoggedIn) LOGIN else NON_LOGIN
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        val cpmProductList = cpmData.cpm.cpmShop.products

        if (cpmProductList.isNotEmpty() && cpmProductList.size > productPosition) {
            val productData = cpmData.cpm.cpmShop.products[productPosition]
            productMap[KEY_NAME] = productData.name
            productMap[KEY_ID] = productData.id
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(
                productData.priceFormat
                    ?: ""
            )
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[KEY_POSITION] = productPosition + 1
            productMap[dimension140] = sourceIdentifier
            productMap[LIST] =
                "/$pagePath - $pageType - ${componentPosition + 1} - $login - ${componentDataItem.name} - ${cpmData.id}"
            list.add(productMap)
        }

        val eCommerce = mapOf(
            CURRENCY_CODE to IDR,
            KEY_IMPRESSIONS to list
        )
        val map = createGeneralEvent(
            eventName = EVENT_PRODUCT_VIEW,
            eventAction = CPM_PRODUCT_LIST_IMPRESSION,
            eventLabel = EMPTY_STRING,
            isPageSourceRequired = false
        )
        map[KEY_CAMPAIGN_CODE] = campaignCode
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackClickTopAdsProducts(
        componentDataItem: ComponentsItem,
        cpmData: CpmData,
        componentPosition: Int,
        productPosition: Int,
        userLoggedIn: Boolean
    ) {
        val login = if (userLoggedIn) LOGIN else NON_LOGIN
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        var productID = ""
        val cpmProductList = cpmData.cpm.cpmShop.products
        productCardItemList =
            "/${removeDashPageIdentifier(pagePath)} - $pageType - ${componentPosition + 1} - $login - ${componentDataItem.name} - ${cpmData.id}"

        if (cpmProductList.isNotEmpty() && cpmProductList.size > productPosition) {
            val productData = cpmData.cpm.cpmShop.products[productPosition]
            productID = productData.id
            productMap[KEY_NAME] = productData.name
            productMap[KEY_ID] = productData.id
            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(
                productData.priceFormat
                    ?: ""
            )
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[KEY_POSITION] = productPosition + 1
            productMap[dimension140] = sourceIdentifier
            productMap[LIST] =
                "/$pagePath - $pageType - ${componentPosition + 1} - $login - ${componentDataItem.name} - ${cpmData.id}"
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
        val map = createGeneralEvent(
            eventName = EVENT_PRODUCT_CLICK,
            eventAction = CLICK_PRODUCT_LIST,
            eventLabel = "${cpmData.id}-${cpmData.cpm.cpmShop.id}-$productID",
            isPageSourceRequired = false
        )
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
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = CLICK_VIEW_ALL_HEADER,
            eventLabel = "$loginValue - ${componentsItems.name} - $headerValue - $creativeName - $unifyTabValue"
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackCategoryTreeCloseClick(userLoggedIn: Boolean) {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = CLOSE_CATEGORY_TREE,
            eventLabel = ""
        )
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = if (userLoggedIn) LOGIN else NON_LOGIN
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackCategoryTreeDropDownClick(userLoggedIn: Boolean) {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = CATEGORY_TREE_ARROW,
            eventLabel = ""
        )
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = if (userLoggedIn) LOGIN else NON_LOGIN
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        getTracker().sendGeneralEvent(map)
    }

    override fun trackCategoryOptionClick(
        userLoggedIn: Boolean,
        childCatID: String,
        applink: String?,
        catDepth: Int,
        childCatName: String
    ) {
        val map = createGeneralEvent(
            eventName = EVENT_CLICK_DISCOVERY,
            eventAction = CATEGORY_TREE_OPTION_SELECTED,
            eventLabel = "$childCatID - $childCatName"
        )
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
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_SOURCE to sourceIdentifier
        )
        getTracker().sendGeneralEvent(map)
    }

    override fun getHostSource(): String {
        return Constant.ChooseAddressGTMSSource.HOST_SOURCE
    }

    override fun getHostTrackingSource(): String {
        return Constant.ChooseAddressGTMSSource.HOST_TRACKING_SOURCE
    }

    override fun onTopAdsProductItemListener(
        position: Int,
        product: Product,
        cpmData: CpmData,
        components: ComponentsItem,
        userLoggedIn: Boolean
    ) {
        val cpmProductPosition = position - 1

        if (cpmProductPosition >= 0) {
            if (position == 1) {
                trackEventImpressionTopAdsShop(components, cpmData)
                trackTopAdsProductImpression(
                    components,
                    cpmData,
                    components.position,
                    cpmProductPosition,
                    userLoggedIn
                )
            } else {
                trackTopAdsProductImpression(
                    components,
                    cpmData,
                    components.position,
                    cpmProductPosition,
                    userLoggedIn
                )
            }
        }
    }

    override fun onTopAdsHeadlineAdsClick(
        position: Int,
        applink: String?,
        cpmData: CpmData,
        components: ComponentsItem,
        userLoggedIn: Boolean
    ) {
        val cpmProductPosition = position - 1
        if (position == 0) {
            trackClickTopAdsShop(components, cpmData)
        } else if (cpmProductPosition >= 0) {
            trackClickTopAdsProducts(
                components,
                cpmData,
                components.position,
                cpmProductPosition,
                userLoggedIn
            )
        }
    }

    override fun trackSingleMerchantVoucherImpression(
        components: ComponentsItem,
        shopId: String,
        userID: String?,
        positionInPage: Int,
        couponName: String?
    ) {
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = components.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to "${components.id}_$shopId",
                KEY_NAME to
                    (
                        if (gtmItemName.isNullOrEmpty()) {
                            "/${removeDashPageIdentifier(pagePath)} - $pageType - ${positionInPage + 1} - $MV_SINGLE_COMPONENT"
                        } else {
                            gtmItemName.replace(
                                "#POSITION",
                                (positionInPage + 1).toString()
                            )
                        }
                        ),
                KEY_CREATIVE to "${couponName ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING}",
                KEY_POSITION to 1
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = IMPRESSION_MV_SINGLE,
            shopId
        )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackMerchantVoucherMultipleImpression(
        components: ComponentsItem,
        userID: String?,
        position: Int
    ) {
        val horizontalPosition: Int
        val componentName: String
        val action: String
        when (components.name) {
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
        val shopId = dataItem?.shopInfo?.id ?: ""
        val list = ArrayList<Map<String, Any>>()
        list.add(
            mapOf(
                KEY_ID to "${components.parentComponentId}_$shopId",
                KEY_NAME to (
                    if (dataItem?.gtmItemName.isNullOrEmpty()) {
                        "/${removeDashPageIdentifier(pagePath)} - $pageType - ${components.parentComponentPosition + 1} - $componentName"
                    } else {
                        dataItem?.gtmItemName?.replace(
                            "#POSITION",
                            (components.parentComponentPosition + 1).toString()
                        ) ?: ""
                    }
                    ),
                KEY_CREATIVE to "${dataItem?.shopInfo?.name ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING}",
                KEY_POSITION to horizontalPosition
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = action, shopId)
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1984
    // Tracker ID: 19679
    override fun trackMerchantVoucherViewClickAll(
        components: ComponentsItem,
        userID: String?,
        position: Int,
        ctaText: String?
    ) {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to CLICK_HOMEPAGE_EVENT,
            KEY_EVENT_ACTION to CLICK_MV_VIEW_ALL,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_LABEL to "${components.name} - $ctaText",
            TRACKER_ID to "19679",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_TYPE to pageType,
            PAGE_DESTINATION to sourceIdentifier
        )
        getTracker().sendGeneralEvent(map)
    }

    //    datatracker/requestdetail/view/1984
    override fun trackSingleMerchantVoucherClick(
        components: ComponentsItem,
        shopId: String,
        userID: String?,
        positionInPage: Int,
        couponName: String?
    ) {
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = components.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to "${components.id}_$shopId",
                KEY_NAME to
                    (
                        if (gtmItemName.isNullOrEmpty()) {
                            "/${removeDashPageIdentifier(pagePath)} - $pageType - ${positionInPage + 1} - $MV_SINGLE_COMPONENT"
                        } else {
                            gtmItemName.replace(
                                "#POSITION",
                                (positionInPage + 1).toString()
                            )
                        }
                        ),
                KEY_CREATIVE to "${couponName ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING}",
                KEY_POSITION to 1
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_MV_SINGLE,
            shopId,
            shouldSendSourceAsDestination = true
        )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[TRACKER_ID] = "19671"
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    //    datatracker/requestdetail/view/1984
    override fun trackMerchantVoucherMultipleShopClicks(
        components: ComponentsItem,
        userID: String?,
        position: Int
    ) {
        val shopInfo = components.data?.firstOrNull()?.shopInfo
        val shopId = shopInfo?.id ?: ""
        val shopName = shopInfo?.name ?: ""
        val horizontalPosition: Int
        val componentName: String
        val action: String
        val trackerId: String
        when (components.name) {
            ComponentNames.MerchantVoucherListItem.componentName -> {
                horizontalPosition = components.position + 1
                componentName = MV_LIST_COMPONENT
                action = CLICK_MV_LIST_SHOP
                trackerId = "19681"
            }

            else -> {
                horizontalPosition = position + 1
                componentName = MV_MULTIPLE_COMPONENT
                action = CLICK_MV_MULTIPLE_SHOP
                trackerId = "19676"
            }
        }
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = components.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to "${components.parentComponentId}_$shopId",
                KEY_NAME to
                    (
                        if (gtmItemName.isNullOrEmpty()) {
                            "/${removeDashPageIdentifier(pagePath)} - $pageType - ${components.parentComponentPosition + 1} - $componentName"
                        } else {
                            gtmItemName.replace(
                                "#POSITION",
                                (components.parentComponentPosition + 1).toString()
                            )
                        }
                        ),
                KEY_CREATIVE to "$SHOP_DETAIL - $shopName",
                KEY_POSITION to horizontalPosition
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = action,
            shopId,
            shouldSendSourceAsDestination = true
        )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[TRACKER_ID] = trackerId
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackMerchantVoucherMultipleVoucherDetailClicks(
        components: ComponentsItem,
        userID: String?,
        position: Int
    ) {
        val dataItem = components.data?.firstOrNull()
        val shopId = dataItem?.shopInfo?.id ?: ""
        val list = ArrayList<Map<String, Any>>()
        val horizontalPosition: Int
        val componentName: String
        val action: String
        val tracker: String
        val gtmItemName = components.data?.firstOrNull()?.gtmItemName
        when (components.name) {
            ComponentNames.MerchantVoucherListItem.componentName -> {
                horizontalPosition = components.position + 1
                componentName = MV_LIST_COMPONENT
                action = CLICK_MV_LIST_DETAIL
                tracker = "19682"
                list.add(
                    mapOf(
                        KEY_ID to "${components.parentComponentId}_$shopId",
                        KEY_NAME to
                            (
                                if (gtmItemName.isNullOrEmpty()) {
                                    "/${removeDashPageIdentifier(pagePath)} - $pageType - ${components.parentComponentPosition + 1} - $componentName"
                                } else {
                                    gtmItemName.replace(
                                        "#POSITION",
                                        (components.parentComponentPosition + 1).toString()
                                    )
                                }
                                ),
                        KEY_CREATIVE to "$VOUCHER_DETAIL - ${dataItem?.title ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING}",
                        KEY_POSITION to horizontalPosition
                    )
                )
            }

            else -> {
                horizontalPosition = position + 1
                componentName = MV_MULTIPLE_COMPONENT
                action = CLICK_MV_MULTIPLE_DETAIL
                tracker = "19677"
                list.add(
                    mapOf(
                        KEY_ID to "${components.parentComponentId}_$shopId",
                        KEY_NAME to
                            (
                                if (gtmItemName.isNullOrEmpty()) {
                                    "/${removeDashPageIdentifier(pagePath)} - $pageType - ${components.parentComponentPosition + 1} - $componentName"
                                } else {
                                    gtmItemName.replace(
                                        "#POSITION",
                                        (components.parentComponentPosition + 1).toString()
                                    )
                                }),
                        KEY_CREATIVE to "${dataItem?.title ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING}",
                        KEY_POSITION to horizontalPosition
                    )
                )
            }
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = action,
            shopId,
            shouldSendSourceAsDestination = true
        )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[TRACKER_ID] = tracker
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
        val horizontalPosition: Int
        val componentName: String
        val action: String
        val tracker: String
        when (components.name) {
            ComponentNames.MerchantVoucherListItem.componentName -> {
                horizontalPosition = productIndex + 1
                componentName = MV_LIST_COMPONENT
                action = CLICK_MV_LIST_PRODUCT
                tracker = "19683"
            }

            else -> {
                horizontalPosition = productIndex + 1
                componentName = MV_MULTIPLE_COMPONENT
                action = CLICK_MV_MULTIPLE_PRODUCT
                tracker = "19678"
            }
        }

        if (!components.data.isNullOrEmpty()) {
            val shopId = components.data?.firstOrNull()?.shopInfo?.id ?: ""
            var productId = ""
            var listItem = ""
            val login = if (userID.isNullOrEmpty()) NON_LOGIN else LOGIN
            val list = ArrayList<Map<String, Any>>()
            val listMap = HashMap<String, Any>()
            components.data?.get(0)?.let { dataItem ->
                if ((dataItem.products?.size ?: 0) > productIndex && dataItem.products?.get(
                        productIndex
                    ) != null
                ) {
                    dataItem.products[productIndex]?.let {
                        productId = it.id ?: ""
                        listItem =
                            "/${removeDashPageIdentifier(pagePath)} - $pageType - ${components.parentComponentPosition + 1} - $login - $componentName - - - - "
                        listMap[KEY_NAME] = it.name ?: ""
                        listMap[KEY_ID] = productId
//                    listMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(it.price ?: "")
                        listMap[PRICE] = ""
                        listMap[DIMENSION90] = sourceIdentifier
                        listMap[KEY_BRAND] = NONE_OTHER
                        listMap[KEY_CATEGORY] = NONE_OTHER
                        listMap[KEY_VARIANT] = NONE_OTHER
                        listMap[KEY_POSITION] = horizontalPosition
                        listMap[DIMENSION83] = NONE_OTHER
                        addSourceData(listMap)
                    }
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
            val map = createGeneralEvent(
                eventName = EVENT_PRODUCT_CLICK,
                eventAction = action,
                eventLabel = "$productId - $shopId",
                shouldSendSourceAsDestination = true
            )
            map[TRACKER_ID] = tracker
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
            map[BUSINESS_UNIT] = HOME_BROWSE
            map[USER_ID] = userID ?: EMPTY_STRING
            map[KEY_E_COMMERCE] = eCommerce
            getTracker().sendEnhanceEcommerceEvent(map)
        }
    }

    override fun trackMerchantCouponDetailImpression(
        components: ComponentsItem,
        shopId: String,
        shopType: String,
        userID: String?,
        positionInPage: Int,
        couponName: String?
    ) {
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = components.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to "${components.id}_$shopId",
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/${removeDashPageIdentifier(pagePath)} - $pageType - ${positionInPage + 1} - $MV_DETAIL_COMPONENT"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (positionInPage + 1).toString()
                        )
                    }),
                KEY_CREATIVE to "${couponName ?: EMPTY_STRING} - ${components.creativeName ?: EMPTY_STRING} - $shopType",
                KEY_POSITION to 1
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = IMPRESSION_MV_DETAIL,
            "$shopId - $shopType"
        )
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[USER_ID] = userID ?: EMPTY_STRING
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    // datatracker/requestdetail/view/1984
    override fun trackMerchantCouponVisitShopCTA(shopId: String, shopType: String) {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_ACTION to CLICK_MV_VISIT_SHOP,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_LABEL to "$shopId - $shopType",
            TRACKER_ID to "19673",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_TYPE to pageType,
            PAGE_DESTINATION to sourceIdentifier
        )
        getTracker().sendGeneralEvent(map)
    }

    override fun trackMerchantCouponCTASection(
        shopId: String,
        shopType: String,
        buttonDetail: String
    ) {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_ACTION to CLICK_MV_CTA_SECTION,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_LABEL to "$shopId - $buttonDetail - $shopType",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_TYPE to pageType,
            TRACKER_ID to "19674",
            PAGE_DESTINATION to sourceIdentifier
        )
        getTracker().sendGeneralEvent(map)
    }

    override fun trackMerchantCouponCloseBottomSheet(shopId: String, shopType: String) {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_ACTION to CLICK_MV_CLOSE_BOTTOMSHEET,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_LABEL to "$shopId - $shopType",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_TYPE to pageType,
            PAGE_SOURCE to sourceIdentifier
        )
        getTracker().sendGeneralEvent(map)
    }

    override fun trackScrollDepth(
        screenScrollPercentage: Int,
        lastVisibleComponent: ComponentsItem?,
        isManualScroll: Boolean
    ) {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to EVENT_CLICK_DISCOVERY,
            KEY_EVENT_ACTION to if (isManualScroll) SCROLL_DEPTH_RATE_MANUAL else SCROLL_DEPTH_RATE_AUTO,
            KEY_EVENT_CATEGORY to VALUE_DISCOVERY_PAGE,
            KEY_EVENT_LABEL to "$screenScrollPercentage%  - ${lastVisibleComponent?.name ?: ""} - ${lastVisibleComponent?.creativeName ?: ""}",
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_TYPE to pageType,
            PAGE_SOURCE to sourceIdentifier
        )
        getTracker().sendGeneralEvent(map)
    }

    override fun trackUnifyShare(
        event: String,
        eventAction: String,
        userID: String?,
        eventLabel: String
    ) {
        val map: MutableMap<String, Any> = mutableMapOf(
            KEY_EVENT to event,
            KEY_EVENT_CATEGORY to eventDiscoveryCategory,
            KEY_EVENT_ACTION to eventAction,
            KEY_EVENT_LABEL to eventLabel,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            USER_ID to "${if (userID.isNullOrBlank()) 0 else userID}",
            BUSINESS_UNIT to SHARING_EXPERIENCE,
            PAGE_TYPE to pageType,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_SOURCE to sourceIdentifier
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
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_SOURCE to sourceIdentifier
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
        val gtmItemName = componentsItems.data?.get(0)?.gtmItemName
        list.add(
            mapOf(
                KEY_NAME to (
                    if (gtmItemName?.isEmpty() == true) {
                        "/discovery/$removedDashPageIdentifier - $pageType - ${
                            getParentPosition(
                                componentsItems
                            ) + 1
                        } - ${componentsItems.data?.firstOrNull()?.title} - - ${componentsItems.name}"
                    } else {
                        (gtmItemName?.replace(
                            "#POSITION",
                            (getParentPosition(componentsItems) + 1).toString()
                        )) ?: ""
                    }),
                KEY_ID to "${componentsItems.position + 1}_${componentsItems.parentComponentId}",
                KEY_POSITION to "${componentsItems.position + 1}",
                KEY_CREATIVE to (componentsItems.data?.firstOrNull()?.creativeName ?: EMPTY_STRING)
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = CALENDAR_WIDGET_IMPRESSION,
            eventLabel = "${componentsItems.properties?.calendarLayout} layout - ${componentsItems.data?.firstOrNull()?.title}",
            shouldSendSourceAsDestination = true
        )
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        map[USER_ID] = userID
        map[TRACKER_ID] = "19698"
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackEventClickCalendarWidget(componentsItems: ComponentsItem, userID: String) {
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = componentsItems.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/discovery/$removedDashPageIdentifier - $pageType - ${
                            getParentPosition(
                                componentsItems
                            ) + 1
                        } - ${componentsItems.data?.firstOrNull()?.title} - - ${componentsItems.name}"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (getParentPosition(
                                componentsItems
                            ) + 1).toString()
                        )
                    }),
                KEY_ID to "${componentsItems.position + 1}_${componentsItems.parentComponentId}",
                KEY_POSITION to "${componentsItems.position + 1}",
                KEY_CREATIVE to (componentsItems.data?.firstOrNull()?.creativeName ?: EMPTY_STRING)
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CALENDAR_WIDGET_CLICK,
            eventLabel = "${componentsItems.properties?.calendarLayout} layout - p${componentsItems.parentComponentPosition + 1} - ${componentsItems.data?.firstOrNull()?.title}",
            shouldSendSourceAsDestination = true
        )
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_TYPE] = pageType
        map[USER_ID] = userID
        map[TRACKER_ID] = "19699"
        map[KEY_AFFINITY_LABEL] = EMPTY_STRING
        map[KEY_ATTRIBUTION] = EMPTY_STRING
        map[KEY_CAMPAIGN_CODE] = EMPTY_STRING
        map[SHOP_ID] = EMPTY_STRING
        map[CATEGORY_ID] = EMPTY_STRING
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackEventClickCalendarCTA(componentsItems: ComponentsItem, userID: String) {
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = componentsItems.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/discovery/$removedDashPageIdentifier - $pageType - ${
                            getParentPosition(
                                componentsItems
                            ) + 1
                        } - ${componentsItems.data?.firstOrNull()?.title} - ${componentsItems.name}"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (getParentPosition(
                                componentsItems
                            ) + 1).toString()
                        )
                    }),
                KEY_ID to "${componentsItems.position + 1}_${componentsItems.parentComponentId}",
                KEY_POSITION to "${componentsItems.position + 1}",
                KEY_CREATIVE to (componentsItems.data?.firstOrNull()?.creativeName ?: EMPTY_STRING)
            )
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CALENDAR_WIDGET_CTA_CLICK,
            eventLabel = "${componentsItems.properties?.calendarLayout} layout - p${componentsItems.parentComponentPosition + 1} - ${componentsItems.data?.firstOrNull()?.title}"
        )
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
        map[PAGE_SOURCE] = sourceIdentifier
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
        map[PAGE_SOURCE] = sourceIdentifier
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
        map[PAGE_SOURCE] = sourceIdentifier
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
        map[PAGE_SOURCE] = sourceIdentifier
        getTracker().sendGeneralEvent(map)
    }

    private fun trackAnchorTabImpression(components: ComponentsItem) {
        val map =
            createGeneralEvent(eventName = EVENT_PROMO_VIEW, eventAction = ANCHOR_TAB_IMPRESSION)
        val list = ArrayList<Map<String, Any>>()
        val functionalName =
            if (components.data?.firstOrNull()?.imageUrlMobile?.isNotEmpty() == true) {
                ANCHOR_TYPE_ICON_TEXT
            } else {
                ANCHOR_TYPE_TEXT_ONLY
            }
        list.add(
            mapOf(
                KEY_ID to components.parentComponentId,
                KEY_NAME to "/discovery/$removedDashPageIdentifier - $pageType - ${
                    getParentPosition(
                        components
                    ) + 1
                } - $functionalName - - $ANCHOR_TAB_COMPONENT",
                KEY_CREATIVE to (components.data?.firstOrNull()?.creativeName ?: EMPTY_STRING),
                KEY_POSITION to (components.position + 1)
            )
        )
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
            if (components.data?.firstOrNull()?.imageUrlMobile?.isNotEmpty() == true) {
                ANCHOR_TYPE_ICON_TEXT
            } else {
                ANCHOR_TYPE_TEXT_ONLY
            }
        list.add(
            mapOf(
                KEY_ID to components.parentComponentId,
                KEY_NAME to "/discovery/$removedDashPageIdentifier - $pageType - ${
                    getParentPosition(
                        components
                    ) + 1
                } - $functionalName - - $ANCHOR_TAB_COMPONENT",
                KEY_CREATIVE to (components.data?.firstOrNull()?.creativeName ?: EMPTY_STRING),
                KEY_POSITION to (components.position + 1)
            )
        )
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

    override fun trackShopCardImpression(componentsItems: ComponentsItem) {
        val list = ArrayList<Map<String, Any>>()
        val shopMap = HashMap<String, Any>()
        val keyName: String =
            if (componentsItems.parentComponentName == ComponentsList.ShopCardInfinite.componentName) {
                "/discovery/$removedDashPageIdentifier - $pageType - ${
                    getParentPosition(
                        componentsItems
                    ) + 1
                } - $RILISAN_SPESIAL - - $SHOP_CARD_INFINITE"
            } else {
                "/discovery/$removedDashPageIdentifier - $pageType - ${
                    getParentPosition(
                        componentsItems
                    ) + 1
                } - ${componentsItems.properties?.shopInfo ?: ""} - - $SHOP_CARD_BANNER"
            }
        componentsItems.data?.firstOrNull()?.let {
            shopMap[KEY_NAME] = keyName
            shopMap[KEY_ID] =
                "${componentsItems.parentComponentId}_${componentsItems.data?.firstOrNull()?.shopId}"
            shopMap[KEY_POSITION] = "${componentsItems.position + 1}"
            shopMap[KEY_CREATIVE] = (componentsItems.creativeName ?: EMPTY_STRING)
        }
        list.add(shopMap)
        val eventModel = EventModel(
            event = EVENT_PROMO_VIEW,
            action = ACTION_SHOP_CARD_VIEW,
            label = "",
            category = eventDiscoveryCategory
        )
        eventModel.key = "${componentsItems.creativeName}"
        val customDimensionMap = HashMap<String, Any>()
        customDimensionMap[PAGE_TYPE] = pageType
        customDimensionMap[PAGE_PATH] = removedDashPageIdentifier
        customDimensionMap[PAGE_SOURCE] = sourceIdentifier
        customDimensionMap[BUSINESS_UNIT] = HOME_BROWSE
        customDimensionMap[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        customDimensionMap[USER_ID] = userSession.userId

        trackingQueue.putEETracking(
            eventModel,
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    EVENT_PROMO_VIEW to hashMapOf(
                        KEY_PROMOTIONS to list
                    )
                )
            ),
            customDimensionMap
        )
    }

    override fun trackEventClickShopCard(componentsItems: ComponentsItem) {
        val list = ArrayList<Map<String, Any>>()
        val shopMap = HashMap<String, Any>()
        val shopName: String
        val keyName: String
        if (componentsItems.parentComponentName == ComponentsList.ShopCardInfinite.componentName) {
            keyName = "/discovery/$removedDashPageIdentifier - $pageType - ${
                getParentPosition(componentsItems) + 1
            } - $RILISAN_SPESIAL - - $SHOP_CARD_INFINITE"
            shopName = SHOP_CARD_INFINITE
        } else {
            keyName = "/discovery/$removedDashPageIdentifier - $pageType - ${
                getParentPosition(componentsItems) + 1
            } - ${componentsItems.properties?.shopInfo ?: ""} - - $SHOP_CARD_BANNER"
            shopName = SHOP_CARD_BANNER
        }
        componentsItems.data?.firstOrNull().let {
            shopMap[KEY_NAME] = keyName
            shopMap[KEY_ID] =
                "${componentsItems.parentComponentId}_${componentsItems.data?.firstOrNull()?.shopId}"
            shopMap[KEY_POSITION] = "${componentsItems.position + 1}"
            shopMap[KEY_CREATIVE] = (
                componentsItems.data?.firstOrNull()?.creativeName
                    ?: EMPTY_STRING
                )
        }
        list.add(shopMap)

        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = ACTION_SHOP_CARD_CLICK,
            eventLabel = "$shopName - - ${componentsItems.data?.firstOrNull()?.shopId}"
        )
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = userSession.userId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackMixLeftBannerImpression(componentsItems: ComponentsItem) {
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = IMPRESSION_DYNAMIC_BANNER
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        val list = ArrayList<Map<String, Any>>()
        val hashMap = HashMap<String, Any>()
        val gtmItemName = componentsItems.data?.firstOrNull()?.gtmItemName
        hashMap[KEY_ID] = componentsItems.parentComponentId
        hashMap[KEY_NAME] =
            (if (gtmItemName.isNullOrEmpty()) {
                "/${removeDashPageIdentifier(pagePath)} - $pageType - ${
                    getParentPosition(componentsItems)
                } - - - $MIX_LEFT_BANNER"
            } else {
                gtmItemName.replace(
                    "#POSITION",
                    (getParentPosition(componentsItems)).toString()
                )
            })
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
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_DYNAMIC_BANNER,
            eventLabel = "$MIX_LEFT_BANNER - $creativeName - $applink"
        )
        val list = ArrayList<Map<String, Any>>()
        val gtmItemName = componentsItems.data?.firstOrNull()?.gtmItemName
        list.add(
            mapOf(
                KEY_ID to componentsItems.parentComponentId,
                KEY_NAME to
                    (if (gtmItemName.isNullOrEmpty()) {
                        "/${removeDashPageIdentifier(pagePath)} - $pageType - ${
                            getParentPosition(
                                componentsItems
                            )
                        } - - - $MIX_LEFT_BANNER"
                    } else {
                        gtmItemName.replace(
                            "#POSITION",
                            (getParentPosition(
                                componentsItems
                            )).toString()
                        )
                    }),
                KEY_CREATIVE to creativeName,
                KEY_POSITION to 1
            )
        )

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
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun track3DotsOptionsClickedLihatToko() {
        track3DotsOptionsClicked(CLICKED_THREE_DOTS_INSIDE_PRODUCT, LABEL_LIHAT_TOKO)
    }

    override fun track3DotsOptionsClickedShareProduct() {
        track3DotsOptionsClicked(CLICKED_THREE_DOTS_INSIDE_PRODUCT, LABEL_SHARE_PRODUCT)
    }

    override fun track3DotsOptionsClickedWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        val productID = productCardOptionsModel.productId
        val compType = ComponentNames.ProductCardSingle.componentName
        track3DotsOptionsClicked(CLICK_ADD_TO_WISHLIST, "$compType - $productID")
    }

    private fun track3DotsOptionsClicked(action: String, label: String) {
        val map = createGeneralEvent(
            eventName = CLICK_HOMEPAGE_EVENT,
            eventAction = action,
            eventLabel = label
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[USER_ID] = userSession.userId
        map[PAGE_SOURCE] = sourceIdentifier
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackShopBannerInfiniteImpression(componentsItems: ComponentsItem) {
        val list = ArrayList<Map<String, Any>>()
        val shopMap = HashMap<String, Any>()
        componentsItems.data?.firstOrNull()?.let {
            shopMap[KEY_NAME] = "/discovery/$removedDashPageIdentifier - $pageType - ${
                getParentPosition(componentsItems) + 1
            } - ${componentsItems.properties?.compType ?: EMPTY_STRING} - - $SHOP_CARD_INFINITE"
            shopMap[KEY_ID] = "${it.id ?: EMPTY_STRING}_${it.shopId ?: EMPTY_STRING}"
            shopMap[KEY_POSITION] = "${componentsItems.position + 1}"
            shopMap[KEY_CREATIVE] = (it.creativeName ?: EMPTY_STRING)
        }
        list.add(shopMap)
        val eventModel = EventModel(
            event = EVENT_PROMO_VIEW,
            action = ACTION_SHOP_CARD_VIEW,
            label = "",
            category = eventDiscoveryCategory
        )
        eventModel.key = "${componentsItems.creativeName}"
        val customDimensionMap = HashMap<String, Any>()
        customDimensionMap[PAGE_TYPE] = pageType
        customDimensionMap[PAGE_PATH] = removedDashPageIdentifier
        customDimensionMap[PAGE_SOURCE] = sourceIdentifier
        customDimensionMap[BUSINESS_UNIT] = HOME_BROWSE
        customDimensionMap[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        customDimensionMap[USER_ID] = userSession.userId

        trackingQueue.putEETracking(
            eventModel,
            hashMapOf(
                KEY_ECOMMERCE to hashMapOf(
                    EVENT_PROMO_VIEW to hashMapOf(
                        KEY_PROMOTIONS to list
                    )
                )
            ),
            customDimensionMap
        )
    }

    override fun trackShopBannerInfiniteClick(componentsItems: ComponentsItem) {
        val list = ArrayList<Map<String, Any>>()
        val shopMap = HashMap<String, Any>()
        componentsItems.data?.firstOrNull()?.let {
            shopMap[KEY_NAME] = "/discovery/$removedDashPageIdentifier - $pageType - ${
                getParentPosition(componentsItems) + 1
            } - ${componentsItems.properties?.compType ?: ""} - - $SHOP_CARD_INFINITE"
            shopMap[KEY_ID] = "${it.id ?: EMPTY_STRING}_${it.shopId ?: EMPTY_STRING}"
            shopMap[KEY_POSITION] = "${componentsItems.position + 1}"
            shopMap[KEY_CREATIVE] = (it.creativeName ?: EMPTY_STRING)
        }
        list.add(shopMap)

        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = ACTION_SHOP_CARD_CLICK,
            eventLabel = "$SHOP_CARD_BANNER - - ${componentsItems.data?.firstOrNull()?.shopId ?: EMPTY_STRING}"
        )
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = userSession.userId
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    private fun convertRupiahToInt(rupiah: String): Int {
        return if (rupiah.isEmpty() || rupiah.contains("?")) {
            0
        } else {
            CurrencyFormatHelper.convertRupiahToInt(rupiah)
        }
    }

    override fun trackEventBundleProductClicked(
        componentsItems: ComponentsItem,
        bundleType: BundleTypes,
        bundle: BundleUiModel,
        selectedMultipleBundle: BundleDetailUiModel,
        selectedProduct: BundleProductUiModel,
        productItemPosition: Int
    ) {
        val list = ArrayList<Map<String, Any>>()
        val login = if (userSession.isLoggedIn) LOGIN else NON_LOGIN
        val dataItem = componentsItems.data?.firstOrNull()
        val productBundlingMap = mutableMapOf<String, Any>()
        dataItem?.let {
            productBundlingMap[KEY_NAME] = selectedProduct.productName
            productBundlingMap[KEY_ID] = selectedProduct.productId
            productBundlingMap[KEY_POSITION] = "${componentsItems.position + 1}"
            productBundlingMap[KEY_CREATIVE] = (it.creativeName ?: EMPTY_STRING)
            productBundlingMap[INDEX] = "$productItemPosition"
            productBundlingMap[ITEM_BRAND] = EMPTY_STRING
            productBundlingMap[KEY_ITEM_CATEGORY] = EMPTY_STRING
            productBundlingMap[ITEM_VARIANT] = EMPTY_STRING
            productBundlingMap[PRICE] = selectedMultipleBundle.displayPriceRaw
            productBundlingMap[DIMENSION40] =
                "/${removeDashPageIdentifier(pagePath)} - $pageType - ${
                    getParentPosition(componentsItems) + 1
                } - $login - ${componentsItems.name} - - ${if (it.isTopads == true) TOPADS else NON_TOPADS} - ${if (it.creativeName.isNullOrEmpty()) "" else it.creativeName} - ${componentsItems.sectionId} - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName}"
            productBundlingMap[DIMENSION117] = selectedMultipleBundle.bundleType
            productBundlingMap[DIMENSION118] = selectedMultipleBundle.bundleId
        }
        list.add(productBundlingMap)
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = ACTION_PB_PRODUCT_CLICK,
            eventLabel = "${selectedMultipleBundle.bundleId} - ${selectedProduct.productId} -${selectedMultipleBundle.shopInfo?.shopId ?: EMPTY_STRING} - ${selectedMultipleBundle.bundleType} - ${selectedMultipleBundle.discountPercentage}"
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        map[ITEM_LIST] = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${
            getParentPosition(componentsItems) + 1
        } - $login - ${componentsItems.name} - - ${if (dataItem?.isTopads == true) TOPADS else NON_TOPADS} - ${if (dataItem?.creativeName.isNullOrEmpty()) "" else dataItem?.creativeName} - ${componentsItems.sectionId} - ${if (dataItem?.tabName.isNullOrEmpty()) "" else dataItem?.tabName}"
        map[PAGE_SOURCE] = sourceIdentifier
        map[BUSINESS_UNIT] = PHYSICAL_GOODS
        map[TRACKER_ID] = "42740"
        map[KEY_SHOP_ID] = selectedMultipleBundle.shopInfo?.shopId ?: EMPTY_STRING
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = userSession.userId

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackEventClickProductBundlingChipSelection(
        componentsItems: ComponentsItem,
        selectedProduct: BundleProductUiModel,
        selectedSingleBundle: BundleDetailUiModel
    ) {
        val list = ArrayList<Map<String, Any>>()
        val map = createGeneralEvent(
            eventName = KEY_NAME_CLICK_CHIP_SELECTION,
            eventAction = ACTION_CLICK_CHIP_SELECTION,
            eventLabel = "${selectedSingleBundle.bundleId} - ${selectedSingleBundle.shopInfo?.shopId ?: EMPTY_STRING} - ${selectedSingleBundle.bundleType} - ${selectedSingleBundle.discountPercentage} - ${selectedSingleBundle.minOrderWording}"
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_SOURCE] = sourceIdentifier
        map[BUSINESS_UNIT] = PHYSICAL_GOODS
        map[TRACKER_ID] = TRACKER_ID_PRODUCT_BUNDLING_VARIANT_CHANGE_VALUE
        map[PRODUCT_ID] = selectedProduct.productId
        map[KEY_SHOP_ID] = selectedSingleBundle.shopInfo?.shopId ?: EMPTY_STRING
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = userSession.userId

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackEventProductBundlingAtcClick(
        componentsItems: ComponentsItem,
        selectedMultipleBundle: BundleDetailUiModel
    ) {
        val list = ArrayList<Map<String, Any>>()
        var productBundlingMap: MutableMap<String, Any>
        selectedMultipleBundle.products.forEach { bundleProductUiModel ->
            productBundlingMap = mutableMapOf()
            componentsItems.data?.firstOrNull()?.let {
                productBundlingMap[KEY_NAME] =
                    "/discovery/$removedDashPageIdentifier - $pageType - ${componentsItems.position + 1} - ${bundleProductUiModel.productName} - - $NAME_KEY_PRODUCT_BUNDLING"
                productBundlingMap[KEY_ID] = bundleProductUiModel.productId
                productBundlingMap[KEY_POSITION] = "${componentsItems.position + 1}"
                productBundlingMap[KEY_CREATIVE] = (it.creativeName ?: EMPTY_STRING)
                productBundlingMap[DIMENSION117] = selectedMultipleBundle.bundleType
                productBundlingMap[DIMENSION118] = selectedMultipleBundle.bundleId
            }
            list.add(productBundlingMap)
        }
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = ACTION_PRODUCT_BUNDLING_PRODUCT_CLICK,
            eventLabel = "${selectedMultipleBundle.bundleId} - ${selectedMultipleBundle.shopInfo?.shopId ?: EMPTY_STRING} - ${selectedMultipleBundle.bundleType} - ${selectedMultipleBundle.discountPercentage}"
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_SOURCE] = sourceIdentifier
        map[BUSINESS_UNIT] = PHYSICAL_GOODS
        map[TRACKER_ID] = TRACKER_ID_PRODUCT_BUNDLING_PRODUCT_CLICK_VALUE
        map[KEY_SHOP_ID] = selectedMultipleBundle.shopInfo?.shopId ?: EMPTY_STRING
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = userSession.userId

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackEventProductBundlingViewImpression(
        componentsItems: ComponentsItem,
        selectedBundle: BundleDetailUiModel,
        bundlePosition: Int
    ) {
        val list = ArrayList<Map<String, Any>>()
        var productBundlingMap: MutableMap<String, Any>
        selectedBundle.products.forEach { bundleProductUiModel ->
            productBundlingMap = mutableMapOf()
            componentsItems.data?.firstOrNull()?.let {
                productBundlingMap[KEY_NAME] =
                    "/discovery/$removedDashPageIdentifier - $pageType - ${componentsItems.position + 1} - ${bundleProductUiModel.productName} - - $NAME_KEY_PRODUCT_BUNDLING"
                productBundlingMap[KEY_ID] = bundleProductUiModel.productId
                productBundlingMap[KEY_POSITION] = "${componentsItems.position + 1}"
                productBundlingMap[KEY_CREATIVE] = (it.creativeName ?: EMPTY_STRING)
                productBundlingMap[DIMENSION117] = selectedBundle.bundleType
                productBundlingMap[DIMENSION118] = selectedBundle.bundleId
            }
            list.add(productBundlingMap)
        }
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = ACTION_PRODUCT_BUNDLING_VIEW,
            eventLabel = "${selectedBundle.bundleId} - ${selectedBundle.shopInfo?.shopId ?: EMPTY_STRING} - ${selectedBundle.bundleType} - ${selectedBundle.discountPercentage}"
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_SOURCE] = sourceIdentifier
        map[BUSINESS_UNIT] = PHYSICAL_GOODS
        map[TRACKER_ID] = TRACKER_ID_PRODUCT_BUNDLING_PRODUCT_VIEW_VALUE
        map[KEY_SHOP_ID] = selectedBundle.shopInfo?.shopId ?: EMPTY_STRING
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = userSession.userId

        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackEventProductBundlingCarouselImpression(
        componentsItems: ComponentsItem,
        bundledProductList: List<BundleUiModel>,
        totalBundlings: Int,
        totalBundleSeenPosition: Int,
        lastVisibleItemPosition: Int
    ) {
        val list = ArrayList<Map<String, Any>>()
        var productBundlingMap: MutableMap<String, Any>
        for (i in totalBundleSeenPosition until lastVisibleItemPosition + 1) {
            productBundlingMap = mutableMapOf()
            componentsItems.data?.firstOrNull()?.let {
                productBundlingMap[KEY_NAME] =
                    "/discovery/$removedDashPageIdentifier - $pageType - ${componentsItems.position + 1} - ${
                        bundledProductList.getOrNull(i)?.bundleName ?: ""
                    } - - $NAME_KEY_PRODUCT_BUNDLING"
                productBundlingMap[KEY_ID] =
                    bundledProductList.getOrNull(i)?.bundleDetails?.firstOrNull()?.bundleId ?: ""
                productBundlingMap[KEY_POSITION] = "${componentsItems.position + 1}"
                productBundlingMap[KEY_CREATIVE] = (it.creativeName ?: EMPTY_STRING)
            }
            list.add(productBundlingMap)
        }
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_VIEW,
            eventAction = ACTION_CAROUSEL_BUNDLING_VIEW,
            eventLabel = "${lastVisibleItemPosition + 1} - $totalBundlings"
        )
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_VIEW to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[KEY_E_COMMERCE] = eCommerce
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[PAGE_SOURCE] = sourceIdentifier
        map[BUSINESS_UNIT] = PHYSICAL_GOODS
        map[TRACKER_ID] = TRACKER_ID_PRODUCT_BUNDLING_PRODUCT_CAROUSEL_VALUE
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = userSession.userId

        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    override fun trackContentCardClick(
        componentsItems: ComponentsItem,
        userID: String?
    ) {
        val banner = componentsItems.data?.first()
        val componentName = componentsItems.name ?: EMPTY_STRING
        val map = createGeneralEvent(
            eventName = EVENT_PROMO_CLICK,
            eventAction = CLICK_DYNAMIC_BANNER,
            eventLabel = "$componentName - ${banner?.creativeName} - ${banner?.landingPage?.appLink}",
            shouldSendSourceAsDestination = true
        )
        val list = ArrayList<Map<String, Any>>()
        banner.let {
            list.add(
                mapOf(
                    KEY_ID to "${it?.product?.productId}_0",
                    KEY_NAME to it?.gtmItemName?.replace(
                        "#POSITION",
                        (componentsItems.parentComponentPosition + 1).toString()
                    ).toString(),
                    KEY_CREATIVE to it?.creativeName.toString(),
                    KEY_POSITION to componentsItems.position + 1
                )
            )
        }
        val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
            EVENT_PROMO_CLICK to mapOf(
                KEY_PROMOTIONS to list
            )
        )
        map[KEY_ATTRIBUTION] = banner?.attribution ?: EMPTY_STRING
        map[KEY_AFFINITY_LABEL] = banner?.name ?: EMPTY_STRING
        map[KEY_CATEGORY_ID] = banner?.category ?: EMPTY_STRING
        map[KEY_SHOP_ID] = banner?.shopId ?: EMPTY_STRING
        map[KEY_CAMPAIGN_CODE] =
            "${if (banner?.campaignCode.isNullOrEmpty()) campaignCode else banner?.campaignCode}"
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[TRACKER_ID] = "2705"
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[USER_ID] = userID ?: EMPTY_STRING
        map[KEY_E_COMMERCE] = eCommerce
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    override fun trackContentCardImpression(
        componentsItems: ComponentsItem,
        userID: String?
    ) {
        val banners = componentsItems.data
        if (banners?.isNotEmpty() == true) {
            banners.forEachIndexed { index, banner ->
                val map = createGeneralEvent(
                    eventName = EVENT_PROMO_VIEW,
                    eventAction = IMPRESSION_DYNAMIC_BANNER,
                    shouldSendSourceAsDestination = true
                )
                map[TRACKER_ID] = "2704"
                map[PAGE_TYPE] = pageType
                map[PAGE_PATH] = removedDashPageIdentifier
                val list = ArrayList<Map<String, Any>>()
                val hashMap = HashMap<String, Any>()
                banner.let {
                    val bannerID = "${it.product?.productId}"
                    hashMap[KEY_ID] = "${bannerID}_0"
                    hashMap[KEY_NAME] = it.gtmItemName?.replace(
                        "#POSITION",
                        (componentsItems.parentComponentPosition + 1).toString()
                    ).toString()
                    hashMap[KEY_CREATIVE] = it.creativeName ?: EMPTY_STRING
                    hashMap[KEY_POSITION] = componentsItems.position + 1
                    list.add(hashMap)
                    val eCommerce: Map<String, Map<String, ArrayList<Map<String, Any>>>> = mapOf(
                        EVENT_PROMO_VIEW to mapOf(
                            KEY_PROMOTIONS to list
                        )
                    )
                    map[KEY_E_COMMERCE] = eCommerce
                    map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
                    map[BUSINESS_UNIT] = HOME_BROWSE
                    map[USER_ID] = userID ?: EMPTY_STRING
                    trackingQueue.putEETracking(map as HashMap<String, Any>)
                }
            }
        }
    }

    // product highlight will use same tracker as Product card thats why trackerId is also same.
    override fun trackPromoProductHighlightImpression(
        productHighlightData: List<DataItem>,
        components: ComponentsItem?
    ) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productHighlightData.forEach { it ->
            productMap[KEY_NAME] = it.productName.toString()
            productMap[KEY_ID] = it.productId.toString()
            productMap[PRICE] = convertRupiahToInt(it.price ?: "")
            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_ITEM_CATEGORY] = NONE_OTHER
            productMap[KEY_VARIANT] = NONE_OTHER
            productMap[KEY_POSITION] = (components?.position ?: 0) + 1
            productMap[LIST] = it.gtmItemName.replace(
                "#POSITION",
                (components?.let { it1 -> getParentPosition(it1) }?.plus(1)).toString()
            )
            productMap[DIMENSION83] = getProductDime83(it)
            productMap[DIMENSION90] = sourceIdentifier
            productMap[DIMENSION96] =
                " - ${if (it.notifyMeCount.toIntOrZero() > 0) it.notifyMeCount else " "} - ${if (it.pdpView.toIntOrZero() > 0) it.pdpView else 0} - " +
                    "${if (it.campaignSoldCount.toIntOrZero() > 0) it.campaignSoldCount else 0} $SOLD - ${if (it.customStock.toIntOrZero() > 0) it.customStock else 0} $LEFT - - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName} - ${
                        getLabelCampaign(
                            it
                        )
                    } - $NOTIFY_ME"
            productMap[DIMENSION38] = ""
            productMap[DIMENSION84] = ""
            val gtmItemName = it.gtmItemName.replace(
                "#POSITION",
                (components?.let { it1 -> getParentPosition(it1) }?.plus(1)).toString()
            )
            productMap[DIMENSION40] = processGtmItemName(gtmItemName, it)
        }
        list.add(productMap)

        val eCommerce = mapOf(
            CURRENCY_CODE to IDR,
            KEY_IMPRESSIONS to list
        )
        val map = createGeneralEvent(
            eventName = EVENT_PRODUCT_VIEW,
            eventAction = PRODUCT_LIST_IMPRESSION,
            shouldSendSourceAsDestination = true
        )
        map[TRACKER_ID] = "2721"
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = (userSession.userId ?: "")
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce

        trackingQueue.putEETracking(map as HashMap<String, Any>)
        productCardImpressionLabel = EMPTY_STRING
    }

    override fun trackProductHighlightClick(
        productHighlightData: DataItem,
        productHighlightPosition: Int,
        components: ComponentsItem?,
        isLogin: Boolean
    ) {
        if (!components?.data.isNullOrEmpty()) {
            val login = if (isLogin) LOGIN else NON_LOGIN
            val list = ArrayList<Map<String, Any>>()
            val listMap = HashMap<String, Any>()
            var productItemList = ""
            productHighlightData.let {
                productItemList = it.gtmItemName.replace(
                    "#POSITION",
                    (components?.let { it1 -> getParentPosition(it1) }?.plus(1)).toString()
                )
                productCardImpressionLabel = EMPTY_STRING
                listMap[KEY_NAME] = it.productName.toString()
                listMap[KEY_ID] = it.productId.toString()
                listMap[PRICE] = convertRupiahToInt(it.price ?: "")
                listMap[KEY_BRAND] = NONE_OTHER
                listMap[KEY_ITEM_CATEGORY] = NONE_OTHER
                listMap[KEY_VARIANT] = NONE_OTHER
                listMap[KEY_POSITION] = (components?.position ?: 0) + 1
                listMap[LIST] = productItemList
                listMap[DIMENSION83] = getProductDime83(it)
                listMap[DIMENSION90] = sourceIdentifier
                listMap[DIMENSION96] =
                    " - ${if (it.notifyMeCount.toIntOrZero() > 0) it.notifyMeCount else " "} - ${if (it.pdpView.toIntOrZero() > 0) it.pdpView else 0} - " +
                        "${if (it.campaignSoldCount.toIntOrZero() > 0) it.campaignSoldCount else 0} $SOLD - ${if (it.customStock.toIntOrZero() > 0) it.customStock else 0} $LEFT - - ${if (it.tabName.isNullOrEmpty()) "" else it.tabName} - ${
                            getLabelCampaign(
                                it
                            )
                        } - $NOTIFY_ME ${components?.let { it1 -> getNotificationStatus(it1) }}"
                listMap[DIMENSION38] = ""
                listMap[DIMENSION84] = ""
                val gtmItemName = it.gtmItemName?.replace(
                    "#POSITION",
                    (components?.let { it1 -> getParentPosition(it1) }?.plus(1)).toString()
                )?.replace("#MEGA_TAB_VALUE", it.tabName ?: "").toString()
                listMap[DIMENSION40] = processGtmItemName(gtmItemName, it)
            }
            list.add(listMap)

            val eCommerce = mapOf(
                CLICK to mapOf(
                    ACTION_FIELD to mapOf(
                        LIST to productItemList
                    ),
                    PRODUCTS to list
                )
            )
            val map = createGeneralEvent(
                eventName = EVENT_PRODUCT_CLICK,
                eventAction = CLICK_PRODUCT_LIST,
                eventLabel = "$login - ${ComponentNames.ProductBundling.componentName}",
                shouldSendSourceAsDestination = true
            )
            map[TRACKER_ID] = "2722"
            map[KEY_CAMPAIGN_CODE] = campaignCode
            map[PAGE_TYPE] = pageType
            map[PAGE_PATH] = removedDashPageIdentifier
            map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
            map[USER_ID] = (userSession.userId ?: "")
            map[BUSINESS_UNIT] = HOME_BROWSE
            map[KEY_E_COMMERCE] = eCommerce
            getTracker().sendEnhanceEcommerceEvent(map)
            productCardImpressionLabel = EMPTY_STRING
        }
    }

    override fun trackProductHighlightOCSClick(
        ocsDataModel: DiscoveryOCSDataModel,
        parentPosition: Int?,
        parentComponentId: String?
    ) {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        val login = if (userSession.isLoggedIn) LOGIN else NON_LOGIN

        with(ocsDataModel.dataItem) {
            val productTypeName = getProductName(typeProductCard)

            productMap[KEY_ATC_CATEGORY_ID] = NONE_OTHER
            productMap[DIMENSION40] = "/${removeDashPageIdentifier(pagePath)} - $pageType - ${
                parentPosition.orZero() + 1
            } - $login - $productTypeName - - ${if (isTopads == true) TOPADS else NON_TOPADS} - ${creativeName.orEmpty()} - ${tabName.orEmpty()}"
            productMap[DIMENSION83] = getProductDime83(this)
            productMap[DIMENSION84] = EMPTY_STRING
            productMap[DIMENSION90] = sourceIdentifier
            productMap[DIMENSION96] = getValueForDimension96(this, parentComponentId)

            productMap[KEY_BRAND] = NONE_OTHER
            productMap[KEY_CATEGORY] = NONE_OTHER
            productMap[KEY_ID] = productId.toString()
            productMap[KEY_NAME] = productName.toString()
            productMap[KEY_VARIANT] = NONE_OTHER

            productMap[PRICE] = CurrencyFormatHelper.convertRupiahToInt(price.orEmpty())
            productMap[KEY_QUANTITY] = minQuantity
            productMap[KEY_ATC_SHOP_ID] = shopId.orEmpty()
            productMap[KEY_SHOP_NAME] = shopName.orEmpty()
            productMap[KEY_SHOP_TYPE] = shopType.orEmpty()
        }

        with(ocsDataModel.atcData.data) {
            productMap[DIMENSION38] = trackerAttribution
            productMap[DIMENSION45] = cartId
        }

        list.add(productMap)

        val productsMap = mapOf(PRODUCTS to list)

        val eCommerce = mapOf(
            CURRENCY_CODE to IDR,
            KEY_ADD to productsMap
        )

        val map = createGeneralEvent(
            eventName = EVENT_PRODUCT_ATC,
            eventAction = CLICK_OCS,
            eventLabel = ComponentNames.ProductHighlight.componentName,
            shouldSendSourceAsDestination = true
        )
        map[TRACKER_ID] = PRODUCT_HIGHLIGHT_CLICK_OCS_TRACKER_ID
        map[PAGE_TYPE] = pageType
        map[PAGE_PATH] = removedDashPageIdentifier
        map[CURRENT_SITE] = TOKOPEDIA_MARKET_PLACE
        map[USER_ID] = userSession.userId.orEmpty()
        map[BUSINESS_UNIT] = HOME_BROWSE
        map[KEY_E_COMMERCE] = eCommerce

        getTracker().sendEnhanceEcommerceEvent(map)
    }

    private fun processGtmItemName(gtmItemName: String, dataItem: DataItem): String {
        val dataToAppend = mutableListOf(
            dataItem.title ?: "",
            if (dataItem.isTopads == true) TOPADS else NON_TOPADS,
            dataItem.creativeName ?: "",
            dataItem.tabName ?: ""
        )
        var j = 0
        val gtmNameParts = gtmItemName.split(" - ").toMutableList()
        for (i in gtmNameParts.indices) {
            if (j >= dataToAppend.size) {
                return gtmNameParts.joinToString("-") { " $it " }
            }
            if (gtmNameParts[i].trim().isEmpty() || gtmNameParts[i].trim() == "-") {
                gtmNameParts[i] = dataToAppend[j++]
            }
        }
        if (j < dataToAppend.size) {
            gtmNameParts.add(dataToAppend[j])
        }
        return gtmNameParts.joinToString("-") { " $it " }
    }

    override fun trackCouponImpression(properties: List<CouponTrackingProperties>) {
        val generalProps = createGeneralCouponEvent(
            EVENT_PROMO_VIEW,
            ACTION_VIEW_COUPON_BANNER
        )

        val impressionProperties = hashMapOf<String, Any>(
            KEY_EVENT_LABEL to EMPTY_STRING,
            TRACKER_ID to COUPON_IMPRESSION_TRACKER_ID,
            USER_ID to userSession.userId,
            KEY_E_COMMERCE to constructPromotionsProps(EVENT_PROMO_VIEW, properties)
        )

        impressionProperties.putAll(generalProps)
        trackingQueue.putEETracking(impressionProperties)
    }

    override fun trackCouponClickEvent(properties: CouponTrackingProperties) {
        val generalProps = createGeneralCouponEvent(
            EVENT_PROMO_CLICK,
            ACTION_CLICK_COUPON_BANNER
        )

        val clickProperties = hashMapOf<String, Any>(
            KEY_EVENT_LABEL to "${properties.componentName} - ${properties.promoCode}",
            TRACKER_ID to COUPON_CLICK_TRACKER_ID,
            USER_ID to userSession.userId,
            KEY_E_COMMERCE to constructPromotionsProps(EVENT_PROMO_CLICK, listOf(properties))
        )

        clickProperties.putAll(generalProps)
        getTracker().sendEnhanceEcommerceEvent(clickProperties)
    }

    override fun trackCouponCTAClickEvent(properties: CouponTrackingProperties) {
        val generalProps = createGeneralCouponEvent(
            CLICK_HOMEPAGE_EVENT,
            CLICK_BUTTON_CLAIM_COUPON_ACTION
        )

        val label = with(properties) {
            "$componentName - $action - $tabName - $creativeName - $promoCode"
        }

        val ctaClickProperties = hashMapOf<String, Any>(
            KEY_EVENT_LABEL to label,
            TRACKER_ID to COUPON_CLICK_CTA_TRACKER_ID
        )

        ctaClickProperties.putAll(generalProps)
        getTracker().sendGeneralEvent(ctaClickProperties)
    }

    override fun trackMvcImpression(properties: List<MvcTrackingProperties>) {
        val generalProps = createGeneralCouponEvent(
            EVENT_PROMO_VIEW,
            IMPRESSION_MV
        )

        val impressionProperties = hashMapOf<String, Any>(
            KEY_EVENT_LABEL to EMPTY_STRING,
            TRACKER_ID to TRACKER_ID_MVC_IMPRESSION,
            USER_ID to userSession.userId,
            KEY_E_COMMERCE to constructMvcProps(EVENT_PROMO_VIEW, properties)
        )

        impressionProperties.putAll(generalProps)
        trackingQueue.putEETracking(impressionProperties)
    }

    override fun trackMvcClickEvent(properties: MvcTrackingProperties, isCta: Boolean) {
        val generalProps = createGeneralCouponEvent(
            event = EVENT_PROMO_CLICK,
            action = if (isCta.not()) CLICK_MVC else CLICK_MVC_CTA
        )

        val clickProperties = hashMapOf<String, Any>(
            KEY_EVENT_LABEL to if (isCta.not()) properties.shopId else "${properties.shopId} - ${properties.action}",
            TRACKER_ID to TRACKER_ID_MVC_CLICK,
            USER_ID to userSession.userId,
            KEY_E_COMMERCE to constructMvcProps(EVENT_PROMO_CLICK, listOf(properties))
        )

        clickProperties.putAll(generalProps)
        getTracker().sendEnhanceEcommerceEvent(clickProperties)
    }

    //region private methods
    private fun createGeneralEvent(
        eventName: String = EVENT_CLICK_DISCOVERY,
        eventAction: String,
        eventLabel: String = EMPTY_STRING,
        isPageSourceRequired: Boolean = true,
        shouldSendSourceAsDestination: Boolean = false
    ): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        map[KEY_EVENT] = eventName
        map[KEY_EVENT_CATEGORY] = eventDiscoveryCategory
        map[KEY_EVENT_ACTION] = eventAction
        map[KEY_EVENT_LABEL] = eventLabel
        if (isPageSourceRequired) {
            if (shouldSendSourceAsDestination) {
                map[PAGE_DESTINATION] = sourceIdentifier
            } else {
                map[PAGE_SOURCE] = sourceIdentifier
            }
        }

        return map
    }

    private fun createGeneralCouponEvent(
        event: String,
        action: String
    ): HashMap<String, Any> {
        return hashMapOf(
            KEY_EVENT to event,
            KEY_EVENT_CATEGORY to eventDiscoveryCategory,
            KEY_EVENT_ACTION to action,
            BUSINESS_UNIT to HOME_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            PAGE_PATH to removedDashPageIdentifier,
            PAGE_TYPE to pageType,
            PAGE_DESTINATION to sourceIdentifier
        )
    }

    private fun constructPromotionsProps(
        keyEvent: String,
        propsList: List<CouponTrackingProperties>
    ): Map<String, Map<String, ArrayList<Map<String, Any>>>> {
        val list = ArrayList<Map<String, Any>>()

        propsList.forEach { dataItem ->
            val itemName = if (dataItem.gtmItem.isNotEmpty()) {
                replacePlaceholder(dataItem)
            } else {
                EMPTY_STRING
            }

            list.add(
                mapOf(
                    KEY_CREATIVE to dataItem.creativeName,
                    KEY_POSITION to (dataItem.position + 1).toString(),
                    KEY_ID to "${dataItem.id} - ${dataItem.promoCode}",
                    KEY_NAME to itemName
                )
            )
        }

        return mapOf(
            keyEvent to mapOf(
                KEY_PROMOTIONS to list
            )
        )
    }

    private fun replacePlaceholder(props: CouponTrackingProperties): String {
        var replacedName = props.gtmItem.replace(
            "#POSITION",
            (props.position + 1).toString()
        )

        replacedName = replacedName.replace("#MEGA_TAB_VALUE", props.tabName)

        return replacedName
    }

    //endregion

    private fun constructMvcProps(
        keyEvent: String,
        propsList: List<MvcTrackingProperties>
    ): Map<String, Map<String, ArrayList<Map<String, Any>>>> {
        val list = ArrayList<Map<String, Any>>()

        propsList.forEach { dataItem ->
            val itemName = if (dataItem.gtmItem.isNotEmpty()) {
                replaceMvcPlaceholder(dataItem)
            } else {
                EMPTY_STRING
            }

            list.add(
                mapOf(
                    KEY_CREATIVE to dataItem.creativeName,
                    KEY_POSITION to (dataItem.position + 1).toString(),
                    KEY_CREATIVE_SLOT to (dataItem.position + 1).toString(),
                    KEY_ID to "${dataItem.compId} - ${dataItem.shopId}",
                    KEY_NAME to itemName
                )
            )
        }

        return mapOf(
            keyEvent to mapOf(
                KEY_PROMOTIONS to list
            )
        )
    }

    private fun replaceMvcPlaceholder(props: MvcTrackingProperties): String {
        var replacedName = props.gtmItem.replace(
            "#POSITION",
            (props.position + 1).toString()
        )

        replacedName = replacedName.replace("#MEGA_TAB_VALUE", props.tabName)

        return replacedName
    }

}
