package com.tokopedia.normalcheckout.view

import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.normalcheckout.model.ProductInfoAndVariant
import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.track.TrackApp

class NormalCheckoutTracking {
    companion object {
        const val CLICK_PDP = "clickPDP"
        const val PRODUCT_DETAIL_PAGE = "product detail page"
        const val SELECT_COLOR_VARIANT = "select color on variants page"
        const val SELECT_SIZE_VARIANT = "select size on variants page"
        const val NONE_OTHER = "none / other"
    }

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
                                     qty:Int,
                                     shopId:String? = NONE_OTHER,
                                     shopType:String? = NONE_OTHER,
                                     shopName:String? = NONE_OTHER,
                                     cartId:String? = NONE_OTHER,
                                     trackerAttribution:String?,
                                     trackerListName:String?) {
        eventClickAddToCartOrBuyInVariant(originalProductInfoAndVariant,
            "click - tambah ke keranjang on variants page",
            selectedVariantId, selectedProductInfo,
            qty, shopId, shopType, shopName,cartId,
            trackerAttribution, trackerListName)
    }

    fun eventClickBuyInVariant(originalProductInfoAndVariant: ProductInfoAndVariant?,
                               selectedVariantId: String,
                               selectedProductInfo: ProductInfo,
                               qty:Int,
                               shopId:String? = NONE_OTHER,
                               shopType:String? = NONE_OTHER,
                               shopName:String? = NONE_OTHER,
                               cartId:String? = NONE_OTHER,
                               trackerAttribution:String?,
                               trackerListName:String?) {
        eventClickAddToCartOrBuyInVariant(originalProductInfoAndVariant,
            "click - beli on variants page",
            selectedVariantId, selectedProductInfo,
            qty, shopId, shopType, shopName, cartId,
            trackerAttribution, trackerListName)
    }

    private fun eventClickAddToCartOrBuyInVariant(originalProductInfoAndVariant: ProductInfoAndVariant?,
                                          actionLabel: String,
                                          selectedVariantId: String,
                                          selectedProductInfo: ProductInfo,
                                          qty:Int,
                                          shopId:String? = NONE_OTHER,
                                          shopType:String? = NONE_OTHER,
                                          shopName:String? = NONE_OTHER,
                                          cartId:String? = NONE_OTHER,
                                          trackerAttribution:String?,
                                          trackerListName:String?) {
        if (originalProductInfoAndVariant == null) return
        val productVariantString = (originalProductInfoAndVariant.productVariant
            .getOptionListString(selectedVariantId)?.joinToString(" - ")
            ?: "non variant")
        TrackApp.getInstance()?.gtm?.sendEnhanceECommerceEvent(
            mutableMapOf<String, Any>(
                "event" to "addToCart",
                "eventCategory" to PRODUCT_DETAIL_PAGE,
                "eventAction" to actionLabel,
                "eventLabel" to productVariantString,
                "ecommerce" to mutableMapOf<String, Any>(
                    "currencyCode" to "IDR",
                    "add" to mutableMapOf<String, Any>(
                        "name" to selectedProductInfo.basic.name,
                        "id" to selectedProductInfo.basic.id,
                        "price" to selectedProductInfo.basic.price,
                        "brand" to selectedProductInfo.brand.name,
                        "category" to selectedProductInfo.category.detail.joinToString("/"),
                        "variant" to productVariantString,
                        "quantity" to qty,
                        "shop_id" to (shopId ?: NONE_OTHER),
                        "shop_type" to (shopType ?: NONE_OTHER),
                        "shop_name" to (shopName ?: NONE_OTHER),
                        "picture" to (selectedProductInfo.pictures?.get(0)?.urlOriginal ?: NONE_OTHER),
                        "url" to selectedProductInfo.basic.url,
                        "category_id" to selectedProductInfo.category.id,
                        "cart_id" to (cartId ?: NONE_OTHER),
                        "dimension38" to (trackerAttribution ?: NONE_OTHER)
                    ).apply {
                        if (trackerListName?.isNotEmpty() == true) {
                            put("actionField", mutableMapOf("list" to trackerListName))
                        }
                    }
                )
            ))
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

    fun eventAppsFlyer(productId: String, priceItem: String,
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
}
