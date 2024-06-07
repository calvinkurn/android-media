package com.tokopedia.checkout.revamp.view.processor

import android.annotation.SuppressLint
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeResponse
import com.tokopedia.checkout.domain.usecase.GetPaymentFeeCheckoutUseCase
import com.tokopedia.checkout.revamp.view.CheckoutViewModel
import com.tokopedia.checkout.revamp.view.PAYMENT_INDEX_FROM_BOTTOM
import com.tokopedia.checkout.revamp.view.address
import com.tokopedia.checkout.revamp.view.buttonPayment
import com.tokopedia.checkout.revamp.view.cost
import com.tokopedia.checkout.revamp.view.firstOrNullInstanceOf
import com.tokopedia.checkout.revamp.view.payment
import com.tokopedia.checkout.revamp.view.promo
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductBenefitModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.OriginalCheckoutPaymentData
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.checkoutpayment.data.AdditionalInfoData
import com.tokopedia.checkoutpayment.data.BenefitSummaryInfoData
import com.tokopedia.checkoutpayment.data.CartAddOnData
import com.tokopedia.checkoutpayment.data.CartAddressData
import com.tokopedia.checkoutpayment.data.CartData
import com.tokopedia.checkoutpayment.data.CartDetail
import com.tokopedia.checkoutpayment.data.CartDetailData
import com.tokopedia.checkoutpayment.data.CartDetailsItem
import com.tokopedia.checkoutpayment.data.CartGroupData
import com.tokopedia.checkoutpayment.data.CartProductCategoryData
import com.tokopedia.checkoutpayment.data.CartProductData
import com.tokopedia.checkoutpayment.data.CartShippingInfoData
import com.tokopedia.checkoutpayment.data.CartShopOrderData
import com.tokopedia.checkoutpayment.data.CreditCardTenorListRequest
import com.tokopedia.checkoutpayment.data.DetailsItemData
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetRequest
import com.tokopedia.checkoutpayment.data.GoCicilAddressRequest
import com.tokopedia.checkoutpayment.data.GoCicilInstallmentRequest
import com.tokopedia.checkoutpayment.data.GoCicilProductRequest
import com.tokopedia.checkoutpayment.data.PaymentData
import com.tokopedia.checkoutpayment.data.PaymentFeeRequest
import com.tokopedia.checkoutpayment.data.PaymentRequest
import com.tokopedia.checkoutpayment.data.PromoDetail
import com.tokopedia.checkoutpayment.data.SummariesItemData
import com.tokopedia.checkoutpayment.data.UsageSummariesData
import com.tokopedia.checkoutpayment.data.VoucherOrderItemData
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentOption
import com.tokopedia.checkoutpayment.domain.PaymentWidgetData
import com.tokopedia.checkoutpayment.processor.PaymentProcessor
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetState
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class CheckoutPaymentProcessor @Inject constructor(
    private val getPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase,
    val processor: PaymentProcessor,
    private val checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection,
    private val userSessionInterface: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val helper: CheckoutDataHelper
) {

    suspend fun checkPlatformFee(
        shipmentPlatformFeeData: ShipmentPlatformFeeData,
        cost: CheckoutCostModel,
        request: PaymentFeeCheckoutRequest
    ): CheckoutCostModel {
        if (!cost.hasSelectAllShipping) {
            return cost.copy(dynamicPlatformFee = ShipmentPaymentFeeModel(isLoading = false))
        }
        if (shipmentPlatformFeeData.isEnable) {
            val platformFeeModel = cost.dynamicPlatformFee
            if (cost.totalPrice > platformFeeModel.minRange &&
                cost.totalPrice < platformFeeModel.maxRange
            ) {
                return cost.copy(dynamicPlatformFee = platformFeeModel.copy(isLoading = false))
            } else {
                val paymentFee = getDynamicPaymentFee(request)
                if (paymentFee != null) {
                    val platformFee = ShipmentPaymentFeeModel()
                    for (fee in paymentFee.data) {
                        if (fee.code.equals(
                                CheckoutViewModel.PLATFORM_FEE_CODE,
                                ignoreCase = true
                            )
                        ) {
                            platformFee.title = fee.title
                            platformFee.fee = fee.fee
                            platformFee.minRange = fee.minRange
                            platformFee.maxRange = fee.maxRange
                            platformFee.isShowTooltip = fee.showTooltip
                            platformFee.tooltip = fee.tooltipInfo
                            platformFee.isShowSlashed = fee.showSlashed
                            platformFee.slashedFee = fee.slashedFee.toDouble()
                        }
                    }
                    checkoutAnalyticsCourierSelection.eventViewPlatformFeeInCheckoutPage(
                        userSessionInterface.userId,
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                            platformFee.fee.toLong(),
                            false
                        ).removeDecimalSuffix()
                    )
                    return cost.copy(dynamicPlatformFee = platformFee)
                } else {
                    return cost.copy(
                        dynamicPlatformFee = ShipmentPaymentFeeModel(
                            isShowTicker = true,
                            ticker = shipmentPlatformFeeData.errorWording
                        )
                    )
                }
            }
        }
        return cost.copy(dynamicPlatformFee = cost.dynamicPlatformFee.copy(isLoading = false))
    }

    private suspend fun getDynamicPaymentFee(request: PaymentFeeCheckoutRequest): PaymentFeeResponse? {
        return withContext(dispatchers.io) {
            try {
                getPaymentFeeCheckoutUseCase.setParams(request)
                val paymentFeeGqlResponse = getPaymentFeeCheckoutUseCase.executeOnBackground()
                if (paymentFeeGqlResponse.response.success) {
                    return@withContext paymentFeeGqlResponse.response
                } else {
                    return@withContext null
                }
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext null
            }
        }
    }

    suspend fun checkPlatformFeeOcc(
        cost: CheckoutCostModel,
        request: PaymentFeeRequest
    ): CheckoutCostModel {
        if (!cost.hasSelectAllShipping) {
            return cost.copy(
                dynamicPlatformFee = ShipmentPaymentFeeModel(isLoading = false),
                usePaymentFees = true
            )
        }
        val paymentFees = processor.getPaymentFee(request)
        if (paymentFees != null) {
            val platformFee = ShipmentPaymentFeeModel()
            for (fee in paymentFees) {
                if (fee.code.equals(
                        CheckoutViewModel.PLATFORM_FEE_CODE,
                        ignoreCase = true
                    )
                ) {
                    platformFee.title = fee.title
                    platformFee.fee = fee.fee
                    platformFee.isShowTooltip = fee.showTooltip
                    platformFee.tooltip = fee.tooltipInfo
                    platformFee.isShowSlashed = fee.showSlashed
                    platformFee.slashedFee = fee.slashedFee.toDouble()
                }
            }
            checkoutAnalyticsCourierSelection.eventViewPlatformFeeInCheckoutPage(
                userSessionInterface.userId,
                CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    platformFee.fee.toLong(),
                    false
                ).removeDecimalSuffix()
            )
            return cost.copy(
                dynamicPlatformFee = platformFee,
                dynamicPaymentFees = paymentFees,
                usePaymentFees = true
            )
        } else {
            return cost.copy(
                dynamicPaymentFees = null,
                dynamicPlatformFee = ShipmentPaymentFeeModel(
                    isLoading = false
                ),
                usePaymentFees = true
            )
        }
    }

    @SuppressLint("PII Data Exposure")
    fun generatePaymentRequest(
        checkoutItems: List<CheckoutItem>,
        payment: CheckoutPaymentModel
    ): PaymentRequest {
        val address = checkoutItems.address()!!
        val promo = checkoutItems.promo()!!
        val cost = checkoutItems.cost()!!
        val paymentData = payment.data?.paymentWidgetData?.firstOrNull()
        return PaymentRequest(
            payment = PaymentData(
                gatewayCode = paymentData?.gatewayCode.ifNullOrBlank { payment.originalData.gatewayCode },
                profileCode = paymentData?.profileCode.orEmpty(),
                paymentAmount = cost.totalPrice
            ),
            CartDetail(
                cart = CartData(
                    data = listOf(
                        CartDetailData(
                            address = CartAddressData(
                                id = address.recipientAddressModel.id,
                                address = address.recipientAddressModel.street,
                                state = address.recipientAddressModel.provinceName,
                                city = address.recipientAddressModel.cityName,
                                country = address.recipientAddressModel.countryName,
                                postalCode = address.recipientAddressModel.postalCode
                            ),
                            checkoutItems.filterIsInstance(CheckoutOrderModel::class.java).mapNotNull {
                                if (it.isError) {
                                    return@mapNotNull null
                                } else {
                                    val selectedShipper = it.shipment.courierItemData?.selectedShipper
                                    val courierData = it.shipment.shippingCourierUiModels.firstOrNull { courier -> courier.productData.shipperProductId == selectedShipper?.shipperProductId }
                                    CartGroupData(
                                        cartStringGroup = it.cartStringGroup,
                                        shippingInfo = CartShippingInfoData(
                                            spId = selectedShipper?.shipperProductId.toZeroIfNull().toString(),
                                            originalShippingPrice = selectedShipper?.shipperPrice.toZeroIfNull().toDouble(),
                                            serviceName = if (selectedShipper?.logPromoCode.isNullOrEmpty()) courierData?.serviceData?.serviceName.orEmpty() else selectedShipper?.promoTitle.orEmpty(),
                                            shipperName = selectedShipper?.shipperName.orEmpty(),
                                            eta = selectedShipper?.etaText.orEmpty(),
                                            insurancePrice = selectedShipper?.insurancePrice.toZeroIfNull().toDouble()
                                        ),
                                        shopOrders = helper.getOrderProducts(
                                            checkoutItems,
                                            it.cartStringGroup
                                        ).groupBy { item ->
                                            if (item is CheckoutProductModel) {
                                                item.cartStringOrder
                                            } else {
                                                (item as CheckoutProductBenefitModel).cartStringOrder
                                            }
                                        }.values.mapNotNull orders@{ order ->
                                            val singleItem = order.firstOrNullInstanceOf(CheckoutProductModel::class.java) ?: return@orders null
                                            CartShopOrderData(
                                                shopId = singleItem.shopId,
                                                warehouseId = singleItem.warehouseId.toLongOrZero(),
                                                shopTier = singleItem.shopTier.toLong(),
                                                products = order.mapNotNull products@{ product ->
                                                    if (product is CheckoutProductModel) {
                                                        if (!product.isError) {
                                                            CartProductData(
                                                                productId = product.productId.toString(),
                                                                name = product.name,
                                                                price = product.price.toLong(),
                                                                quantity = product.quantity.toLong(),
                                                                totalPrice = product.price.toLong() * product.quantity,
                                                                bundleGroupId = "",
                                                                addonItems = generateAddonProductLevel(
                                                                    product
                                                                ),
                                                                category = CartProductCategoryData(
                                                                    id = product.productCatId.toString(),
                                                                    name = product.lastLevelCategory,
                                                                    identifier = product.categoryIdentifier
                                                                )
                                                            )
                                                        } else {
                                                            null
                                                        }
                                                    } else {
                                                        val orderProduct = product as CheckoutProductBenefitModel
                                                        CartProductData(
                                                            productId = orderProduct.productId,
                                                            name = orderProduct.productName,
                                                            price = orderProduct.finalPrice.toLong(),
                                                            quantity = orderProduct.quantity.toLong(),
                                                            totalPrice = orderProduct.finalPrice.toLong() * orderProduct.quantity,
                                                            bundleGroupId = "",
                                                            addonItems = emptyList(),
                                                            category = CartProductCategoryData(
                                                                id = "",
                                                                name = "",
                                                                identifier = ""
                                                            )
                                                        )
                                                    }
                                                },
                                                bundle = emptyList(),
                                                addonItems = generateAddonOrderLevel(it),
                                                cartStringOrder = singleItem.cartStringOrder
                                            )
                                        }
                                    )
                                }
                            }
                        )
                    )
                )
            ),
            PromoDetail(
                benefitSummaryInfo = BenefitSummaryInfoData(
                    promo.promo.benefitSummaryInfo.summaries.map { summary ->
                        SummariesItemData(
                            type = summary.type,
                            details = summary.details.map { detail ->
                                DetailsItemData(
                                    amount = detail.amount.toLong(),
                                    type = detail.type
                                )
                            }
                        )
                    }
                ),
                voucherOrders = promo.promo.voucherOrders.mapNotNull { voucher ->
                    if (voucher.uniqueId.isNotEmpty() &&
                        voucher.message.state != "red" &&
                        voucher.code.isNotEmpty() &&
                        voucher.type.isNotEmpty()
                    ) {
                        VoucherOrderItemData(
                            code = voucher.code,
                            uniqueId = voucher.uniqueId,
                            shippingId = voucher.shippingId.toLong(),
                            spId = voucher.spId.toLong(),
                            type = voucher.type,
                            success = voucher.success,
                            cartStringGroup = voucher.cartStringGroup
                        )
                    } else {
                        null
                    }
                },
                additionalInfo = AdditionalInfoData(
                    usageSummaries = promo.promo.additionalInfo.usageSummaries.map { usage ->
                        UsageSummariesData(
                            type = usage.type,
                            amountString = usage.amountStr,
                            amount = usage.amount.toLong()
                        )
                    }
                )
            )
        )
    }

    private fun generateAddonOrderLevel(order: CheckoutOrderModel): List<CartAddOnData> {
        val addOnItemModel = order.addOnsOrderLevelModel.addOnsDataItemModelList.firstOrNull()
        if (order.addOnsOrderLevelModel.status == 1 && addOnItemModel != null) {
            return listOf(
                CartAddOnData(
                    name = order.addOnsOrderLevelModel.addOnsButtonModel.title,
                    price = addOnItemModel.addOnPrice.toLong() / addOnItemModel.addOnQty,
                    quantity = addOnItemModel.addOnQty,
                    totalPrice = addOnItemModel.addOnPrice.toLong()
                )
            )
        }
        return emptyList()
    }

    private fun generateAddonProductLevel(orderProduct: CheckoutProductModel): List<CartAddOnData> {
        val addOnProductLevelItems = mutableListOf<CartAddOnData>()
        val addOnItemModel = orderProduct.addOnGiftingProductLevelModel.addOnsDataItemModelList.firstOrNull()
        if (orderProduct.addOnGiftingProductLevelModel.status == 1 && addOnItemModel != null) {
            addOnProductLevelItems.add(
                CartAddOnData(
                    name = orderProduct.addOnGiftingProductLevelModel.addOnsButtonModel.title,
                    price = addOnItemModel.addOnPrice.toLong() / addOnItemModel.addOnQty,
                    quantity = addOnItemModel.addOnQty,
                    totalPrice = addOnItemModel.addOnPrice.toLong()
                )
            )
        }

        orderProduct.addOnProduct.listAddOnProductData.forEach { addOnsProduct ->
            if (addOnsProduct.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK || addOnsProduct.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY) {
                val addOnQty: Long =
                    if (addOnsProduct.fixedQty) 1 else orderProduct.quantity.toLong()
                addOnProductLevelItems.add(
                    CartAddOnData(
                        name = addOnsProduct.name,
                        price = addOnsProduct.price.toLong(),
                        quantity = addOnQty,
                        totalPrice = addOnsProduct.price.toLong() * addOnQty
                    )
                )
            }
        }
        return addOnProductLevelItems
    }

    suspend fun getPaymentWidget(param: GetPaymentWidgetRequest, payment: CheckoutPaymentModel): CheckoutPaymentModel {
        val data = processor.getPaymentWidget(param)
        val originalFirstData = data?.paymentWidgetData?.firstOrNull()
        var newPayment = payment
        if (originalFirstData != null) {
            newPayment = newPayment.copy(
                originalData = OriginalCheckoutPaymentData(
                    gatewayCode = originalFirstData.gatewayCode,
                    tenureType = originalFirstData.installmentPaymentData.selectedTenure,
                    metadata = originalFirstData.metadata
                )
            )
        }
        return newPayment.copy(
            data = data,
            widget = payment.widget.copy(
                state = if (data == null || data.paymentWidgetData.isEmpty()) CheckoutPaymentWidgetState.Error else CheckoutPaymentWidgetState.Normal
            ),
            tenorList = null,
            installmentData = null
        )
    }

    fun generateCreditCardTenorRequest(
        payment: CheckoutPaymentModel,
        paymentData: PaymentWidgetData,
        paymentRequest: PaymentRequest,
        checkoutItems: List<CheckoutItem>,
        cost: CheckoutCostModel,
        shipmentPlatformFeeData: ShipmentPlatformFeeData
    ): CreditCardTenorListRequest {
        return CreditCardTenorListRequest(
            tokenId = paymentData.installmentPaymentData.creditCardAttribute.tokenId,
            userId = userSessionInterface.userId.toLongOrZero(),
            totalAmount = cost.originalItemPrice,
            discountAmount = cost.totalDiscounts,
            profileCode = paymentData.profileCode,
            otherAmount = cost.totalOtherFee,
            cartDetails = checkoutItems.filterIsInstance(CheckoutOrderModel::class.java).map {
                CartDetailsItem(it.shopTypeInfoData.shopTier, cost.totalProductAndShippingPrice)
            },
            ccfeeSignature = paymentData.installmentPaymentData.creditCardAttribute.tenureSignature,
            timestamp = paymentData.unixTimestamp,
            additionalData = shipmentPlatformFeeData.additionalData,
            detailData = paymentRequest
        )
    }

    suspend fun getTenorList(
        payment: CheckoutPaymentModel,
        paymentData: PaymentWidgetData,
        paymentRequest: PaymentRequest,
        checkoutItems: List<CheckoutItem>,
        cost: CheckoutCostModel,
        shipmentPlatformFeeData: ShipmentPlatformFeeData
    ): CheckoutPaymentModel {
        try {
            val tenorList = processor.getCreditCardTenorList(
                generateCreditCardTenorRequest(payment, paymentData, paymentRequest, checkoutItems, cost, shipmentPlatformFeeData)
            )
            val paymentWidgetData = payment.data!!.paymentWidgetData.toMutableList()
            val widgetData = paymentWidgetData.first()
            val selectedTenure = widgetData.installmentPaymentData.selectedTenure
            val selectedTenor = tenorList?.firstOrNull { it.tenure == selectedTenure }
            if (selectedTenor?.disable != false) {
                // force user to choose tenure again
                return payment.copy(tenorList = null)
            }
            val newGatewayCode = selectedTenor.gatewayCode.ifBlank { widgetData.gatewayCode }
            paymentWidgetData[0] = widgetData.copy(gatewayCode = newGatewayCode)
            return payment.copy(tenorList = tenorList, data = payment.data.copy(paymentWidgetData))
        } catch (e: Exception) {
            Timber.d(e)
            return payment.copy(tenorList = null)
        }
    }

    fun generateInstallmentRequest(
        payment: CheckoutPaymentModel,
        paymentData: PaymentWidgetData,
        paymentRequest: PaymentRequest,
        checkoutItems: List<CheckoutItem>,
        cost: CheckoutCostModel,
        shipmentPlatformFeeData: ShipmentPlatformFeeData
    ): GoCicilInstallmentRequest {
        val address = checkoutItems.address()!!.recipientAddressModel
        val order = checkoutItems.filterIsInstance(CheckoutOrderModel::class.java).first()
        return GoCicilInstallmentRequest(
            gatewayCode = paymentData.gatewayCode,
            merchantCode = paymentData.merchantCode,
            profileCode = paymentData.profileCode,
            userId = userSessionInterface.userId.toLongOrZero(),
            paymentAmount = paymentRequest.payment.paymentAmount,
            merchantType = getMerchantTypeFromShopTier(order.shopTypeInfoData.shopTier),
            address = GoCicilAddressRequest(
                addressStreet = address.street,
                provinceName = address.provinceName,
                cityName = address.cityName,
                country = address.countryName,
                postalCode = address.postalCode
            ),
            shopId = order.shopId.toString(),
            products = checkoutItems.filterIsInstance(CheckoutProductModel::class.java).map {
                GoCicilProductRequest(
                    it.productId.toString(),
                    it.price,
                    it.quantity,
                    it.productCatId,
                    it.lastLevelCategory,
                    it.categoryIdentifier
                )
            },
            promoCodes = getValidPromoCodes(checkoutItems.promo()?.promo),
            additionalData = shipmentPlatformFeeData.additionalData,
            detailData = paymentRequest
        )
    }

    private fun getValidPromoCodes(lastApplyUiModel: LastApplyUiModel?): List<String> {
        val promoCodes = mutableListOf<String>()
        if (lastApplyUiModel != null) {
            if (lastApplyUiModel.message.state != "red") {
                promoCodes.addAll(lastApplyUiModel.codes)
            }
            lastApplyUiModel.voucherOrders.forEach {
                if (it.message.state != "red") {
                    promoCodes.add(it.code)
                }
            }
        }
        return promoCodes
    }

    private fun getMerchantTypeFromShopTier(shopTier: Int): String {
        return when (shopTier) {
            0 -> MERCHANT_TYPE_REGULAR_MERCHANT
            1 -> MERCHANT_TYPE_POWER_MERCHANT
            2 -> MERCHANT_TYPE_OFFICIAL_STORE
            3 -> MERCHANT_TYPE_POWER_MERCHANT_PRO
            else -> ""
        }
    }

    suspend fun getInstallmentList(
        payment: CheckoutPaymentModel,
        paymentData: PaymentWidgetData,
        paymentRequest: PaymentRequest,
        checkoutItems: List<CheckoutItem>,
        cost: CheckoutCostModel,
        shipmentPlatformFeeData: ShipmentPlatformFeeData
    ): CheckoutPaymentModel {
        try {
            val installmentData = processor.getGocicilInstallmentOption(
                generateInstallmentRequest(payment, paymentData, paymentRequest, checkoutItems, cost, shipmentPlatformFeeData)
            )
            val paymentWidgetData = payment.data!!.paymentWidgetData.toMutableList()
            val widgetData = paymentWidgetData.first()
            val selectedTenure = widgetData.installmentPaymentData.selectedTenure
            val result = autoSelectGoCicilTerm(selectedTenure, installmentData?.installmentOptions) ?: return payment.copy(installmentData = null)
            paymentWidgetData[0] = widgetData.copy(installmentPaymentData = widgetData.installmentPaymentData.copy(selectedTenure = result.installmentTerm))
            return payment.copy(installmentData = installmentData, data = payment.data.copy(paymentWidgetData))
        } catch (e: Exception) {
            Timber.d(e)
            return payment.copy(installmentData = null)
        }
    }

    private fun autoSelectGoCicilTerm(
        selectedTenure: Int,
        installmentTerms: List<GoCicilInstallmentOption>?
    ): GoCicilInstallmentOption? {
        if (installmentTerms == null) {
            return null
        }
        var selectedTerm: GoCicilInstallmentOption?
        if (selectedTenure > 0) {
            selectedTerm = installmentTerms.firstOrNull { it.installmentTerm == selectedTenure && it.isActive }
            return selectedTerm
        }
        selectedTerm = installmentTerms.lastOrNull { it.isRecommended && it.isActive }
        if (selectedTerm == null) {
            selectedTerm = installmentTerms.firstOrNull { it.isActive }
        }
        return selectedTerm
    }

    fun validatePayment(
        listData: List<CheckoutItem>
    ): List<CheckoutItem> {
        val checkoutItems = listData.toMutableList()
        val latestPayment = checkoutItems.payment()!!
        val latestPaymentData = latestPayment.data
        val latestCost = checkoutItems.cost()!!
        val latestButtonPayment = checkoutItems.buttonPayment()!!
        if (latestPayment.widget.state == CheckoutPaymentWidgetState.Error) {
            checkoutItems[checkoutItems.size - PAYMENT_INDEX_FROM_BOTTOM] = latestPayment.copy(
                widget = latestPayment.widget.copy(
                    errorMessage = latestPayment.defaultErrorMessage
                )
            )
        } else if (latestPaymentData != null) {
            val result = processor.validatePayment(
                latestPaymentData,
                latestPayment.tenorList,
                latestPayment.installmentData,
                latestCost.totalPriceWithInternalPaymentFees
            )
            val newPaymentWidgetData = processor.generateCheckoutPaymentWidgetData(
                latestPaymentData,
                latestPayment.tenorList,
                latestPayment.installmentData,
                latestPayment.widget,
                result,
                latestPayment.defaultErrorMessage
            )
            checkoutItems[checkoutItems.size - PAYMENT_INDEX_FROM_BOTTOM] = latestPayment.copy(
                widget = newPaymentWidgetData,
                validationReport = result
            )
        }
        return checkoutItems
    }

    companion object {
        // 0 = RM, 1 = PM, 2 = OS, 3 = PMPRO
        private const val MERCHANT_TYPE_REGULAR_MERCHANT = "RM"
        private const val MERCHANT_TYPE_POWER_MERCHANT = "PM"
        private const val MERCHANT_TYPE_OFFICIAL_STORE = "OS"
        private const val MERCHANT_TYPE_POWER_MERCHANT_PRO = "PMPRO"
    }
}
