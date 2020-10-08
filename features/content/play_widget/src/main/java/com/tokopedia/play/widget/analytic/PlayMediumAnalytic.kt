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
