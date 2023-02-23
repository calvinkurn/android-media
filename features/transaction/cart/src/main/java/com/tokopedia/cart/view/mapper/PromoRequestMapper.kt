package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel

object PromoRequestMapper {

    fun generateValidateUseRequestParams(
        promoData: Any?,
        selectedCartShopHolderDataList: List<CartShopHolderData>,
        lastValidateUsePromoRequest: ValidateUsePromoRequest?
    ): ValidateUsePromoRequest {
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
                            codes = getPromoCodesFromValidateUseByUniqueId(
                                it,
                                cartShopHolderData,
                            ).toMutableList()

                            val boShipmentData = getShippingFromValidateUseByUniqueId(
                                    it,
                                    cartShopHolderData,
                                    lastValidateUsePromoRequest
                            )
                            shippingId = boShipmentData.shippingId
                            spId = boShipmentData.spId
                            boCampaignId = boShipmentData.boCampaignId.toLongOrZero()
                            shippingSubsidy = boShipmentData.shippingSubsidy
                            benefitClass = boShipmentData.benefitClass
                            shippingPrice = boShipmentData.shippingPrice
                            etaText = boShipmentData.etaText
                        } else if (it is LastApplyPromo) {
                            codes = getPromoCodesFromLastApplyByUniqueId(
                                it,
                                cartShopHolderData,
                            ).toMutableList()
                            val boShipmentData = getShippingFromLastApplyByUniqueId(
                                it,
                                cartShopHolderData,
                            )
                            shippingId = boShipmentData.shippingId
                            spId = boShipmentData.spId
                            boCampaignId = boShipmentData.boCampaignId.toLongOrZero()
                            shippingSubsidy = boShipmentData.shippingSubsidy
                            benefitClass = boShipmentData.benefitClass
                            shippingPrice = boShipmentData.shippingPrice
                            etaText = boShipmentData.etaText
                        }
                    }
                    shopId = cartShopHolderData.shopId.toLongOrZero()
                    uniqueId = cartShopHolderData.cartString
                    boType = cartShopHolderData.boMetadata.boType
                    warehouseId = cartShopHolderData.warehouseId
                    isPo = cartShopHolderData.isPo
                    poDuration = cartShopHolderData.poDuration.toIntOrZero()
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
        val promoCodes = arrayListOf<String>()
        lastApplyPromo.lastApplyPromoData.listVoucherOrders.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartShopHolderData.cartString) {
                if (voucherOrder.code.isNotBlank())
                    promoCodes.add(voucherOrder.code)
            }
        }
        if (promoCodes.isNotEmpty()) {
            return promoCodes.distinct()
        }

        // if there is no promo code on voucher order, check from ui model (got from cart response)
        // Otherwise return empty list (ui model promo codes default value)
        return cartShopHolderData.promoCodes
    }

    private fun getPromoCodesFromValidateUseByUniqueId(promoUiModel: PromoUiModel, cartShopHolderData: CartShopHolderData): List<String> {
        // get from voucher order first
        val promoCodes = arrayListOf<String>()
        promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartShopHolderData.cartString) {
                if (voucherOrder.code.isNotBlank())
                    promoCodes.add(voucherOrder.code)
            }
        }
        if (promoCodes.isNotEmpty()) {
            return promoCodes.distinct()
        }
        // if there is no promo code on voucher order, check from ui model (got from cart response)
        // Otherwise return empty list (ui model promo codes default value)
        return cartShopHolderData.promoCodes
    }

    private fun getShippingFromLastApplyByUniqueId(
        lastApplyPromo: LastApplyPromo,
        cartShopHolderData: CartShopHolderData
    ): PromoRequestBoShipmentData {
        lastApplyPromo.lastApplyPromoData.listVoucherOrders.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartShopHolderData.cartString &&
                voucherOrder.shippingId > 0 &&
                voucherOrder.spId > 0 &&
                voucherOrder.type == "logistic"
            ) {
                return PromoRequestBoShipmentData(
                    voucherOrder.shippingId,
                    voucherOrder.spId,
                    voucherOrder.boCampaignId,
                        voucherOrder.shippingSubsidy,
                    voucherOrder.benefitClass,
                    voucherOrder.shippingPrice,
                        voucherOrder.etaText
                )
            }
        }
        return PromoRequestBoShipmentData()
    }

    private fun getShippingFromValidateUseByUniqueId(
        promoUiModel: PromoUiModel,
        cartShopHolderData: CartShopHolderData,
        lastValidateUsePromoRequest: ValidateUsePromoRequest?
    ): PromoRequestBoShipmentData {
        promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartShopHolderData.cartString &&
                voucherOrder.shippingId > 0 &&
                voucherOrder.spId > 0 &&
                voucherOrder.type == "logistic"
            ) {
                val validateOrderRequest = lastValidateUsePromoRequest?.orders?.firstOrNull { it.uniqueId == cartShopHolderData.cartString }
                if (validateOrderRequest != null) {
                    return PromoRequestBoShipmentData(
                        voucherOrder.shippingId,
                        voucherOrder.spId,
                        validateOrderRequest.boCampaignId.toString(),
                            validateOrderRequest.shippingSubsidy,
                        validateOrderRequest.benefitClass,
                        validateOrderRequest.shippingPrice,
                            validateOrderRequest.etaText
                    )
                }
            }
        }
        val validateOrderRequest = lastValidateUsePromoRequest?.orders?.firstOrNull { it.uniqueId == cartShopHolderData.cartString }
        if (validateOrderRequest != null && validateOrderRequest.spId > 0) {
            return PromoRequestBoShipmentData(
                validateOrderRequest.shippingId,
                validateOrderRequest.spId,
                validateOrderRequest.boCampaignId.toString(),
                validateOrderRequest.shippingSubsidy,
                validateOrderRequest.benefitClass,
                validateOrderRequest.shippingPrice,
                validateOrderRequest.etaText
            )
        }
        return PromoRequestBoShipmentData()
    }

    fun generateCouponListRequestParams(
        promoData: Any?,
        availableCartShopHolderDataList: List<CartShopHolderData>
    ): PromoRequest {
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
                    boType = cartShopHolderData.boMetadata.boType,
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
                        if (order.shippingId <= 0)
                            order.shippingId = voucherOrder.shippingId
                        if (order.spId <= 0)
                            order.spId = voucherOrder.spId
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
                        if (order.shippingId <= 0)
                            order.shippingId = voucherOrders.shippingId
                        if (order.spId <= 0)
                            order.spId = voucherOrders.spId
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

    fun generateClearBoParam(
        promoData: Any?,
        availableCartShopHolderDataList: List<CartShopHolderData>
    ): ClearPromoOrderData? {
        val orders = arrayListOf<ClearPromoOrder>()
        availableCartShopHolderDataList.forEach { cartShopHolderData ->
            val order = ClearPromoOrder(
                uniqueId = cartShopHolderData.cartString,
                boType = cartShopHolderData.boMetadata.boType,
                shopId = cartShopHolderData.shopId.toLongOrZero(),
                warehouseId = cartShopHolderData.warehouseId,
                isPo = cartShopHolderData.isPo,
                poDuration = cartShopHolderData.poDuration,
            )
            orders.add(order)
        }

        var hasBo = false
        if (promoData is PromoUiModel) {
            promoData.voucherOrderUiModels.forEach { voucherOrder ->
                orders.forEach { order ->
                    if (voucherOrder.uniqueId == order.uniqueId &&
                        voucherOrder.shippingId > 0 &&
                        voucherOrder.spId > 0 &&
                        voucherOrder.type == "logistic"
                    ) {
                        order.codes.add(voucherOrder.code)
                        hasBo = true
                    }
                }
            }

        } else if (promoData is LastApplyPromo) {
            promoData.lastApplyPromoData.listVoucherOrders.forEach { voucherOrders ->
                orders.forEach { order ->
                    if (voucherOrders.uniqueId == order.uniqueId &&
                        voucherOrders.shippingId > 0 &&
                        voucherOrders.spId > 0 &&
                        voucherOrders.type == "logistic"
                    ) {
                        order.codes.add(voucherOrders.code)
                        hasBo = true
                    }
                }
            }
        }

        return if (hasBo) ClearPromoOrderData(
                orders = orders.filter { it.codes.isNotEmpty() }
        ) else null
    }
}

private class PromoRequestBoShipmentData(
        val shippingId: Int = 0,
        val spId: Int = 0,
        val boCampaignId: String = "",
        val shippingSubsidy: Long = 0,
        val benefitClass: String = "",
        val shippingPrice: Double = 0.0,
        val etaText: String = "",
)
