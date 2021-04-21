package com.tokopedia.play.analytic

import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface


/**
 * Created by mzennis on 15/02/21.
 */
class PlayAnalytic(
        private val userSession: UserSessionInterface,
        private val trackingQueue: TrackingQueue,
) {
    val channelId: String
        get() = mChannelId

    private val userId: String 
        get() = userSession.userId
    
    private var mChannelId: String = ""
    private var mChannelType: PlayChannelType = PlayChannelType.Unknown
    private val mSessionId: String = generateSwipeSession()
    private var mSourceType = ""

    fun sendScreen(channelId: String, channelType: PlayChannelType, sourceType: String = "") {
        this.mChannelId = channelId
        this.mChannelType = channelType
        if (sourceType.isNotEmpty() && sourceType.isNotBlank()) this.mSourceType = sourceType
        TrackApp.getInstance().gtm.sendScreenAuthenticated("/${KEY_TRACK_SCREEN_NAME}/$channelId/${channelType.value}")
    }

    /**
     * User swipe room
     */
    fun swipeRoom() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "swipe channel",
                        KEY_EVENT_LABEL to "$mSessionId - $mChannelId - ${mChannelType.value} - $mSourceType",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    fun clickLeaveRoom(duration: Long) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_BACK,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "leave room",
                "$mChannelId - $duration - ${mChannelType.value}"
        )
    }

    fun clickShop(shopId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - shop",
                "$shopId - $mChannelId - ${mChannelType.value}"
        )
    }

    fun clickFollowShop(shopId: String, action: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK $action shop",
                "$mChannelId - $shopId - ${mChannelType.value}"
        )
    }

    fun clickWatchArea(screenOrientation: ScreenOrientation) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "$KEY_TRACK_CLICK watch area",
                        KEY_EVENT_LABEL to "$mChannelId - ${mChannelType.value} - ${screenOrientation.value}",
                        KEY_SCREEN_NAME to "/${KEY_TRACK_SCREEN_NAME}/$mChannelId/${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_CLIENT_ID to TrackApp.getInstance().gtm.cachedClientIDString,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    fun clickPinnedMessage(message: String, appLink: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on admin pinned message",
                "$mChannelId - $message - $appLink - ${mChannelType.value}"
        )
    }

    fun clickLike(isLike: Boolean) {
        val action = if(isLike) "like" else "unlike"
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK $action",
                "$mChannelId - ${mChannelType.value}"
        )
    }

    fun trackVideoError(message: String) {
        errorState("$ERR_STATE_VIDEO: $message")
    }

    fun trackSocketError(message: String) {
        errorState("$ERR_STATE_SOCKET: $message")
    }

    fun trackGlobalError(message: String) {
        errorState("$ERR_STATE_GLOBAL: $message")
    }

    private fun errorState(errorMessage: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_VIEW_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "error state",
                "$mChannelId - $errorMessage - ${mChannelType.value}"
        )
    }

    fun clickQuickReply() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on quick reply component",
                mChannelId
        )
    }

    fun clickSendChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on button send",
                mChannelId
        )
    }

    fun clickWatchMode() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK watch mode option",
                "$mChannelId - ${mChannelType.value}"
        )
    }

    fun clickPlayVideo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK on play button video",
                "$mChannelId - ${mChannelType.value}"
        )
    }

    fun clickCartIcon() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK cart icon",
                "$mChannelId - ${mChannelType.value}"
        )
    }

    fun impressionProductList(listOfProducts: List<PlayProductUiModel.Product>) {
        if (listOfProducts.isNotEmpty()) {
            trackingQueue.putEETracking(
                    EventModel(
                            "productView",
                            KEY_TRACK_GROUP_CHAT_ROOM,
                            "view product",
                            "$mChannelId - ${listOfProducts[0].id} - ${mChannelType.value} - product in bottom sheet"
                    ),
                    hashMapOf<String, Any>(
                            "ecommerce" to hashMapOf(
                                    "currencyCode" to "IDR",
                                    "impressions" to convertProductsToListOfObject(listOfProducts, "bottom sheet")
                            )
                    )
            )
        }
    }

    fun clickProduct(product: PlayProductUiModel.Product,
                     position: Int) {
        trackingQueue.putEETracking(
                EventModel(
                        "productClick",
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_TRACK_CLICK,
                        "$mChannelId - ${product.id} - ${mChannelType.value} - product in bottom sheet"
                ),
                hashMapOf<String, Any>(
                        "ecommerce" to hashMapOf(
                                "click" to hashMapOf(
                                        "actionField" to hashMapOf( "list" to "/groupchat - bottom sheet" ),
                                        "products" to  listOf(convertProductToHashMapWithList(product, position, "bottom sheet"))
                                )
                        )
                )
        )
    }

    fun scrollMerchantVoucher(lastPositionViewed: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "scroll merchant voucher",
                "$mChannelId - $lastPositionViewed"
        )
    }

    fun clickActionProductWithVariant(productId: String, productAction: ProductAction) {
        when(productAction) {
            ProductAction.AddToCart -> clickAtcButtonProductWithVariant(productId)
            ProductAction.Buy -> clickBeliButtonProductWithVariant(productId)
        }
    }

    fun clickProductAction(product: PlayProductUiModel.Product,
                           cartId: String,
                           productAction: ProductAction,
                           bottomInsetsType: BottomInsetsType) {
        when(productAction) {
            ProductAction.AddToCart ->
                when (bottomInsetsType) {
                    BottomInsetsType.VariantSheet -> clickAtcButtonInVariant(trackingQueue, product, cartId)
                    else -> clickAtcButtonProductWithNoVariant(trackingQueue, product, cartId)
                }
            ProductAction.Buy -> {
                when (bottomInsetsType) {
                    BottomInsetsType.VariantSheet -> clickBeliButtonInVariant(trackingQueue, product, cartId)
                    else -> clickBeliButtonProductWithNoVariant(trackingQueue, product, cartId)
                }
            }
        }
    }

    fun clickSeeToasterAfterAtc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK lihat in message ticker",
                "$mChannelId - ${mChannelType.value}"
        )
    }

    fun trackVideoBuffering(
            bufferCount: Int,
            bufferDurationInSecond: Int
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_VIEW_GROUP_CHAT,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "buffer",
                        KEY_EVENT_LABEL to "$bufferCount - $bufferDurationInSecond - $mChannelId - ${mChannelType.value}",
                        KEY_SCREEN_NAME to "/${KEY_TRACK_SCREEN_NAME}/$mChannelId/${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_CLIENT_ID to TrackApp.getInstance().gtm.cachedClientIDString,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    /**
     * User click "full screen" CTA from portrait to landscape video (not triggered when user click from landscape to portrait)
     */
    fun clickCtaFullScreenFromPortraitToLandscape() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "$KEY_TRACK_CLICK full screen to landscape",
                        KEY_EVENT_LABEL to "$mChannelId - ${mChannelType.value}",
                        KEY_SCREEN_NAME to "/${KEY_TRACK_SCREEN_NAME}/$mChannelId/${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_CLIENT_ID to TrackApp.getInstance().gtm.cachedClientIDString,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    /**
     * User tilt/rotate their phone to see in full screen
     */
    fun userTiltFromPortraitToLandscape() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "rotate phone to full screen",
                        KEY_EVENT_LABEL to "$mChannelId - ${mChannelType.value}",
                        KEY_SCREEN_NAME to "/${KEY_TRACK_SCREEN_NAME}/$mChannelId/${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_CLIENT_ID to TrackApp.getInstance().gtm.cachedClientIDString,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    /**
     * User click copy link
     */
    fun clickCopyLink() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "click on button share link",
                        KEY_EVENT_LABEL to "$mChannelId - ${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    fun impressionHighlightedVoucher(voucher: MerchantVoucherUiModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_VIEW_GROUP_CHAT_IRIS,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "impression on merchant voucher",
                        KEY_EVENT_LABEL to "$mChannelId - ${voucher.id} - ${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    fun clickHighlightedVoucher(voucher: MerchantVoucherUiModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "click on merchant voucher",
                        KEY_EVENT_LABEL to "$mChannelId - ${voucher.id} - ${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    fun impressionFeaturedProduct(featuredProduct: PlayProductUiModel.Product, position: Int) {
        val finalPosition = position + 1
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                mapOf(
                        KEY_EVENT to "productView",
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "view on featured product",
                        KEY_EVENT_LABEL to "$mChannelId - ${featuredProduct.id} - ${mChannelType.value} - featured product tagging",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        "ecommerce" to hashMapOf(
                                "currencyCode" to "IDR",
                                "impressions" to mutableListOf(
                                        convertProductToHashMapWithList(featuredProduct, finalPosition,"featured product")
                                )
                        )
                )
        )
    }

    fun clickFeaturedProduct(featuredProduct: PlayProductUiModel.Product, position: Int) {
        trackingQueue.putEETracking(
                EventModel(
                        "productClick",
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_TRACK_CLICK,
                        "$mChannelId - ${featuredProduct.id} - ${mChannelType.value} - featured product tagging"
                ),
                hashMapOf(
                        "ecommerce" to hashMapOf(
                                "click" to hashMapOf(
                                        "actionField" to hashMapOf( "list" to "/groupchat - featured product" ),
                                        "products" to  listOf(convertProductToHashMapWithList(featuredProduct, position, "featured product"))
                                )
                        )
                ),
                hashMapOf(
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    fun impressionPrivateVoucher(vouchers: List<MerchantVoucherUiModel>) {
        val voucherId = vouchers.firstOrNull { it.highlighted }?.id ?: return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_VIEW_GROUP_CHAT_IRIS,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "impression on private voucher",
                        KEY_EVENT_LABEL to "$mChannelId - $voucherId - ${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    fun clickCopyVoucher(voucher: MerchantVoucherUiModel) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "click copy on private voucher",
                        KEY_EVENT_LABEL to "$mChannelId - ${voucher.id} - ${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    fun clickFeaturedProductSeeMore() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "click product pinned message",
                        KEY_EVENT_LABEL to "$mChannelId - ${mChannelType.value}",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
                )
        )
    }

    fun getTrackingQueue() = trackingQueue

    /**
     * Private methods
     */
    private fun convertProductsToListOfObject(
            listOfProducts: List<PlayProductUiModel.Product>,
            sourceFrom: String,
            startPosition: Int = 0
    ): MutableList<HashMap<String, Any>> {
        val products = mutableListOf<HashMap<String, Any>>()
        listOfProducts.forEachIndexed { index, product ->
            val position = startPosition + index
            products.add(convertProductToHashMapWithList(product, position, sourceFrom))
        }
        return products
    }

    private fun convertProductToHashMapWithList(product: PlayProductUiModel.Product, position: Int, sourceFrom: String): HashMap<String, Any> {
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
                "list" to "/groupchat - $sourceFrom",
                "position" to position
        )
    }

    private fun clickBeliButtonProductWithVariant(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK buy in bottom sheet with varian",
                "$mChannelId - $productId - ${mChannelType.value}"
        )
    }

    private fun clickAtcButtonProductWithVariant(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK atc in bottom sheet with varian",
                "$mChannelId - $productId - ${mChannelType.value}"
        )
    }

    private fun clickBeliButtonProductWithNoVariant(trackingQueue: TrackingQueue,
                                                    product: PlayProductUiModel.Product,
                                                    cartId: String) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK buy in bottom sheet",
                        "$mChannelId - ${product.id} - ${mChannelType.value}"
                ),
                hashMapOf(
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
                                                   product: PlayProductUiModel.Product,
                                                   cartId: String) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK atc in bottom sheet",
                        "$mChannelId - ${product.id} - ${mChannelType.value}"
                ),
                hashMapOf(
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
                                        product: PlayProductUiModel.Product,
                                        cartId: String) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK atc in varian page",
                        "$mChannelId - ${product.id} - ${mChannelType.value}"
                ),
                hashMapOf(
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
                                         product: PlayProductUiModel.Product,
                                         cartId: String) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK beli in varian page",
                        "$mChannelId - ${product.id} - ${mChannelType.value}"
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

    private fun convertProductToHashMap(product: PlayProductUiModel.Product, cartId: String, page: String): MutableList<HashMap<String, Any>> {
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

    private fun generateSwipeSession(): String {
        val identifier = if (userId.isNotBlank() && userId.isNotEmpty()) userId else "nonlogin"
        return identifier + System.currentTimeMillis()
    }
    
    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_SCREEN_NAME = "screenName"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_CLIENT_ID = "clientId"
        private const val KEY_SESSION_IRIS = "sessionIris"
        private const val KEY_USER_ID = "userId"
        private const val KEY_BUSINESS_UNIT = "businessUnit"

        private const val KEY_TRACK_SCREEN_NAME = "group-chat-room"
        private const val KEY_TRACK_CLICK_BACK = "clickBack"
        private const val KEY_TRACK_ADD_TO_CART = "addToCart"
        private const val KEY_TRACK_CLICK_GROUP_CHAT = "clickGroupChat"
        private const val KEY_TRACK_VIEW_GROUP_CHAT = "viewGroupChat"
        private const val KEY_TRACK_VIEW_GROUP_CHAT_IRIS = "viewGroupChatIris"
        private const val KEY_TRACK_CURRENT_SITE = "tokopediamarketplace"
        private const val KEY_TRACK_BUSINESS_UNIT = "play"

        private const val KEY_TRACK_CLICK = "click"
        private const val KEY_TRACK_GROUP_CHAT_ROOM = "groupchat room"

        private const val ERR_STATE_VIDEO = "Video Player"
        private const val ERR_STATE_GLOBAL = "Global Error"
        private const val ERR_STATE_SOCKET = "Socket Connection"
    }
}