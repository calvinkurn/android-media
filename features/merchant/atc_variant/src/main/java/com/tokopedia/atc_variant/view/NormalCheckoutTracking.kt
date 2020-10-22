package com.tokopedia.atc_variant.view

import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_variant.model.ProductInfoAndVariant
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import org.json.JSONArray
import org.json.JSONObject

class NormalCheckoutTracking {
    companion object {
        private const val KEY_PRODUCT_ID = "productId"
        private const val KEY_DIMENSION_81 = "dimension81"
        private const val KEY_DIMENSION_40 = "dimension40"
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

        const val CATEGORY_FIN_PDP_INSURANCE = "fin - product detail page"
        const val ACTION_FIN_PDP_CLICK_INFO = "click - ins - click info"
        const val ACTION_FIN_PDP_INSURANCR_STATE = "click - ins - tick include insurance to cart"
        const val ACTION_FIN_PDP_INSURANCR_BUY = "ins - click ATC"

        const val LABEL_FIN_PDP_INSURANCE = "pdp page"
    }

    private var isTrackTradeIn = false

    fun eventViewErrorWhenAddToCart(errorMessage: String) {
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(
                VIEW_PDP,
                PRODUCT_DETAIL_PAGE,
                ACTION_VIEW_ERROR_WHEN_ADD_TO_CART,
                "not success - $errorMessage")
    }

    fun eventClickInsuranceInfo(productId: String?) {
        val mapEvent = TrackAppUtils.gtmData(
                "",
                CATEGORY_FIN_PDP_INSURANCE,
                ACTION_FIN_PDP_CLICK_INFO,
                LABEL_FIN_PDP_INSURANCE
        )
        mapEvent[KEY_PRODUCT_ID] = productId ?: ""
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickInsuranceBuy(title: String, productId: String) {
        val eventLabel = "pdp page - $title"
        val mapEvent = TrackAppUtils.gtmData(
                "",
                CATEGORY_FIN_PDP_INSURANCE,
                ACTION_FIN_PDP_INSURANCR_BUY,
                eventLabel
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventClickInsuranceState(productId: String?, isChecked: Boolean, title: String) {
        val eventLabel = if (isChecked) {
            "pdp page - tick $title"
        } else {
            "pdp page - untick $title"
        }
        val mapEvent = TrackAppUtils.gtmData(
                "",
                CATEGORY_FIN_PDP_INSURANCE,
                ACTION_FIN_PDP_INSURANCR_STATE,
                eventLabel
        )
        mapEvent[KEY_PRODUCT_ID] = productId ?: ""
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }


    fun eventClickBuyInVariantNotLogin(productInfo: ProductInfo?,
                                       layoutName: String) {
        val mapEvent = TrackAppUtils.gtmData(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - beli on variants page - before login",
                productInfo?.basic?.id.toString()
        )
        addComponentTracker(mapEvent, productInfo, layoutName)
    }

    fun eventClickAddToCartInVariant(irisSessionId: String,
                                     originalProductInfoAndVariant: ProductInfoAndVariant?,
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
                                     reference: String,
                                     isFreeOngkir: Boolean,
                                     customEventLabel: String,
                                     customEventAction: String,
                                     customDimension40: String,
                                     layoutName: String
    ) {
        eventClickAddToCartOrBuyInVariant(
                irisSessionId,
                originalProductInfoAndVariant,
                "click - tambah ke keranjang on variants page",
                selectedVariantId, selectedProductInfo,
                qty, shopId, shopType, shopName, cartId,
                trackerAttribution, trackerListName, multiOrigin, reference, isFreeOngkir,
                customEventLabel, customEventAction,
                customDimension40 = customDimension40
        )
    }

    fun eventClickBuyInVariant(
                              irisSessionId: String,
                               originalProductInfoAndVariant: ProductInfoAndVariant?,
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
                               isFreeOngkir: Boolean,
                               reference: String,
                               customEventLabel: String,
                               customEventAction: String,
                               customDimension40: String,
                               layoutName: String?) {
        eventClickAddToCartOrBuyInVariant(irisSessionId,
                originalProductInfoAndVariant,
                "click - beli on variants page",
                selectedVariantId, selectedProductInfo,
                qty, shopId, shopType, shopName, cartId,
                trackerAttribution, trackerListName, multiOrigin, reference, isFreeOngkir,
                customEventLabel, customEventAction, layoutName ?: "", customDimension40)
    }

    fun eventClickBuyTradeIn(irisSessionId: String,
                             originalProductInfoAndVariant: ProductInfoAndVariant?,
                             selectedVariantId: String,
                             selectedProductInfo: ProductInfo,
                             qty: Int,
                             shopId: String? = NONE_OTHER,
                             shopType: String? = NONE_OTHER,
                             shopName: String? = NONE_OTHER,
                             cartId: String? = NONE_OTHER,
                             trackerAttribution: String?,
                             trackerListName: String?,
                             customEventLabel: String,
                             layoutName: String) {
        isTrackTradeIn = true
        eventClickAddToCartOrBuyInVariant(
                irisSessionId,
                originalProductInfoAndVariant,
                "click beli sekarang",
                selectedVariantId, selectedProductInfo,
                qty, shopId, shopType, shopName, cartId,
                trackerAttribution, trackerListName, false,
                "", false, customEventLabel, "", layoutName)
    }

    private fun eventClickAddToCartOrBuyInVariant(irisSessionId:String,
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
                                                  trackerListName: String?,
                                                  multiOrigin: Boolean,
                                                  reference: String = "",
                                                  isFreeOngkir: Boolean = false,
                                                  customEventLabel: String = "",
                                                  customEventAction: String = "",
                                                  layoutName: String = "",
                                                  customDimension40: String = ""
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

        val eventLabel = when {
            reference == ApplinkConst.TOPCHAT && customEventLabel.isNotEmpty() -> customEventLabel
            isTrackTradeIn -> customEventLabel
            else -> productVariantString
        }

        val eventAction = when {
            reference == ApplinkConst.TOPCHAT && customEventAction.isNotEmpty() -> customEventAction
            else -> actionLabel
        }

        val dimension40 = when {
            reference == ApplinkConst.TOPCHAT && customDimension40.isNotEmpty() -> customDimension40
            else -> ""
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                mutableMapOf<String, Any>(
                        "event" to "addToCart",
                        "eventCategory" to category,
                        "eventAction" to eventAction,
                        "eventLabel" to eventLabel,
                        "sessionIris" to irisSessionId,
                        KEY_PRODUCT_ID to selectedProductInfo.basic.id,
                        "layout" to "layout:${layoutName};catName:${originalProductInfoAndVariant.productInfo.category.name};catId:${originalProductInfoAndVariant.productInfo.category.id};",
                        "component" to "",
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
                                                KEY_DIMENSION_81 to shopType,
                                                KEY_DIMENSION_40 to dimension40
                                        )),
                                        "actionField" to mutableMapOf("list" to (trackerListName
                                                ?: ""))
                                )
                        )
                ))
        isTrackTradeIn = false
    }


    fun eventClickAtcInVariantNotLogin(productInfo: ProductInfo?, layoutName: String?) {
        val mapEvent = TrackAppUtils.gtmData(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - tambah ke keranjang on variants page - before login",
                productInfo?.basic?.id.toString()
        )
        addComponentTracker(mapEvent, productInfo, layoutName ?: "")

    }

    fun eventSelectSizeVariant(size: String?, productId: String, productInfo: ProductInfo?, layoutName: String?) {
        if (size.isNullOrEmpty()) return
        val mapEvent = TrackAppUtils.gtmData(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                SELECT_SIZE_VARIANT,
                size
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        addComponentTracker(mapEvent, productInfo, layoutName ?: "")
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventSelectColorVariant(color: String?, productId: String, productInfo: ProductInfo?, layoutName: String?) {
        if (color.isNullOrEmpty()) return
        val mapEvent = TrackAppUtils.gtmData(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                SELECT_COLOR_VARIANT,
                color
        )
        mapEvent[KEY_PRODUCT_ID] = productId
        addComponentTracker(mapEvent, productInfo, layoutName ?: "")
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
                mutableMapOf(
                        AFInAppEventParameterName.CONTENT_ID to productId,
                        AFInAppEventParameterName.CONTENT_TYPE to "product",
                        AFInAppEventParameterName.DESCRIPTION to productName,
                        AFInAppEventParameterName.CURRENCY to "IDR",
                        AFInAppEventParameterName.QUANTITY to quantity,
                        AFInAppEventParameterName.PRICE to priceItem,
                        "category" to category
                ).also {
                    val jsonArray = JSONArray()
                    val jsonObject = JSONObject()
                    jsonObject.put("id", productId)
                    jsonObject.put("quantity", quantity)
                    jsonArray.put(jsonObject)
                    it[AFInAppEventParameterName.CONTENT] = jsonArray.toString()
                })
    }

    private fun addComponentTracker(mapEvent: MutableMap<String, Any>,
                                    productInfo: ProductInfo?, layoutName: String) {
        mapEvent[KEY_PRODUCT_ID] = productInfo?.basic?.id.toString()
        mapEvent["layout"] = "layout:${layoutName};catName:${productInfo?.category?.name};catId:${productInfo?.category?.id};"
        mapEvent["component"] = ""

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    private fun getMultiOriginAttribution(isMultiOrigin: Boolean): String = when (isMultiOrigin) {
        true -> "tokopedia"
        else -> "regular"
    }
}
