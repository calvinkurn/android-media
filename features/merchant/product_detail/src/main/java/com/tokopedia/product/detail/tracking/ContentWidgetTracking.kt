package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object ContentWidgetTracking {

    private const val ACTION_IMPRESSION_CHANNEL_CARD = "impression - play widget video card"
    private const val ACTION_CLICK_CHANNEL_CARD = "click - play widget video card"
    private const val ACTION_CLICK_BANNER_CARD = "click - cek konten lainnya on play widget"
    private const val ACTION_CLICK_VIEW_ALL = "click - lihat semua on play widget"
    private const val ACTION_CLICK_TOGGLE_REMINDER = "click - reminder on play widget video card"

    fun impressChannelCard(
        trackingQueue: TrackingQueue,
        data: ContentWidgetTracker
    ) {
        val componentName = data.componentName
        val componentType = data.componentType
        val componentPosition = data.componentPosition

        val mapEvent = hashMapOf(
            "event" to ProductTrackingConstant.Tracking.PROMO_VIEW,
            "eventAction" to ACTION_IMPRESSION_CHANNEL_CARD,
            "eventCategory" to ProductTrackingConstant.Category.PDP,
            "eventLabel" to "shop_id:${data.channelShopId};",
            "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            "component" to "'comp:$componentName;temp:$componentType;elem:$ACTION_IMPRESSION_CHANNEL_CARD;cpos:$componentPosition;",
            "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
            "layout" to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
            "productId" to data.productId,
            "ecommerce" to mapOf(
                "promoView" to mapOf(
                    "promotions" to listOf(
                        mapOf(
                            "creative_name" to componentName,
                            "creative_slot" to componentPosition,
                            "item_id" to data.channelId,
                            "item_name" to data.channelTitle
                        )
                    )
                )
            ),
            "shopId" to "${data.shopId};",
            "shopType" to data.shopType,
            "userId" to "${data.userId};"
        )
        trackingQueue.putEETracking(mapEvent)
    }

    fun clickChannelCard(data: ContentWidgetTracker) {
        val componentName = data.componentName
        val componentPosition = data.componentPosition
        val componentType = data.componentType

        val mapEvent = hashMapOf(
            "event" to ProductTrackingConstant.Tracking.SELECT_CONTENT,
            "eventAction" to ACTION_CLICK_CHANNEL_CARD,
            "eventCategory" to ProductTrackingConstant.Category.PDP,
            "eventLabel" to "shop_id:${data.channelShopId};channel_id:${data.channelId};",
            "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            "component" to "'comp:$componentName;temp:$componentType;elem:$ACTION_CLICK_CHANNEL_CARD;cpos:$componentPosition;",
            "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
            "layout" to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
            "productId" to data.productId,
            "ecommerce" to mapOf(
                "promoView" to mapOf(
                    "promotions" to listOf(
                        mapOf(
                            "creative_name" to componentName,
                            "creative_slot" to componentPosition,
                            "item_id" to data.channelId,
                            "item_name" to data.channelTitle
                        )
                    )
                )
            ),
            "shopId" to "${data.shopId};",
            "shopType" to data.shopType,
            "userId" to "${data.userId};"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun clickBannerCard(data: ContentWidgetTracker) {
        val mapEvent = hashMapOf<String, Any>(
            "event" to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            "eventAction" to ACTION_CLICK_BANNER_CARD,
            "eventCategory" to ProductTrackingConstant.Category.PDP,
            "eventLabel" to "shop_id:${data.channelShopId};",
            "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            "component" to "'comp:${data.componentName};temp:${data.componentType};elem:$ACTION_CLICK_CHANNEL_CARD;cpos:${data.componentPosition};",
            "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
            "layout" to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
            "productId" to data.productId,
            "shopId" to "${data.shopId};",
            "shopType" to data.shopType,
            "userId" to "${data.userId};"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun clickViewAll(data: ContentWidgetTracker) {
        val mapEvent = hashMapOf<String, Any>(
            "event" to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            "eventAction" to ACTION_CLICK_VIEW_ALL,
            "eventCategory" to ProductTrackingConstant.Category.PDP,
            "eventLabel" to "shop_id:;",
            "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            "component" to "'comp:${data.componentName};temp:${data.componentType};elem:$ACTION_CLICK_CHANNEL_CARD;cpos:${data.componentPosition};",
            "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
            "layout" to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
            "productId" to data.productId,
            "shopId" to "${data.shopId};",
            "shopType" to data.shopType,
            "userId" to "${data.userId};"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun clickToggleReminderChannel(data: ContentWidgetTracker) {
        val mapEvent = hashMapOf<String, Any>(
            "event" to ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
            "eventAction" to ACTION_CLICK_TOGGLE_REMINDER,
            "eventCategory" to ProductTrackingConstant.Category.PDP,
            "eventLabel" to "shop_id:${data.channelShopId};channel_id:${data.channelId};is_active:${data.isRemindMe};",
            "businessUnit" to ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP,
            "component" to "'comp:${data.componentName};temp:${data.componentType};elem:$ACTION_CLICK_CHANNEL_CARD;cpos:${data.componentPosition};",
            "currentSite" to ProductTrackingConstant.Tracking.CURRENT_SITE,
            "layout" to "layout:${data.layoutName};catName:${data.categoryName};catId:${data.categoryId};",
            "productId" to data.productId,
            "shopId" to "${data.shopId};",
            "shopType" to data.shopType,
            "userId" to "${data.userId};"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun fintechActivationBottomSheetImpression(
        userStatus: String,
        gatewayCode: String,
        userId:String,
        ctaWording:String
    )
    {
        val mapEvent = hashMapOf<String, Any>(
            "event" to ProductTrackingConstant.Fintech.EVENT_VIEW_BOTTOMSHEET,
            "eventAction" to ProductTrackingConstant.Fintech.ACTION_VIEW_BOTTOMSHEET,
            "eventCategory" to ProductTrackingConstant.Fintech.EVENT_FINTECH_BOTTOMSHEET_CATEGORY,
            "eventLabel" to "$userStatus - $gatewayCode - $ctaWording - $userId",
            "businessUnit" to ProductTrackingConstant.Fintech.FINTECH_BOTTOMSHEET_BUSINESS,
            "currentSite" to ProductTrackingConstant.Fintech.FINTECH_CURRENT_SITE
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun fintechActivationClickBottomSheet(
        userStatus: String,
        gatewayCode: String,
        userId:String,
        redirectionUrl: String,
        ctaWording:String
    )
    {
        val mapEvent = hashMapOf<String, Any>(
            "event" to ProductTrackingConstant.Fintech.EVENT_CLICK_BOTTOMSHEET,
            "eventAction" to ProductTrackingConstant.Fintech.ACTION_CLICK_BOTTOMSHEET,
            "eventCategory" to ProductTrackingConstant.Fintech.EVENT_FINTECH_BOTTOMSHEET_CATEGORY,
            "eventLabel" to "$userStatus - $gatewayCode - $redirectionUrl - $ctaWording - $userId",
            "businessUnit" to ProductTrackingConstant.Fintech.FINTECH_BOTTOMSHEET_BUSINESS,
            "currentSite" to ProductTrackingConstant.Fintech.FINTECH_CURRENT_SITE
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

}