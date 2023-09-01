package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.revamp.view.buttonPayment
import com.tokopedia.checkout.revamp.view.cost
import com.tokopedia.checkout.revamp.view.crossSellGroup
import com.tokopedia.checkout.revamp.view.promo
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.upsell
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.CrossSellOrderSummaryModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class CheckoutCalculator @Inject constructor(
    private val helper: CheckoutDataHelper,
    private val dispatchers: CoroutineDispatchers
) {

    private fun hasSetAllCourier(listData: List<CheckoutItem>): Boolean {
        for (itemData in listData) {
            if (itemData is CheckoutOrderModel && itemData.shipment.courierItemData == null && !itemData.isError) {
                return false
            }
        }
        return true
    }

    fun setPromoBenefit(
        cost: CheckoutCostModel,
        summariesUiModels: List<SummariesItemUiModel>,
        listData: List<CheckoutItem>
    ): CheckoutCostModel {
        cost.isHasDiscountDetails = false
        cost.discountAmount = 0
        cost.discountLabel = ""
        cost.shippingDiscountAmount = 0
        cost.shippingDiscountLabel = ""
        cost.productDiscountAmount = 0
        cost.productDiscountLabel = ""
        cost.cashbackAmount = 0
        cost.cashbackLabel = ""
        for (benefitSummary in summariesUiModels) {
            if (benefitSummary.type == SummariesUiModel.TYPE_DISCOUNT) {
                if (benefitSummary.details.isNotEmpty()) {
                    cost.isHasDiscountDetails = true
                    for (detail in benefitSummary.details) {
                        if (detail.type == SummariesUiModel.TYPE_SHIPPING_DISCOUNT) {
                            cost.shippingDiscountAmount = detail.amount
                            cost.shippingDiscountLabel = detail.description
                        } else if (detail.type == SummariesUiModel.TYPE_PRODUCT_DISCOUNT) {
                            cost.productDiscountAmount = detail.amount
                            cost.productDiscountLabel = detail.description
                        }
                    }
                } else if (hasSetAllCourier(listData)) {
                    cost.isHasDiscountDetails = false
                    cost.discountAmount = benefitSummary.amount
                    cost.discountLabel = benefitSummary.description
                }
            } else if (benefitSummary.type == SummariesUiModel.TYPE_CASHBACK) {
                cost.cashbackAmount = benefitSummary.amount
                cost.cashbackLabel = benefitSummary.description
            }
        }
        return cost
    }

    fun resetPromoBenefit(cost: CheckoutCostModel): CheckoutCostModel {
        cost.isHasDiscountDetails = false
        cost.discountAmount = 0
        cost.discountLabel = ""
        cost.shippingDiscountAmount = 0
        cost.shippingDiscountLabel = ""
        cost.productDiscountAmount = 0
        cost.productDiscountLabel = ""
        cost.cashbackAmount = 0
        cost.cashbackLabel = ""
        return cost
    }

    fun calculateWithoutPayment(
        listData: List<CheckoutItem>,
        isTradeInByDropOff: Boolean,
        summariesAddOnUiModel: HashMap<Int, String>
    ): List<CheckoutItem> {
        var totalWeight = 0.0
        var totalPrice: Double
        var additionalFee = 0.0
        var totalItemPrice = 0.0
        var tradeInPrice = 0.0
        var totalItem = 0
        var totalPurchaseProtectionPrice = 0.0
        var totalPurchaseProtectionItem = 0
        var shippingFee = 0.0
        var insuranceFee = 0.0
        var totalBookingFee = 0
        var hasAddOnSelected = false
        var totalAddOnGiftingPrice = 0.0
        var totalAddOnProductServicePrice = 0.0
        var qtyAddOn = 0
        val countMapSummaries = hashMapOf<Int, Pair<Double, Int>>()
        val listShipmentAddOnSummary: ArrayList<ShipmentAddOnSummaryModel> = arrayListOf()
        val checkoutCostModel = listData.cost()!!
        val promo = listData.promo()!!.promo
        var hasSelectAllShipping = true
        for (shipmentData in listData) {
            if (shipmentData is CheckoutOrderModel) {
                val cartItemModels = helper.getOrderProducts(listData, shipmentData.cartStringGroup)
                for (cartItem in cartItemModels) {
                    if (!cartItem.isError) {
                        totalWeight += cartItem.weight * cartItem.quantity
                        totalItem += cartItem.quantity
                        if (cartItem.isProtectionOptIn) {
                            totalPurchaseProtectionItem += cartItem.quantity
                            totalPurchaseProtectionPrice += cartItem.protectionPrice
                        }
                        if (cartItem.isValidTradeIn) {
                            tradeInPrice += cartItem.oldDevicePrice.toDouble()
                        }
                        if (cartItem.isBundlingItem) {
                            if (cartItem.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                                totalItemPrice += cartItem.bundleQuantity * cartItem.bundlePrice
                            }
                        } else {
                            totalItemPrice += cartItem.quantity * cartItem.price
                        }
                        if (cartItem.addOnGiftingProductLevelModel.status == 1) {
                            if (cartItem.addOnGiftingProductLevelModel.addOnsDataItemModelList.isNotEmpty()) {
                                for (addOnsData in cartItem.addOnGiftingProductLevelModel.addOnsDataItemModelList) {
                                    totalAddOnGiftingPrice += addOnsData.addOnPrice
                                    hasAddOnSelected = true
                                }
                            }
                        }
                        if (cartItem.addOnProduct.listAddOnProductData.isNotEmpty()) {
                            for (addOnProductService in cartItem.addOnProduct.listAddOnProductData) {
                                if (addOnProductService.isChecked) {
                                    totalAddOnProductServicePrice += (addOnProductService.price * cartItem.quantity)
                                    if (countMapSummaries.containsKey(addOnProductService.type)) {
                                        qtyAddOn += cartItem.quantity
                                    } else {
                                        qtyAddOn = cartItem.quantity
                                    }
                                    val totalPriceAddOn =
                                        (qtyAddOn * addOnProductService.price) + (
                                            countMapSummaries[addOnProductService.type]?.first
                                                ?: 0.0
                                            )
                                    countMapSummaries[addOnProductService.type] =
                                        totalPriceAddOn to qtyAddOn
                                }
                            }
                        }
                    }
                }
                if (shipmentData.shipment.courierItemData != null && !shipmentData.isError) {
                    /*val isTradeInPickup = isTradeInByDropOff
                    if (isTradeInPickup) {
                        if (shipmentData.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
                            shippingFee += shipmentData.selectedShipmentDetailData!!
                                .selectedCourierTradeInDropOff!!.shipperPrice.toDouble()
                            if (useInsurance != null && useInsurance) {
                                insuranceFee += shipmentData.selectedShipmentDetailData!!
                                    .selectedCourierTradeInDropOff!!.insurancePrice.toDouble()
                            }
                            additionalFee += shipmentData.selectedShipmentDetailData!!
                                .selectedCourierTradeInDropOff!!.additionalPrice.toDouble()
                        } else {
                            shippingFee = 0.0
                            insuranceFee = 0.0
                            additionalFee = 0.0
                        }
                    } else if (shipmentData.selectedShipmentDetailData!!.selectedCourier != null) {*/
                    shippingFee += shipmentData.shipment
                        .courierItemData.selectedShipper.shipperPrice.toDouble()
                    if (shipmentData.shipment.insurance.isCheckInsurance) {
                        insuranceFee += shipmentData.shipment
                            .courierItemData.selectedShipper.insurancePrice.toDouble()
                    }
                    additionalFee += shipmentData.shipment.courierItemData.additionalPrice.toDouble()
                } else if (!shipmentData.isError) {
                    hasSelectAllShipping = false
                }
                if (shipmentData.isLeasingProduct) {
                    totalBookingFee += shipmentData.bookingFee
                }
                val addOnsDataModel = shipmentData.addOnsOrderLevelModel
                if (addOnsDataModel.status == 1 && addOnsDataModel.addOnsDataItemModelList.isNotEmpty()) {
                    for ((addOnPrice) in addOnsDataModel.addOnsDataItemModelList) {
                        totalAddOnGiftingPrice += addOnPrice
                        hasAddOnSelected = true
                    }
                }
            }
        }
        var shipmentCost = checkoutCostModel.copy()
        shipmentCost = setPromoBenefit(shipmentCost, promo.benefitSummaryInfo.summaries, listData)
        var finalShippingFee = shippingFee - shipmentCost.shippingDiscountAmount
        if (finalShippingFee < 0) {
            finalShippingFee = 0.0
        }
        totalPrice =
            totalItemPrice + finalShippingFee + insuranceFee + totalPurchaseProtectionPrice + additionalFee + totalBookingFee -
            shipmentCost.productDiscountAmount - tradeInPrice + totalAddOnGiftingPrice + totalAddOnProductServicePrice
        shipmentCost = shipmentCost.copy(totalWeight = totalWeight)
        shipmentCost = shipmentCost.copy(additionalFee = additionalFee)
        shipmentCost = shipmentCost.copy(originalItemPrice = totalItemPrice)
        shipmentCost =
            shipmentCost.copy(finalItemPrice = totalItemPrice - shipmentCost.productDiscountAmount)
        shipmentCost = shipmentCost.copy(totalItem = totalItem)
        shipmentCost = shipmentCost.copy(originalShippingFee = shippingFee)
        shipmentCost = shipmentCost.copy(finalShippingFee = finalShippingFee)
        shipmentCost = shipmentCost.copy(hasSelectAllShipping = hasSelectAllShipping)
        shipmentCost = shipmentCost.copy(shippingInsuranceFee = insuranceFee)
        var totalOtherFee = insuranceFee + totalAddOnGiftingPrice + totalAddOnProductServicePrice
        shipmentCost.totalPurchaseProtectionItem = totalPurchaseProtectionItem
        shipmentCost.purchaseProtectionFee = totalPurchaseProtectionPrice
        shipmentCost.tradeInPrice = tradeInPrice
        shipmentCost.totalAddOnPrice = totalAddOnGiftingPrice
        shipmentCost.hasAddOn = hasAddOnSelected
        shipmentCost = shipmentCost.copy(totalPrice = totalPrice)
        var upsellCost: CheckoutCrossSellModel? = null
        val upsellModel = listData.upsell()
        if (upsellModel != null && upsellModel.upsell.isSelected && upsellModel.upsell.isShow) {
            val crossSellModel = CrossSellModel()
            crossSellModel.orderSummary =
                CrossSellOrderSummaryModel(upsellModel.upsell.summaryInfo, "")
            crossSellModel.price = upsellModel.upsell.price.toDouble()
            upsellCost = CheckoutCrossSellModel(crossSellModel, true, true, -1)
        }
        val listCheckedCrossModel = ArrayList<CheckoutCrossSellModel>()
        val crossSellGroup = listData.crossSellGroup() ?: CheckoutCrossSellGroupModel()
        val listCrossSellItem = arrayListOf<CheckoutCrossSellItem>()
        var egold: CheckoutEgoldModel? = null
        var egoldIndex: Int = -1
        for ((index, crossSellModel) in crossSellGroup.crossSellList.withIndex()) {
            when (crossSellModel) {
                is CheckoutCrossSellModel -> {
                    if (crossSellModel.isChecked) {
                        listCheckedCrossModel.add(crossSellModel)
                        totalPrice += crossSellModel.crossSellModel.price
                        totalOtherFee += crossSellModel.crossSellModel.price
                    }
                    listCrossSellItem.add(index, crossSellModel)
                }

                is CheckoutDonationModel -> {
                    if (crossSellModel.donation.isChecked) {
                        shipmentCost =
                            shipmentCost.copy(donation = crossSellModel.donation.nominal.toDouble())
                    } else {
                        if (shipmentCost.donation > 0) {
                            shipmentCost = shipmentCost.copy(donation = 0.0)
                        }
                    }
                    totalPrice += shipmentCost.donation
                    totalOtherFee += shipmentCost.donation
                    listCrossSellItem.add(index, crossSellModel)
                }

                is CheckoutEgoldModel -> {
                    egold = crossSellModel
                    egoldIndex = index
                    listCrossSellItem.add(index, crossSellModel)
                }
            }
        }
        if (upsellCost != null) {
            listCheckedCrossModel.add(upsellCost)
            totalPrice += upsellCost.crossSellModel.price
            totalOtherFee += upsellCost.crossSellModel.price
        }
        if (egold != null && egoldIndex >= 0) {
            var egoldAttribute = egold.egoldAttributeModel
            if (egold.egoldAttributeModel.isEligible) {
                egoldAttribute = updateEmasCostModel(totalPrice, egoldAttribute)
                if (egoldAttribute.isChecked) {
                    totalPrice += egoldAttribute.buyEgoldValue.toDouble()
                    shipmentCost.emasPrice = egoldAttribute.buyEgoldValue.toDouble()
                } else if (shipmentCost.emasPrice > 0) {
                    shipmentCost.emasPrice = 0.0
                }
            }
            totalOtherFee += shipmentCost.emasPrice
            listCrossSellItem[egoldIndex] = egold.copy(
                egoldAttributeModel = egoldAttribute,
                buyEgoldValue = egoldAttribute.buyEgoldValue
            )
        }
        shipmentCost = shipmentCost.copy(totalOtherFee = totalOtherFee)
        shipmentCost = shipmentCost.copy(totalPrice = totalPrice)
        shipmentCost.listCrossSell = listCheckedCrossModel
        shipmentCost.bookingFee = totalBookingFee

        for (entry in countMapSummaries) {
            val addOnWording = summariesAddOnUiModel[entry.key]?.replace(
                CartConstant.QTY_ADDON_REPLACE,
                entry.value.second.toString()
            )
            val addOnPrice =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(entry.value.first, false)
                    .removeDecimalSuffix()
            val summaryAddOn = ShipmentAddOnSummaryModel(
                wording = addOnWording ?: "",
                type = entry.key,
                qty = entry.value.second,
                priceLabel = addOnPrice,
                priceValue = entry.value.first.toLong()
            )
            listShipmentAddOnSummary.add(summaryAddOn)
        }

        shipmentCost.listAddOnSummary = listShipmentAddOnSummary
        shipmentCost =
            shipmentCost.copy(dynamicPlatformFee = shipmentCost.dynamicPlatformFee.copy(isLoading = true))

        val buttonPaymentModel =
            updateCheckoutButtonData(listData, shipmentCost, isTradeInByDropOff)

        return listData.toMutableList().apply {
            set(size - 3, shipmentCost)
            set(size - 2, crossSellGroup.copy(crossSellList = listCrossSellItem))
            set(size - 1, buttonPaymentModel)
        }
    }

    fun updateShipmentCostModel(
        listData: List<CheckoutItem>,
        newCost: CheckoutCostModel,
        isTradeInByDropOff: Boolean,
        summariesAddOnUiModel: HashMap<Int, String>
    ): List<CheckoutItem> {
        val newList = calculateWithoutPayment(listData, isTradeInByDropOff, summariesAddOnUiModel)
        var shipmentCost = newList.cost()!!.copy(dynamicPlatformFee = newCost.dynamicPlatformFee)
        var buttonPaymentModel = newList.buttonPayment()!!
        var cartItemCounter = 0
        var cartItemErrorCounter = 0
        var hasLoadingItem = false
        var shouldShowInsuranceTnc = false
        for (shipmentCartItemModel in newList) {
            if (shipmentCartItemModel is CheckoutOrderModel) {
                if ((shipmentCartItemModel.shipment.courierItemData != null && !isTradeInByDropOff) /*|| (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null && isTradeInByDropOff)*/) {
                    if (!hasLoadingItem) {
                        hasLoadingItem = validateLoadingItem(shipmentCartItemModel)
                    }
                    cartItemCounter++
                }
                if (shipmentCartItemModel.isError) {
                    cartItemErrorCounter++
                } else if (!shouldShowInsuranceTnc) {
                    val orderProducts =
                        helper.getOrderProducts(listData, shipmentCartItemModel.cartStringGroup)
                    for (cartItemModel in orderProducts) {
                        if (cartItemModel.addOnProduct.listAddOnProductData.any { it.type == AddOnConstant.PRODUCT_PROTECTION_INSURANCE_TYPE && it.isChecked }) {
                            shouldShowInsuranceTnc = true
                        }
                    }
                    if (shipmentCartItemModel.shipment.insurance.isCheckInsurance) {
                        shouldShowInsuranceTnc = true
                    }
                }
            }
        }
        val checkoutOrderModels = newList.filterIsInstance(CheckoutOrderModel::class.java)
        val priceTotal: Double =
            if (shipmentCost.totalPrice <= 0) 0.0 else shipmentCost.totalPrice
        val platformFee: Double =
            if (shipmentCost.dynamicPlatformFee.fee <= 0) 0.0 else shipmentCost.dynamicPlatformFee.fee
        val finalPrice = priceTotal + platformFee
        if (cartItemCounter > 0 && cartItemCounter <= checkoutOrderModels.size) {
            val priceTotalFormatted =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    finalPrice,
                    false
                ).removeDecimalSuffix()
            shipmentCost = shipmentCost.copy(totalPriceString = priceTotalFormatted)
            buttonPaymentModel = buttonPaymentModel.copy(
                useInsurance = shouldShowInsuranceTnc,
                enable = !hasLoadingItem,
                totalPrice = priceTotalFormatted
            )
        } else {
            shipmentCost = shipmentCost.copy(totalPriceString = "-")
            buttonPaymentModel = buttonPaymentModel.copy(
                useInsurance = shouldShowInsuranceTnc,
                enable = cartItemErrorCounter < checkoutOrderModels.size,
                totalPrice = "-"
            )
        }

        buttonPaymentModel = buttonPaymentModel.copy(
            totalPriceNum = finalPrice
        )

        return newList.toMutableList().apply {
            set(size - 3, shipmentCost)
            set(size - 1, buttonPaymentModel)
        }
    }

    private fun updateEmasCostModel(
        totalPrice: Double,
        egoldAttribute: EgoldAttributeModel
    ): EgoldAttributeModel {
        var valueTOCheck = 0
        val buyEgoldValue: Long
        if (egoldAttribute.isTiering) {
            egoldAttribute.egoldTieringModelArrayList.sortWith { o1, o2 -> (o1.minTotalAmount - o2.minTotalAmount).toInt() }
            var egoldTieringModel = EgoldTieringModel()
            for (data in egoldAttribute.egoldTieringModelArrayList) {
                if (totalPrice >= data.minTotalAmount) {
                    valueTOCheck = (totalPrice % data.basisAmount).toInt()
                    egoldTieringModel = data
                }
            }
            buyEgoldValue = calculateBuyEgoldValue(
                valueTOCheck,
                egoldTieringModel.minAmount.toInt(),
                egoldTieringModel.maxAmount.toInt(),
                egoldTieringModel.basisAmount
            )
        } else {
            valueTOCheck = (totalPrice % LAST_THREE_DIGIT_MODULUS).toInt()
            buyEgoldValue = calculateBuyEgoldValue(
                valueTOCheck,
                egoldAttribute.minEgoldRange,
                egoldAttribute.maxEgoldRange,
                LAST_THREE_DIGIT_MODULUS
            )
        }
        egoldAttribute.buyEgoldValue = buyEgoldValue
        return egoldAttribute
    }

    private fun calculateBuyEgoldValue(
        valueTOCheck: Int,
        minRange: Int,
        maxRange: Int,
        basisAmount: Long
    ): Long {
        if (basisAmount == 0L) {
            return 0
        }
        var buyEgoldValue: Long = 0
        for (i in minRange..maxRange) {
            if ((valueTOCheck + i) % basisAmount == 0L) {
                buyEgoldValue = i.toLong()
                break
            }
        }
        return buyEgoldValue
    }

    private fun updateCheckoutButtonData(
        listData: List<CheckoutItem>,
        shipmentCost: CheckoutCostModel,
        isTradeInByDropOff: Boolean
    ): CheckoutButtonPaymentModel {
        var cartItemCounter = 0
        var cartItemErrorCounter = 0
        var hasLoadingItem = false
        for (shipmentCartItemModel in listData) {
            if (shipmentCartItemModel is CheckoutOrderModel) {
                if ((shipmentCartItemModel.shipment.courierItemData != null && !isTradeInByDropOff) /*|| (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null && isTradeInByDropOff)*/) {
                    if (!hasLoadingItem) {
                        hasLoadingItem = validateLoadingItem(shipmentCartItemModel)
                    }
                    cartItemCounter++
                }
                if (shipmentCartItemModel.isError) {
                    cartItemErrorCounter++
                }
            }
        }
        val checkoutOrderModels = listData.filterIsInstance(CheckoutOrderModel::class.java)
        if (cartItemCounter > 0 && cartItemCounter <= checkoutOrderModels.size) {
            val priceTotal: Double =
                if (shipmentCost.totalPrice <= 0) 0.0 else shipmentCost.totalPrice
            val platformFee: Double =
                if (shipmentCost.dynamicPlatformFee.fee <= 0) 0.0 else shipmentCost.dynamicPlatformFee.fee
            val finalPrice = priceTotal + platformFee
            val priceTotalFormatted =
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    finalPrice,
                    false
                ).removeDecimalSuffix()
            return updateShipmentButtonPaymentModel(
                listData.buttonPayment()!!,
                enable = !hasLoadingItem,
                totalPrice = priceTotalFormatted
            )
        } else {
            return updateShipmentButtonPaymentModel(
                listData.buttonPayment()!!,
                enable = cartItemErrorCounter < checkoutOrderModels.size,
                totalPrice = "-"
            )
        }
    }

    private fun validateLoadingItem(shipmentCartItemModel: CheckoutOrderModel): Boolean {
        return shipmentCartItemModel.isStateLoadingCourierState || shipmentCartItemModel.shipment.isLoading
    }

    fun updateShipmentButtonPaymentModel(
        buttonPaymentModel: CheckoutButtonPaymentModel,
        enable: Boolean? = null,
        totalPrice: String? = null,
        loading: Boolean? = null
    ): CheckoutButtonPaymentModel {
        return buttonPaymentModel.copy(
            enable = enable ?: buttonPaymentModel.enable,
            totalPrice = totalPrice ?: buttonPaymentModel.totalPrice,
            loading = /*if (isValidatingFinalPromo) {
                true
            } else {*/
            loading ?: buttonPaymentModel.loading
            /*}*/
        )
    }

    companion object {
        private const val LAST_THREE_DIGIT_MODULUS: Long = 1000
    }
}
