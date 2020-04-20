package com.tokopedia.play.analytic

import com.tokopedia.play.view.type.*
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

    fun clickPinnedMessage(channelId: String, message: String, appLink: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on admin pinned message",
                "$channelId - $message - $appLink - ${channelType.value}"
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
        if (listOfProducts.isNotEmpty())
            trackingQueue.putEETracking(
                    EventModel(
                            "productView",
                            KEY_TRACK_GROUP_CHAT_ROOM,
                            "view product",
                            "$channelId - ${listOfProducts[0].id} - ${channelType.value} - product in bottom sheet"
                    ),
                    hashMapOf<String, Any>(
                            "ecommerce" to hashMapOf(
                                    "currencyCode" to "IDR",
                                    "impressions" to convertProductsToListOfObject(listOfProducts)
                            )
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
                hashMapOf<String, Any>(
                        "ecommerce" to hashMapOf(
                                "click" to hashMapOf(
                                        "actionField" to hashMapOf( "list" to "/groupchat - bottom sheet" ),
                                        "products" to convertProductsToListOfObject(listOf(product))
                                )
                        )
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

    fun clickActionProductWithVariant(channelId: String, productId: String, channelType: PlayChannelType, productAction: ProductAction) {
        when(productAction) {
            ProductAction.AddToCart -> clickAtcButtonProductWithVariant(channelId, productId, channelType)
            ProductAction.Buy -> clickBeliButtonProductWithVariant(channelId, productId, channelType)
        }
    }

    fun clickProductAction(trackingQueue: TrackingQueue,
                           channelId: String,
                           productLineUiModel: ProductLineUiModel,
                           cartId: String,
                           channelType: PlayChannelType,
                           productAction: ProductAction,
                           bottomInsetsType: BottomInsetsType) {
        when(productAction) {
            ProductAction.AddToCart ->
                when (bottomInsetsType) {
                    BottomInsetsType.VariantSheet -> clickAtcButtonInVariant(trackingQueue, channelId, productLineUiModel, cartId, channelType)
                    else -> clickAtcButtonProductWithNoVariant(trackingQueue, channelId, productLineUiModel, cartId, channelType)
                }
            ProductAction.Buy -> {
                when (bottomInsetsType) {
                    BottomInsetsType.VariantSheet -> clickBeliButtonInVariant(trackingQueue, channelId, productLineUiModel, cartId, channelType)
                    else -> clickBeliButtonProductWithNoVariant(trackingQueue, channelId, productLineUiModel, cartId, channelType)
                }
            }
        }
    }

    fun clickSeeToasterAfterAtc(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK lihat in message ticker",
                "$channelId - ${channelType.value}"
        )
    }

    fun trackVideoBuffering(
            bufferCount: Int,
            bufferDurationInSecond: Int
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_VIEW_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "buffer",
                "$bufferCount - $bufferDurationInSecond"
        )
    }

    private fun convertProductsToListOfObject(listOfProducts: List<ProductLineUiModel>): MutableList<HashMap<String, Any>> {
        val products = mutableListOf<HashMap<String, Any>>()
        listOfProducts.forEachIndexed { index, product ->
            products.add(convertProductToHashMapWithList(product, index))
        }
        return products
    }

    private fun convertProductToHashMapWithList(product: ProductLineUiModel, position: Int): HashMap<String, Any> {
        return hashMapOf(
                "name" to product.title,
                "id" to product.id,
                "price" to when(product.price) {
                    is DiscountedPrice -> product.price.discountedPriceNumber
                    is OriginalPrice -> product.price.priceNumber
                },
                "brand" to "",
                "category" to "",
                "variant" to "",
                "list" to "/groupchat - bottom sheet",
                "position" to position
        )
    }

    private fun clickBeliButtonProductWithVariant(channelId: String, productId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK buy in bottom sheet with varian",
                "$channelId - $productId - ${channelType.value}"
        )
    }

    private fun clickAtcButtonProductWithVariant(channelId: String, productId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK atc in bottom sheet with varian",
                "$channelId - $productId - ${channelType.value}"
        )
    }

    private fun clickBeliButtonProductWithNoVariant(trackingQueue: TrackingQueue,
                                                    channelId: String,
                                                    product: ProductLineUiModel,
                                                    cartId: String,
                                                    channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK buy in bottom sheet",
                        "$channelId - ${product.id} - ${channelType.value}"
                ),
                hashMapOf<String, Any>(
                        "ecommerce" to hashMapOf(
                                "currencyCode" to "IDR",
                                "add" to hashMapOf(
                                        "products" to convertProductToHashMap(product, cartId, "bottom sheet")
                                )
                        )
                )
        )
    }

    private fun clickAtcButtonProductWithNoVariant(trackingQueue: TrackingQueue,
                                                   channelId: String,
                                                   product: ProductLineUiModel,
                                                   cartId: String,
                                                   channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK atc in bottom sheet",
                        "$channelId - ${product.id} - ${channelType.value}"
                ),
                hashMapOf<String, Any>(
                        "ecommerce" to hashMapOf(
                                "currencyCode" to "IDR",
                                "add" to hashMapOf(
                                        "products" to convertProductToHashMap(product, cartId, "bottom sheet")
                                )
                        )
                )
        )
    }

    private fun clickAtcButtonInVariant(trackingQueue: TrackingQueue,
                                        channelId: String,
                                        product: ProductLineUiModel,
                                        cartId: String,
                                        channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK atc in varian page",
                        "$channelId - ${product.id} - ${channelType.value}"
                ),
                hashMapOf<String, Any>(
                        "ecommerce" to hashMapOf(
                                "currencyCode" to "IDR",
                                "add" to hashMapOf(
                                        "products" to convertProductToHashMap(product, cartId, "varian page")
                                )
                        )
                )
        )
    }

    private fun clickBeliButtonInVariant(trackingQueue: TrackingQueue,
                                         channelId: String,
                                         product: ProductLineUiModel,
                                         cartId: String,
                                         channelType: PlayChannelType) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK beli in varian page",
                        "$channelId - ${product.id} - ${channelType.value}"
                ),
                hashMapOf<String, Any>(
                        "ecommerce" to hashMapOf(
                                "currencyCode" to "IDR",
                                "add" to hashMapOf(
                                        "products" to convertProductToHashMap(product, cartId, "varian page")
                                )
                        )
                )
        )
    }

    private fun convertProductToHashMap(product: ProductLineUiModel, cartId: String, page: String): MutableList<HashMap<String, Any>> {
        return mutableListOf(
                hashMapOf(
                        "name" to product.title,
                        "id" to product.id,
                        "price" to when(product.price) {
                            is DiscountedPrice -> product.price.discountedPriceNumber
                            is OriginalPrice -> product.price.priceNumber
                        },
                        "brand" to "",
                        "category" to "",
                        "variant" to "",
                        "quantity" to product.minQty,
                        "dimension79" to product.shopId,
                        "dimension81" to "", // shop type
                        "dimension80" to "", // shop name
                        "dimension82" to "", // category child id
                        "dimension45" to cartId,
                        "dimension40" to "/groupchat - $page"
                )
        )
    }
}
