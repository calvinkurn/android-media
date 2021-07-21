package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.CHANGE_PAYMENT_METHOD_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MAXIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MINIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.ceil

class OrderSummaryPageCalculator @Inject constructor(private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                     private val executorDispatchers: CoroutineDispatchers) {

    val total: MutableSharedFlow<Pair<OrderPayment, OrderTotal>> = MutableSharedFlow()

    private fun generateMinimumAmountPaymentErrorMessage(gatewayName: String): String {
        return "$MINIMUM_AMOUNT_ERROR_MESSAGE $gatewayName."
    }

    private fun generateMaximumAmountPaymentErrorMessage(gatewayName: String): String {
        return "$MAXIMUM_AMOUNT_ERROR_MESSAGE $gatewayName."
    }

    fun validatePaymentState(orderCart: OrderCart, orderProfile: OrderProfile, shipping: OrderShipment): Boolean {
        return shipping.isValid() && shipping.serviceErrorMessage.isNullOrEmpty() && !orderCart.shop.isError && orderCart.shop.overweight == 0.0 && orderCart.products.all { it.isError || it.quantity.orderQuantity > 0 } && orderProfile.isValidProfile
    }

    suspend fun calculateTotal(orderCart: OrderCart, orderProfile: OrderProfile, shipping: OrderShipment,
                               validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, orderPayment: OrderPayment,
                               orderTotal: OrderTotal): Pair<OrderPayment, OrderTotal> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.default) {
            val isValidState = validatePaymentState(orderCart, orderProfile, shipping)
            var payment = orderPayment
            if (!isValidState) {
                return@withContext payment to orderTotal.copy(orderCost = OrderCost(), buttonState = OccButtonState.DISABLE)
            }
            val (orderCost, newPayment) = calculateOrderCost(orderCart, shipping, validateUsePromoRevampUiModel, payment)
            val subtotal = orderCost.totalPrice
            payment = newPayment
            var currentState = OccButtonState.NORMAL
            val isHideDigitalInt = if (payment.walletData.topUp.isHideDigital) 1 else 0
            payment = payment.copy(walletErrorData = null, errorData = null)
            if (payment.revampErrorMessage.message.isNotEmpty()) {
                // new revamp error
                if (payment.isDisablePayButton) {
                    return@withContext payment.copy(isCalculationError = false,
                            errorData = OrderPaymentErrorData(payment.revampErrorMessage.message, payment.revampErrorMessage.button.text, payment.revampErrorMessage.button.action)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = OccButtonState.DISABLE)
                }
                if (payment.errorMessage.message.isNotEmpty() && payment.errorMessage.button.text.isNotEmpty()) {
                    // cc error should disable button pay
                    return@withContext payment.copy(isCalculationError = false,
                            errorData = OrderPaymentErrorData(payment.revampErrorMessage.message, payment.revampErrorMessage.button.text, payment.revampErrorMessage.button.action)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = OccButtonState.DISABLE)
                }
                return@withContext payment.copy(isCalculationError = false,
                        errorData = OrderPaymentErrorData(payment.revampErrorMessage.message, payment.revampErrorMessage.button.text, payment.revampErrorMessage.button.action)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.isOvo && payment.ovoData.isPhoneNumberMissing) {
                if (payment.isOvoOnlyCampaign) {
                    return@withContext payment.copy(isCalculationError = true, errorData = null,
                            walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = payment.ovoData.phoneNumber.errorMessage,
                                    type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE, isOvo = true)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(isCalculationError = true, errorData = null,
                        walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = payment.ovoData.phoneNumber.errorMessage,
                                type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE, isOvo = true)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.walletData.enableWalletAmountValidation && payment.walletData.isPhoneNumberMissing) {
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    return@withContext payment.copy(isCalculationError = true, errorData = null,
                            walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = payment.walletData.phoneNumber.errorMessage,
                                    type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(isCalculationError = true, errorData = null,
                        walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = payment.walletData.phoneNumber.errorMessage,
                                type = OrderPaymentWalletErrorData.TYPE_MISSING_PHONE)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.isOvo && payment.ovoData.isActivationRequired) {
                if (payment.isOvoOnlyCampaign) {
                    return@withContext payment.copy(isCalculationError = true, errorData = null,
                            walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = payment.ovoData.activation.errorMessage, buttonTitle = payment.ovoData.activation.buttonTitle,
                                    type = OrderPaymentWalletErrorData.TYPE_ACTIVATION, callbackUrl = payment.ovoData.callbackUrl, isOvo = true)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(isCalculationError = true, errorData = null,
                        walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = payment.ovoData.activation.errorMessage, buttonTitle = payment.ovoData.activation.buttonTitle,
                                type = OrderPaymentWalletErrorData.TYPE_ACTIVATION, callbackUrl = payment.ovoData.callbackUrl, isOvo = true)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.walletData.enableWalletAmountValidation && payment.walletData.isActivationRequired) {
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    var isBlockingError = false
                    val buttonTitle = payment.walletData.activation.buttonTitle
                    if (buttonTitle.isEmpty()) isBlockingError = true
                    return@withContext payment.copy(isCalculationError = true, errorData = null,
                            walletErrorData = OrderPaymentWalletErrorData(isBlockingError = isBlockingError, message = payment.walletData.activation.errorMessage, buttonTitle = payment.walletData.activation.buttonTitle,
                                    type = OrderPaymentWalletErrorData.TYPE_ACTIVATION, callbackUrl = payment.walletData.callbackUrl)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(isCalculationError = true, errorData = null,
                        walletErrorData = OrderPaymentWalletErrorData(isBlockingError = false, message = payment.walletData.activation.errorMessage, buttonTitle = payment.walletData.activation.buttonTitle,
                                type = OrderPaymentWalletErrorData.TYPE_ACTIVATION, callbackUrl = payment.walletData.callbackUrl)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.minimumAmount > subtotal) {
                var buttonType = OccButtonType.CHOOSE_PAYMENT
                if (payment.isOvoOnlyCampaign) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(generateMinimumAmountPaymentErrorMessage(payment.gatewayName), CHANGE_PAYMENT_METHOD_MESSAGE, OrderPaymentErrorData.ACTION_CHANGE_PAYMENT)) to orderTotal.copy(orderCost = orderCost,
                        buttonType = buttonType, buttonState = currentState)
            }
            if (payment.maximumAmount > 0 && payment.maximumAmount < subtotal) {
                var buttonType = OccButtonType.CHOOSE_PAYMENT
                if (payment.isOvoOnlyCampaign) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    currentState = disableButtonState(currentState)
                    buttonType = OccButtonType.PAY
                }
                return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(generateMaximumAmountPaymentErrorMessage(payment.gatewayName), CHANGE_PAYMENT_METHOD_MESSAGE, OrderPaymentErrorData.ACTION_CHANGE_PAYMENT)) to orderTotal.copy(orderCost = orderCost,
                        buttonType = buttonType, buttonState = currentState)
            }
            if (payment.isOvo && subtotal > payment.walletAmount) {
                orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_PAYMENT_OVO_BALANCE)
                if (payment.isOvoOnlyCampaign) {
                    return@withContext payment.copy(isCalculationError = true, walletErrorData = OrderPaymentWalletErrorData(isBlockingError = true, message = payment.ovoData.topUp.errorMessage, buttonTitle = payment.ovoData.topUp.buttonTitle,
                            type = OrderPaymentWalletErrorData.TYPE_TOP_UP, callbackUrl = payment.ovoData.callbackUrl, isHideDigital = payment.ovoData.topUp.isHideDigital, isOvo = true)) to orderTotal.copy(orderCost = orderCost,
                            buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(isCalculationError = true, walletErrorData = OrderPaymentWalletErrorData(isBlockingError = true, message = payment.ovoData.topUp.errorMessage, buttonTitle = payment.ovoData.topUp.buttonTitle,
                        type = OrderPaymentWalletErrorData.TYPE_TOP_UP, callbackUrl = payment.ovoData.callbackUrl, isHideDigital = payment.ovoData.topUp.isHideDigital, isOvo = true)) to orderTotal.copy(orderCost = orderCost,
                        buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.walletData.enableWalletAmountValidation && subtotal > payment.walletAmount) {
                if (payment.specificGatewayCampaignOnlyType > 0) {
                    return@withContext payment.copy(isCalculationError = true, walletErrorData = OrderPaymentWalletErrorData(isBlockingError = true, message = payment.walletData.topUp.errorMessage, buttonTitle = payment.walletData.topUp.buttonTitle,
                            type = OrderPaymentWalletErrorData.TYPE_TOP_UP, callbackUrl = payment.walletData.callbackUrl, isHideDigital = isHideDigitalInt)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = disableButtonState(currentState))
                }
                return@withContext payment.copy(isCalculationError = true, walletErrorData = OrderPaymentWalletErrorData(isBlockingError = true, message = payment.walletData.topUp.errorMessage, buttonTitle = payment.walletData.topUp.buttonTitle,
                        type = OrderPaymentWalletErrorData.TYPE_TOP_UP, callbackUrl = payment.walletData.callbackUrl, isHideDigital = isHideDigitalInt)) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.creditCard.selectedTerm?.isError == true) {
                currentState = disableButtonState(currentState)
            }
            return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, buttonType = OccButtonType.PAY, buttonState = currentState)
        }
        total.emit(result)
        OccIdlingResource.decrement()
        return result
    }

    private fun disableButtonState(currentState: OccButtonState): OccButtonState {
        return if (currentState == OccButtonState.NORMAL) OccButtonState.DISABLE else currentState
    }

    fun calculateOrderCost(orderCart: OrderCart, shipping: OrderShipment, validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, orderPayment: OrderPayment): Pair<OrderCost, OrderPayment> {
        var payment = orderPayment
        var totalProductPrice = 0.0
        var totalPurchaseProtectionPrice = 0
        for (product in orderCart.products) {
            if (!product.isError) {
                totalProductPrice += product.quantity.orderQuantity * product.getPrice().toDouble()
                var purchaseProtectionPriceMultiplier = product.quantity.orderQuantity
                if (product.purchaseProtectionPlanData.source.equals(PurchaseProtectionPlanData.SOURCE_READINESS, true)) {
                    purchaseProtectionPriceMultiplier = 1
                }
                totalPurchaseProtectionPrice += if (product.purchaseProtectionPlanData.stateChecked == PurchaseProtectionPlanData.STATE_TICKED) purchaseProtectionPriceMultiplier * product.purchaseProtectionPlanData.protectionPricePerProduct else 0
            }
        }
        val totalShippingPrice = shipping.getRealOriginalPrice().toDouble()
        val insurancePrice = shipping.getRealInsurancePrice().toDouble()
        val (productDiscount, shippingDiscount, cashbacks) = calculatePromo(validateUsePromoRevampUiModel)
        var subtotal = totalProductPrice + totalPurchaseProtectionPrice + totalShippingPrice + insurancePrice
        payment = calculateInstallmentDetails(payment, subtotal, if (orderCart.shop.isOfficial == 1) subtotal - productDiscount - shippingDiscount else 0.0, productDiscount + shippingDiscount)
        val fee = payment.getRealFee()
        subtotal += fee
        subtotal -= productDiscount
        subtotal -= shippingDiscount
        val orderCost = OrderCost(subtotal, totalProductPrice, totalShippingPrice, insurancePrice, fee, shippingDiscount, productDiscount, totalPurchaseProtectionPrice, cashbacks)
        return orderCost to payment
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
        return ceil(subTotal * (mdr / 100.0) - subsidize * (mdrSubsidize / 100.0))
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
}