package com.tokopedia.normalcheckout.view

import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.normalcheckout.model.ProductInfoAndVariant
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.track.TrackApp

class NormalCheckoutTracking {
    companion object {
        const val CLICK_PDP = "clickPDP"
        const val PRODUCT_DETAIL_PAGE = "product detail page"
        const val HARGA_FINAL_TRADEIN = "harga final trade in"
        const val SELECT_COLOR_VARIANT = "select color on variants page"
        const val SELECT_SIZE_VARIANT = "select size on variants page"
        const val NONE_OTHER = "none / other"
    }

    private var isTrackTradeIn = false

    fun eventClickBuyInVariantNotLogin(productId: String?) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            CLICK_PDP,
            PRODUCT_DETAIL_PAGE,
            "click - beli on variants page - before login",
            productId ?: "")
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
                                     multiOrigin: Boolean) {
        eventClickAddToCartOrBuyInVariant(originalProductInfoAndVariant,
            "click - tambah ke keranjang on variants page",
            selectedVariantId, selectedProductInfo,
            qty, shopId, shopType, shopName, cartId,
            trackerAttribution, trackerListName, multiOrigin)
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
                               multiOrigin: Boolean) {
        eventClickAddToCartOrBuyInVariant(originalProductInfoAndVariant,
            "click - beli on variants page",
            selectedVariantId, selectedProductInfo,
            qty, shopId, shopType, shopName, cartId,
            trackerAttribution, trackerListName, multiOrigin)
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
                                                  multiOrigin: Boolean) {
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
        TrackApp.getInstance()?.gtm?.sendEnhanceEcommerceEvent(
            mutableMapOf<String, Any>(
                "event" to "addToCart",
                "eventCategory" to category,
                "eventAction" to actionLabel,
                "eventLabel" to productVariantString,
                "ecommerce" to mutableMapOf<String, Any>(
                    "currencyCode" to "IDR",
                    "add" to mutableMapOf<String, Any>(
                        "products" to arrayListOf(mutableMapOf(
                            "name" to selectedProductInfo.basic.name,
                            "id" to selectedProductInfo.basic.id,
                            "price" to selectedProductInfo.basic.price.toDouble(),
                            "brand" to selectedProductInfo.brand.name,
                            "category" to selectedProductInfo.category.detail.map { it.name }.joinToString("/"),
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
                            "dimension54" to getMultiOriginAttribution(multiOrigin)
                        )),
                        "actionField" to mutableMapOf("list" to (trackerListName ?: ""))
                    )
                )
            ))
        isTrackTradeIn = false
    }


    fun eventClickAtcInVariantNotLogin(productId: String?) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            CLICK_PDP,
            PRODUCT_DETAIL_PAGE,
            "click - tambah ke keranjang on variants page - before login",
            productId ?: "")
    }

    fun eventSelectSizeVariant(size: String?) {
        if (size.isNullOrEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            CLICK_PDP,
            PRODUCT_DETAIL_PAGE,
            SELECT_SIZE_VARIANT,
            size)
    }

    fun eventSelectColorVariant(color: String?) {
        if (color.isNullOrEmpty()) return
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
            CLICK_PDP,
            PRODUCT_DETAIL_PAGE,
            SELECT_COLOR_VARIANT,
            color)
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
