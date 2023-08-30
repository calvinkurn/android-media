package com.tokopedia.cartrevamp.view.mapper

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartPromoHolderData
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
import com.tokopedia.purchase_platform.common.revamp.CartCheckoutRevampRollenceManager
import com.tokopedia.remoteconfig.RemoteConfigInstance

object PromoRequestMapper {

    fun generateGetLastApplyRequestParams(
        promoData: Any?,
        selectedCartGroupHolderDataList: List<CartGroupHolderData>,
        lastValidateUsePromoRequest: ValidateUsePromoRequest?
    ): ValidateUsePromoRequest {
        return ValidateUsePromoRequest().apply {
            val tmpOrders = mutableListOf<OrdersItem>()
            val selectedPromoHolderDataList: MutableCollection<CartPromoHolderData> =
                mapSelectedCartGroupToPromoData(
                    selectedCartGroupHolderDataList
                ).values
            val cartStringGroupSet = mutableSetOf<String>()
            selectedPromoHolderDataList.forEach { cartPromoHolderData ->
                if (!cartPromoHolderData.hasSelectedProduct) {
                    return@forEach
                }
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
                                cartPromoHolderData,
                                cartStringGroupSet
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
                        } else if (it is LastApplyPromo) {
                            codes = getPromoCodesFromLastApplyByUniqueId(
                                it,
                                cartPromoHolderData,
                                cartStringGroupSet
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
            isCartCheckoutRevamp = CartCheckoutRevampRollenceManager(RemoteConfigInstance.getInstance()
                .abTestPlatform).isRevamp()
        }
    }

    private fun getPromoCodesFromLastApplyByUniqueId(
        lastApplyPromo: LastApplyPromo,
        cartPromoHolderData: CartPromoHolderData,
        cartStringGroupSet: MutableSet<String>
    ): List<String> {
        // get from voucher order first
        val promoCodes = arrayListOf<String>()
        lastApplyPromo.lastApplyPromoData.listVoucherOrders.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartPromoHolderData.cartStringOrder) {
                if (voucherOrder.code.isNotBlank() && needToAddCode(
                        cartPromoHolderData.cartStringGroup,
                        voucherOrder.isTypeLogistic(),
                        cartStringGroupSet
                    )
                ) {
                    promoCodes.add(voucherOrder.code)
                }
            }
        }

        if (promoCodes.isNotEmpty()) {
            return promoCodes.distinct()
        }

        // if there is no promo code on voucher order, check from ui model (got from cart response)
        // Otherwise return empty list (ui model promo codes default value)
        return cartPromoHolderData.promoCodes
    }

    private fun getPromoCodesFromValidateUseByUniqueId(
        promoUiModel: PromoUiModel,
        cartPromoHolderData: CartPromoHolderData,
        cartStringGroupSet: MutableSet<String>
    ): List<String> {
        // get from voucher order first
        val promoCodes = arrayListOf<String>()
        promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartPromoHolderData.cartStringOrder) {
                if (voucherOrder.code.isNotBlank() && needToAddCode(
                        cartPromoHolderData.cartStringGroup,
                        voucherOrder.isTypeLogistic(),
                        cartStringGroupSet
                    )
                ) {
                    promoCodes.add(voucherOrder.code)
                }
            }
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
                        shopId = product.shopHolderData.shopId
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
        cartPromoHolderData: CartPromoHolderData
    ): PromoRequestBoShipmentData {
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
                    voucherOrder.etaText
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
        promoUiModel.voucherOrderUiModels.forEach { voucherOrder ->
            if (voucherOrder.uniqueId == cartPromoHolderData.cartStringOrder &&
                voucherOrder.shippingId > 0 &&
                voucherOrder.spId > 0 &&
                voucherOrder.type == "logistic"
            ) {
                val validateOrderRequest =
                    lastValidateUsePromoRequest?.orders?.firstOrNull { it.uniqueId == cartPromoHolderData.cartStringOrder }
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
        val validateOrderRequest =
            lastValidateUsePromoRequest?.orders?.firstOrNull { it.uniqueId == cartPromoHolderData.cartStringOrder }
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
        val cartStringGroupSet = mutableSetOf<String>()
        val selectedPromoHolderDataMap = mapSelectedCartGroupToPromoData(
            availableCartGroupHolderDataList
        )
        selectedPromoHolderDataMap.values.forEach { cartPromoHolderData ->
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
                cartStringGroup = cartPromoHolderData.cartStringGroup
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
                        if (!order.codes.contains(voucherOrder.code) && needToAddCode(
                                order.cartStringGroup,
                                voucherOrder.isTypeLogistic(),
                                cartStringGroupSet
                            )
                        ) {
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
        } else if (promoData is LastApplyPromo) {
            globalPromo.addAll(promoData.lastApplyPromoData.codes)
            promoData.lastApplyPromoData.listVoucherOrders.forEach { voucherOrders ->
                orders.forEach { order ->
                    if (voucherOrders.uniqueId == order.uniqueId) {
                        if (voucherOrders.code.isNotBlank() && !order.codes.contains(voucherOrders.code) && needToAddCode(
                                order.cartStringGroup,
                                voucherOrders.isTypeLogistic(),
                                cartStringGroupSet
                            )
                        ) {
                            order.codes.add(voucherOrders.code)
                        }
                        if (order.shippingId <= 0) {
                            order.shippingId = voucherOrders.shippingId
                        }
                        if (order.spId <= 0) {
                            order.spId = voucherOrders.spId
                        }
                    }
                }
            }
        }
        val isCartCheckoutRevamp = CartCheckoutRevampRollenceManager(
            RemoteConfigInstance.getInstance().abTestPlatform
        ).isRevamp()

        return PromoRequest(
            codes = globalPromo,
            state = "cart",
            isSuggested = 0,
            orders = orders,
            isCartCheckoutRevamp = isCartCheckoutRevamp
        )
    }

    fun generateClearBoParam(
        promoData: Any?,
        availableCartGroupHolderDataList: List<CartGroupHolderData>
    ): ClearPromoOrderData? {
        val orders = arrayListOf<ClearPromoOrder>()
        val cartStringGroupSet = mutableSetOf<String>()
        val promoHolderDataMap = mapSelectedCartGroupToPromoData(
            availableCartGroupHolderDataList
        )
        promoHolderDataMap.values.forEach { cartPromoHolderData ->
            if (!cartPromoHolderData.hasSelectedProduct) {
                return@forEach
            }
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
                        voucherOrder.type == "logistic" &&
                        needToAddCode(order.cartStringGroup, true, cartStringGroupSet)
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
                        voucherOrders.type == "logistic" &&
                        needToAddCode(order.cartStringGroup, true, cartStringGroupSet)
                    ) {
                        order.codes.add(voucherOrders.code)
                        hasBo = true
                    }
                }
            }
        }

        return if (hasBo) {
            ClearPromoOrderData(
                orders = orders
            )
        } else {
            null
        }
    }

    private fun needToAddCode(
        cartStringGroup: String,
        isTypeLogistic: Boolean,
        set: MutableSet<String>
    ): Boolean {
        if (!isTypeLogistic) return true
        val isNotExistIsCurrentGroup = !set.contains(cartStringGroup)
        if (isNotExistIsCurrentGroup) {
            set.add(cartStringGroup)
        }
        return isNotExistIsCurrentGroup
    }
}

private class PromoRequestBoShipmentData(
    val shippingId: Int = 0,
    val spId: Int = 0,
    val boCampaignId: String = "",
    val shippingSubsidy: Long = 0,
    val benefitClass: String = "",
    val shippingPrice: Double = 0.0,
    val etaText: String = ""
)
