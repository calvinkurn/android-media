package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.CHANGE_PAYMENT_METHOD_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MAXIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MINIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonType
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderCostCashbackData
import com.tokopedia.oneclickcheckout.order.view.model.OrderCostInstallmentData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentErrorData
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentInstallmentTerm
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentWalletErrorData
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY
import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.ceil

class OrderSummaryPageCalculator @Inject constructor(
    private val orderSummaryAnalytics: OrderSummaryAnalytics,
    private val executorDispatchers: CoroutineDispatchers
) {

    val total: MutableSharedFlow<Pair<OrderPayment, OrderTotal>> = MutableSharedFlow()

    private fun generateMinimumAmountPaymentErrorMessage(gatewayName: String): String {
        return "$MINIMUM_AMOUNT_ERROR_MESSAGE $gatewayName."
    }

    private fun generateMaximumAmountPaymentErrorMessage(gatewayName: String): String {
        return "$MAXIMUM_AMOUNT_ERROR_MESSAGE $gatewayName."
    }

    private fun validatePaymentState(orderCart: OrderCart, orderProfile: OrderProfile, shipping: OrderShipment): Boolean {
        return shipping.isValid() && shipping.serviceErrorMessage.isNullOrEmpty() && !orderCart.shop.isError && orderCart.shop.overweight == 0.0 && orderCart.products.all { it.isError || it.orderQuantity > 0 } && orderProfile.isValidProfile
    }

    suspend fun calculateTotal(
        orderCart: OrderCart,
        orderProfile: OrderProfile,
        shipping: OrderShipment,
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?,
        orderPayment: OrderPayment,
        orderTotal: OrderTotal
    ): Pair<OrderPayment, OrderTotal> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.default) {
            val isValidState = validatePaymentState(orderCart, orderProfile, shipping)
            var payment = orderPayment
            var total = orderTotal
            if (!isValidState) {
                return@withContext payment to total.copy(orderCost = OrderCost(), buttonState = OccButtonState.DISABLE, showTickerError = false)
            }
            if (payment.isDynamicPaymentFeeError) {
                return@withContext payment to total.copy(orderCost = OrderCost(), buttonState = OccButtonState.DISABLE, showTickerError = true)
            }
            val (orderCost, newPayment) = calculateOrderCostWithPaymentFee(orderCart, shipping, validateUsePromoRevampUiModel, payment)
            total = total.copy(showTickerError = false)
            val subtotal = orderCost.totalPrice
            payment = newPayment
            var currentState = OccButtonState.NORMAL
            val isHideDigitalInt = if (payment.walletData.topUp.isHideDigital) 1 else 0
            payment = payment.copy(walletErrorData = null, errorData = null)
            if (payment.revampErrorMessage.message.isNotEmpty()) {
                // new revamp error
                if (payment.isDisablePayButton) {
                    return@withContext payment.copy(
                        isCalculationError = false,
                        errorData = OrderPaymentErrorData(payment.revampErrorMessage.message, payment.revampErrorMessage.button.text, payment.revampErrorMessage.button.action)
                    ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = OccButtonState.DISABLE)
                }
                if (payment.errorMessage.message.isNotEmpty() && payment.errorMessage.button.text.isNotEmpty()) {
                    // cc error should disable button pay
                    return@withContext payment.copy(
                        isCalculationError = false,
                        errorData = OrderPaymentErrorData(payment.revampErrorMessage.message, payment.revampErrorMessage.button.text, payment.revampErrorMessage.button.action)
                    ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = OccButtonState.DISABLE)
                }
                return@withContext payment.copy(
                    isCalculationError = false,
                    errorData = OrderPaymentErrorData(payment.revampErrorMessage.message, payment.revampErrorMessage.button.text, payment.revampErrorMessage.button.action)
                ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.walletData.isGoCicil && payment.walletData.goCicilData.availableTerms.isNotEmpty() &&
                payment.walletData.goCicilData.availableTerms.find { it.isActive } == null
            ) {
                // no active terms
                return@withContext payment.copy(
                    isCalculationError = false,
                    errorData = OrderPaymentErrorData(payment.walletData.goCicilData.errorMessageUnavailableTenures)
                ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.isOvo && payment.ovoData.isPhoneNumberMissing) {
                if (payment.isOvoOnlyCampaign) {
                    return@withContext payment.copy(
                        isCalculationError = true,
                        errorData = null,
                        walletErrorData = OrderPaymentWalletErrorData(
                            isBlockingError = false,
                            message = payment.ovoData.phoneNumber.errorMessage,
                            type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE,
                            isOvo = true
                        )
                    ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(
                    isCalculationError = true,
                    errorData = null,
                    walletErrorData = OrderPaymentWalletErrorData(
                        isBlockingError = false,
                        message = payment.ovoData.phoneNumber.errorMessage,
                        type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE,
                        isOvo = true
                    )
                ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.walletData.enableWalletAmountValidation && payment.walletData.isPhoneNumberMissing) {
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    return@withContext payment.copy(
                        isCalculationError = true,
                        errorData = null,
                        walletErrorData = OrderPaymentWalletErrorData(
                            isBlockingError = false,
                            message = payment.walletData.phoneNumber.errorMessage,
                            type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE
                        )
                    ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(
                    isCalculationError = true,
                    errorData = null,
                    walletErrorData = OrderPaymentWalletErrorData(
                        isBlockingError = false,
                        message = payment.walletData.phoneNumber.errorMessage,
                        type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE
                    )
                ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.isOvo && payment.ovoData.isActivationRequired) {
                if (payment.isOvoOnlyCampaign) {
                    return@withContext payment.copy(
                        isCalculationError = true,
                        errorData = null,
                        walletErrorData = OrderPaymentWalletErrorData(
                            isBlockingError = false,
                            message = payment.ovoData.activation.errorMessage,
                            buttonTitle = payment.ovoData.activation.buttonTitle,
                            type = OrderPaymentWalletErrorData.TYPE_ACTIVATION,
                            callbackUrl = payment.ovoData.callbackUrl,
                            isOvo = true
                        )
                    ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(
                    isCalculationError = true,
                    errorData = null,
                    walletErrorData = OrderPaymentWalletErrorData(
                        isBlockingError = false,
                        message = payment.ovoData.activation.errorMessage,
                        buttonTitle = payment.ovoData.activation.buttonTitle,
                        type = OrderPaymentWalletErrorData.TYPE_ACTIVATION,
                        callbackUrl = payment.ovoData.callbackUrl,
                        isOvo = true
                    )
                ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.walletData.enableWalletAmountValidation && payment.walletData.isActivationRequired) {
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    var isBlockingError = false
                    val buttonTitle = payment.walletData.activation.buttonTitle
                    if (buttonTitle.isEmpty()) isBlockingError = true
                    return@withContext payment.copy(
                        isCalculationError = true,
                        errorData = null,
                        walletErrorData = OrderPaymentWalletErrorData(
                            isBlockingError = isBlockingError,
                            message = payment.walletData.activation.errorMessage,
                            buttonTitle = payment.walletData.activation.buttonTitle,
                            type = OrderPaymentWalletErrorData.TYPE_ACTIVATION,
                            callbackUrl = payment.walletData.callbackUrl
                        )
                    ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(
                    isCalculationError = true,
                    errorData = null,
                    walletErrorData = OrderPaymentWalletErrorData(
                        isBlockingError = false,
                        message = payment.walletData.activation.errorMessage,
                        buttonTitle = payment.walletData.activation.buttonTitle,
                        type = OrderPaymentWalletErrorData.TYPE_ACTIVATION,
                        callbackUrl = payment.walletData.callbackUrl
                    )
                ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (!payment.walletData.isGoCicil && payment.minimumAmount > subtotal) {
                var buttonType = OccButtonType.CHOOSE_PAYMENT
                if (payment.isOvoOnlyCampaign) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(generateMinimumAmountPaymentErrorMessage(payment.gatewayName), CHANGE_PAYMENT_METHOD_MESSAGE, OrderPaymentErrorData.ACTION_CHANGE_PAYMENT)) to total.copy(
                    orderCost = orderCost,
                    buttonType = buttonType,
                    buttonState = currentState
                )
            }
            if (payment.walletData.isGoCicil && payment.minimumAmount > orderCost.totalPriceWithoutPaymentFees) {
                var buttonType = OccButtonType.CHOOSE_PAYMENT
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(message = payment.walletData.goCicilData.errorMessageBottomLimit)) to total.copy(
                    orderCost = orderCost,
                    buttonType = buttonType,
                    buttonState = currentState
                )
            }
            if (!payment.walletData.isGoCicil && payment.maximumAmount > 0 && payment.maximumAmount < subtotal) {
                var buttonType = OccButtonType.CHOOSE_PAYMENT
                if (payment.isOvoOnlyCampaign) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(generateMaximumAmountPaymentErrorMessage(payment.gatewayName), CHANGE_PAYMENT_METHOD_MESSAGE, OrderPaymentErrorData.ACTION_CHANGE_PAYMENT)) to total.copy(
                    orderCost = orderCost,
                    buttonType = buttonType,
                    buttonState = currentState
                )
            }
            if (payment.walletData.isGoCicil && payment.maximumAmount < orderCost.totalPriceWithoutPaymentFees) {
                var buttonType = OccButtonType.CHOOSE_PAYMENT
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(generateMaximumAmountPaymentErrorMessage(payment.gatewayName), CHANGE_PAYMENT_METHOD_MESSAGE, OrderPaymentErrorData.ACTION_CHANGE_PAYMENT)) to total.copy(
                    orderCost = orderCost,
                    buttonType = buttonType,
                    buttonState = currentState
                )
            }
            if (payment.isOvo && subtotal > payment.walletAmount) {
                orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_PAYMENT_OVO_BALANCE)
                if (payment.isOvoOnlyCampaign) {
                    return@withContext payment.copy(
                        isCalculationError = true,
                        walletErrorData = OrderPaymentWalletErrorData(
                            isBlockingError = true,
                            message = payment.ovoData.topUp.errorMessage,
                            buttonTitle = payment.ovoData.topUp.buttonTitle,
                            type = OrderPaymentWalletErrorData.TYPE_TOP_UP,
                            callbackUrl = payment.ovoData.callbackUrl,
                            isHideDigital = payment.ovoData.topUp.isHideDigital,
                            isOvo = true
                        )
                    ) to total.copy(
                        orderCost = orderCost,
                        buttonType = OccButtonType.PAY,
                        buttonState = disableButtonState(currentState)
                    )
                }
                return@withContext payment.copy(
                    isCalculationError = true,
                    walletErrorData = OrderPaymentWalletErrorData(
                        isBlockingError = true,
                        message = payment.ovoData.topUp.errorMessage,
                        buttonTitle = payment.ovoData.topUp.buttonTitle,
                        type = OrderPaymentWalletErrorData.TYPE_TOP_UP,
                        callbackUrl = payment.ovoData.callbackUrl,
                        isHideDigital = payment.ovoData.topUp.isHideDigital,
                        isOvo = true
                    )
                ) to total.copy(
                    orderCost = orderCost,
                    buttonType = OccButtonType.CHOOSE_PAYMENT,
                    buttonState = currentState
                )
            }
            if (payment.walletData.isGoCicil && payment.walletAmount < orderCost.totalPriceWithoutPaymentFees) {
                var buttonType = OccButtonType.CHOOSE_PAYMENT
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(message = payment.walletData.goCicilData.errorMessageTopLimit)) to total.copy(
                    orderCost = orderCost,
                    buttonType = buttonType,
                    buttonState = currentState
                )
            }
            if (!payment.walletData.isGoCicil && payment.walletData.enableWalletAmountValidation && subtotal > payment.walletAmount) {
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    return@withContext payment.copy(
                        isCalculationError = true,
                        walletErrorData = OrderPaymentWalletErrorData(
                            isBlockingError = true,
                            message = payment.walletData.topUp.errorMessage,
                            buttonTitle = payment.walletData.topUp.buttonTitle,
                            type = OrderPaymentWalletErrorData.TYPE_TOP_UP,
                            callbackUrl = payment.walletData.callbackUrl,
                            isHideDigital = isHideDigitalInt
                        )
                    ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(
                    isCalculationError = true,
                    walletErrorData = OrderPaymentWalletErrorData(
                        isBlockingError = true,
                        message = payment.walletData.topUp.errorMessage,
                        buttonTitle = payment.walletData.topUp.buttonTitle,
                        type = OrderPaymentWalletErrorData.TYPE_TOP_UP,
                        callbackUrl = payment.walletData.callbackUrl,
                        isHideDigital = isHideDigitalInt
                    )
                ) to total.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.creditCard.selectedTerm?.isError == true) {
                currentState = disableButtonState(currentState)
            }
            if (payment.creditCard.isAfpb && payment.creditCard.selectedTerm == null) {
                currentState = disableButtonState(currentState)
            }
            if (payment.walletData.isGoCicil && !payment.walletData.goCicilData.hasValidTerm) {
                currentState = disableButtonState(currentState)
            }
            return@withContext payment.copy(isCalculationError = false) to total.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = currentState)
        }
        total.emit(result)
        OccIdlingResource.decrement()
        return result
    }

    private fun disableButtonState(currentState: OccButtonState): OccButtonState {
        return if (currentState == OccButtonState.NORMAL) OccButtonState.DISABLE else currentState
    }

    private suspend fun calculateOrderCostWithPaymentFee(orderCart: OrderCart, shipping: OrderShipment, validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, orderPayment: OrderPayment): Pair<OrderCost, OrderPayment> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.default) {
            val (cost, _) = calculateOrderCostWithoutPaymentFee(orderCart, shipping, validateUsePromoRevampUiModel, orderPayment)
            var subtotal = cost.totalPrice
            var payment = orderPayment
            val totalPaymentFee = payment.getTotalPaymentFee()
            if (!orderPayment.creditCard.isAfpb) {
                val subTotalWithPaymentFees = cost.totalPriceWithoutDiscountsAndPaymentFees + totalPaymentFee
                val subsidizeWithPaymentFees = if (orderCart.shop.isOfficial == 1) cost.totalPriceWithoutPaymentFees + totalPaymentFee else 0.0
                payment = calculateInstallmentDetails(payment, subTotalWithPaymentFees, subsidizeWithPaymentFees, cost.totalDiscounts)
            }
            val fee = payment.getRealFee()
            subtotal += fee
            if (payment.isInstallment()) {
                subtotal += totalPaymentFee
            }
            var installmentData: OrderCostInstallmentData? = null
            val selectedTerm = orderPayment.walletData.goCicilData.selectedTerm
            if (orderPayment.walletData.isGoCicil && selectedTerm != null && orderPayment.walletData.goCicilData.hasValidTerm &&
                payment.minimumAmount <= cost.totalPriceWithoutPaymentFees && cost.totalPriceWithoutPaymentFees <= payment.maximumAmount
            ) {
                installmentData = OrderCostInstallmentData(
                    installmentFee = selectedTerm.interestAmount,
                    installmentTerm = selectedTerm.installmentTerm,
                    installmentAmountPerPeriod = selectedTerm.installmentAmountPerPeriod,
                    installmentFirstDate = selectedTerm.firstInstallmentDate,
                    installmentLastDate = selectedTerm.lastInstallmentDate
                )
                subtotal += installmentData.installmentFee
            }
            val orderCost = OrderCost(
                totalPrice = subtotal,
                totalItemPrice = cost.totalItemPrice,
                shippingFee = cost.shippingFee,
                insuranceFee = cost.insuranceFee,
                isUseInsurance = cost.isUseInsurance,
                paymentFee = fee,
                shippingDiscountAmount = cost.shippingDiscountAmount,
                productDiscountAmount = cost.productDiscountAmount,
                purchaseProtectionPrice = cost.purchaseProtectionPrice,
                addOnPrice = cost.addOnPrice,
                hasAddOn = cost.hasAddOn,
                summaryAddOnsProduct = cost.summaryAddOnsProduct,
                addOnsProductSelectedList = cost.addOnsProductSelectedList,
                cashbacks = cost.cashbacks,
                installmentData = installmentData,
                totalPriceWithoutPaymentFees = cost.totalPriceWithoutPaymentFees,
                totalPriceWithoutDiscountsAndPaymentFees = cost.totalPriceWithoutDiscountsAndPaymentFees,
                totalItemPriceAndShippingFee = cost.totalItemPriceAndShippingFee,
                totalAdditionalFee = cost.totalAdditionalFee,
                totalDiscounts = cost.totalDiscounts,
                orderPaymentFees = payment.originalPaymentFees + (payment.dynamicPaymentFees ?: emptyList()),
                isInstallment = payment.isInstallment()
            )
            return@withContext orderCost to payment
        }
        OccIdlingResource.decrement()
        return result
    }

    suspend fun calculateOrderCostWithoutPaymentFee(orderCart: OrderCart, shipping: OrderShipment, validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, orderPayment: OrderPayment): Pair<OrderCost, ArrayList<Int>> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.default) {
            var totalProductPrice = 0.0
            var totalProductWholesalePrice = 0.0
            val mapParentWholesalePrice: HashMap<String, Double> = HashMap()
            val updatedProductIndex = arrayListOf<Int>()
            var totalPurchaseProtectionPrice = 0
            var totalAddOnPrice = 0.0
            var totalAddOnProductPrice = 0.0
            var hasAddOn = false
            val addOnsProductSelectedList: MutableList<AddOnsProductDataModel.Data> = mutableListOf()

            // This is for add on shop level
            val addOnShopLevel = orderCart.shop.addOn.addOnsDataItemModelList.firstOrNull()
            if (addOnShopLevel != null) {
                totalAddOnPrice += addOnShopLevel.addOnPrice
                hasAddOn = true
            }

            for (productIndex in orderCart.products.indices) {
                val product = orderCart.products[productIndex]
                if (!product.isError) {
                    var itemQty = 0
                    if (product.hasParentId() && product.wholesalePriceList.isNotEmpty()) {
                        orderCart.products.filter { !it.isError && it.parentId == product.parentId }
                            .forEach { itemQty += it.orderQuantity }
                    } else {
                        itemQty = product.orderQuantity
                    }
                    if (product.wholesalePriceList.isNotEmpty()) {
                        var finalPrice = product.productPrice
                        product.wholesalePrice = 0.0
                        for (price in product.wholesalePriceList) {
                            if (itemQty >= price.qtyMin) {
                                finalPrice = price.prdPrc
                                product.wholesalePrice = finalPrice
                            }
                        }
                        if (product.finalPrice != finalPrice) {
                            product.finalPrice = finalPrice
                            updatedProductIndex.add(productIndex)
                        }
                        if (!mapParentWholesalePrice.containsKey(product.parentId)) {
                            val totalPrice = itemQty * product.finalPrice
                            totalProductWholesalePrice += totalPrice
                            mapParentWholesalePrice[product.parentId] = totalPrice
                        }
                    } else {
                        product.wholesalePrice = 0.0
                        product.finalPrice = product.productPrice
                        totalProductPrice += itemQty * product.finalPrice
                    }
                    var purchaseProtectionPriceMultiplier = product.orderQuantity
                    if (product.purchaseProtectionPlanData.source.equals(PurchaseProtectionPlanData.SOURCE_READINESS, true)) {
                        purchaseProtectionPriceMultiplier = 1
                    }
                    totalPurchaseProtectionPrice += if (product.purchaseProtectionPlanData.stateChecked == PurchaseProtectionPlanData.STATE_TICKED) purchaseProtectionPriceMultiplier * product.purchaseProtectionPlanData.protectionPricePerProduct else 0
                    // This is for add on product level
                    val addOnProductLevel = product.addOn.addOnsDataItemModelList.firstOrNull()
                    if (addOnProductLevel != null) {
                        totalAddOnPrice += addOnProductLevel.addOnPrice
                        hasAddOn = true
                    }
                    product.addOnsProductData.data.filter {
                        it.status == ADD_ON_PRODUCT_STATUS_CHECK ||
                            it.status == ADD_ON_PRODUCT_STATUS_MANDATORY
                    }.forEach { addOnProductChecked ->
                        val addOnQty = if (addOnProductChecked.fixedQuantity) 1 else product.orderQuantity
                        totalAddOnProductPrice += addOnProductChecked.price * addOnQty
                        addOnsProductSelectedList.add(addOnProductChecked.copy(productQuantity = addOnQty))
                    }
                }
            }
            totalProductPrice += totalProductWholesalePrice
            val totalShippingPrice = shipping.getRealOriginalPrice().toDouble()
            val insurancePrice = shipping.getRealInsurancePrice().toDouble()
            val isUseInsurance = shipping.isUseInsurance()
            val (productDiscount, shippingDiscount, cashbacks) = calculatePromo(validateUsePromoRevampUiModel)
            val subtotalWithoutDiscountsAndPaymentFee = totalProductPrice + totalPurchaseProtectionPrice + totalShippingPrice + insurancePrice + totalAddOnPrice + totalAddOnProductPrice
            val totalDiscounts = productDiscount + shippingDiscount
            val subtotal = subtotalWithoutDiscountsAndPaymentFee - totalDiscounts
            val orderCost = OrderCost(
                totalPrice = subtotal,
                totalItemPrice = totalProductPrice,
                shippingFee = totalShippingPrice,
                insuranceFee = insurancePrice,
                isUseInsurance = isUseInsurance,
                paymentFee = 0.0,
                shippingDiscountAmount = shippingDiscount,
                productDiscountAmount = productDiscount,
                purchaseProtectionPrice = totalPurchaseProtectionPrice,
                addOnPrice = totalAddOnPrice,
                hasAddOn = hasAddOn,
                summaryAddOnsProduct = orderCart.summaryAddOnsProduct,
                addOnsProductSelectedList = addOnsProductSelectedList,
                cashbacks = cashbacks,
                totalPriceWithoutPaymentFees = subtotal,
                totalPriceWithoutDiscountsAndPaymentFees = subtotalWithoutDiscountsAndPaymentFee,
                totalItemPriceAndShippingFee = totalProductPrice + totalShippingPrice,
                totalAdditionalFee = insurancePrice + totalPurchaseProtectionPrice + totalAddOnPrice + totalAddOnProductPrice,
                totalDiscounts = totalDiscounts
            )
            return@withContext orderCost to updatedProductIndex
        }
        OccIdlingResource.decrement()
        return result
    }

    private fun calculatePromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?): Triple<Int, Int, ArrayList<OrderCostCashbackData>> {
        var productDiscount = 0
        var shippingDiscount = 0
        val cashbacks = ArrayList<OrderCostCashbackData>()
        val summaries = validateUsePromoRevampUiModel?.promoUiModel?.benefitSummaryInfoUiModel?.summaries
            ?: emptyList()
        for (summary in summaries) {
            if (summary.type == SummariesUiModel.TYPE_DISCOUNT) {
                for (detail in summary.details) {
                    if (detail.type == SummariesUiModel.TYPE_SHIPPING_DISCOUNT) {
                        shippingDiscount += detail.amount
                    }
                    if (detail.type == SummariesUiModel.TYPE_PRODUCT_DISCOUNT) {
                        productDiscount += detail.amount
                    }
                }
            }
        }

        if (validateUsePromoRevampUiModel?.promoUiModel?.additionalInfoUiModel?.usageSummariesUiModel != null) {
            validateUsePromoRevampUiModel.promoUiModel.additionalInfoUiModel.usageSummariesUiModel.map {
                if (it.type == SummariesUiModel.TYPE_CASHBACK) {
                    cashbacks.add(
                        OrderCostCashbackData(
                            description = it.desc,
                            amountStr = it.amountStr,
                            currencyDetailStr = it.currencyDetailStr
                        )
                    )
                }
            }
        }

        return Triple(productDiscount, shippingDiscount, cashbacks)
    }

    private fun calculateMdrFee(subTotal: Double, mdr: Float, subsidize: Double, mdrSubsidize: Float): Double {
        return ceil(subTotal * (mdr / PERCENT_DIVIDER) - subsidize * (mdrSubsidize / PERCENT_DIVIDER))
    }

    private fun calculateInstallmentDetails(payment: OrderPayment, subTotal: Double, subsidize: Double, discount: Int): OrderPayment {
        if (payment.creditCard.selectedTerm == null) {
            return payment
        }
        val installments = payment.creditCard.availableTerms
        var selectedInstallmentTerm: OrderPaymentInstallmentTerm? = null
        for (i in installments.lastIndex downTo 0) {
            val installment = installments[i]
            installment.isEnable = installment.minAmount <= (subTotal - discount)
            if (installment.isError) {
                installment.isError = !installment.isEnable
            }
            installment.fee = calculateMdrFee(subTotal, installment.mdr, subsidize, installment.mdrSubsidize)
            val total = subTotal + installment.fee - discount
            installment.monthlyAmount = if (installment.term > 0) ceil(total / installment.term) else total
            if (installment.isSelected) {
                selectedInstallmentTerm = installment
            }
        }
        return payment.copy(creditCard = payment.creditCard.copy(availableTerms = installments, selectedTerm = selectedInstallmentTerm))
    }

    companion object {
        private const val PERCENT_DIVIDER = 100.0
    }
}
