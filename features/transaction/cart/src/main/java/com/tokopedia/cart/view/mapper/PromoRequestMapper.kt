package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartPromoHolderData
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

    fun generateGetLastApplyRequestParams(
        promoData: Any?,
        selectedCartGroupHolderDataList: List<CartGroupHolderData>,
        lastValidateUsePromoRequest: ValidateUsePromoRequest?
    ): ValidateUsePromoRequest {
        return ValidateUsePromoRequest().apply {
            val tmpOrders = mutableListOf<OrdersItem>()
            val selectedPromoHolderDataList: MutableCollection<CartPromoHolderData> = mapSelectedCartGroupToPromoData(
                selectedCartGroupHolderDataList
            ).values
            selectedPromoHolderDataList.forEach { cartPromoHolderData ->
                if (!cartPromoHolderData.hasSelectedProduct) 
                    return@forEach
                val ordersItem = OrdersItem().apply {
                    val tmpProductDetails = mutableListOf<ProductDetailsItem>()
                    cartPromoHolderData.productUiModelList.forEach { cartItemHolderData ->
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
                                cartPromoHolderData
                            ).toMutableList()

                            val boShipmentData = getShippingFromValidateUseByUniqueId(
                                it,
                                cartPromoHolderData,
                                lastValidateUsePromoRequest
                            )
                            shippingId = boShipmentData.shippingId
                            spId = boShipmentData.spId
                            boCampaignId = boShipmentData.boCampaignId.toLongOrZero()
                            shippingSubsidy = boShipmentData.shippingSubsidy
                            benefitClass = boShipmentData.benefitClass
                            shippingPrice = boShipmentData.shippingPrice
                            etaText = boShipmentData.etaText
                            shippingMetadata = boShipmentData.shippingMetadata
                        } else if (it is LastApplyPromo) {
                            codes = getPromoCodesFromLastApplyByUniqueId(
                                it,
                                cartPromoHolderData
                            ).toMutableList()
                            val boShipmentData = getShippingFromLastApplyByUniqueId(
                                it,
                                cartPromoHolderData
                            )
                            shippingId = boShipmentData.shippingId
                            spId = boShipmentData.spId
                            boCampaignId = boShipmentData.boCampaignId.toLongOrZero()
                            shippingSubsidy = boShipmentData.shippingSubsidy
                            benefitClass = boShipmentData.benefitClass
                            shippingPrice = boShipmentData.shippingPrice
                            etaText = boShipmentData.etaText
                            shippingMetadata = boShipmentData.shippingMetadata
                        }
                    }
                    shopId = cartPromoHolderData.shopId.toLongOrZero()
                    uniqueId = cartPromoHolderData.cartStringOrder
                    boType = cartPromoHolderData.boMetadata.boType
                    warehouseId = cartPromoHolderData.warehouseId
                    isPo = cartPromoHolderData.isPo
                    poDuration = cartPromoHolderData.poDuration.toIntOrZero()
                    cartStringGroup = cartPromoHolderData.cartStringGroup
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

    private fun getPromoCodesFromLastApplyByUniqueId(lastApplyPromo: LastApplyPromo, cartPromoHolderData: CartPromoHolderData): List<String> {
        // get from voucher order first
        val promoCodes = arrayListOf<String>()
        lastApplyPromo.lastApplyPromoData.listVoucherOrders.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartPromoHolderData.cartStringOrder) {
                if (voucherOrder.code.isNotBlank()) {
                    promoCodes.add(voucherOrder.code)
                }
            }
        }

        if (cartPromoHolderData.needToMoveBoData) {
            promoCodes.add(cartPromoHolderData.boCode)
        }
        
        if (promoCodes.isNotEmpty()) {
            return promoCodes.distinct()
        }

        // if there is no promo code on voucher order, check from ui model (got from cart response)
        // Otherwise return empty list (ui model promo codes default value)
        return cartPromoHolderData.promoCodes
    }

    private fun getPromoCodesFromValidateUseByUniqueId(promoUiModel: PromoUiModel, cartPromoHolderData: CartPromoHolderData): List<String> {
        // get from voucher order first
        val promoCodes = arrayListOf<String>()
        promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartPromoHolderData.cartStringOrder) {
                if (voucherOrder.code.isNotBlank()) {
                    promoCodes.add(voucherOrder.code)
                }
            }
        }
        
        if (cartPromoHolderData.needToMoveBoData) {
            promoCodes.add(cartPromoHolderData.boCode)
        }
        
        if (promoCodes.isNotEmpty()) {
            return promoCodes.distinct()
        }
        // if there is no promo code on voucher order, check from ui model (got from cart response)
        // Otherwise return empty list (ui model promo codes default value)
        return cartPromoHolderData.promoCodes
    }

    fun mapSelectedCartGroupToPromoData(selectedCartGroupHolderDataList: List<CartGroupHolderData>): HashMap<String, CartPromoHolderData> {
        val groupPromoHolderDataMap = hashMapOf<String, CartPromoHolderData>()
        selectedCartGroupHolderDataList.forEach { cartGroupHolderData ->
            val isOrderWithBoStillExist = cartGroupHolderData.productUiModelList
                .any { it.cartStringOrder == cartGroupHolderData.boUniqueId }
            cartGroupHolderData.productUiModelList.forEachIndexed { productIndex, product ->
                val cartStringOrder = product.cartStringOrder
                if (!groupPromoHolderDataMap.containsKey(cartStringOrder)) {
                    groupPromoHolderDataMap[cartStringOrder] = CartPromoHolderData(
                        promoCodes = cartGroupHolderData.promoCodes,
                        warehouseId = cartGroupHolderData.warehouseId,
                        boMetadata = cartGroupHolderData.boMetadata,
                        isPo = cartGroupHolderData.isPo,
                        cartStringOrder = product.cartStringOrder,
                        cartStringGroup = cartGroupHolderData.cartString,
                        poDuration = product.shopHolderData.poDuration,
                        shopId = product.shopHolderData.shopId,
                        boCode = cartGroupHolderData.boCode,
                        boUniqueId = cartGroupHolderData.boUniqueId,
                        needToMoveBoData = cartGroupHolderData.boCode.isNotEmpty() && !isOrderWithBoStillExist && productIndex == 0
                    )
                }
                val selectedPromoHolderData = groupPromoHolderDataMap[cartStringOrder]
                selectedPromoHolderData?.apply {
                    if (product.isSelected) {
                        hasSelectedProduct = true
                    }
                    productUiModelList.add(product)
                }
            }
        }
        return groupPromoHolderDataMap
    }

    private fun getShippingFromLastApplyByUniqueId(
        lastApplyPromo: LastApplyPromo,
        cartPromoHolderData: CartPromoHolderData,
    ): PromoRequestBoShipmentData {
        if (cartPromoHolderData.needToMoveBoData) {
            val voucherOrder = lastApplyPromo.lastApplyPromoData.listVoucherOrders.firstOrNull {
                it.uniqueId == cartPromoHolderData.boUniqueId
            }
            if (voucherOrder != null) {
                return PromoRequestBoShipmentData(
                    voucherOrder.shippingId,
                    voucherOrder.spId,
                    voucherOrder.boCampaignId,
                    voucherOrder.shippingSubsidy,
                    voucherOrder.benefitClass,
                    voucherOrder.shippingPrice,
                    voucherOrder.etaText,
                    voucherOrder.shippingMetadata
                )
            }
        }
        
        lastApplyPromo.lastApplyPromoData.listVoucherOrders.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartPromoHolderData.cartStringOrder &&
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
                    voucherOrder.etaText,
                    voucherOrder.shippingMetadata
                )
            }
        }
        
        return PromoRequestBoShipmentData()
    }

    private fun getShippingFromValidateUseByUniqueId(
        promoUiModel: PromoUiModel,
        cartPromoHolderData: CartPromoHolderData,
        lastValidateUsePromoRequest: ValidateUsePromoRequest?
    ): PromoRequestBoShipmentData {
        if (cartPromoHolderData.needToMoveBoData) {
            val voucherOrder = promoUiModel.voucherOrderUiModels.firstOrNull {
                it.uniqueId == cartPromoHolderData.boUniqueId
            }
            val validateOrderRequest = lastValidateUsePromoRequest?.orders?.firstOrNull { it.uniqueId == cartPromoHolderData.boUniqueId }
            if (voucherOrder != null && validateOrderRequest != null) {
                return PromoRequestBoShipmentData(
                    voucherOrder.shippingId,
                    voucherOrder.spId,
                    validateOrderRequest.boCampaignId.toString(),
                    validateOrderRequest.shippingSubsidy,
                    validateOrderRequest.benefitClass,
                    validateOrderRequest.shippingPrice,
                    validateOrderRequest.etaText,
                    validateOrderRequest.shippingMetadata
                )
            }
        }
        
        promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartPromoHolderData.cartStringOrder &&
                voucherOrder.shippingId > 0 &&
                voucherOrder.spId > 0 &&
                voucherOrder.type == "logistic"
            ) {
                val validateOrderRequest = lastValidateUsePromoRequest?.orders?.firstOrNull { it.uniqueId == cartPromoHolderData.cartStringOrder }
                if (validateOrderRequest != null) {
                    return PromoRequestBoShipmentData(
                        voucherOrder.shippingId,
                        voucherOrder.spId,
                        validateOrderRequest.boCampaignId.toString(),
                        validateOrderRequest.shippingSubsidy,
                        validateOrderRequest.benefitClass,
                        validateOrderRequest.shippingPrice,
                        validateOrderRequest.etaText,
                        validateOrderRequest.shippingMetadata
                    )
                }
            }
        }
        val validateOrderRequest = lastValidateUsePromoRequest?.orders?.firstOrNull { it.uniqueId == cartPromoHolderData.cartStringOrder }
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
        availableCartGroupHolderDataList: List<CartGroupHolderData>,
        lastValidateUsePromoRequest: ValidateUsePromoRequest?
    ): PromoRequest {
        val orders = mutableListOf<Order>()
        val selectedPromoHolderDataList = mapSelectedCartGroupToPromoData(
            availableCartGroupHolderDataList
        ).values
        selectedPromoHolderDataList.forEach { cartPromoHolderData ->
            val listProductDetail = mutableListOf<ProductDetail>()
            var hasCheckedItem = false
            cartPromoHolderData.productUiModelList.forEach { cartItem ->
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
                shopId = cartPromoHolderData.shopId.toLongOrZero(),
                uniqueId = cartPromoHolderData.cartStringOrder,
                boType = cartPromoHolderData.boMetadata.boType,
                product_details = listProductDetail,
                codes = cartPromoHolderData.promoCodes.toMutableList(),
                isChecked = hasCheckedItem,
                cartStringGroup = cartPromoHolderData.cartStringGroup,
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
                        if (order.shippingId <= 0) {
                            order.shippingId = voucherOrder.shippingId
                        }
                        if (order.spId <= 0) {
                            order.spId = voucherOrder.spId
                        }
                    }
                }
            }
            orders.forEach { order ->
                val validateOrderRequest = lastValidateUsePromoRequest?.orders?.firstOrNull {
                    it.uniqueId == order.uniqueId
                }
                validateOrderRequest?.let {
                    order.shippingMetadata = it.shippingMetadata
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
                        if (order.shippingId <= 0) {
                            order.shippingId = voucherOrders.shippingId
                        }
                        if (order.spId <= 0) {
                            order.spId = voucherOrders.spId
                        }
                        order.shippingMetadata = voucherOrders.shippingMetadata
                    }
                }
            }
        }

        return PromoRequest(
            codes = globalPromo,
            state = "cart",
            isSuggested = 0,
            orders = orders
        )
    }

    fun generateClearBoParam(
        promoData: Any?,
        availableCartGroupHolderDataList: List<CartGroupHolderData>
    ): ClearPromoOrderData? {
        val orders = arrayListOf<ClearPromoOrder>()
        val promoHolderDataMap = mapSelectedCartGroupToPromoData(
            availableCartGroupHolderDataList
        )
        promoHolderDataMap.values.forEach { cartPromoHolderData ->
            val order = ClearPromoOrder(
                uniqueId = cartPromoHolderData.cartStringOrder,
                boType = cartPromoHolderData.boMetadata.boType,
                shopId = cartPromoHolderData.shopId.toLongOrZero(),
                warehouseId = cartPromoHolderData.warehouseId,
                isPo = cartPromoHolderData.isPo,
                poDuration = cartPromoHolderData.poDuration,
                cartStringGroup = cartPromoHolderData.cartStringGroup
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

        return if (hasBo) {
            ClearPromoOrderData(
                orders = orders.filter { it.codes.isNotEmpty() }
            )
        } else {
            null
        }
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
    val shippingMetadata: String = ""
)
