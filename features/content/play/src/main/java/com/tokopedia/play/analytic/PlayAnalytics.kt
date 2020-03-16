package com.tokopedia.play.analytic

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel


/**
 * Created by mzennis on 2020-01-02.
 */

object PlayAnalytics {

    private const val KEY_TRACK_CLICK_BACK = "clickBack"
    private const val KEY_TRACK_ADD_TO_CART = "addToCart"
    private const val KEY_TRACK_CLICK_GROUP_CHAT = "clickGroupChat"
    private const val KEY_TRACK_VIEW_GROUP_CHAT = "viewGroupChat"

    private const val KEY_TRACK_CLICK = "click"
    private const val KEY_TRACK_GROUP_CHAT_ROOM = "groupchat room"

    fun sendScreen(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated("/group-chat-room/$channelId/${channelType.value}")
    }

    fun clickLeaveRoom(channelId: String, duration: Long, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_BACK,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "leave room",
                "$channelId - $duration - ${channelType.value}"
        )
    }

    fun clickShop(channelId: String, shopId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - shop",
                "$shopId - $channelId - ${channelType.value}"
        )
    }

    fun clickFollowShop(channelId: String, shopId: String, action: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK $action shop",
                "$channelId - $shopId - ${channelType.value}"
        )
    }

    fun clickWatchArea(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK watch area",
                "$channelId - ${channelType.value}"
        )
    }

    fun clickPinnedMessage(channelId: String, message: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on admin pinned message",
                "$channelId - $message - ${channelType.value}"
        )
    }

    fun clickLike(channelId: String, isLike: Boolean, channelType: PlayChannelType) {
        val action = if(isLike) "like" else "unlike"
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK $action",
                "$channelId - ${channelType.value}"
        )
    }

    fun errorState(channelId: String, errorMessage: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_VIEW_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "error state",
                "$channelId - $errorMessage - ${channelType.value}"
        )
    }

    fun clickQuickReply(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on quick reply component",
                channelId
        )
    }

    fun clickSendChat(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on button send",
                channelId
        )
    }

    fun clickWatchMode(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK watch mode option",
                "$channelId - ${channelType.value}"
        )
    }

    fun clickPlayVideo(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on play button video",
                "$channelId - ${channelType.value}"
        )
    }

    fun bufferVideo(bufferX: Int, bufferDuration: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_VIEW_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "buffer",
                "$bufferX - $bufferDuration"
        )
    }

    fun clickCartIcon(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK cart icon",
                "$channelId - ${channelType.value}"
        )
    }

    fun clickPinnedProduct(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK product pinned message",
                channelId
        )
    }

    fun impressionProductList(trackingQueue: TrackingQueue,
                              channelId: String,
                              listOfProducts: List<ProductLineUiModel>,
                              channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        "productView",
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "view product",
                        "$channelId - ${listOfProducts[0].id} - ${channelType.value} - product in bottom sheet"
                ),
                hashMapOf(

                )
        )
    }

    fun clickProduct(trackingQueue: TrackingQueue,
                     channelId: String,
                     product: ProductLineUiModel,
                     channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        "productClick",
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_TRACK_CLICK,
                        "$channelId - ${product.id} - ${channelType.value} - product in bottom sheet"
                ),
                hashMapOf(

                )
        )
    }

    fun scrollMerchantVoucher(channelId: String, lastPositionViewed: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "scroll merchant voucher",
                "$channelId - $lastPositionViewed"
        )
    }

    fun clickBeliButtonProductWithVariant(channelId: String, productId: Int, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK buy in bottom sheet",
                "$channelId - $productId - ${channelType.value}"
        )
    }

    fun clickBeliButtonProductWithNoVariant(trackingQueue: TrackingQueue,
                                          channelId: String,
                                          productLineUiModel: ProductLineUiModel,
                                          cartId: String,
                                          channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK buy in bottom sheet with variant",
                        ""
                ),
                hashMapOf(

                )
        )
    }

    fun clickAtcButtonProductWithVariant(channelId: String, productId: Int, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK atc in bottom sheet",
                "$channelId - $productId - ${channelType.value}"
        )
    }

    fun clickAtcButtonProductWithNoVariant(trackingQueue: TrackingQueue,
                                          channelId: String,
                                          productLineUiModel: ProductLineUiModel,
                                          cartId: String,
                                          channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK atc in bottom sheet with variant",
                        ""
                ),
                hashMapOf(

                )
        )
    }

    fun clickAtcButtonInVariant(trackingQueue: TrackingQueue,
                                channelId: String,
                                productLineUiModel: ProductLineUiModel,
                                cartId: String,
                                channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK atc in variant page",
                        "$channelId - ${productLineUiModel.id} - ${channelType.value}"
                ),
                hashMapOf(

                )
        )
    }

    fun clickBuyButtonInVariant(trackingQueue: TrackingQueue,
                                channelId: String,
                                productLineUiModel: ProductLineUiModel,
                                cartId: String,
                                channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK beli in variant page",
                        "$channelId - ${productLineUiModel.id} - ${channelType.value}"
                ),
                hashMapOf(

                )
        )
    }

    fun clickSeeToasterAfterAtc(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK lihat in message ticket",
                "$channelId - ${channelType.value}"
        )
    }
}
