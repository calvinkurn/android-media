package com.tokopedia.play.widget.analytic

import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumOverlayUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface


/**
 * Created by mzennis on 08/10/20.
 */
class PlayMediumAnalytic(
        private val userSession: UserSessionInterface,
        private val trackingQueue: TrackingQueue
) {

    fun impressionOverlayCardShopPage(
            overlayUiModel: PlayWidgetMediumOverlayUiModel,
            shopId: String,
            cardPosition: String,
            widgetId: String,
            widgetPosition: String
    ){
        trackingQueue.putEETracking(
                hashMapOf(
                        KEY_EVENT to KEY_TRACK_PROMO_VIEW,
                        KEY_EVENT_CATEGORY to "$KEY_TRACK_SHOP_PAGE - $KEY_TRACK_BUYER",
                        KEY_EVENT_ACTION to "impression on play sgc banner",
                        KEY_EVENT_LABEL to "view on banner play - $shopId - $cardPosition",
                        KEY_USER_ID to userSession.userId,
                        KEY_TRACK_E_COMMERCE to mapOf(
                                KEY_TRACK_PROMO_VIEW to mapOf(
                                        KEY_TRACK_PROMOTION to listOf(
                                                mapOf(
                                                        "id" to widgetId,
                                                        "name" to "/$KEY_TRACK_PROMOTION_NAME - p$widgetPosition",
                                                        "creative" to overlayUiModel.imageUrl,
                                                        "position" to widgetPosition
                                                )
                                        )
                                )
                        )
                )
        )
    }

    fun clickOverlayCardShopPage(
            overlayUiModel: PlayWidgetMediumOverlayUiModel,
            shopId: String,
            cardPosition: String,
            widgetId: String,
            widgetPosition: String
    ){
        trackingQueue.putEETracking(
                hashMapOf(
                        KEY_EVENT to KEY_TRACK_PROMO_CLICK,
                        KEY_EVENT_CATEGORY to "$KEY_TRACK_SHOP_PAGE - $KEY_TRACK_BUYER",
                        KEY_EVENT_ACTION to KEY_TRACK_CLICK,
                        KEY_EVENT_LABEL to "$KEY_TRACK_CLICK on banner play - $shopId - $cardPosition",
                        KEY_USER_ID to userSession.userId,
                        KEY_TRACK_E_COMMERCE to mapOf(
                                KEY_TRACK_PROMO_CLICK to mapOf(
                                        KEY_TRACK_PROMOTION to listOf(
                                                mapOf(
                                                        "id" to widgetId,
                                                        "name" to "/$KEY_TRACK_PROMOTION_NAME - p$widgetPosition",
                                                        "creative" to overlayUiModel.imageUrl,
                                                        "position" to widgetPosition
                                                )
                                        )
                                )
                        )
                )
        )
    }

    fun impressionChannelCardShopPage(
            channelUiModel: PlayWidgetMediumChannelUiModel,
            cardPosition: String,
            autoPlay: String,
            widgetId: String,
            widgetPosition: Int

    ) {
        trackingQueue.putEETracking(
                hashMapOf(
                        KEY_EVENT to KEY_TRACK_PROMO_VIEW,
                        KEY_EVENT_CATEGORY to "$KEY_TRACK_SHOP_PAGE - $KEY_TRACK_BUYER",
                        KEY_EVENT_ACTION to "impression on play sgc channel",
                        KEY_EVENT_LABEL to "view channel - ${channelUiModel.partner.id} - ${channelUiModel.channelId} - $cardPosition - $widgetPosition - $autoPlay",
                        KEY_USER_ID to userSession.userId,
                        KEY_TRACK_E_COMMERCE to mapOf(
                                KEY_TRACK_PROMO_VIEW to mapOf(
                                        KEY_TRACK_PROMOTION to listOf(
                                                mapOf(
                                                        "id" to widgetId,
                                                        "name" to "/$KEY_TRACK_PROMOTION_NAME - p$cardPosition",
                                                        "creative" to channelUiModel.video.coverUrl,
                                                        "position" to cardPosition
                                                )
                                        )
                                )
                        )
                )
        )
    }

    fun clickChannelCardShopPage(
            channelUiModel: PlayWidgetMediumChannelUiModel,
            cardPosition: String,
            autoPlay: String,
            widgetId: String,
            widgetPosition: Int

    ) {
        trackingQueue.putEETracking(
                hashMapOf(
                        KEY_EVENT to KEY_TRACK_PROMO_CLICK,
                        KEY_EVENT_CATEGORY to "$KEY_TRACK_SHOP_PAGE - $KEY_TRACK_BUYER",
                        KEY_EVENT_ACTION to KEY_TRACK_CLICK,
                        KEY_EVENT_LABEL to "$KEY_TRACK_CLICK channel - ${channelUiModel.partner.id} - ${channelUiModel.channelId} - $cardPosition - $widgetPosition - $autoPlay",
                        KEY_USER_ID to userSession.userId,
                        KEY_TRACK_E_COMMERCE to mapOf(
                                KEY_TRACK_PROMO_CLICK to mapOf(
                                        KEY_TRACK_PROMOTION to listOf(
                                                mapOf(
                                                        "id" to widgetId,
                                                        "name" to "/$KEY_TRACK_PROMOTION_NAME - p$cardPosition",
                                                        "creative" to channelUiModel.video.coverUrl,
                                                        "position" to cardPosition
                                                )
                                        )
                                )
                        )
                )
        )
    }

    fun clickBannerCardShopPage(shopId: String) {
        trackingQueue.putEETracking(
                hashMapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_SHOP_PAGE,
                        KEY_EVENT_CATEGORY to "$KEY_TRACK_SHOP_PAGE - $KEY_TRACK_BUYER",
                        KEY_EVENT_ACTION to "$KEY_TRACK_CLICK other content",
                        KEY_EVENT_LABEL to shopId,
                        KEY_USER_ID to userSession.userId
                )
        )
    }

//    fun impressionLeftPlayBanner(
//            shopId: String,
//            positionChannel: String,
//            userId: String,
//            bannerId: String,
//            creativeName: String,
//            position: String
//    ){
//        val eventMap = mapOf(
//                EVENT to PROMO_VIEW,
//                EVENT_CATEGORY to SHOP_PAGE_BUYER,
//                EVENT_ACTION to IMPRESSION_SGC_BANNER,
//                EVENT_LABEL to "view on banner play - $shopId - $position",
//                USER_ID to userId,
//                ECOMMERCE to mapOf(
//                        PROMO_VIEW to mapOf(
//                                PROMOTIONS to listOf(
//                                        mapOf(
//                                                ID to bannerId,
//                                                NAME to PLAY_LEFT_BANNER_NAME.format(positionChannel),
//                                                CREATIVE to creativeName,
//                                                POSITION to positionChannel
//                                        )
//                                )
//                        )
//                )
//        ) as HashMap<String, Any>
//        trackingQueue.putEETracking(eventMap)
//    }

//    fun clickLeftPlayBanner(
//            shopId: String,
//            positionChannel: String,
//            userId: String,
//            bannerId: String,
//            creativeName: String,
//            position: String
//    ){
//        val eventMap = mapOf(
//                EVENT to PROMO_CLICK,
//                EVENT_CATEGORY to SHOP_PAGE_BUYER,
//                EVENT_ACTION to CLICK,
//                EVENT_LABEL to "click on banner play - $shopId - $position",
//                USER_ID to userId,
//                ECOMMERCE to mapOf(
//                        PROMO_CLICK to mapOf(
//                                PROMOTIONS to listOf(
//                                        mapOf(
//                                                ID to bannerId,
//                                                NAME to PLAY_LEFT_BANNER_NAME.format(positionChannel),
//                                                CREATIVE to creativeName,
//                                                POSITION to positionChannel
//                                        )
//                                )
//                        )
//                )
//        ) as HashMap<String, Any>
//        trackingQueue.putEETracking(eventMap)
//    }
//
//    fun impressionPlayBanner(
//            shopId: String,
//            channelId: String,
//            positionWidget: Int,
//            positionChannel: String,
//            autoPlay: String,
//            userId: String,
//            bannerId: String,
//            creativeName: String
//
//    ){
//        val eventMap = mapOf(
//                EVENT to PROMO_VIEW,
//                EVENT_CATEGORY to SHOP_PAGE_BUYER,
//                EVENT_ACTION to IMPRESSION_SGC_CHANNEL,
//                EVENT_LABEL to "view channel - $shopId - $channelId - $positionChannel - $positionWidget - $autoPlay",
//                USER_ID to userId,
//                ECOMMERCE to mapOf(
//                        PROMO_VIEW to mapOf(
//                                PROMOTIONS to listOf(
//                                        mapOf(
//                                                ID to bannerId,
//                                                NAME to PLAY_SGC_NAME.format(positionChannel),
//                                                CREATIVE to creativeName,
//                                                POSITION to positionChannel
//                                        )
//                                )
//                        )
//                )
//        ) as HashMap<String, Any>
//        trackingQueue.putEETracking(eventMap)
//    }
//
//    fun clickPlayBanner(
//            shopId: String,
//            channelId: String,
//            positionWidget: Int,
//            positionChannel: String,
//            autoPlay: String,
//            userId: String,
//            bannerId: String,
//            creativeName: String
//
//    ){
//        val eventMap = mapOf(
//                EVENT to PROMO_CLICK,
//                EVENT_CATEGORY to SHOP_PAGE_BUYER,
//                EVENT_ACTION to CLICK,
//                EVENT_LABEL to "click channel - $shopId - $channelId - $positionChannel - $positionWidget - $autoPlay",
//                USER_ID to userId,
//                ECOMMERCE to mapOf(
//                        PROMO_CLICK to mapOf(
//                                PROMOTIONS to listOf(
//                                        mapOf(
//                                                ID to bannerId,
//                                                NAME to PLAY_SGC_NAME.format(positionChannel),
//                                                CREATIVE to creativeName,
//                                                POSITION to positionChannel
//                                        )
//                                )
//                        )
//                )
//        ) as HashMap<String, Any>
//        sendDataLayerEvent(eventMap)
//    }
//
//    fun clickSeeMorePlayCarouselBanner(shopId: String, userId: String, widgetPosition: Int) {
//        val eventMap = mapOf(
//                EVENT to CLICK_SHOP_PAGE,
//                EVENT_CATEGORY to SHOP_PAGE_BUYER,
//                EVENT_ACTION to CLICK_OTHER_CONTENT,
//                EVENT_LABEL to "$shopId - $widgetPosition",
//                USER_ID to userId,
//                BUSINESS_UNIT to ADS_SOLUTION,
//                CURRENT_SITE to TOKOPEDIA_MARKETPLACE
//        )
//        sendDataLayerEvent(eventMap)
//    }
//
//    fun clickSeeMorePlayCarousel(shopId: String, userId: String, widgetPosition: Int) {
//        val eventMap = mapOf(
//                EVENT to CLICK_SHOP_PAGE,
//                EVENT_CATEGORY to SHOP_PAGE_BUYER,
//                EVENT_ACTION to CLICK_VIEW_ALL_PLAY,
//                EVENT_LABEL to "$shopId - Tokopedia Play - $widgetPosition",
//                USER_ID to userId,
//                BUSINESS_UNIT to ADS_SOLUTION,
//                CURRENT_SITE to TOKOPEDIA_MARKETPLACE
//        )
//        sendDataLayerEvent(eventMap)
//    }
//
//    fun clickRemindMePlayCarousel(channelId: String, userId: String, isRemoveRemindMe: Boolean, widgetPosition: Int, position: Int) {
//        val eventMap = mapOf(
//                EVENT to CLICK_SHOP_PAGE,
//                EVENT_CATEGORY to SHOP_PAGE_BUYER,
//                EVENT_ACTION to if(isRemoveRemindMe) CLICK_REMOVE_REMIND_ME_PLAY else CLICK_REMIND_ME_PLAY,
//                EVENT_LABEL to "$channelId - $position - $widgetPosition",
//                USER_ID to userId,
//                BUSINESS_UNIT to ADS_SOLUTION,
//                CURRENT_SITE to TOKOPEDIA_MARKETPLACE
//        )
//        sendDataLayerEvent(eventMap)
//    }

    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_USER_ID = "userId"
        private const val KEY_CHANNEL_ID = "channelId"

        private const val KEY_TRACK_E_COMMERCE = "ecommerce"
        private const val KEY_TRACK_PROMOTION = "promotions"

        private const val KEY_TRACK_PROMO_VIEW = "promoView"
        private const val KEY_TRACK_PROMO_CLICK = "promoClick"

        private const val KEY_TRACK_SHOP_PAGE = "shop page"
        private const val KEY_TRACK_HOME_PAGE = "homepage-cmp"
        private const val KEY_TRACK_BUYER = "buyer"

        private const val KEY_TRACK_CLICK_SHOP_PAGE = "clickShopPage"
        private const val KEY_TRACK_PROMOTION_NAME = "shoppage play inside banner"

        private const val KEY_TRACK_CLICK = "click"
    }
}
