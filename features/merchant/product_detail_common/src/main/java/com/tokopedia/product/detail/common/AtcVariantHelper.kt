package com.tokopedia.product.detail.common

import android.content.Context
import android.content.Intent
import android.graphics.Point
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.aggregator.SimpleBasicInfo
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.carttype.AlternateCopy
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.re.RestrictionInfoResponse
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo

/**
 * Created by Yehezkiel on 17/05/21
 */
object AtcVariantHelper {
    const val PDP_PARCEL_KEY_RESPONSE = "pdp_page_response"
    const val PDP_PARCEL_KEY_RESULT = "pdp_page_result"

    const val ATC_VARIANT_CACHE_ID = "atc_variant_cache_id"
    const val ATC_VARIANT_RESULT_CODE = 19202
    const val KEY_DISMISS_AFTER_ATC = "dismiss_after_atc"
    const val KEY_DISMISS_WHEN_TRANSACTION_ERROR = "dismiss_when_transaction_error"
    const val KEY_EXT_PARAMS = "ext_params"
    const val KEY_SAVE_AFTER_CLOSE = "save_after_close"
    const val KEY_SHOW_QTY_EDITOR = "show_qty_editor"
    const val KEY_CHANGE_VARIANT = "change_variant"

    /**
     * For PDP and ProductBundle only
     */
    fun pdpToAtcVariant(
        context: Context,
        pageSource: VariantPageSource,
        productId: String,
        productInfoP1: ProductInfoP1,
        warehouseId: String,
        pdpSession: String,
        isTokoNow: Boolean,
        isShopOwner: Boolean,
        productVariant: ProductVariant,
        warehouseResponse: Map<String, WarehouseInfo>,
        cartRedirection: Map<String, CartTypeData>,
        miniCart: Map<String, MiniCartItem.MiniCartItemProduct>?,
        alternateCopy: List<AlternateCopy>?,
        boData: BebasOngkir?,
        rates: List<P2RatesEstimate>?,
        restrictionData: RestrictionInfoResponse?,
        isFavorite: Boolean = false,
        uspImageUrl: String = "",
        dismissAfterTransaction: Boolean = false,
        saveAfterClose: Boolean = true,
        cartViewLocation: Point? = null, // only for pdp
        startActivitResult: (Intent, Int) -> Unit
    ) {
        val cacheManager = SaveInstanceCacheManager(context, true)
        val updatedReData = manipulateRestrictionFollowers(restrictionData, isFavorite)

        val parcelData = ProductVariantBottomSheetParams(
            productId = productId,
            pageSource = pageSource.source,
            whId = warehouseId,
            pdpSession = pdpSession,
            isTokoNow = isTokoNow,
            isShopOwner = isShopOwner,
            dismissAfterTransaction = dismissAfterTransaction,
            saveAfterClose = saveAfterClose,
            variantAggregator = ProductVariantAggregatorUiData(
                variantData = productVariant,
                cardRedirection = cartRedirection,
                nearestWarehouse = warehouseResponse,
                alternateCopy = alternateCopy ?: listOf(),
                rates = rates ?: listOf(),
                simpleBasicInfo = SimpleBasicInfo(
                    shopID = productInfoP1.basic.shopID,
                    shopName = productInfoP1.basic.shopName,
                    category = productInfoP1.basic.category,
                    defaultMediaURL = productInfoP1.basic.defaultMediaUrl
                ),
                shopType = productInfoP1.shopTypeString,
                boData = boData ?: BebasOngkir(),
                isCod = productInfoP1.data.isCod,
                reData = updatedReData,
                uspImageUrl = uspImageUrl,
                cashBackPercentage = productInfoP1.data.isCashback.percentage
            ),
            shopId = productInfoP1.basic.shopID,
            miniCartData = miniCart,
            minimumShippingPrice = productInfoP1.basic.getDefaultOngkirDouble(),
            showQtyEditor = isTokoNow,
            cartPosition = cartViewLocation
        )
        cacheManager.put(PDP_PARCEL_KEY_RESPONSE, parcelData)

        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            productInfoP1.basic.productID,
            "",
            pageSource.source,
            "",
            "",
            ""
        ).putExtra(ATC_VARIANT_CACHE_ID, cacheManager.id)
        startActivitResult.invoke(intent, ATC_VARIANT_RESULT_CODE)
    }

    fun onActivityResultAtcVariant(context: Context, requestCode: Int, data: Intent?, updateView: ProductVariantResult.() -> Unit) {
        if (requestCode != ATC_VARIANT_RESULT_CODE || data == null) return
        val cacheId = data.getStringExtra(ATC_VARIANT_CACHE_ID)
        val cacheManager = SaveInstanceCacheManager(context.applicationContext, cacheId)

        val result: ProductVariantResult = cacheManager.get(PDP_PARCEL_KEY_RESULT, ProductVariantResult::class.java)
            ?: return

        updateView.invoke(result)
    }

    fun goToAtcVariant(
        context: Context,
        productId: String,
        pageSource: VariantPageSource,
        isTokoNow: Boolean = false,
        shopId: String,
        trackerCdListName: String = "",
        extParams: String = "",
        dismissAfterTransaction: Boolean = false,
        saveAfterClose: Boolean = true,
        showQuantityEditor: Boolean = false,
        changeVariant: ProductVariantBottomSheetParams.ChangeVariant? = null,
        dismissWhenTransactionError: Boolean = false,
        startActivitResult: (Intent, Int) -> Unit
    ) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            productId,
            shopId,
            pageSource.source,
            isTokoNow.toString(),
            trackerCdListName
        )
        val qtyEditorData = if (isTokoNow) true else showQuantityEditor
        intent.putExtra(KEY_DISMISS_AFTER_ATC, dismissAfterTransaction)
        intent.putExtra(KEY_SAVE_AFTER_CLOSE, saveAfterClose)
        intent.putExtra(KEY_EXT_PARAMS, extParams)
        intent.putExtra(KEY_SHOW_QTY_EDITOR, qtyEditorData)
        intent.putExtra(KEY_CHANGE_VARIANT, changeVariant)
        intent.putExtra(KEY_DISMISS_WHEN_TRANSACTION_ERROR, dismissWhenTransactionError)
        startActivitResult(intent, ATC_VARIANT_RESULT_CODE)
    }

    /**
     * Generate string extParams based on key value
     * eg: "promoID=123&deviceID=123&source="search"
     */
    fun generateExtParams(keyValue: Map<String, String>): String {
        val result = keyValue.map { "${it.key}=${it.value}" }
        return result.joinToString(separator = "&")
    }

    private fun manipulateRestrictionFollowers(restrictionData: RestrictionInfoResponse?, isFavorite: Boolean): RestrictionInfoResponse {
        val manipulateRestrictionShopFollowers = restrictionData?.restrictionData?.map {
            if (it.restrictionShopFollowersType()) {
                it.copy(isEligible = isFavorite)
            } else {
                it
            }
        } ?: listOf()

        return restrictionData?.copy(restrictionData = manipulateRestrictionShopFollowers)
            ?: RestrictionInfoResponse()
    }

    fun generateSimpanCartRedirection(
        productVariant: ProductVariant,
        buttonText: String,
        customCartType: String = ProductDetailCommonConstant.KEY_SAVE_BUNDLING_BUTTON
    ): Map<String, CartTypeData>? {
        if (!productVariant.hasChildren) return null
        val mapOfCartRedirection = mutableMapOf<String, CartTypeData>()
        productVariant.children.forEach {
            mapOfCartRedirection[it.productId] = generateCartTypeDataSimpan(it.productId, buttonText, customCartType)
        }
        return mapOfCartRedirection
    }

    private fun generateCartTypeDataSimpan(
        productId: String,
        buttonText: String,
        customCartType: String
    ): CartTypeData {
        return CartTypeData(
            productId = productId,
            availableButtons = listOf(
                AvailableButton(
                    cartType = customCartType,
                    color = ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN,
                    text = buttonText,
                    showRecommendation = false
                )
            ),
            unavailableButtons = listOf(ProductDetailCommonConstant.KEY_CHAT),
            hideFloatingButton = false
        )
    }
}

enum class VariantPageSource(val source: String) {
    WISHLIST_PAGESOURCE("wishlist"),
    PDP_PAGESOURCE("product detail page"),
    TOPCHAT_PAGESOURCE("topchat"),
    NOTIFCENTER_PAGESOURCE("notifcenter"),
    PLAY_PAGESOURCE("play"),
    HOMEPAGE_PAGESOURCE("homepage"),
    ME_PAGE_PAGESOURCE("me-page"),
    DISCOVERY_PAGESOURCE("discovery page"),
    SHOP_PAGE_PAGESOURCE("shop-direct-purchase"),
    SHOP_PAGE_REIMAGINED_DIRECT_PURCHASE_WIDGET_PAGESOURCE("/shoppage reimagined"),
    CART_PAGESOURCE("cart"),
    SEARCH_PAGESOURCE("search result"),
    CATEGORY_PAGESOURCE("category page"),
    BUNDLING_PAGESOURCE("bundling page"),
    TOKONOW_PAGESOURCE("tokonow"),
    BNPL_PAGESOURCE("bnpl-v2"),
    SHOP_COUPON_PAGESOURCE("shop-coupon-product"),
    SRP_PAGESOURCE("srp_search"),
    FEED_PAGESOURCE("feed"),
    BUY_MORE_GET_MORE("offerpage"),
    CATALOG_PAGESOURCE("catalog"),
    PRODUCT_PREVIEW_PAGESOURCE("fullscreen_media_pdp"),
    STORIES_PAGESOURCE("stories"),
    CART_CHANGE_VARIANT("cart_change_variant")
}
