package com.tokopedia.play.analytic.tagitem

import com.tokopedia.play.analytic.KEY_ADD
import com.tokopedia.play.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.play.analytic.KEY_CATEGORY_ID
import com.tokopedia.play.analytic.KEY_CHANNEL
import com.tokopedia.play.analytic.KEY_CLICK
import com.tokopedia.play.analytic.KEY_CURRENCY_CODE
import com.tokopedia.play.analytic.KEY_CURRENT_SITE
import com.tokopedia.play.analytic.KEY_DIMENSION_40
import com.tokopedia.play.analytic.KEY_DIMENSION_45
import com.tokopedia.play.analytic.KEY_ECOMMERCE
import com.tokopedia.play.analytic.KEY_EVENT
import com.tokopedia.play.analytic.KEY_EVENT_ACTION
import com.tokopedia.play.analytic.KEY_EVENT_ADD_TO_CART
import com.tokopedia.play.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.play.analytic.KEY_EVENT_LABEL
import com.tokopedia.play.analytic.KEY_EVENT_PRODUCT_CLICK
import com.tokopedia.play.analytic.KEY_EVENT_PRODUCT_VIEW
import com.tokopedia.play.analytic.KEY_IMPRESSIONS
import com.tokopedia.play.analytic.KEY_INDEX
import com.tokopedia.play.analytic.KEY_IS_LOGGED_IN_STATUS
import com.tokopedia.play.analytic.KEY_ITEM_BRAND
import com.tokopedia.play.analytic.KEY_ITEM_CATEGORY
import com.tokopedia.play.analytic.KEY_ITEM_ID
import com.tokopedia.play.analytic.KEY_ITEM_NAME
import com.tokopedia.play.analytic.KEY_ITEM_VARIANT
import com.tokopedia.play.analytic.KEY_PRICE
import com.tokopedia.play.analytic.KEY_PRODUCTS
import com.tokopedia.play.analytic.KEY_QUANTITY
import com.tokopedia.play.analytic.KEY_SESSION_IRIS
import com.tokopedia.play.analytic.KEY_SHOP_ID
import com.tokopedia.play.analytic.KEY_SHOP_NAME
import com.tokopedia.play.analytic.KEY_SHOP_TYPE
import com.tokopedia.play.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.analytic.KEY_TRACK_CLICK
import com.tokopedia.play.analytic.KEY_TRACK_CLICK_GROUP_CHAT
import com.tokopedia.play.analytic.KEY_TRACK_CURRENT_SITE
import com.tokopedia.play.analytic.KEY_TRACK_GROUP_CHAT_ROOM
import com.tokopedia.play.analytic.KEY_TRACK_VIEW_CONTENT_IRIS
import com.tokopedia.play.analytic.KEY_USER_ID
import com.tokopedia.play.analytic.VAL_BUSINESS_UNIT
import com.tokopedia.play.analytic.VAL_CURRENCY_CODE
import com.tokopedia.play.analytic.VAL_CURRENT_SITE
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ProductPrice
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_PRODUCT_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created by kenny.hadisaputra on 10/03/22
 */
class PlayTagItemsAnalyticImpl @AssistedInject constructor(
    @Assisted private val trackingQueue: TrackingQueue,
    @Assisted private val channelInfo: PlayChannelInfoUiModel,
    private val userSession: UserSessionInterface,
) : PlayTagItemsAnalytic {
    
    @AssistedFactory
    interface Factory : PlayTagItemsAnalytic.Factory {
        override fun create(
            trackingQueue: TrackingQueue,
            channelInfo: PlayChannelInfoUiModel,
        ): PlayTagItemsAnalyticImpl
    }
    
    private val channelId: String
        get() = channelInfo.id
    
    private val channelType: PlayChannelType
        get() = channelInfo.channelType
    
    private val channelName: String
        get() = channelInfo.title

    private val isLoggedIn: Boolean
        get() = userSession.isLoggedIn

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else "0"

    private val irisSessionId: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    override fun impressBottomSheetProducts(
        products: List<Pair<PlayProductUiModel.Product, Int>>,
        sectionInfo: ProductSectionUiModel.Section
    ) {
        if (products.isEmpty()) return

        val (eventAction, eventLabel) = when(sectionInfo.config.type){
            ProductSectionType.Active -> Pair("impression - product in ongoing section",generateBaseEventLabel(productId = products.first().first.id, campaignId = sectionInfo.id))
            ProductSectionType.Upcoming -> Pair("impression - product in upcoming section",generateBaseEventLabel(productId = products.first().first.id, campaignId = sectionInfo.id))
            else -> Pair("view product", "$channelId - ${products.first().first.id} - ${channelType.value} - product in bottom sheet")
        }
        trackingQueue.putEETracking(
            EventModel(
                "productView",
                KEY_TRACK_GROUP_CHAT_ROOM,
                eventAction,
                eventLabel
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "currencyCode" to "IDR",
                    "impressions" to mutableListOf<HashMap<String, Any>>().apply {
                        products.forEach {
                            add(convertProductToHashMapWithList(it.first, it.second, "bottom sheet"))
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

    override fun impressFeaturedProducts(
        products: List<Pair<PlayProductUiModel.Product, Int>>
    ) {
        if (products.isEmpty()) return

        trackingQueue.putEETracking(
            EventModel(
                "productView",
                KEY_TRACK_GROUP_CHAT_ROOM,
                "view on featured product",
                "$channelId - ${products.first().first.id} - ${channelType.value} - featured product tagging",
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

    override fun clickProduct(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int
    ) {
        val (eventAction, eventLabel) = when (sectionInfo.config.type) {
            ProductSectionType.Upcoming -> Pair("$KEY_TRACK_CLICK - product in upcoming section", generateBaseEventLabel(productId = product.id, campaignId = sectionInfo.id))
            ProductSectionType.Active -> Pair("$KEY_TRACK_CLICK - product in ongoing section", generateBaseEventLabel(productId = product.id, campaignId = sectionInfo.id))
            else -> Pair(KEY_TRACK_CLICK, "$channelId - ${product.id} - ${channelType.value} - product in bottom sheet")
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
                        "actionField" to hashMapOf( "list" to "/groupchat - bottom sheet" ),
                        "products" to  listOf(convertProductToHashMapWithList(product, position + 1, "bottom sheet"))
                    )
                )
            ),
            hashMapOf(
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_ITEM_LIST to "/groupchat - bottom sheet",
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
                KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                KEY_PRODUCT_ID to product.id,
                KEY_PRODUCT_NAME to product.title,
                KEY_PRODUCT_URL to product.applink.toString(),
                KEY_CHANNEL to channelName
            )
        )
    }

    override fun clickFeaturedProduct(
        featuredProduct: PlayProductUiModel.Product,
        position: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                "productClick",
                KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_TRACK_CLICK,
                "$channelId - ${featuredProduct.id} - ${channelType.value} - featured product tagging"
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
                KEY_CHANNEL to channelName
            )
        )
    }

    override fun scrollMerchantVoucher(lastPositionViewed: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            KEY_TRACK_CLICK_GROUP_CHAT,
            KEY_TRACK_GROUP_CHAT_ROOM,
            "scroll merchant voucher",
            "$channelId - $lastPositionViewed"
        )
    }

    override fun clickActionProductWithVariant(productId: String, productAction: ProductAction) {
        when(productAction) {
            ProductAction.AddToCart -> clickAtcButtonProductWithVariant(productId)
            ProductAction.Buy, ProductAction.OCC -> clickBeliButtonProductWithVariant(productId)
            else -> {
                //no-op
            }
        }
    }

    override fun clickProductAction(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        productAction: ProductAction,
        bottomInsetsType: BottomInsetsType,
        shopInfo: PlayPartnerInfo
    ) {
        when(productAction) {
            ProductAction.AddToCart ->
                clickAtcButtonProductWithNoVariant(trackingQueue, product, sectionInfo, cartId, shopInfo)
            ProductAction.Buy -> {
                clickBeliButtonProductWithNoVariant(trackingQueue, product, sectionInfo, cartId, shopInfo)
            }
            else -> {
                //no-op
            }
        }
    }

    override fun clickSeeToasterAfterAtc() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            KEY_TRACK_CLICK_GROUP_CHAT,
            KEY_TRACK_GROUP_CHAT_ROOM,
            "$KEY_TRACK_CLICK lihat in message ticker",
            "$channelId - ${channelType.value}"
        )
    }

    override fun impressPinnedProductInCarousel(
        product: PlayProductUiModel.Product,
        position: Int
    ) {
        val trackerMap = mapOf(
            KEY_EVENT to KEY_EVENT_PRODUCT_VIEW,
            KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
            KEY_EVENT_ACTION to "view on pinned featured product",
            KEY_EVENT_LABEL to "$channelId - ${product.id} - ${channelType.value} - is rilisan spesial ${product.isRilisanSpesial}",
            KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VAL_CURRENT_SITE,
            KEY_ITEM_LIST to "/groupchat - featured product",
            KEY_SESSION_IRIS to irisSessionId,
            KEY_USER_ID to userId,
            KEY_ECOMMERCE to getNewImpressionsEcommerce(product, position)
        )

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun clickPinnedProductInCarousel(product: PlayProductUiModel.Product, position: Int) {
        val trackerMap = mapOf(
            KEY_EVENT to KEY_EVENT_PRODUCT_CLICK,
            KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
            KEY_EVENT_ACTION to "click pinned featured product tagging",
            KEY_EVENT_LABEL to "$channelId - ${product.id} - ${channelType.value} - is rilisan spesial ${product.isRilisanSpesial}",
            KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VAL_CURRENT_SITE,
            KEY_ITEM_LIST to "/groupchat - featured product",
            KEY_SESSION_IRIS to irisSessionId,
            KEY_USER_ID to userId,
            KEY_ECOMMERCE to getNewClickEcommerce(product, position)
        )

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun buyPinnedProductInCarousel(
        product: PlayProductUiModel.Product,
        cartId: String,
        quantity: Int,
        action: ProductAction,
    ) {
        val trackerMap = mapOf(
            KEY_EVENT to KEY_EVENT_ADD_TO_CART,
            KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
            KEY_EVENT_ACTION to "click buy pinned product",
            KEY_EVENT_LABEL to "$channelId - ${product.id} - ${channelType.value} - is rilisan spesial ${product.isRilisanSpesial} - beli langsung ${action == ProductAction.OCC}",
            KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VAL_CURRENT_SITE,
            KEY_SESSION_IRIS to irisSessionId,
            KEY_USER_ID to userId,
            KEY_ECOMMERCE to getNewAtcEcommerce(
                product = product,
                cartId = cartId,
                quantity = quantity,
                dimension40 = "/groupchat - featured product"
            )
        )

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun atcPinnedProductInCarousel(
        product: PlayProductUiModel.Product,
        cartId: String,
        quantity: Int,
    ) {
        val trackerMap = mapOf(
            KEY_EVENT to KEY_EVENT_ADD_TO_CART,
            KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
            KEY_EVENT_ACTION to "click atc pinned product",
            KEY_EVENT_LABEL to "$channelId - ${product.id} - ${channelType.value} - is rilisan spesial ${product.isRilisanSpesial}",
            KEY_BUSINESS_UNIT to VAL_BUSINESS_UNIT,
            KEY_CURRENT_SITE to VAL_CURRENT_SITE,
            KEY_SESSION_IRIS to irisSessionId,
            KEY_USER_ID to userId,
            KEY_ECOMMERCE to getNewAtcEcommerce(
                product = product,
                cartId = cartId,
                quantity = quantity,
                dimension40 = "/groupchat - featured product"
            )
        )

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun impressToasterAtcPinnedProductCarousel() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("view - pinned lihat keranjang")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - ${channelType.value}")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(VAL_CURRENT_SITE)
            .build()
            .send()
    }

    /**
     * Util
     */
    private fun generateBaseEventLabel(
        productId: String,
        campaignId: String
    ): String = "$channelId - $productId - ${channelType.value} - $campaignId"

    private fun clickBeliButtonProductWithVariant(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            KEY_TRACK_CLICK_GROUP_CHAT,
            KEY_TRACK_GROUP_CHAT_ROOM,
            "$KEY_TRACK_CLICK buy in bottom sheet with varian",
            "$channelId - $productId - ${channelType.value}"
        )
    }

    private fun clickAtcButtonProductWithVariant(productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            KEY_TRACK_CLICK_GROUP_CHAT,
            KEY_TRACK_GROUP_CHAT_ROOM,
            "$KEY_TRACK_CLICK atc in bottom sheet with varian",
            "$channelId - $productId - ${channelType.value}"
        )
    }

    private fun clickBeliButtonProductWithNoVariant(trackingQueue: TrackingQueue,
                                                    product: PlayProductUiModel.Product,
                                                    sectionInfo: ProductSectionUiModel.Section,
                                                    cartId: String,
                                                    shopInfo: PlayPartnerInfo) {
        val (eventAction, eventLabel) = when (sectionInfo.config.type) {
            ProductSectionType.Active -> Pair("$KEY_TRACK_CLICK - buy in ongoing section", generateBaseEventLabel(productId = product.id, campaignId = sectionInfo.id))
            else -> Pair(KEY_TRACK_CLICK + "buy in bottom sheet", "$channelId - ${product.id} - ${channelType.value}")
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
            hashMapOf(
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
                KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                KEY_PRODUCT_ID to product.id,
                KEY_PRODUCT_NAME to product.title,
                KEY_PRODUCT_URL to product.applink.toString(),
                KEY_CHANNEL to channelName
            )
        )
    }

    private fun clickAtcButtonProductWithNoVariant(trackingQueue: TrackingQueue,
                                                   product: PlayProductUiModel.Product,
                                                   sectionInfo: ProductSectionUiModel.Section,
                                                   cartId: String,
                                                   shopInfo: PlayPartnerInfo) {
        val (eventAction, eventLabel) = when (sectionInfo.config.type) {
            ProductSectionType.Active -> Pair("${KEY_TRACK_CLICK} - atc in ongoing section", generateBaseEventLabel(productId = product.id, campaignId = sectionInfo.id))
            else -> Pair(KEY_TRACK_CLICK + "atc in bottom sheet", "$channelId - ${product.id} - ${channelType.value}")
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
            hashMapOf(
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
                KEY_IS_LOGGED_IN_STATUS to isLoggedIn,
                KEY_PRODUCT_ID to product.id,
                KEY_PRODUCT_NAME to product.title,
                KEY_PRODUCT_URL to product.applink.toString(),
                KEY_CHANNEL to channelName
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
                "$channelId - ${product.id} - ${channelType.value}"
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
                KEY_CHANNEL to channelName
            )
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
                "$channelId - ${product.id} - ${channelType.value}"
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
                KEY_CHANNEL to channelName
            )
        )
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

    private fun getNewImpressionsEcommerce(
        product: PlayProductUiModel.Product,
        position: Int,
    ): Map<String, Any> {
        return mapOf(
            KEY_CURRENCY_CODE to VAL_CURRENCY_CODE,
            KEY_IMPRESSIONS to listOf(getNewProductEcommerce(product, position))
        )
    }

    private fun getNewClickEcommerce(
        product: PlayProductUiModel.Product,
        position: Int,
    ): Map<String, Any> {
        return mapOf(
            KEY_CURRENCY_CODE to VAL_CURRENCY_CODE,
            KEY_CLICK to mapOf(
                KEY_PRODUCTS to listOf(getNewProductEcommerce(product, position))
            )
        )
    }

    private fun getNewAtcEcommerce(
        product: PlayProductUiModel.Product,
        cartId: String,
        quantity: Int,
        dimension40: String,
    ): Map<String, Any> {
        return mapOf(
            KEY_CURRENCY_CODE to VAL_CURRENCY_CODE,
            KEY_ADD to mapOf(
                KEY_PRODUCTS to listOf(
                    mapOf(
                        KEY_CATEGORY_ID to "",
                        KEY_DIMENSION_40 to dimension40,
                        KEY_DIMENSION_45 to cartId,
                        KEY_ITEM_BRAND to "",
                        KEY_ITEM_CATEGORY to "",
                        KEY_ITEM_ID to product.id,
                        KEY_ITEM_NAME to product.title,
                        KEY_ITEM_VARIANT to "",
                        KEY_PRICE to product.price.currentPrice.toString(),
                        KEY_QUANTITY to quantity.toString(),
                        KEY_SHOP_ID to product.shopId,
                        KEY_SHOP_NAME to "",
                        KEY_SHOP_TYPE to "",
                    )
                )
            )
        )
    }

    private fun getNewProductEcommerce(
        product: PlayProductUiModel.Product,
        position: Int,
    ) = mapOf(
        KEY_INDEX to (position + 1).toString(),
        KEY_ITEM_BRAND to "",
        KEY_ITEM_CATEGORY to "",
        KEY_ITEM_ID to product.id,
        KEY_ITEM_NAME to product.title,
        KEY_ITEM_VARIANT to "",
        KEY_PRICE to product.price.currentPrice.toString()
    )

    private val ProductPrice.currentPrice
        get() = when (this) {
            is OriginalPrice -> priceNumber
            is DiscountedPrice -> discountedPriceNumber
        }

    companion object {
        private const val KEY_ITEM_LIST = "item_list"
        private const val KEY_PRODUCT_NAME = "productName"
        private const val KEY_PRODUCT_URL = "productUrl"
        private const val KEY_TRACK_ADD_TO_CART = "addToCart"
    }
}
