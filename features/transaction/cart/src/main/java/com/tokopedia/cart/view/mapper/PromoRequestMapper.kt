package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel

object PromoRequestMapper {

    fun generateValidateUseRequestParams(promoData: Any?, selectedCartShopHolderDataList: List<CartShopHolderData>): ValidateUsePromoRequest {
        return ValidateUsePromoRequest().apply {
            val tmpOrders = mutableListOf<OrdersItem>()
            selectedCartShopHolderDataList.forEach { cartShopHolderData ->
                val ordersItem = OrdersItem().apply {
                    val tmpProductDetails = mutableListOf<ProductDetailsItem>()
                    cartShopHolderData.productUiModelList.forEach { cartItemHolderData ->
                        if (cartItemHolderData.isSelected) {
                            val productDetailsItem = ProductDetailsItem(
                                productId = cartItemHolderData.productId.toLongOrZero(),
                                quantity = cartItemHolderData.quantity,
                                bundleId = cartItemHolderData.bundleId.toLongOrZero()
                            )
                            tmpProductDetails.add(productDetailsItem)
                        }
                    }
                    productDetails = tmpProductDetails
                    promoData?.let {
                        if (it is PromoUiModel) {
                            codes = getPromoCodesFromValidateUseByUniqueId(it, cartShopHolderData).toMutableList()
                        } else if (it is LastApplyPromo) {
                            codes = getPromoCodesFromLastApplyByUniqueId(it, cartShopHolderData).toMutableList()
                        }
                    }
                    shippingId = 0
                    spId = 0
                    shopId = cartShopHolderData.shopId.toLongOrZero()
                    uniqueId = cartShopHolderData.cartString
                }
                tmpOrders.add(ordersItem)
            }
            orders = tmpOrders
            promoData?.let {
                if (it is PromoUiModel) {
                    codes = it.codes.toMutableList()
                } else if (it is LastApplyPromo) {
                    codes = it.lastApplyPromoData.codes.toMutableList()
                }
            }
            state = CartConstant.PARAM_CART
            skipApply = 0
            cartType = CartConstant.PARAM_DEFAULT
        }
    }

    private fun getPromoCodesFromLastApplyByUniqueId(lastApplyPromo: LastApplyPromo, cartShopHolderData: CartShopHolderData): List<String> {
        // get from voucher order first
        lastApplyPromo.lastApplyPromoData.listVoucherOrders.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartShopHolderData.cartString) {
                val promoCodes = listOf(voucherOrder.code)
                if (promoCodes.isNotEmpty()) {
                    return promoCodes
                }
            }
        }

        // if there is no promo code on voucher order, check from ui model (got from cart response)
        // Otherwise return empty list (ui model promo codes default value)
        return cartShopHolderData.promoCodes
    }

    private fun getPromoCodesFromValidateUseByUniqueId(promoUiModel: PromoUiModel, cartShopHolderData: CartShopHolderData): List<String> {
        // get from voucher order first
        promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartShopHolderData.cartString) {
                val promoCodes = listOf(voucherOrder.code)
                if (promoCodes.isNotEmpty()) {
                    return promoCodes
                }
            }
        }

        // if there is no promo code on voucher order, check from ui model (got from cart response)
        // Otherwise return empty list (ui model promo codes default value)
        return cartShopHolderData.promoCodes
    }

    fun generateCouponListRequestParams(promoData: Any?, availableCartShopHolderDataList: List<CartShopHolderData>): PromoRequest {
        val orders = mutableListOf<Order>()
        availableCartShopHolderDataList.forEach { cartShopHolderData ->
            val listProductDetail = mutableListOf<ProductDetail>()
            var hasCheckedItem = false
            cartShopHolderData.productUiModelList.forEach { cartItem ->
                if (!hasCheckedItem && cartItem.isSelected) {
                    hasCheckedItem = true
                }
                val productDetail = ProductDetail(
                        productId = cartItem.productId.toLong(),
                        quantity = cartItem.quantity,
                        bundleId = cartItem.bundleId.toLongOrZero()
                )
                listProductDetail.add(productDetail)
            }
            val order = Order(
                    shopId = cartShopHolderData.shopId.toLongOrZero(),
                    uniqueId = cartShopHolderData.cartString,
                    product_details = listProductDetail,
                    codes = cartShopHolderData.promoCodes.toMutableList(),
                    isChecked = hasCheckedItem
            )
            orders.add(order)
        }

        val globalPromo = arrayListOf<String>()
        if (promoData is PromoUiModel) {
            promoData.codes.forEach {
                if (!globalPromo.contains(it)) globalPromo.add(it)
            }
            promoData.voucherOrderUiModels.forEach { voucherOrder ->
                orders.forEach { order ->
                    if (voucherOrder.uniqueId == order.uniqueId) {
                        if (!order.codes.contains(voucherOrder.code)) {
                            order.codes.add(voucherOrder.code)
                        }
                    }
                }
            }

        } else if (promoData is LastApplyPromo) {
            globalPromo.addAll(promoData.lastApplyPromoData.codes)
            promoData.lastApplyPromoData.listVoucherOrders.forEach { voucherOrders ->
                orders.forEach { order ->
                    if (voucherOrders.uniqueId == order.uniqueId) {
                        if (voucherOrders.code.isNotBlank() && !order.codes.contains(voucherOrders.code)) {
                            order.codes.add(voucherOrders.code)
                        }
                    }
                }
            }
        }

        return PromoRequest(
                codes = globalPromo,
                state = "cart",
                isSuggested = 0,
                orders = orders)
    }
}