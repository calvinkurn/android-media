package com.tokopedia.play.analytic.tokonow

import android.os.Bundle
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
    private val userSession: UserSessionInterface, private val trackingQueue: TrackingQueue,
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
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - out of now coverage",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_USER_ID to userId,
            )
        )
    }

    /**
     * Button Ganti Alamat
     */
    override fun impressChooseAddressNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - ganti alamat",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun clickChooseAddressNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_CONTENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - ganti alamat",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    /**
     * Click cek jangkauan
     */
    override fun clickInfoAddressWidgetNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_CONTENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - now cek jangkauan",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_USER_ID to userId,
            )
        )
    }

    override fun impressInfoNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - now info bottomsheet",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun clickInfoNow() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_CONTENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - now info bottomsheet",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun impressNowToaster() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - now toaster",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun clickLihatNowToaster() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_CONTENT,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "click - lihat now toaster",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    override fun clickProductBottomSheetNow(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                "productClick",
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - now product bottomsheet",
                "$channelId - ${product.id} - ${channelType.value}"
            ),
            hashMapOf (
                "ecommerce" to hashMapOf(
                    "click" to hashMapOf(
                        "actionField" to hashMapOf( "list" to "/groupchat - bottom sheet now"),
                        "products" to  listOf(convertProductToHashMapWithList(product, position + 1, "bottom sheet"))
                    )
                )
            ),
            generateBaseTracking())
    }

    override fun clickFeaturedProductNow(featuredProduct: PlayProductUiModel.Product, position: Int) {
        trackingQueue.putEETracking(
            EventModel(
                "productClick",
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - now product carousel",
                "$channelId - ${featuredProduct.id} - ${channelType.value}"
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
            putString(KEY_EVENT_CATEGORY, KEY_TRACK_GROUP_CHAT_ROOM)
            putString(KEY_EVENT_ACTION, "view - now product bottomsheet",)
            putString(KEY_EVENT_LABEL,  "$channelId - ${products.keys.firstOrNull()?.product?.id.orEmpty()} - ${channelType.value}")
            putString(KEY_CURRENT_SITE, KEY_TRACK_CURRENT_SITE)
            putString(KEY_SESSION_IRIS, TrackApp.getInstance().gtm.irisSessionId)
            putString(KEY_USER_ID, userId)
            putString(KEY_BUSINESS_UNIT, KEY_TRACK_BUSINESS_UNIT)
            putParcelableArrayList("items", items)
            putString(KEY_ITEM_LIST, "/groupchat - bottom sheet")
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            KEY_EVENT_ITEM_LIST, dataLayer
        )
    }

    override fun impressFeaturedProductNow(
        products: List<Pair<PlayProductUiModel.Product, Int>>,
    ) {
        if (products.isEmpty()) return

        trackingQueue.putEETracking(
            EventModel(
                "productView",
                KEY_TRACK_GROUP_CHAT_ROOM,
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
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT
            )
        )
    }

    override fun clickBeliNowProduct(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                KEY_TRACK_ADD_TO_CART,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - buy now product",
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
        shopInfo: PlayPartnerInfo,
    ) {
        trackingQueue.putEETracking(
            EventModel(
                KEY_TRACK_ADD_TO_CART,
                KEY_TRACK_GROUP_CHAT_ROOM,
                "$KEY_TRACK_CLICK - atc now product", "$channelId - ${product.id} - ${channelType.value}"
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
                KEY_EVENT to KEY_TRACK_VIEW_CONTENT_IRIS,
                KEY_EVENT_CATEGORY to KEY_TRACK_GROUP_CHAT_ROOM,
                KEY_EVENT_ACTION to "view - lihat keranjang",
                KEY_EVENT_LABEL to "$channelId - ${channelType.value}",
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            )
        )
    }

    private fun generateBaseTracking(): HashMap<String, Any>{
        return hashMapOf(
            KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
            KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
            KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
            KEY_USER_ID to userId,
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

    companion object {
        private const val KEY_PRODUCT_NAME = "productName"
        private const val KEY_PRODUCT_URL = "productUrl"
        private const val KEY_ITEM_LIST = "item_list"
        private const val KEY_EVENT_ITEM_LIST = "view_item_list"
        private const val KEY_TRACK_ADD_TO_CART = "addToCart"
    }
}
