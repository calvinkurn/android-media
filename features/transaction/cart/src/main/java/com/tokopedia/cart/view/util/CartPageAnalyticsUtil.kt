package com.tokopedia.cart.view.util

import com.tokopedia.analytics.byteio.CartClickAnalyticsModel
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartModel
import com.tokopedia.cart.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cart.view.uimodel.SubTotalState
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCartMapData
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.recommendation_widget_common.viewutil.asSuccess

object CartPageAnalyticsUtil {

    fun generateCheckoutDataAnalytics(
        cartItemDataList: List<CartItemHolderData>,
        step: String
    ): Map<String, Any> {
        val checkoutMapData = HashMap<String, Any>()
        val enhancedECommerceActionField = EnhancedECommerceActionField().apply {
            setStep(step)
            if (step == EnhancedECommerceActionField.STEP_0) {
                setOption(EnhancedECommerceActionField.STEP_0_OPTION_VIEW_CART_PAGE)
            } else if (step == EnhancedECommerceActionField.STEP_1) {
                setOption(EnhancedECommerceActionField.STEP_1_OPTION_CART_PAGE_LOADED)
            }
        }
        val enhancedECommerceCheckout = EnhancedECommerceCheckout().apply {
            for (cartItemData in cartItemDataList) {
                val enhancedECommerceProductCartMapData =
                    getCheckoutEnhancedECommerceProductCartMapData(cartItemData)
                addProduct(enhancedECommerceProductCartMapData.getProduct())
            }
            setCurrencyCode(EnhancedECommerceCartMapData.VALUE_CURRENCY_IDR)
            setActionField(enhancedECommerceActionField.getActionFieldMap())
        }
        checkoutMapData[EnhancedECommerceCheckout.KEY_CHECKOUT] =
            enhancedECommerceCheckout.getCheckoutMap()
        return checkoutMapData
    }

    fun generateByteIoAnalyticsModel(cartItemDataList: List<CartItemHolderData>, subTotalState: SubTotalState?): CartClickAnalyticsModel {
        val salePrice = subTotalState?.asSuccess()?.data?.subtotalPrice.orZero()
        val originalPriceWithWholesale = subTotalState?.asSuccess()?.data?.subtotalBeforeSlashedPrice.orZero()

        val originalPriceValueWithoutWholesale = cartItemDataList.sumOf { cartItem ->
            val price = cartItem.productOriginalPrice
                .takeIf { it > 0.0 }
                .ifNull { cartItem.productPrice }
            price * cartItem.quantity
        }
        return CartClickAnalyticsModel(
            cartItemId = cartItemDataList.joinToString(",") { it.cartId },
            originalPriceValue = originalPriceValueWithoutWholesale,
            productId = cartItemDataList.map { it.parentId }.distinct().joinToString(","),
            skuId = cartItemDataList.joinToString(",") { it.productId },
            skuNum = cartItemDataList.size,
            ItemCnt = cartItemDataList.sumOf { it.quantity },
            salePriceValue = salePrice,
            discountedAmount = originalPriceWithWholesale - salePrice,
        )
    }

    private fun getCheckoutEnhancedECommerceProductCartMapData(cartItemHolderData: CartItemHolderData): EnhancedECommerceProductCartMapData {
        val enhancedECommerceProductCartMapData = EnhancedECommerceProductCartMapData().apply {
            setDimension38(
                cartItemHolderData.trackerAttribution.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setDimension45(cartItemHolderData.cartId)
            setDimension54(cartItemHolderData.isFulfillment)
            setDimension53(cartItemHolderData.productOriginalPrice > 0)
            setDimension56(cartItemHolderData.warehouseId)
            setDimension58(cartItemHolderData.isFulfillment)
            setProductName(cartItemHolderData.productName)
            setProductID(cartItemHolderData.productId)
            setPrice(cartItemHolderData.productPrice.toString())
            setBrand(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setCategory(
                cartItemHolderData.category.ifBlank {
                    EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                }
            )
            setVariant(EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER)
            setQty(cartItemHolderData.quantity)
            setShopId(cartItemHolderData.shopHolderData.shopId)
            setShopType(cartItemHolderData.shopHolderData.shopTypeInfo.titleFmt)
            setShopName(cartItemHolderData.shopHolderData.shopName)
            setCategoryId(cartItemHolderData.categoryId)
            setWarehouseId(cartItemHolderData.warehouseId)
            setProductWeight(cartItemHolderData.productWeight.toString())
            setCartId(cartItemHolderData.cartId)
            setPromoCode(cartItemHolderData.promoCodes)
            setPromoDetails(cartItemHolderData.promoDetails)
            setDimension73(cartItemHolderData.productTagInfo.firstOrNull()?.message)
            setDimension83(cartItemHolderData.freeShippingName)
            setDimension117(cartItemHolderData.bundleType)
            setDimension118(cartItemHolderData.bundleId)
            setDimension136(cartItemHolderData.cartStringOrder)
            setDimension137(cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId.toString())
            setCampaignId(cartItemHolderData.campaignId)
            setImpressionAlgorithm(if (!cartItemHolderData.isError) cartItemHolderData.productInformation.firstOrNull() else "")
            if (cartItemHolderData.shopCartShopGroupTickerData.tickerText.isNotBlank()) {
                val fulfillText =
                    if (cartItemHolderData.shopCartShopGroupTickerData.state == CartShopGroupTickerState.SUCCESS_AFFORD) {
                        ConstantTransactionAnalytics.EventLabel.BO_FULFILL
                    } else {
                        ConstantTransactionAnalytics.EventLabel.BO_UNFULFILL
                    }
                setBoAffordability("${fulfillText}_${cartItemHolderData.shopBoMetadata.boType}")
            } else {
                setBoAffordability("")
            }
        }
        return enhancedECommerceProductCartMapData
    }

    fun generateRemoveCartFromSubtractButtonAnalytics(cartItemHolderData: CartItemHolderData): Map<String, Any> {
        return mapOf<String, Any>(
            ConstantTransactionAnalytics.Key.CATEGORY_ID to cartItemHolderData.categoryId,
            ConstantTransactionAnalytics.Key.CATEGORY to cartItemHolderData.category,
            ConstantTransactionAnalytics.Key.ITEM_ID to cartItemHolderData.productId,
            ConstantTransactionAnalytics.Key.ITEM_NAME to cartItemHolderData.productName,
            ConstantTransactionAnalytics.Key.ITEM_VARIANT to cartItemHolderData.variant,
            ConstantTransactionAnalytics.Key.ITEM_BRAND to String.EMPTY,
            ConstantTransactionAnalytics.Key.PRICE to cartItemHolderData.productPrice.toString(),
            ConstantTransactionAnalytics.Key.QUANTITY to cartItemHolderData.quantity,
            ConstantTransactionAnalytics.Key.SHOP_ID to cartItemHolderData.shopHolderData.shopId,
            ConstantTransactionAnalytics.Key.SHOP_NAME to cartItemHolderData.shopHolderData.shopName,
            ConstantTransactionAnalytics.Key.SHOP_TYPE to cartItemHolderData.shopHolderData.shopTypeInfo.titleFmt
        )
    }

    fun generateCartImpressionAnalytic(mutableSet: MutableSet<CartItemHolderData>): List<Map<String, Any>> {
        val data = arrayListOf<Map<String, Any>>()
        mutableSet.forEach {
            val productDataMap = mapOf(
                ConstantTransactionAnalytics.Key.CREATIVE_NAME to "",
                ConstantTransactionAnalytics.Key.CREATIVE_SLOT to "",
                ConstantTransactionAnalytics.Key.ITEM_ID to it.cartId,
                ConstantTransactionAnalytics.Key.ITEM_NAME to it.productName
            )
            data.add(productDataMap)
        }
        return data
    }
}
