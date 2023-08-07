package com.tokopedia.play.analytic.tokonow

import android.os.Bundle
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.analytic.*
import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_PRODUCT_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.model.EventModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 17/06/22
 */
class PlayTokoNowAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue
) : PlayTokoNowAnalytic {

    private val userId: String
        get() = userSession.userId

    private val isLoggedIn: Boolean
        get() = userSession.isLoggedIn

    private var channelId: String = ""
    private var channelType: PlayChannelType = PlayChannelType.Unknown
    private var channelName = ""

    override fun sendDataNow(channelId: String, channelType: PlayChannelType, channelName: String) {
        this.channelId = channelId
        this.channelType = channelType
        this.channelName = channelName
    }

    override fun impressAddressWidgetNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewContentIris,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "view - out of now coverage",
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.userId to userId
            )
        )
    }

    /**
     * Button Ganti Alamat
     */
    override fun impressChooseAddressNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewContentIris,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "view - ganti alamat",
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace
            )
        )
    }

    override fun clickChooseAddressNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickContent,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "click - ganti alamat",
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace
            )
        )
    }

    /**
     * Click cek jangkauan
     */
    override fun clickInfoAddressWidgetNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickContent,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "click - now cek jangkauan",
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.userId to userId
            )
        )
    }

    override fun impressInfoNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewContentIris,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "view - now info bottomsheet",
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace
            )
        )
    }

    override fun clickInfoNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickContent,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "click - now info bottomsheet",
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace
            )
        )
    }

    override fun impressNowToaster() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewContentIris,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "view - now toaster",
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace
            )
        )
    }

    override fun clickLihatNowToaster() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickContent,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "click - lihat now toaster",
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace
            )
        )
    }

    override fun clickProductBottomSheetNow(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int
    ) {
        trackingQueue.putEETracking(
            EventModel(
                "productClick",
                EventCategory.groupChatRoom,
                "click - now product bottomsheet",
                "$channelId - ${product.id} - ${channelType.value}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "click" to hashMapOf(
                        "actionField" to hashMapOf("list" to "/groupchat - bottom sheet now"),
                        "products" to listOf(convertProductToHashMapWithList(product, position + 1, "bottom sheet"))
                    )
                )
            ),
            generateBaseTracking()
        )
    }

    override fun clickFeaturedProductNow(featuredProduct: PlayProductUiModel.Product, position: Int) {
        trackingQueue.putEETracking(
            EventModel(
                "productClick",
                EventCategory.groupChatRoom,
                "click - now product carousel",
                "$channelId - ${featuredProduct.id} - ${channelType.value}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "click" to hashMapOf(
                        "actionField" to hashMapOf("list" to "/groupchat - featured product"),
                        "products" to listOf(convertProductToHashMapWithList(featuredProduct, position, "featured product"))
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
                KEY_CHANNEL to channelName
            )
        )
    }

    override fun impressProductBottomSheetNow(
        products: Map<ProductSheetAdapter.Item.Product, Int>
    ) {
        if (products.isEmpty()) return

        val items = arrayListOf<Bundle>().apply {
            products.forEach {
                add(productsToBundle(it.key.product, it.value, "bottom sheet"))
            }
        }

        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, KEY_EVENT_ITEM_LIST)
            putString(Key.eventCategory, EventCategory.groupChatRoom)
            putString(Key.eventAction, "view - now product bottomsheet")
            putString(Key.eventLabel, "$channelId - ${products.keys.firstOrNull()?.product?.id.orEmpty()} - ${channelType.value}")
            putString(Key.currentSite, CurrentSite.tokopediaMarketplace)
            putString(Key.sessionIris, TrackApp.getInstance().gtm.irisSessionId)
            putString(Key.userId, userId)
            putString(Key.businessUnit, BusinessUnit.play)
            putParcelableArrayList("items", items)
            putString(KEY_ITEM_LIST, "/groupchat - bottom sheet")
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            KEY_EVENT_ITEM_LIST,
            dataLayer
        )
    }

    override fun impressFeaturedProductNow(
        products: List<Pair<PlayProductUiModel.Product, Int>>
    ) {
        if (products.isEmpty()) return

        trackingQueue.putEETracking(
            EventModel(
                "productView",
                EventCategory.groupChatRoom,
                "view - now product carousel",
                "$channelId - ${products.first().first.id} - ${channelType.value}"
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
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId,
                Key.businessUnit to BusinessUnit.play
            )
        )
    }

    override fun clickBeliNowProduct(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo
    ) {
        trackingQueue.putEETracking(
            EventModel(
                Event.addToCart,
                EventCategory.groupChatRoom,
                "click - buy now product",
                "$channelId - ${product.id} - ${channelType.value}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "currencyCode" to "IDR",
                    "add" to hashMapOf(
                        "products" to listOf(convertProductAndShopToHashMapWithList(product, shopInfo, "/groupchat - bottom sheet"))
                    )
                )
            ),
            generateBaseTracking()
        )
    }

    override fun clickAtcNowProduct(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo
    ) {
        trackingQueue.putEETracking(
            EventModel(
                Event.addToCart,
                EventCategory.groupChatRoom,
                "click - atc now product",
                "$channelId - ${product.id} - ${channelType.value}"
            ),
            hashMapOf(
                "ecommerce" to hashMapOf(
                    "currencyCode" to "IDR",
                    "add" to hashMapOf(
                        "products" to listOf(convertProductAndShopToHashMapWithList(product, shopInfo, "/groupchat - bottom sheet"))
                    )
                )
            ),
            generateBaseTracking()
        )
    }

    override fun impressGlobalToaster() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewContentIris,
                Key.eventCategory to EventCategory.groupChatRoom,
                Key.eventAction to "view - lihat keranjang",
                Key.eventLabel to "$channelId - ${channelType.value}",
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace
            )
        )
    }

    private fun generateBaseTracking(): HashMap<String, Any> {
        return hashMapOf(
            Key.businessUnit to BusinessUnit.play,
            Key.currentSite to CurrentSite.tokopediaMarketplace,
            Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
            Key.userId to userId
        )
    }

    private fun convertProductToHashMapWithList(product: PlayProductUiModel.Product, position: Int, sourceFrom: String): HashMap<String, Any> {
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
            "list" to "/groupchat - $sourceFrom",
            "position" to position
        )
    }

    private fun convertProductAndShopToHashMapWithList(product: PlayProductUiModel.Product, shopInfo: PlayPartnerInfo, dimension39: String = ""): HashMap<String, Any> {
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
            "category_id" to "",
            "quantity" to product.minQty,
            "shop_id" to shopInfo.id,
            "shop_name" to shopInfo.name,
            "shop_type" to shopInfo.type.value
        )
    }

    private fun productsToBundle(product: PlayProductUiModel.Product, position: Int, sourceFrom: String): Bundle =
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
            putInt("index", position)
        }

    companion object {
        private const val KEY_PRODUCT_NAME = "productName"
        private const val KEY_PRODUCT_URL = "productUrl"
        private const val KEY_ITEM_LIST = "item_list"
        private const val KEY_EVENT_ITEM_LIST = "view_item_list"
    }
}
