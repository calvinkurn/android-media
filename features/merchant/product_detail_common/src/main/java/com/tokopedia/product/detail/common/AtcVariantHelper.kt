package com.tokopedia.product.detail.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.carttype.AlternateCopy
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo

/**
 * Created by Yehezkiel on 17/05/21
 */
object AtcVariantHelper {
    const val PDP_PARCEL_KEY_RESPONSE = "pdp_page_response"
    const val PDP_PARCEL_KEY_RESULT = "pdp_page_result"

    const val ATC_VARIANT_CACHE_ID = "atc_variant_cache_id"
    const val PDP_PAGE_SOURCE = "pdp"

    const val ATC_VARIANT_RESULT_CODE = 19202

    const val WISHLIST_PAGESOURCE = "wishlist"
    const val PDP_PAGESOURCE = "pdp"
    const val PLAY_PAGESOURCE = "play"
    const val TOPCHAT_PAGESOURCE = "topchat"
    const val NOTIFCENTER_PAGESOURCE = "notifcenter"

    /**
     * For PDP only
     */
    fun pdpToAtcVariant(context: Context,
                        productInfoP1: DynamicProductInfoP1,
                        warehouseId: String,
                        pdpSession: String,
                        isTokoNow: Boolean,
                        isFreeOngkir: Boolean,
                        isShopOwner: Boolean,
                        productVariant: ProductVariant,
                        warehouseResponse: Map<String, WarehouseInfo>,
                        cartRedirection: Map<String, CartTypeData>,
                        miniCart: Map<String, MiniCartItem>?,
                        alternateCopy: List<AlternateCopy>?,
                        startActivitResult: (Intent, Int) -> Unit) {

        val cacheManager = SaveInstanceCacheManager(context, true)

        val categoryName = productInfoP1.basic.category.detail.map {
            it.name
        }.joinToString("/", postfix = "/ ${productInfoP1.basic.category.id}")

        val categoryId = productInfoP1.basic.category.detail.map {
            it.id
        }.joinToString("/")

        val parcelData = ProductVariantBottomSheetParams(
                productId = productInfoP1.basic.productID,
                pageSource = PDP_PAGE_SOURCE,
                whId = warehouseId,
                pdpSession = pdpSession,
                isTokoNow = isTokoNow,
                isFreeOngkir = isFreeOngkir,
                isShopOwner = isShopOwner,
                variantAggregator = ProductVariantAggregatorUiData(
                        variantData = productVariant,
                        cardRedirection = cartRedirection,
                        nearestWarehouse = warehouseResponse,
                        alternateCopy = alternateCopy ?: listOf()
                ),
                miniCartData = miniCart,
                categoryName = categoryName,
                categoryId = categoryId,
                isTradein = productInfoP1.data.isTradeIn,
                minimumShippingPrice = productInfoP1.basic.getDefaultOngkirInt(),
                shopId = productInfoP1.basic.shopID,
                shopName = productInfoP1.basic.shopName
        )
        cacheManager.put(PDP_PARCEL_KEY_RESPONSE, parcelData)

        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ATC_VARIANT, productInfoP1.basic.productID, PDP_PAGESOURCE, "", "")
                .putExtra(ATC_VARIANT_CACHE_ID, cacheManager.id)
        startActivitResult.invoke(intent, ATC_VARIANT_RESULT_CODE)
    }

    fun onActivityResultAtcVariant(context: Context, requestCode: Int, data: Intent?, updateView: ProductVariantResult.() -> Unit) {
        if (requestCode != ATC_VARIANT_RESULT_CODE || data == null) return
        val cacheId = data.getStringExtra(ATC_VARIANT_CACHE_ID)
        val cacheManager = SaveInstanceCacheManager(context, cacheId)

        val result: ProductVariantResult = cacheManager.get(PDP_PARCEL_KEY_RESULT, ProductVariantResult::class.java)
                ?: return

        updateView.invoke(result)
    }

    fun goToAtcVariant(context: Context,
                       productId: String,
                       pageSource: String,
                       isTokoNow: Boolean = false,
                       shopId: String,
                       startActivitResult: (Intent, Int) -> Unit) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ATC_VARIANT, productId, shopId, pageSource, isTokoNow.toString())
        startActivitResult(intent, ATC_VARIANT_RESULT_CODE)
    }
}