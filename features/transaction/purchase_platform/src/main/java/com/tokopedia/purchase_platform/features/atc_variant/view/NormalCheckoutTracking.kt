package com.tokopedia.purchase_platform.features.atc_variant.view

import android.os.Bundle
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.google.firebase.analytics.FirebaseAnalytics
import com.tokopedia.purchase_platform.features.atc_variant.model.ProductInfoAndVariant
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class NormalCheckoutTracking {
    companion object {
        private const val KEY_PRODUCT_ID = "productId"
        private const val KEY_DIMENSION_81 = "dimension81"
        const val VIEW_PDP = "viewPDP"
        const val CLICK_PDP = "clickPDP"
        const val PRODUCT_DETAIL_PAGE = "product detail page"
        const val CHAT_DETAIL_PAGE = "chat detail"
        const val HARGA_FINAL_TRADEIN = "harga final trade in"
        const val SELECT_COLOR_VARIANT = "select color on variants page"
        const val SELECT_SIZE_VARIANT = "select size on variants page"
        const val NONE_OTHER = "none / other"
        const val ACTION_VIEW_ERROR_WHEN_ADD_TO_CART = "view error when add to cart"
        private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        private const val VALUE_NONE_OTHER = "none / other"
    }

    private var isTrackTradeIn = false

    fun eventViewErrorWhenAddToCart(errorMessage: String) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                VIEW_PDP,
                PRODUCT_DETAIL_PAGE,
                ACTION_VIEW_ERROR_WHEN_ADD_TO_CART,
                "not success - $errorMessage")
    }

    fun eventClickBuyInVariantNotLogin(productId: String?) {
        val mapEvent = TrackAppUtils.gtmData(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - beli on variants page - before login",
                productId ?: ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId ?: ""
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickAddToCartInVariant(originalProductInfoAndVariant: ProductInfoAndVariant?,
                                     selectedVariantId: String,
                                     selectedProductInfo: ProductInfo,
                                     qty: Int,
                                     shopId: String? = NONE_OTHER,
                                     shopType: String? = NONE_OTHER,
                                     shopName: String? = NONE_OTHER,
                                     cartId: String? = NONE_OTHER,
                                     trackerAttribution: String?,
                                     trackerListName: String?,
                                     multiOrigin: Boolean,
                                     reference: String = "",
                                     isFreeOngkir: Boolean
    ) {
        eventClickAddToCartOrBuyInVariant(originalProductInfoAndVariant,
            "click - tambah ke keranjang on variants page",
            selectedVariantId, selectedProductInfo,
            qty, shopId, shopType, shopName, cartId,
            trackerAttribution, trackerListName, multiOrigin, reference, isFreeOngkir)
    }

    fun eventClickBuyInVariant(originalProductInfoAndVariant: ProductInfoAndVariant?,
                               selectedVariantId: String,
                               selectedProductInfo: ProductInfo,
                               qty: Int,
                               shopId: String? = NONE_OTHER,
                               shopType: String? = NONE_OTHER,
                               shopName: String? = NONE_OTHER,
                               cartId: String? = NONE_OTHER,
                               trackerAttribution: String?,
                               trackerListName: String?,
                               multiOrigin: Boolean, isFreeOngkir:Boolean) {
        eventClickAddToCartOrBuyInVariant(originalProductInfoAndVariant,
            "click - beli on variants page",
            selectedVariantId, selectedProductInfo,
            qty, shopId, shopType, shopName, cartId,
            trackerAttribution, trackerListName, multiOrigin,"",isFreeOngkir)
    }

    fun eventClickBuyTradeIn(originalProductInfoAndVariant: ProductInfoAndVariant?,
                             selectedVariantId: String,
                             selectedProductInfo: ProductInfo,
                             qty: Int,
                             shopId: String? = NONE_OTHER,
                             shopType: String? = NONE_OTHER,
                             shopName: String? = NONE_OTHER,
                             cartId: String? = NONE_OTHER,
                             trackerAttribution: String?,
                             trackerListName: String?) {
        isTrackTradeIn = true
        eventClickAddToCartOrBuyInVariant(originalProductInfoAndVariant,
            "click beli sekarang",
            selectedVariantId, selectedProductInfo,
            qty, shopId, shopType, shopName, cartId,
            trackerAttribution, trackerListName, false)
    }

    private fun eventClickAddToCartOrBuyInVariant(originalProductInfoAndVariant: ProductInfoAndVariant?,
                                                  actionLabel: String,
                                                  selectedVariantId: String,
                                                  selectedProductInfo: ProductInfo,
                                                  qty: Int,
                                                  shopId: String? = NONE_OTHER,
                                                  shopType: String? = NONE_OTHER,
                                                  shopName: String? = NONE_OTHER,
                                                  cartId: String? = NONE_OTHER,
                                                  trackerAttribution: String?,
                                                  trackerListName: String?,
                                                  multiOrigin: Boolean,
                                                  reference: String = "",
                                                  isFreeOngkir: Boolean = false
                                                  ) {
        val dimension83 = if (isFreeOngkir) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
        if (originalProductInfoAndVariant == null) {
            isTrackTradeIn = false
            return
        }
        var productVariantString = (originalProductInfoAndVariant.productVariant
            .getOptionListString(selectedVariantId)?.joinToString(" - ")
            ?: "non variant")
        val category: String = if (isTrackTradeIn) {
            productVariantString = ""
            HARGA_FINAL_TRADEIN
        } else if (reference == ApplinkConst.TOPCHAT) {
            CHAT_DETAIL_PAGE
        } else {
            PRODUCT_DETAIL_PAGE
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            mutableMapOf<String, Any>(
                "event" to "addToCart",
                "eventCategory" to category,
                "eventAction" to actionLabel,
                "eventLabel" to productVariantString,
                KEY_PRODUCT_ID to selectedProductInfo.basic.id,
                "ecommerce" to mutableMapOf(
                    "currencyCode" to "IDR",
                    "add" to mutableMapOf(
                        "products" to arrayListOf(mutableMapOf(
                            "name" to selectedProductInfo.basic.name,
                            "id" to selectedProductInfo.basic.id,
                            "price" to selectedProductInfo.basic.price.toDouble(),
                            "brand" to selectedProductInfo.brand.name,
                            "category" to selectedProductInfo.category.detail.joinToString("/") { it.name },
                            "variant" to productVariantString,
                            "quantity" to qty,
                            "shop_id" to (shopId ?: NONE_OTHER),
                            "shop_type" to (shopType ?: NONE_OTHER),
                            "shop_name" to (shopName ?: NONE_OTHER),
                            "picture" to (selectedProductInfo.pictures?.get(0)?.urlOriginal
                                ?: NONE_OTHER),
                            "url" to selectedProductInfo.basic.url,
                            "category_id" to selectedProductInfo.category.id,
                            "dimension45" to (cartId ?: NONE_OTHER),
                            "dimension38" to (trackerAttribution ?: NONE_OTHER),
                            "dimension54" to getMultiOriginAttribution(multiOrigin),
                            "dimension83" to dimension83,
                            KEY_DIMENSION_81 to  shopType

                        )),
                        "actionField" to mutableMapOf("list" to (trackerListName ?: ""))
                    )
                )
            ))
        isTrackTradeIn = false

        eventClickAddToCartOrBuyInVariantV5(originalProductInfoAndVariant, actionLabel,
                selectedVariantId, selectedProductInfo, qty, shopId, shopType, shopName,
                cartId, trackerAttribution, multiOrigin,isFreeOngkir)
    }

    private fun eventClickAddToCartOrBuyInVariantV5(
            originalProductInfoAndVariant: ProductInfoAndVariant?,
            actionLabel: String,
            selectedVariantId: String,
            selectedProductInfo: ProductInfo,
            qty: Int,
            shopId: String? = NONE_OTHER,
            shopType: String? = NONE_OTHER,
            shopName: String? = NONE_OTHER,
            cartId: String? = NONE_OTHER,
            trackerAttribution: String?,
            multiOrigin: Boolean,
            isFreeOngkir: Boolean) {
        val dimension83 = if(isFreeOngkir) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
        if (originalProductInfoAndVariant == null) {
            isTrackTradeIn = false
            return
        }
        var productVariantString = (originalProductInfoAndVariant.productVariant
                .getOptionListString(selectedVariantId)?.joinToString(" - ")
                ?: "non variant")
        val category: String = if (isTrackTradeIn) {
            productVariantString = ""
            HARGA_FINAL_TRADEIN
        } else
            PRODUCT_DETAIL_PAGE


        val ecommerce = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, selectedProductInfo.basic.id.toString())
            putString(FirebaseAnalytics.Param.ITEM_NAME, selectedProductInfo.basic.name)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, selectedProductInfo.brand.name)
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, selectedProductInfo.category.detail.joinToString("/") { it.name })
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, productVariantString)
            putString(KEY_PRODUCT_ID, selectedProductInfo.basic.id.toString())
            putDouble(FirebaseAnalytics.Param.PRICE, selectedProductInfo.basic.price.toDouble())
            putLong(FirebaseAnalytics.Param.INDEX, 1)
            putLong(FirebaseAnalytics.Param.QUANTITY, qty.toLong())
            putString("dimension38", trackerAttribution ?: NONE_OTHER)
            putString("dimension45", cartId ?: NONE_OTHER)
            putString("dimension54", getMultiOriginAttribution(multiOrigin))
            putString("dimension83", dimension83)
            putString(KEY_DIMENSION_81, shopType)

        }

        val event = Bundle().apply {
            putString("eventCategory", category)
            putString("eventAction",actionLabel)
            putString("eventLabel", productVariantString)
            putString("screenName", "/product")
            putString("shopId", shopId ?: NONE_OTHER)
            putString("shopName", shopName ?: NONE_OTHER)
            putString("shopType", shopType ?: NONE_OTHER)
            putString("categoryId", selectedProductInfo.category.id)
            putBundle("items", ecommerce)
        }

        TrackApp.getInstance().gtm.pushEECommerce("addToCart", event)
        isTrackTradeIn = false
    }

    fun eventClickAtcInVariantNotLogin(productId: String?) {
        val mapEvent = TrackAppUtils.gtmData(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - tambah ke keranjang on variants page - before login",
                productId ?: ""
        )
        mapEvent[KEY_PRODUCT_ID] = productId ?: ""
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventSelectSizeVariant(size: String?, productId: String) {
        if (size.isNullOrEmpty()) return
        val mapEvent = TrackAppUtils.gtmData(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                SELECT_SIZE_VARIANT,
                size
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventSelectColorVariant(color: String?, productId: String) {
        if (color.isNullOrEmpty()) return
        val mapEvent = TrackAppUtils.gtmData(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                SELECT_COLOR_VARIANT,
                color
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    ////////////////////////////////////////////////////////////
    // APSSFLYER
    ////////////////////////////////////////////////////////////

    fun eventAppsFlyerInitiateCheckout(productId: String,
                                       priceItem: String,
                                       quantity: Int,
                                       productName: String,
                                       category: String) {
        TrackApp.getInstance()?.appsFlyer?.sendEvent(
            AFInAppEventType.INITIATED_CHECKOUT,
            mutableMapOf<String, Any>(
                AFInAppEventParameterName.CONTENT_ID to productId,
                AFInAppEventParameterName.CONTENT_TYPE to "product",
                AFInAppEventParameterName.DESCRIPTION to productName,
                AFInAppEventParameterName.CURRENCY to "IDR",
                AFInAppEventParameterName.QUANTITY to quantity,
                AFInAppEventParameterName.PRICE to priceItem,
                "category" to category
            ))
    }

    fun eventAppsFlyerAddToCart(productId: String, priceItem: String,
                                quantity: Int,
                                productName: String,
                                category: String) {
        TrackApp.getInstance()?.appsFlyer?.sendEvent(
            AFInAppEventType.ADD_TO_CART,
            mutableMapOf<String, Any>(
                AFInAppEventParameterName.CONTENT_ID to productId,
                AFInAppEventParameterName.DESCRIPTION to productName,
                AFInAppEventParameterName.CURRENCY to "IDR",
                AFInAppEventParameterName.QUANTITY to quantity,
                AFInAppEventParameterName.PRICE to priceItem,
                "category" to category
            ))
    }

    private fun getMultiOriginAttribution(isMultiOrigin: Boolean): String = when(isMultiOrigin) {
        true -> "tokopedia"
        else -> "regular"
    }
}
