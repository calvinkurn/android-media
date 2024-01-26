package com.tokopedia.checkout.revamp.view.processor

import com.google.gson.Gson
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderInsurance
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.data.constant.InsuranceConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.scheduledelivery.domain.mapper.ScheduleDeliveryMapper
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.processor.LogisticProcessorGetRatesParam
import com.tokopedia.logisticcart.shipping.processor.LogisticProcessorGetSchellyParam
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MvcShippingBenefitUiModel
import javax.inject.Inject

class CheckoutLogisticProcessor @Inject constructor(
    private val schellyMapper: ScheduleDeliveryMapper
) {

    fun getProductForRatesRequest(orderProducts: List<CheckoutProductModel>): ArrayList<Product> {
        val products = arrayListOf<Product>()
        for (cartItemModel in orderProducts) {
            if (!cartItemModel.isError) {
                val product = Product()
                product.productId = cartItemModel.productId
                product.isFreeShipping = cartItemModel.isFreeShipping
                product.isFreeShippingTc = cartItemModel.isFreeShippingExtra
                products.add(product)
            }
        }
        return products
    }

    fun generateRatesMvcParam(cartString: String, promo: CheckoutPromoModel): String {
        var mvc = ""
        val tmpMvcShippingBenefitUiModel: MutableList<MvcShippingBenefitUiModel> = arrayListOf()
        val promoSpIdUiModels = promo.promo.additionalInfo.promoSpIds
        if (promoSpIdUiModels.isNotEmpty()) {
            for ((uniqueId, mvcShippingBenefits) in promoSpIdUiModels) {
                if (cartString == uniqueId) {
                    tmpMvcShippingBenefitUiModel.addAll(mvcShippingBenefits)
                }
            }
        }
        if (tmpMvcShippingBenefitUiModel.size > 0) {
            mvc = Gson().toJson(tmpMvcShippingBenefitUiModel)
        }
        return mvc.replace("\n", "").replace(" ", "")
    }

    private fun generateShippingBottomsheetParam(
        order: CheckoutOrderModel,
        orderProducts: List<CheckoutProductModel>,
        recipientAddressModel: RecipientAddressModel,
        isTradeIn: Boolean
    ): ShipmentDetailData {
        var orderValue = 0L
        var totalWeight = 0.0
        var totalWeightActual = 0.0
        var productFInsurance = 0
        var preOrder = false
        var productPreOrderDuration = 0
        val productList: ArrayList<Product> = ArrayList()
        val categoryList: HashSet<String> = hashSetOf()
        orderProducts.forEach {
            if (!it.isError) {
                if (it.isBundlingItem) {
                    if (it.bundlingItemPosition == ShipmentMapper.BUNDLING_ITEM_HEADER) {
                        orderValue += (it.bundleQuantity * it.bundlePrice).toLong()
                    }
                } else {
                    orderValue += (it.quantity * it.price).toLong()
                }
                if (it.isBMGMItem && it.bmgmItemPosition == ShipmentMapper.BMGM_ITEM_HEADER) {
                    orderValue -= it.bmgmTotalDiscount.toLong()
                }
                totalWeight += it.quantity * it.weight
                totalWeightActual += if (it.weightActual > 0) {
                    it.quantity * it.weightActual
                } else {
                    it.quantity * it.weight
                }
                if (it.fInsurance) {
                    productFInsurance = 1
                }
                preOrder = it.isPreOrder
                productPreOrderDuration = it.preOrderDurationDay
                categoryList.add(it.productCatId.toString())
                productList.add(Product(it.productId, it.isFreeShipping, it.isFreeShippingExtra))
            }
        }
        return ShipmentDetailData().apply {
            shopId = order.shopId.toString()
            preorder = preOrder
            isBlackbox = order.isBlackbox
            this.isTradein = isTradeIn
            addressId =
                if (recipientAddressModel.selectedTabIndex == 1 && recipientAddressModel.locationDataModel != null) recipientAddressModel.locationDataModel.addrId else order.addressId
            shipmentCartData = ShipmentCartData(
                originDistrictId = order.districtId,
                originPostalCode = order.postalCode,
                originLatitude = order.latitude,
                originLongitude = order.longitude,
                weight = totalWeight,
                weightActual = totalWeightActual,
                shopTier = order.shopTypeInfoData.shopTier,
                groupType = order.groupType,
                token = order.keroToken,
                ut = order.keroUnixTime,
                insurance = 1,
                productInsurance = productFInsurance,
                orderValue = orderValue,
                categoryIds = categoryList.joinToString(","),
                preOrderDuration = productPreOrderDuration,
                isFulfillment = order.isFulfillment,
                boMetadata = order.boMetadata,
                destinationAddress = recipientAddressModel.addressName,
                destinationDistrictId = recipientAddressModel.destinationDistrictId,
                destinationPostalCode = recipientAddressModel.postalCode,
                destinationLatitude = recipientAddressModel.latitude,
                destinationLongitude = recipientAddressModel.longitude,
                groupingState = order.groupingState
            )
        }
    }

    private fun getShippingParam(
        shipmentDetailData: ShipmentDetailData?,
        products: List<Product>?,
        cartString: String?,
        isTradeInDropOff: Boolean,
        recipientAddressModel: RecipientAddressModel?
    ): ShippingParam {
        val shippingParam = ShippingParam(
            originDistrictId = shipmentDetailData!!.shipmentCartData!!.originDistrictId,
            originPostalCode = shipmentDetailData.shipmentCartData!!.originPostalCode,
            originLatitude = shipmentDetailData.shipmentCartData!!.originLatitude,
            originLongitude = shipmentDetailData.shipmentCartData!!.originLongitude,
            weightInKilograms = shipmentDetailData.shipmentCartData!!.weight / 1000,
            weightActualInKilograms = shipmentDetailData.shipmentCartData!!.weightActual / 1000,
            shopId = shipmentDetailData.shopId,
            shopTier = shipmentDetailData.shipmentCartData!!.shopTier,
            token = shipmentDetailData.shipmentCartData!!.token,
            ut = shipmentDetailData.shipmentCartData!!.ut,
            insurance = shipmentDetailData.shipmentCartData!!.insurance,
            productInsurance = shipmentDetailData.shipmentCartData!!.productInsurance,
            orderValue = shipmentDetailData.shipmentCartData!!.orderValue,
            categoryIds = shipmentDetailData.shipmentCartData!!.categoryIds,
            isBlackbox = shipmentDetailData.isBlackbox,
            isPreorder = shipmentDetailData.preorder,
            addressId = recipientAddressModel!!.id,
            isTradein = shipmentDetailData.isTradein,
            products = products,
            uniqueId = cartString,
            isTradeInDropOff = isTradeInDropOff,
            preOrderDuration = shipmentDetailData.shipmentCartData!!.preOrderDuration,
            isFulfillment = shipmentDetailData.shipmentCartData!!.isFulfillment,
            boMetadata = shipmentDetailData.shipmentCartData!!.boMetadata,
            groupingState = shipmentDetailData.shipmentCartData!!.groupingState
        )
        if (isTradeInDropOff && recipientAddressModel.locationDataModel != null) {
            shippingParam.destinationDistrictId = recipientAddressModel.locationDataModel.district
            shippingParam.destinationPostalCode = recipientAddressModel.locationDataModel.postalCode
            shippingParam.destinationLatitude = recipientAddressModel.locationDataModel.latitude
            shippingParam.destinationLongitude = recipientAddressModel.locationDataModel.longitude
        } else {
            shippingParam.destinationDistrictId =
                shipmentDetailData.shipmentCartData!!.destinationDistrictId
            shippingParam.destinationPostalCode =
                shipmentDetailData.shipmentCartData!!.destinationPostalCode
            shippingParam.destinationLatitude =
                shipmentDetailData.shipmentCartData!!.destinationLatitude
            shippingParam.destinationLongitude =
                shipmentDetailData.shipmentCartData!!.destinationLongitude
        }
        shippingParam.groupType = shipmentDetailData.shipmentCartData!!.groupType
        return shippingParam
    }

    fun getRatesParam(
        orderModel: CheckoutOrderModel,
        orderProducts: List<CheckoutProductModel>,
        address: RecipientAddressModel,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean,
        codData: CodModel?,
        cartDataForRates: String,
        pslCode: String,
        useMvc: Boolean,
        promo: CheckoutPromoModel
    ): RatesParam {
        val shippingParam = getShippingParam(
            generateShippingBottomsheetParam(orderModel, orderProducts, address, isTradeIn),
            getProductForRatesRequest(orderProducts),
            orderModel.cartStringGroup,
            isTradeInDropOff,
            address
        )
        val counter = codData?.counterCod ?: -1
        val cornerId = address.isCornerAddress
        val isLeasing = orderModel.isLeasingProduct
        val ratesParamBuilder = RatesParam.Builder(orderModel.shopShipmentList, shippingParam)
            .isCorner(cornerId)
            .codHistory(counter)
            .isLeasing(isLeasing)
            .promoCode(pslCode)
            .cartData(cartDataForRates)
            .warehouseId(orderModel.fulfillmentId.toString())
            .groupMetadata(orderModel.groupMetadata)
        if (useMvc) {
            ratesParamBuilder.mvc(generateRatesMvcParam(orderModel.cartStringGroup, promo))
        }
        return ratesParamBuilder.build()
    }

    fun getSchellyProcessorParam(
        orderModel: CheckoutOrderModel,
        orderProducts: List<CheckoutItem>,
        address: RecipientAddressModel,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean,
        codData: CodModel?,
        cartDataForRates: String,
        pslCode: String,
        useMvc: Boolean,
        promo: CheckoutPromoModel,
        // specific schelly
        fullfilmentId: String,
        recommend: Boolean,
        startDate: String
    ): LogisticProcessorGetSchellyParam {
        val ratesGeneralParam = getRatesParam(
            orderModel,
            orderProducts,
            address,
            isTradeIn,
            isTradeInDropOff,
            codData,
            cartDataForRates,
            pslCode,
            useMvc,
            promo
        )
        val schellyParam = schellyMapper.map(
            ratesGeneralParam,
            fullfilmentId,
            startDate,
            recommend
        )

        return LogisticProcessorGetSchellyParam(
            schellyParam = schellyParam,
            validationMetadata = orderModel.validationMetadata
        )
    }

    fun getRatesProcessorParam(
        order: CheckoutOrderModel,
        orderProducts: List<CheckoutItem>,
        address: RecipientAddressModel,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean,
        codData: CodModel?,
        cartDataForRates: String,
        pslCode: String,
        useMvc: Boolean,
        promo: CheckoutPromoModel
    ): LogisticProcessorGetRatesParam {
        val ratesParam = getRatesParam(
            order,
            orderProducts,
            address,
            isTradeIn,
            isTradeInDropOff,
            codData,
            cartDataForRates,
            pslCode,
            useMvc,
            promo
        )
        return LogisticProcessorGetRatesParam(
            ratesParam = ratesParam,
            selectedServiceId = order.shippingId,
            selectedSpId = order.spId,
            boPromoCode = order.boCode,
            shouldResetCourier = order.shouldResetCourier,
            validationMetadata = order.validationMetadata,
            isDisableChangeCourier = order.isDisableChangeCourier,
            currentServiceId = order.shipment.courierItemData?.serviceId,
            isAutoCourierSelection = order.isAutoCourierSelection,
            isTradeInDropOff = isTradeInDropOff
        )
    }

    fun handleSyncShipmentCartItemModel(
        courierItemData: CourierItemData,
        orderModel: CheckoutOrderModel
    ) {
        if (courierItemData.scheduleDeliveryUiModel != null) {
            val isScheduleDeliverySelected = courierItemData.scheduleDeliveryUiModel?.isSelected
            if (isScheduleDeliverySelected == true &&
                (
                    courierItemData.scheduleDeliveryUiModel?.timeslotId != orderModel.timeslotId ||
                        courierItemData.scheduleDeliveryUiModel?.scheduleDate != orderModel.scheduleDate
                    )
            ) {
                orderModel.scheduleDate =
                    courierItemData.scheduleDeliveryUiModel?.scheduleDate ?: ""
                orderModel.timeslotId =
                    courierItemData.scheduleDeliveryUiModel?.timeslotId ?: 0
                orderModel.validationMetadata =
                    courierItemData.scheduleDeliveryUiModel?.deliveryProduct?.validationMetadata
                        ?: ""
            } else if (isScheduleDeliverySelected == false) {
                orderModel.scheduleDate = ""
                orderModel.timeslotId = 0L
                orderModel.validationMetadata = ""
            }
        }
    }
}

internal fun generateCheckoutOrderInsuranceFromCourier(
    courierItemData: CourierItemData,
    order: CheckoutOrderModel
): CheckoutOrderInsurance {
    return CheckoutOrderInsurance(
        when (courierItemData.selectedShipper.insuranceType) {
            InsuranceConstant.INSURANCE_TYPE_MUST -> {
                true
            }

            InsuranceConstant.INSURANCE_TYPE_NO -> {
                false
            }

            InsuranceConstant.INSURANCE_TYPE_OPTIONAL -> {
                courierItemData.selectedShipper.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES || order.isInsurance
            }

            else -> false
        }
    )
}
