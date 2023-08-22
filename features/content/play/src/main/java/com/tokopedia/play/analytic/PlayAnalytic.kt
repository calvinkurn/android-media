package com.tokopedia.play.analytic

import android.os.Bundle
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
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
    private val dimensionTrackingHelper: PlayDimensionTrackingHelper
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
        TrackApp.getInstance().gtm.sendScreenAuthenticated("/$KEY_TRACK_SCREEN_NAME/$channelId/${channelType.value}")
    }

    fun clickLeaveRoom(durationInMs: Long) {
        val durationInSec = TimeUnit.MILLISECONDS.toSeconds(durationInMs)
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventAction to "leave room",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$mChannelId - $durationInSec - ${mChannelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.isLoggedInStatus to isLoggedIn,
                KEY_CHANNEL to mChannelName,
                "duration" to durationInSec.toString()
            )
        )
    }

    fun clickWatchArea(screenOrientation: ScreenOrientation) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "click watch area",
                Key.eventLabel to "$mChannelId - ${mChannelType.value} - ${screenOrientation.value}",
                Key.screenName to "/$KEY_TRACK_SCREEN_NAME/$mChannelId/${mChannelType.value}",
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.clientId to TrackApp.getInstance().gtm.cachedClientIDString,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    fun clickPinnedMessage(message: String, appLink: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click on admin pinned message",
            "$mChannelId - $message - $appLink - ${mChannelType.value}"
        )
    }

    fun clickLike(isLike: Boolean) {
        val action = if (isLike) "like" else "unlike"
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf<String, String>(
                Key.event to Event.clickGroupChat,
                Key.eventAction to "click $action",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$mChannelId - ${mChannelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.isLoggedInStatus to isLoggedIn,
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
            Event.viewGroupChat,
            EventCategory.groupChatRoom,
            "error state",
            "$mChannelId - $errorMessage - ${mChannelType.value}"
        )
    }

    fun clickQuickReply() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click on quick reply component",
            mChannelId
        )
    }

    fun clickSendChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click on button send",
            mChannelId
        )
    }

    fun clickWatchMode() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click watch mode option",
            "$mChannelId - ${mChannelType.value}"
        )
    }

    fun clickPlayVideo() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click on play button video",
            "$mChannelId - ${mChannelType.value}"
        )
    }

    fun impressBottomSheetProduct(
        products: Map<ProductSheetAdapter.Item.Product, Int>
    ) {
        if (products.isEmpty()) return
        val section = products.keys.firstOrNull()?.section?.config?.type ?: ProductSectionType.Unknown

        val (eventAction, eventLabel) = when (section) {
            ProductSectionType.Active -> Pair("impression - product in ongoing section", generateBaseEventLabel(product = products.keys.firstOrNull()?.product ?: PlayProductUiModel.Product.Empty, campaignId = products.keys.firstOrNull()?.section?.id.orEmpty(), rankType = products.keys.firstOrNull()?.product?.label?.rankType ?: PlayProductUiModel.Product.Label.RIBBON_TYPE_DEFAULT))
            ProductSectionType.Upcoming -> Pair("impression - product in upcoming section", generateBaseEventLabel(product = products.keys.firstOrNull()?.product ?: PlayProductUiModel.Product.Empty, campaignId = products.keys.firstOrNull()?.section?.id.orEmpty(), rankType = products.keys.firstOrNull()?.product?.label?.rankType ?: PlayProductUiModel.Product.Label.RIBBON_TYPE_DEFAULT))
            else -> Pair("view product", "$mChannelId - ${products.keys.firstOrNull()?.product?.id.orEmpty()} - ${mChannelType.value} - product in bottom sheet - is pinned product ${products.keys.firstOrNull()?.product?.isPinned.orFalse()} - ${products.keys.firstOrNull()?.product?.label?.rankType ?: PlayProductUiModel.Product.Label.RIBBON_TYPE_DEFAULT}")
        }

        val items = arrayListOf<Bundle>().apply {
            products.forEach {
                add(
                    productsToBundle(
                        product = it.key.product,
                        position = it.value,
                        sourceFrom = "bottom sheet",
                        dimension90 = if (section == ProductSectionType.Active) {
                            dimensionTrackingHelper.getDimension90()
                        } else {
                            ""
                        }
                    )
                )
            }
        }

        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, KEY_EVENT_ITEM_LIST)
            putString(Key.eventCategory, EventCategory.groupChatRoom)
            putString(Key.eventAction, eventAction)
            putString(Key.eventLabel, eventLabel)
            putString(Key.currentSite, CurrentSite.tokopediaMarketplace)
            putString(Key.sessionIris, TrackApp.getInstance().gtm.irisSessionId)
            putString(Key.userId, userId)
            putString(Key.businessUnit, BusinessUnit.play)
            putParcelableArrayList(KEY_EVENT_ITEMS, items)
            putString(KEY_ITEM_LIST, "/groupchat - bottom sheet")
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            KEY_EVENT_ITEM_LIST,
            dataLayer
        )
    }

    fun clickProduct(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int
    ) {
        val (eventAction, eventLabel) = when (sectionInfo.config.type) {
            ProductSectionType.Upcoming -> Pair("click - product in upcoming section", generateBaseEventLabel(product = product, campaignId = sectionInfo.id, rankType = product.label.rankType))
            ProductSectionType.Active -> Pair("click - product in ongoing section", generateBaseEventLabel(product = product, campaignId = sectionInfo.id, rankType = product.label.rankType))
            else -> Pair("click product in bottom sheet", "$mChannelId - ${product.id} - ${mChannelType.value} - product in bottom sheet - is pinned product ${product.isPinned} - ${product.label.rankType}")
        }

        trackingQueue.putEETracking(
            EventModel(
                "productClick",
                EventCategory.groupChatRoom,
                eventAction,
                eventLabel
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "click" to hashMapOf(
                        "actionField" to hashMapOf("list" to "/groupchat - bottom sheet"),
                        "products" to listOf(
                            convertProductToHashMapWithList(
                                product = product,
                                position = position + 1,
                                sourceFrom = "bottom sheet",
                                dimension90 = if (sectionInfo.config.type != ProductSectionType.Upcoming) {
                                    dimensionTrackingHelper.getDimension90()
                                } else {
                                    ""
                                }
                            )
                        )
                    )
                )
            ),
            generateBaseTracking(product = product, sectionInfo.config.type)
        )
    }

    fun clickActionProductWithVariant(productId: String, productAction: ProductAction) {
        when (productAction) {
            ProductAction.AddToCart -> clickAtcButtonProductWithVariant(productId)
            ProductAction.Buy, ProductAction.OCC -> clickBeliButtonProductWithVariant(productId)
            else -> {
                //no-op
            }
        }
    }

    fun clickATCBuyWithVariantRSProduct(product: PlayProductUiModel.Product, productAction: ProductAction, sectionInfo: ProductSectionUiModel.Section, shopInfo: PlayPartnerInfo) {
        val action = if (productAction == ProductAction.AddToCart) "atc" else "buy"

        trackingQueue.putEETracking(
            EventModel(
                Event.addToCart,
                EventCategory.groupChatRoom,
                "click - $action in varian page in ongoing section",
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
                                "price" to when (product.price) {
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
                            )
                        )
                    )
                )
            ),
            generateBaseTracking(product = product, sectionInfo.config.type)
        )
    }

    fun clickProductAction(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        productAction: ProductAction,
        shopInfo: PlayPartnerInfo
    ) {
        when (productAction) {
            ProductAction.AddToCart -> clickAtcButtonProductWithNoVariant(trackingQueue, product, sectionInfo, cartId, shopInfo)
            ProductAction.Buy, ProductAction.OCC -> {
                clickBeliButtonProductWithNoVariant(trackingQueue, product, sectionInfo, action = productAction, shopInfo)
            }
        }
    }

    fun clickSeeToasterAfterAtc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click lihat in message ticker",
            "$mChannelId - ${mChannelType.value}"
        )
    }

    fun trackVideoBuffering(
        bufferCount: Int,
        bufferDurationInSecond: Long
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewGroupChat,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "buffer",
                Key.eventLabel to "$bufferCount - $bufferDurationInSecond - $mChannelId - ${mChannelType.value}",
                Key.screenName to "/$KEY_TRACK_SCREEN_NAME/$mChannelId/${mChannelType.value}",
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.clientId to TrackApp.getInstance().gtm.cachedClientIDString,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    /**
     * User click "full screen" CTA from portrait to landscape video (not triggered when user click from landscape to portrait)
     */
    fun clickCtaFullScreenFromPortraitToLandscape() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "click full screen to landscape",
                Key.eventLabel to "$mChannelId - ${mChannelType.value}",
                Key.screenName to "/$KEY_TRACK_SCREEN_NAME/$mChannelId/${mChannelType.value}",
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.clientId to TrackApp.getInstance().gtm.cachedClientIDString,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    /**
     * User tilt/rotate their phone to see in full screen
     */
    fun userTiltFromPortraitToLandscape() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "rotate phone to full screen",
                Key.eventLabel to "$mChannelId - ${mChannelType.value}",
                Key.screenName to "/$KEY_TRACK_SCREEN_NAME/$mChannelId/${mChannelType.value}",
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.clientId to TrackApp.getInstance().gtm.cachedClientIDString,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    fun impressFeaturedProducts(products: List<Pair<PlayProductUiModel.Product, Int>>) {
        if (products.isEmpty()) return

        trackingQueue.putEETracking(
            EventModel(
                "productView",
                EventCategory.groupChatRoom,
                "view on featured product",
                "$mChannelId - ${products.first().first.id} - ${mChannelType.value} - featured product tagging - ${products.first().first.label.rankType}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "currencyCode" to "IDR",
                    "impressions" to mutableListOf<HashMap<String, Any>>().apply {
                        products.forEach {
                            add(convertProductToHashMapWithList(
                                product = it.first,
                                position = it.second + 1,
                                sourceFrom = "featured product",
                                dimension90 = dimensionTrackingHelper.getDimension90()
                            ))
                        }
                    }
                )
            ),
            hashMapOf(
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    fun clickFeaturedProduct(featuredProduct: PlayProductUiModel.Product, position: Int) {
        trackingQueue.putEETracking(
            EventModel(
                "productClick",
                EventCategory.groupChatRoom,
                "click featured product tagging",
                "$mChannelId - ${featuredProduct.id} - ${mChannelType.value} - featured product tagging - ${featuredProduct.label.rankType}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "click" to hashMapOf(
                        "actionField" to hashMapOf("list" to "/groupchat - featured product"),
                        "products" to listOf(
                            convertProductToHashMapWithList(
                                product = featuredProduct,
                                position = position,
                                sourceFrom = "featured product",
                                dimension90 = dimensionTrackingHelper.getDimension90()
                            )
                        )
                    )
                )
            ),
            hashMapOf(
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                KEY_ITEM_LIST to "/groupchat - featured product",
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.isLoggedInStatus to isLoggedIn,
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
                Key.event to Event.viewGroupChatIris,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "impression on private voucher",
                Key.eventLabel to "$mChannelId - ${voucher.id} - ${mChannelType.value}",
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    fun clickFeaturedProductSeeMore() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "click product pinned message",
                Key.eventLabel to "$mChannelId - ${mChannelType.value}",
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    fun getTrackingQueue() = trackingQueue

    fun connectCast(isSuccess: Boolean, id: String = mChannelId, type: PlayChannelType = mChannelType) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewGroupChatIris,
                Key.eventAction to "chromecast connecting state",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$id - ${type.value} - ${if (isSuccess) "success" else "failed"}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
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
                Key.event to Event.viewGroupChatIris,
                Key.eventAction to "chromecast disconnected",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$channelId - ${mChannelType.value} - $duration",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    /**
     * Private methods
     */
    private fun convertProductToHashMapWithList(
        product: PlayProductUiModel.Product,
        position: Int,
        sourceFrom: String,
        dimension90: String,
    ): HashMap<String, Any> {
        val dimension115 = buildString {
            append("pinned.${product.isPinned}, ")
            append("ribbon.${product.label.rankType}")
        }

        return hashMapOf<String, Any>(
            "name" to product.title,
            "id" to product.id,
            "price" to when (product.price) {
                is DiscountedPrice -> product.price.discountedPriceNumber
                is OriginalPrice -> product.price.priceNumber
            },
            "brand" to "",
            "category" to "",
            "variant" to "",
            "list" to "/groupchat - $sourceFrom",
            "position" to position,
            "dimension115" to dimension115
        ).apply {
            if (dimension90.isNotEmpty()) {
                put("dimension90", dimension90)
            }
        }
    }

    private fun productsToBundle(
        product: PlayProductUiModel.Product,
        position: Int,
        sourceFrom: String,
        dimension90: String,
    ): Bundle =
        Bundle().apply {
            putString("item_name", product.title)
            putString("item_id", product.id)
            putDouble(
                "price",
                when (product.price) {
                    is DiscountedPrice -> product.price.discountedPriceNumber
                    is OriginalPrice -> product.price.priceNumber
                }
            )
            putString("item_brand", "")
            putString("item_category", "")
            putString("item_variant", "")
            putString("dimension40", "/groupchat - $sourceFrom")
            if (dimension90.isNotEmpty()) {
                putString("dimension90", dimension90)
            }
            putInt("index", position)
        }

    private fun convertProductAndShopToHashMapWithList(
        product: PlayProductUiModel.Product,
        shopInfo: PlayPartnerInfo,
        dimension39: String,
        dimension90: String,
    ): HashMap<String, Any> {
        return hashMapOf(
            "name" to product.title,
            "id" to product.id,
            "price" to when (product.price) {
                is DiscountedPrice -> product.price.discountedPriceNumber
                is OriginalPrice -> product.price.priceNumber
            },
            "brand" to "",
            "category" to "",
            "variant" to "",
            "dimension39" to dimension39,
            "dimension90" to dimension90,
            "category_id" to "",
            "quantity" to product.minQty,
            "shop_id" to shopInfo.id,
            "shop_name" to shopInfo.name,
            "shop_type" to shopInfo.type.value
        )
    }

    private fun clickBeliButtonProductWithVariant(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click buy in bottom sheet with varian",
            "$mChannelId - $productId - ${mChannelType.value}"
        )
    }

    private fun clickAtcButtonProductWithVariant(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            Event.clickGroupChat,
            EventCategory.groupChatRoom,
            "click atc in bottom sheet with varian",
            "$mChannelId - $productId - ${mChannelType.value}"
        )
    }

    private fun clickBeliButtonProductWithNoVariant(
        trackingQueue: TrackingQueue,
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        action: ProductAction,
        shopInfo: PlayPartnerInfo
    ) {
        val (eventAction, eventLabel) = when (sectionInfo.config.type) {
            ProductSectionType.Active -> Pair("click - buy in ongoing section", "${generateBaseEventLabel(product = product, campaignId = sectionInfo.id, rankType = product.label.rankType)} - beli langsung ${action == ProductAction.OCC}")
            else -> Pair("click buy in bottom sheet", "$mChannelId - ${product.id} - ${mChannelType.value} - is pinned product ${product.isPinned} - beli langsung ${action == ProductAction.OCC} - ${product.label.rankType}")
        }
        trackingQueue.putEETracking(
            EventModel(
                Event.addToCart,
                EventCategory.groupChatRoom,
                eventAction,
                eventLabel
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "currencyCode" to "IDR",
                    "add" to hashMapOf(
                        "products" to listOf(
                            convertProductAndShopToHashMapWithList(
                                product = product,
                                shopInfo = shopInfo,
                                dimension39 = "/groupchat - bottom sheet",
                                dimension90 = dimensionTrackingHelper.getDimension90()
                            )
                        )
                    )
                )
            ),
            generateBaseTracking(product = product, sectionInfo.config.type)
        )
    }

    private fun clickAtcButtonProductWithNoVariant(
        trackingQueue: TrackingQueue,
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo
    ) {
        val (eventAction, eventLabel) = when (sectionInfo.config.type) {
            ProductSectionType.Active -> Pair("click - atc in ongoing section", generateBaseEventLabel(product = product, campaignId = sectionInfo.id, rankType = product.label.rankType))
            else -> Pair("click atc in bottom sheet", "$mChannelId - ${product.id} - ${mChannelType.value} - is pinned product ${product.isPinned} - ${product.label.rankType}")
        }
        trackingQueue.putEETracking(
            EventModel(
                Event.addToCart,
                EventCategory.groupChatRoom,
                eventAction,
                eventLabel
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "currencyCode" to "IDR",
                    "add" to hashMapOf(
                        "products" to listOf(
                            convertProductAndShopToHashMapWithList(
                                product = product,
                                shopInfo = shopInfo,
                                dimension39 = "/groupchat - bottom sheet",
                                dimension90 = dimensionTrackingHelper.getDimension90()
                            )
                        )
                    )
                )
            ),
            generateBaseTracking(product = product, sectionInfo.config.type)
        )
    }

    fun clickKebabMenu() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventAction to "click - three dots menu",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$mChannelId - $userId",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    fun clickUserReport() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventAction to "click - laporkan video",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$mChannelId - $userId",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    fun clickUserReportSubmissionBtnSubmit(isUse: Boolean) {
        val useValue = if (isUse) "use" else "not use"
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventAction to "click - laporkan on bottom sheet",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$mChannelId - $userId - $useValue",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    fun clickUserReportSubmissionDialogSubmit() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickGroupChat,
                Key.eventAction to "click - laporkan on pop up",
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventLabel to "$mChannelId - $userId",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    fun sendScreenArchived(channelId: String) {
        val customDimension = mapOf(Key.currentSite to CurrentSite.tokopediaMarketplace, Key.businessUnit to BusinessUnit.play, Key.trackerId to "40353")
        TrackApp.getInstance().gtm.sendScreenAuthenticated("/$KEY_TRACK_SCREEN_NAME/$channelId/archive delete channel", customDimension)
    }

    fun clickCtaArchived(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - to tokopedia play")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel(channelId)
            .setCustomProperty(Key.trackerId, "40354")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, TrackApp.getInstance().gtm.irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    fun clickExitArchived(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - exit archive page")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel(channelId)
            .setCustomProperty(Key.trackerId, "40355")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, TrackApp.getInstance().gtm.irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    fun openScreen(channelId: String, channelType: PlayChannelType) {
        Tracker.Builder()
            .setEvent(Event.openScreen)
            .setCustomProperty(Key.trackerId, "13881")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.isLoggedInStatus, isLoggedIn)
            .setCustomProperty(Key.pageSource, dimensionTrackingHelper.getDimension90())
            .setCustomProperty(Key.screenName, "/group-chat-room/$channelId/${channelType.value}/is coachmark true")
            .setCustomProperty(Key.sessionIris, TrackApp.getInstance().gtm.irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    fun clickCommentIcon(partnerId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - comment button")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $partnerId")
            .setCustomProperty(Key.trackerId, "42591")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, TrackApp.getInstance().gtm.irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    private fun generateSwipeSession(): String {
        val identifier = if (userId.isNotBlank() && userId.isNotEmpty()) userId else "nonlogin"
        return identifier + System.currentTimeMillis()
    }

    private fun generateBaseEventLabel(product: PlayProductUiModel.Product, campaignId: String, rankType: String): String {
        return "$mChannelId - ${product.id} - ${mChannelType.value} - $campaignId - is pinned product ${product.isPinned} - $rankType"
    }

    private fun generateBaseTracking(product: PlayProductUiModel.Product, type: ProductSectionType): HashMap<String, Any> {
        val base: HashMap<String, Any> = hashMapOf(
            Key.businessUnit to BusinessUnit.play,
            Key.currentSite to CurrentSite.tokopediaMarketplace,
            Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
            Key.userId to userId
        )
        if (type == ProductSectionType.Other) {
            base.putAll(
                mapOf(
                    Key.isLoggedInStatus to isLoggedIn,
                    KEY_PRODUCT_ID to product.id,
                    KEY_PRODUCT_NAME to product.title,
                    KEY_PRODUCT_URL to product.applink.toString(),
                    KEY_CHANNEL to mChannelName
                )
            )
        }
        return base
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/222
    // Tracker ID: 42864
    fun clickCartFromSheet () {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - cart button bottom sheet")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - ${mChannelType.value}")
            .setCustomProperty(Key.trackerId, "42864")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, TrackApp.getInstance().gtm.irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/222
    // Tracker ID: 42863
    fun impressCartFromBottomSheet () {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("view - cart button bottom sheet")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - ${mChannelType.value}")
            .setCustomProperty(Key.trackerId, "42863")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, TrackApp.getInstance().gtm.irisSessionId)
            .setUserId(userId)
            .build()
            .send()
    }

    companion object {
        private const val KEY_CHANNEL = "channel"
        private const val KEY_PRODUCT_ID = "productId"
        private const val KEY_PRODUCT_NAME = "productName"
        private const val KEY_PRODUCT_URL = "productUrl"
        private const val KEY_ITEM_LIST = "item_list"
        private const val KEY_EVENT_ITEM_LIST = "view_item_list"
        private const val KEY_EVENT_ITEMS = "items"

        private const val KEY_TRACK_SCREEN_NAME = "group-chat-room"

        private const val ERR_STATE_VIDEO = "Video Player"
        private const val ERR_STATE_GLOBAL = "Global Error"
    }
}
