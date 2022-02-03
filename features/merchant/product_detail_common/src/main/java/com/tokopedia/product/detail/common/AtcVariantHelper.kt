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
import com.tokopedia.product.detail.common.data.model.aggregator.SimpleBasicInfo
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.carttype.AlternateCopy
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
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

    const val WISHLIST_PAGESOURCE = "wishlist"
    const val PDP_PAGESOURCE = "product detail page"
    const val PLAY_PAGESOURCE = "play"
    const val TOPCHAT_PAGESOURCE = "topchat"
    const val NOTIFCENTER_PAGESOURCE = "notifcenter"
    const val HOMEPAGE_PAGESOURCE = "homepage"
    const val DISCOVERY_PAGESOURCE = "discovery page"
    const val SHOP_PAGE_PAGESOURCE = "shop page - buyer"
    const val CART_PAGESOURCE = "cart"
    const val SEARCH_PAGESOURCE = "search result"
    const val CATEGORY_PAGESOURCE = "category page"
    const val BUNDLING_PAGESOURCE = "bundling page"
    const val SHOP_COUPON_PAGESOURCE = "shop-coupon-product"

    /**
     * For PDP and ProductBundle only
     */
    fun pdpToAtcVariant(context: Context,
                        pageSource: String,
                        productId: String,
                        productInfoP1: DynamicProductInfoP1,
                        warehouseId: String,
                        pdpSession: String,
                        isTokoNow: Boolean,
                        isShopOwner: Boolean,
                        productVariant: ProductVariant,
                        warehouseResponse: Map<String, WarehouseInfo>,
                        cartRedirection: Map<String, CartTypeData>,
                        miniCart: Map<String, MiniCartItem>?,
                        alternateCopy: List<AlternateCopy>?,
                        boData: BebasOngkir?,
                        rates: List<P2RatesEstimate>?,
                        restrictionData: RestrictionInfoResponse?,
                        isFavorite: Boolean = false,
                        uspImageUrl: String = "",
                        startActivitResult: (Intent, Int) -> Unit) {

        val cacheManager = SaveInstanceCacheManager(context, true)
        val updatedReData = manipulateRestrictionFollowers(restrictionData, isFavorite)

        val parcelData = ProductVariantBottomSheetParams(
                productId = productId,
                pageSource = pageSource,
                whId = warehouseId,
                pdpSession = pdpSession,
                isTokoNow = isTokoNow,
                isShopOwner = isShopOwner,
                variantAggregator = ProductVariantAggregatorUiData(
                        variantData = productVariant,
                        cardRedirection = cartRedirection,
                        nearestWarehouse = warehouseResponse,
                        alternateCopy = alternateCopy ?: listOf(),
                        rates = rates ?: listOf(),
                        simpleBasicInfo = SimpleBasicInfo(
                                shopID = productInfoP1.basic.shopID,
                                shopName = productInfoP1.basic.shopName,
                                category = productInfoP1.basic.category
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
        )
        cacheManager.put(PDP_PARCEL_KEY_RESPONSE, parcelData)

        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ATC_VARIANT, productInfoP1.basic.productID, "", PDP_PAGESOURCE, "", "", "")
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
                       trackerCdListName: String = "",
                       startActivitResult: (Intent, Int) -> Unit) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ATC_VARIANT, productId, shopId, pageSource, isTokoNow.toString(), trackerCdListName)
        startActivitResult(intent, ATC_VARIANT_RESULT_CODE)
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

    fun generateSimpanCartRedirection(productVariant: ProductVariant, buttonText: String,
                                      customCartType: String = ProductDetailCommonConstant.KEY_SAVE_BUNDLING_BUTTON): Map<String, CartTypeData>? {
        if (!productVariant.hasChildren) return null
        val mapOfCartRedirection = mutableMapOf<String, CartTypeData>()
        productVariant.children.forEach {
            mapOfCartRedirection[it.productId] = generateCartTypeDataSimpan(it.productId, buttonText, customCartType)
        }
        return mapOfCartRedirection
    }

    private fun generateCartTypeDataSimpan(productId: String, buttonText: String,
                                           customCartType: String): CartTypeData {
        return CartTypeData(
                productId = productId,
                availableButtons = listOf(
                        AvailableButton(
                                cartType = customCartType,
                                color = ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN,
                                text = buttonText,
                                showRecommendation = false
                        )),
                unavailableButtons = listOf(ProductDetailCommonConstant.KEY_CHAT),
                hideFloatingButton = false
        )
    }
}