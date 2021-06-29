package com.tokopedia.product.detail.common

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.CURRENT_SITE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_BUSINESS_UNIT
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_CURRENT_SITE
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_PRODUCT_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * Created by Yehezkiel on 17/05/21
 */
object ProductTrackingCommon {

    fun eventActivationOvo(productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK_BUY_ACTIVATION_OVO,
                ProductTrackingConstant.Label.EMPTY_LABEL)
        addComponentOvoTracker(mapEvent, productId, userId)
    }

    fun eventSeeBottomSheetOvo(title: String, productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "${ProductTrackingConstant.Action.CLICK_SEE_BOTTOMSHEET_OVO} $title",
                ProductTrackingConstant.Label.EMPTY_LABEL)
        addComponentOvoTracker(mapEvent, productId, userId)
    }

    fun eventTopupBottomSheetOvo(title: String, buttonTitle: String, productId: String, userId: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                ProductTrackingConstant.Category.PDP,
                "${ProductTrackingConstant.Action.CLICK} - $buttonTitle ${ProductTrackingConstant.Action.CLICK_TOPUP_BOTTOMSHEET_OVO} $title",
                ProductTrackingConstant.Label.EMPTY_LABEL)
        addComponentOvoTracker(mapEvent, productId, userId)
    }

    fun addComponentOvoTracker(mapEvent: MutableMap<String, Any>, productId: String, userId: String) {
        mapEvent[KEY_PRODUCT_ID] = productId
        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId != "0").toString()
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun onRemindMeClicked(productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                "$pageSource - global variant bottomsheet",
                ProductTrackingConstant.Action.CLICK_NOTIFY_ME_VARIANT_BOTTOMSHEET,
                "")

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onQuantityEditorClicked(productId: String, pageSource: String, oldQuantity: Int, newQuantity: Int) {
        val label = "quantity button:${if (newQuantity > oldQuantity) "plus" else "minus"}"
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                "$pageSource - global variant bottomsheet",
                ProductTrackingConstant.Action.CLICK_VARIANT_QUANTITY_EDITOR,
                label)

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onWishlistCheckClicked(productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                "$pageSource - global variant bottomsheet",
                ProductTrackingConstant.Action.CLICK_CHECK_WISHLIST,
                "")

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onSeeCartVariantBottomSheetClicked(message: String, productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                "$pageSource - global variant bottomsheet",
                ProductTrackingConstant.Action.CLICK_TOASTER_LIHAT_SUCCESS_ATC,
                "toaster message:$message")

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onVariantImageBottomSheetClicked(productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                "$pageSource - global variant bottomsheet",
                ProductTrackingConstant.Action.CLICK_PRODUCT_IMAGE,
                "")

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onVariantGuidelineClicked(productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                "$pageSource - global variant bottomsheet",
                ProductTrackingConstant.Action.CLICK_VARIANT_BOTTOMSHEET_GUIDELINE,
                "")

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    fun onVariantPartiallySelected(errorMessage: String, productId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(
                ProductTrackingConstant.PDP.EVENT_VIEW_PDP_IRIS,
                "$pageSource - global variant bottomsheet",
                ProductTrackingConstant.Action.VIEW_CHOOSE_VARIANT_ERROR,
                "not success - $errorMessage")

        addAdditionalParams(productId, mapEvent, pageSource)
    }

    private fun addAdditionalParams(productId: String, mapEvent: MutableMap<String, Any>, pageSource: String) {
        mapEvent[KEY_BUSINESS_UNIT] = generateBusinessUnit(pageSource)
        mapEvent[KEY_CURRENT_SITE] = CURRENT_SITE
        mapEvent[KEY_PRODUCT_ID] = productId

        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventEcommerceAtcError(errorMessage: String, productId: String, userId: String, pageSource: String) {
        val mapEvent = TrackAppUtils.gtmData(ProductTrackingConstant.PDP.EVENT_VIEW_PDP,
                "$pageSource - global variant bottomsheet",
                ProductTrackingConstant.Action.ACTION_VIEW_ERROR_WHEN_ADD_TO_CART,
                "not success - $errorMessage")
        mapEvent[ProductTrackingConstant.Tracking.KEY_USER_ID_VARIANT] = userId
        mapEvent[ProductTrackingConstant.Tracking.KEY_ISLOGGIN] = (userId.isNotEmpty()).toString()
        mapEvent[ProductTrackingConstant.Tracking.PROMO_ID] = productId
        mapEvent[KEY_BUSINESS_UNIT] = generateBusinessUnit(pageSource)
        mapEvent[KEY_CURRENT_SITE] = CURRENT_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
    }

    fun eventEcommerceAddToCart(
            userId: String, cartId: String, buttonAction: Int,
            buttonText: String, productId: String, shopId: String,
            productName: String, productPrice: String, quantity: Int,
            variantName: String, isMultiOrigin: Boolean,
            shopType: String = "", shopName: String = "",
            categoryName: String = "", categoryId: String = "", isFreeOngkir: Boolean = false, pageSource: String = "",
            cdListName: String = ""
    ) {
        val generateButtonActionString = when (buttonAction) {
            ProductDetailCommonConstant.OCS_BUTTON -> "$buttonText ocs"
            ProductDetailCommonConstant.OCC_BUTTON -> "$buttonText occ"
            else -> "$buttonText normal"
        }

        val multiOrigin = when (isMultiOrigin) {
            true -> "tokopedia"
            else -> "regular"
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                ProductTrackingConstant.Tracking.KEY_EVENT, "addToCart",
                ProductTrackingConstant.Tracking.KEY_CATEGORY, "$pageSource - global variant bottomsheet",
                ProductTrackingConstant.Tracking.KEY_ACTION, "click - tambah ke keranjang on global variant bottomsheet",
                ProductTrackingConstant.Tracking.KEY_LABEL, if (buttonAction == ProductDetailCommonConstant.ATC_BUTTON) "" else "fitur : $generateButtonActionString",
                KEY_PRODUCT_ID, productId,
                ProductTrackingConstant.Tracking.KEY_USER_ID, userId,
                ProductTrackingConstant.Tracking.KEY_ISLOGGIN, (userId.isNotEmpty()).toString(),
                KEY_BUSINESS_UNIT, generateBusinessUnit(pageSource),
                KEY_CURRENT_SITE, CURRENT_SITE,

                ProductTrackingConstant.Tracking.KEY_ECOMMERCE, DataLayer.mapOf(
                ProductTrackingConstant.Tracking.CURRENCY_CODE, ProductTrackingConstant.Tracking.CURRENCY_DEFAULT_VALUE,
                ProductTrackingConstant.Tracking.KEY_ADD, DataLayer.mapOf(
                ProductTrackingConstant.Tracking.PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        ProductTrackingConstant.Tracking.NAME, productName,
                        ProductTrackingConstant.Tracking.ID, productId,
                        ProductTrackingConstant.Tracking.PRICE, productPrice,
                        ProductTrackingConstant.Tracking.BRAND, ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                        ProductTrackingConstant.Tracking.CATEGORY, categoryName,
                        ProductTrackingConstant.Tracking.VARIANT, variantName,
                        ProductTrackingConstant.Tracking.QUANTITY, quantity,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_79, shopId,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_80, shopName,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_81, shopType,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_45, cartId,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_82, categoryId,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_40, cdListName, //cd listname --> /tokonow - searchproduct - {organic/organic ads/topads productlist}
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_54, multiOrigin,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_83, if (isFreeOngkir) ProductTrackingConstant.Tracking.VALUE_BEBAS_ONGKIR else ProductTrackingConstant.Tracking.VALUE_NONE_OTHER,
                        ProductTrackingConstant.Tracking.KEY_DIMENSION_38, pageSource
                ))))))
    }

    private fun generateBusinessUnit(pageSource: String) = if (pageSource == AtcVariantHelper.PDP_PAGESOURCE) {
        ProductTrackingConstant.Tracking.BUSINESS_UNIT_PDP
    } else {
        ProductTrackingConstant.Tracking.BUSINESS_UNIT
    }
}