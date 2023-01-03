package com.tokopedia.play.analytic

import android.os.Bundle
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import java.util.concurrent.TimeUnit

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

    private val isLoggedIn: String
        get() = userSession.isLoggedIn.toString()

    private var mChannelId: String = ""
    private var mChannelType: PlayChannelType = PlayChannelType.Unknown
    private val mSessionId: String = generateSwipeSession()
    private var mSourceType = ""
    private var mChannelName = ""

    fun sendScreen(channelId: String, channelType: PlayChannelType, sourceType: String = "", channelName: String = "") {
        this.mChannelId = channelId
        this.mChannelType = channelType
        this.mChannelName = channelName
        if (sourceType.isNotEmpty() && sourceType.isNotBlank()) this.mSourceType = sourceType
        TrackApp.getInstance().gtm.sendScreenAuthenticated("/${KEY_TRACK_SCREEN_NAME}/$channelId/${channelType.value}")
    }

    /**
     * User swipe room
     */
    fun swipeRoom(nextId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_CLICK_TOP_ADS,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "swipe channel",
                        KEY_EVENT_LABEL to "$mSessionId - $mChannelId - ${mChannelType.value} - $mSourceType - $nextId",
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_USER_ID to userId,
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        KEY_TRACKER_ID to "6663"
                )
        )
    }

    fun clickLeaveRoom(durationInMs: Long) {
        val durationInSec = TimeUnit.MILLISECONDS.toSeconds(durationInMs)
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_EVENT_ACTION to "leave room",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$mChannelId - $durationInSec - ${mChannelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
                KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                KEY_CHANNEL to mChannelName,
                "duration" to durationInSec.toString()
            )
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
            mapOf<String, String>(
                KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_EVENT_ACTION to "$KEY_TRACK_CLICK $action",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$mChannelId - ${mChannelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
                KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                KEY_CHANNEL to mChannelName
            )
        )
    }

    fun trackVideoError(message: String) {
        errorState("$ERR_STATE_VIDEO: $message")
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

    fun impressBottomSheetProduct(
        products: Map<ProductSheetAdapter.Item.Product, Int>
    ) {
        if (products.isEmpty()) return
        val section = products.keys.firstOrNull()?.section?.config?.type ?: ProductSectionType.Unknown

        val (eventAction, eventLabel) = when(section) {
            ProductSectionType.Active -> Pair("impression - product in ongoing section", generateBaseEventLabel(product = products.keys.firstOrNull()?.product ?: PlayProductUiModel.Product.Empty, campaignId = products.keys.firstOrNull()?.section?.id.orEmpty()))
            ProductSectionType.Upcoming -> Pair("impression - product in upcoming section", generateBaseEventLabel(product = products.keys.firstOrNull()?.product ?: PlayProductUiModel.Product.Empty, campaignId = products.keys.firstOrNull()?.section?.id.orEmpty()))
            else -> Pair("view product", "$mChannelId - ${products.keys.firstOrNull()?.product?.id.orEmpty()} - ${mChannelType.value} - product in bottom sheet - is pinned product ${products.keys.firstOrNull()?.product?.isPinned.orFalse()}")
        }

        val items = arrayListOf<Bundle>().apply {
            products.forEach {
                add(productsToBundle(it.key.product, it.value, "bottom sheet"))
            }
        }

        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, KEY_EVENT_ITEM_LIST)
            putString(KEY_EVENT_CATEGORY, KEY_TRACK_GROUP_CHAT_ROOM)
            putString(KEY_EVENT_ACTION, eventAction)
            putString(KEY_EVENT_LABEL, eventLabel)
            putString(KEY_CURRENT_SITE, KEY_TRACK_CURRENT_SITE)
            putString(KEY_SESSION_IRIS, TrackApp.getInstance().gtm.irisSessionId)
            putString(KEY_USER_ID, userId)
            putString(KEY_BUSINESS_UNIT, KEY_TRACK_BUSINESS_UNIT)
            putParcelableArrayList(KEY_EVENT_ITEMS, items)
            putString(KEY_ITEM_LIST , "/groupchat - bottom sheet")
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            KEY_EVENT_ITEM_LIST, dataLayer
        )
    }

    fun clickProduct(product: PlayProductUiModel.Product,
                     sectionInfo: ProductSectionUiModel.Section,
                     position: Int) {

        val (eventAction, eventLabel) = when (sectionInfo.config.type) {
            ProductSectionType.Upcoming -> Pair("$KEY_TRACK_CLICK - product in upcoming section", generateBaseEventLabel(product = product, campaignId = sectionInfo.id))
            ProductSectionType.Active -> Pair("$KEY_TRACK_CLICK - product in ongoing section", generateBaseEventLabel(product = product, campaignId = sectionInfo.id))
            else -> Pair("click product in bottom sheet", "$mChannelId - ${product.id} - ${mChannelType.value} - product in bottom sheet - is pinned product ${product.isPinned}")
        }

        trackingQueue.putEETracking(
                EventModel(
                        "productClick",
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        eventAction,
                        eventLabel
                ),
                hashMapOf (
                    "ecommerce" to hashMapOf(
                        "click" to hashMapOf(
                            "actionField" to hashMapOf( "list" to "/groupchat - bottom sheet"),
                            "products" to  listOf(convertProductToHashMapWithList(product, position + 1, "bottom sheet"))
                        )
                    )
                ),
                generateBaseTracking(product = product, sectionInfo.config.type)
        )
    }

    fun clickActionProductWithVariant(productId: String, productAction: ProductAction) {
        when(productAction) {
            ProductAction.AddToCart -> clickAtcButtonProductWithVariant(productId)
            ProductAction.Buy -> clickBeliButtonProductWithVariant(productId)
        }
    }

    fun clickATCBuyWithVariantRSProduct(product: PlayProductUiModel.Product, productAction: ProductAction, sectionInfo: ProductSectionUiModel.Section, shopInfo: PlayPartnerInfo){
        val action = if(productAction == ProductAction.AddToCart) "atc" else "buy"

        trackingQueue.putEETracking(
            EventModel(
                KEY_TRACK_ADD_TO_CART,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - $action in varian page in ongoing section",
                "$mChannelId - ${product.id} - ${mChannelType.value} - ${sectionInfo.id}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "currencyCode" to "IDR",
                    "add" to hashMapOf(
                        "products" to listOf(
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
                            "category_id" to "",
                            "quantity" to product.minQty,
                            "shop_id" to shopInfo.id,
                            "shop_name" to shopInfo.name,
                            "shop_type" to shopInfo.type.value
                        ))
                        )
                    )
            ),
            generateBaseTracking(product = product, sectionInfo.config.type)
        )
    }

    fun clickProductAction(product: PlayProductUiModel.Product,
                           sectionInfo: ProductSectionUiModel.Section,
                           cartId: String,
                           productAction: ProductAction,
                           bottomInsetsType: BottomInsetsType,
                           shopInfo: PlayPartnerInfo) {
        when(productAction) {
            ProductAction.AddToCart ->
                when (bottomInsetsType) {
                    BottomInsetsType.VariantSheet -> {
                        if(sectionInfo.config.type != ProductSectionType.Active) clickAtcButtonInVariant(trackingQueue, product, cartId, shopInfo)
                        else clickATCBuyWithVariantRSProduct(product, productAction, sectionInfo, shopInfo)
                    }
                    else -> clickAtcButtonProductWithNoVariant(trackingQueue, product, sectionInfo, cartId, shopInfo)
                }
            ProductAction.Buy, ProductAction.OCC -> {
                when (bottomInsetsType) {
                    BottomInsetsType.VariantSheet -> {
                        if(sectionInfo.config.type != ProductSectionType.Active) clickBeliButtonInVariant(trackingQueue, product, cartId, shopInfo)
                        else clickATCBuyWithVariantRSProduct(product, productAction, sectionInfo, shopInfo)
                    }
                    else -> clickBeliButtonProductWithNoVariant(trackingQueue, product, sectionInfo, action = productAction, shopInfo)
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
            bufferDurationInSecond: Long
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

    fun impressFeaturedProducts(products: List<Pair<PlayProductUiModel.Product, Int>>) {
        if (products.isEmpty()) return

        trackingQueue.putEETracking(
                EventModel(
                        "productView",
                        KEY_TRACK_GROUP_CHAT_ROOM,
                    "view on featured product",
                    "$mChannelId - ${products.first().first.id} - ${mChannelType.value} - featured product tagging"
                ),
                hashMapOf(
                        "ecommerce" to hashMapOf(
                                "currencyCode" to "IDR",
                                "impressions" to mutableListOf<HashMap<String, Any>>().apply {
                                    products.forEach {
                                        add(convertProductToHashMapWithList(it.first, it.second + 1, "featured product"))
                                    }
                                }
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

    fun clickFeaturedProduct(featuredProduct: PlayProductUiModel.Product, position: Int) {
        trackingQueue.putEETracking(
                EventModel(
                    "productClick",
                    KEY_TRACK_GROUP_CHAT_ROOM,
                    "click featured product tagging",
                    "$mChannelId - ${featuredProduct.id} - ${mChannelType.value} - featured product tagging",
                ),
                hashMapOf(
                    "ecommerce" to hashMapOf(
                        "click" to hashMapOf(
                            "actionField" to hashMapOf( "list" to "/groupchat - featured product"),
                            "products" to  listOf(convertProductToHashMapWithList(featuredProduct, position, "featured product"))
                        )
                    )
                ),
                hashMapOf(
                        KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                        KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                        KEY_ITEM_LIST to "/groupchat - featured product",
                        KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                        KEY_USER_ID to userId,
                        KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                        KEY_PRODUCT_ID to featuredProduct.id,
                        KEY_PRODUCT_NAME to featuredProduct.title,
                        KEY_PRODUCT_URL to featuredProduct.applink.toString(),
                        KEY_CHANNEL to mChannelName
                )
        )
    }

    fun impressionPrivateVoucher(voucher: PlayVoucherUiModel.Merchant) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        KEY_EVENT to KEY_TRACK_VIEW_GROUP_CHAT_IRIS,
                        KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                        KEY_EVENT_ACTION to "impression on private voucher",
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
     * Cast
     */
    fun impressCast(channelId: String, channelType: PlayChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_EVENT_ACTION to "impression on chromecast button",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
            )
        )
    }

    fun clickCast() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_EVENT_ACTION to "click chromecast button",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$mChannelId - ${mChannelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
            )
        )
    }

    fun connectCast(isSuccess: Boolean, id: String = mChannelId, type: PlayChannelType = mChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_GROUP_CHAT_IRIS,
                KEY_EVENT_ACTION to "chromecast connecting state",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$id - ${type.value} - ${if(isSuccess) "success" else "failed"}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
            )
        )
    }

    fun recordCastDuration(duration: Long) {
        /**
         * When swipe screen, recordCastDuration() will be called first, followed by sendScreen()
         * So, it will send tracking from the previous screen
         */
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_GROUP_CHAT_IRIS,
                KEY_EVENT_ACTION to "chromecast disconnected",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$channelId - ${mChannelType.value} - $duration",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
            )
        )
    }

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

    private fun productsToBundle(product: PlayProductUiModel.Product, position: Int, sourceFrom: String) : Bundle =
        Bundle().apply {
            putString("item_name", product.title)
            putString("item_id", product.id)
            putDouble("price", when(product.price) {
                is DiscountedPrice -> product.price.discountedPriceNumber
                is OriginalPrice -> product.price.priceNumber
            })
            putString("item_brand", "")
            putString("item_category", "")
            putString("item_variant", "")
            putString("dimension40", "/groupchat - $sourceFrom")
            putInt("index", position)
        }

    private fun convertProductAndShopToHashMapWithList(product: PlayProductUiModel.Product, shopInfo: PlayPartnerInfo, dimension39: String = ""): HashMap<String, Any> {
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
            "dimension39" to dimension39,
            "category_id" to "",
            "quantity" to product.minQty,
            "shop_id" to shopInfo.id,
            "shop_name" to shopInfo.name,
            "shop_type" to shopInfo.type.value
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
                                                    sectionInfo: ProductSectionUiModel.Section,
                                                    action: ProductAction,
                                                    shopInfo: PlayPartnerInfo) {
        val (eventAction, eventLabel) = when (sectionInfo.config.type) {
            ProductSectionType.Active -> Pair("$KEY_TRACK_CLICK - buy in ongoing section", "${generateBaseEventLabel(product = product, campaignId = sectionInfo.id)} - beli langsung ${action == ProductAction.OCC}")
            else -> Pair("click buy in bottom sheet", "$mChannelId - ${product.id} - ${mChannelType.value} - is pinned product ${product.isPinned} - beli langsung ${action == ProductAction.OCC}")
        }
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        eventAction,
                        eventLabel
                ),
                hashMapOf(
                    "ecommerce" to hashMapOf(
                        "currencyCode" to "IDR",
                        "add" to hashMapOf(
                            "products" to listOf(convertProductAndShopToHashMapWithList(product, shopInfo, "/groupchat - bottom sheet"))
                        )
                    )
                ),
                generateBaseTracking(product = product, sectionInfo.config.type)
        )
    }

    private fun clickAtcButtonProductWithNoVariant(trackingQueue: TrackingQueue,
                                                   product: PlayProductUiModel.Product,
                                                   sectionInfo: ProductSectionUiModel.Section,
                                                   cartId: String,
                                                   shopInfo: PlayPartnerInfo) {
        val (eventAction, eventLabel) = when (sectionInfo.config.type) {
            ProductSectionType.Active -> Pair("$KEY_TRACK_CLICK - atc in ongoing section", generateBaseEventLabel(product = product, campaignId = sectionInfo.id))
            else -> Pair("$KEY_TRACK_CLICK atc in bottom sheet", "$mChannelId - ${product.id} - ${mChannelType.value} - is pinned product ${product.isPinned}")
        }
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        eventAction,
                        eventLabel
                ),
                hashMapOf(
                    "ecommerce" to hashMapOf(
                        "currencyCode" to "IDR",
                        "add" to hashMapOf(
                            "products" to listOf(convertProductAndShopToHashMapWithList(product, shopInfo, "/groupchat - bottom sheet"))
                        )
                    )
                ),
                generateBaseTracking(product = product, sectionInfo.config.type)
        )
    }

    private fun clickAtcButtonInVariant(trackingQueue: TrackingQueue,
                                        product: PlayProductUiModel.Product,
                                        cartId: String,
                                        shopInfo: PlayPartnerInfo) {
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
                            "products" to listOf(convertProductAndShopToHashMapWithList(product, shopInfo, "/groupchat - varian page"))
                        )
                    )
                ),
                hashMapOf(
                    KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                    KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                    KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                    KEY_USER_ID to userId,
                    KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                    KEY_PRODUCT_ID to product.id,
                    KEY_PRODUCT_NAME to product.title,
                    KEY_PRODUCT_URL to product.applink.toString(),
                    KEY_CHANNEL to mChannelName
                )
        )
    }

    private fun clickBeliButtonInVariant(trackingQueue: TrackingQueue,
                                         product: PlayProductUiModel.Product,
                                         cartId: String,
                                         shopInfo: PlayPartnerInfo) {
        trackingQueue.putEETracking(
                EventModel(
                        KEY_TRACK_ADD_TO_CART,
                        KEY_TRACK_GROUP_CHAT_ROOM,
                        "$KEY_TRACK_CLICK beli in varian page",
                        "$mChannelId - ${product.id} - ${mChannelType.value}"
                ),
                hashMapOf (
                    "ecommerce" to hashMapOf(
                        "currencyCode" to "IDR",
                        "add" to hashMapOf(
                            "products" to listOf(convertProductAndShopToHashMapWithList(product, shopInfo, "/groupchat - varian page"))
                        )
                    )
                ),
                hashMapOf(
                    KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                    KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                    KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                    KEY_USER_ID to userId,
                    KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                    KEY_PRODUCT_ID to product.id,
                    KEY_PRODUCT_NAME to product.title,
                    KEY_PRODUCT_URL to product.applink.toString(),
                    KEY_CHANNEL to mChannelName
                )
        )
    }

    fun clickKebabMenu(){
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_EVENT_ACTION to "$KEY_TRACK_CLICK - three dots menu",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$mChannelId - $userId",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
                )
        )
    }

    fun clickUserReport(){
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_EVENT_ACTION to "$KEY_TRACK_CLICK - laporkan video",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$mChannelId - $userId",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    fun clickUserReportSubmissionBtnSubmit(isUse: Boolean){
        val useValue = if(isUse) "use" else "not use"
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_EVENT_ACTION to "$KEY_TRACK_CLICK - laporkan on bottom sheet",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$mChannelId - $userId - $useValue",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    fun clickUserReportSubmissionDialogSubmit(){
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_GROUP_CHAT,
                KEY_EVENT_ACTION to "$KEY_TRACK_CLICK - laporkan on pop up",
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_LABEL to "$mChannelId - $userId",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    fun sendScreenArchived(channelId: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated("/${KEY_TRACK_SCREEN_NAME}/$channelId/archive delete channel")
    }

    fun clickCtaArchived(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - to tokopedia play")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel(channelId)
            .setCustomProperty(KEY_TRACKER_ID, "40354")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, TrackApp.getInstance().gtm.irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    fun clickExitArchived (channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - exit archive page")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel(channelId)
            .setCustomProperty(KEY_TRACKER_ID, "40355")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, TrackApp.getInstance().gtm.irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    private fun generateSwipeSession(): String {
        val identifier = if (userId.isNotBlank() && userId.isNotEmpty()) userId else "nonlogin"
        return identifier + System.currentTimeMillis()
    }

    private fun generateBaseEventLabel(product: PlayProductUiModel.Product, campaignId: String): String {
        return "$mChannelId - ${product.id} - ${mChannelType.value} - $campaignId - is pinned product ${product.isPinned}"
    }

    private fun generateBaseTracking(product: PlayProductUiModel.Product, type: ProductSectionType): HashMap<String, Any>{
        val base: HashMap<String, Any> = hashMapOf(
            KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
            KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
            KEY_USER_ID to userId,
            )
       if (type == ProductSectionType.Other) {
           base.putAll(
               mapOf(
                   KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                   KEY_PRODUCT_ID to product.id,
                   KEY_PRODUCT_NAME to product.title,
                   KEY_PRODUCT_URL to product.applink.toString(),
                   KEY_CHANNEL to mChannelName
               )
           )
       }
        return base
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
        private const val KEY_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        private const val KEY_CHANNEL = "channel"
        private const val KEY_PRODUCT_ID = "productId"
        private const val KEY_PRODUCT_NAME = "productName"
        private const val KEY_PRODUCT_URL = "productUrl"
        private const val KEY_ITEM_LIST = "item_list"
        private const val KEY_EVENT_ITEM_LIST = "view_item_list"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val KEY_EVENT_ITEMS = "items"

        private const val KEY_TRACK_SCREEN_NAME = "group-chat-room"
        private const val KEY_TRACK_ADD_TO_CART = "addToCart"
        private const val KEY_TRACK_CLICK_GROUP_CHAT = "clickGroupChat"
        private const val KEY_TRACK_VIEW_GROUP_CHAT = "viewGroupChat"
        private const val KEY_TRACK_VIEW_GROUP_CHAT_IRIS = "viewGroupChatIris"
        private const val KEY_TRACK_CURRENT_SITE = "tokopediamarketplace"
        private const val KEY_TRACK_BUSINESS_UNIT = "play"

        private const val KEY_TRACK_CLICK = "click"
        private const val KEY_TRACK_CLICK_CONTENT = "clickContent"
        private const val KEY_TRACK_GROUP_CHAT_ROOM = "groupchat room"

        private const val ERR_STATE_VIDEO = "Video Player"
        private const val ERR_STATE_GLOBAL = "Global Error"
    }
}
