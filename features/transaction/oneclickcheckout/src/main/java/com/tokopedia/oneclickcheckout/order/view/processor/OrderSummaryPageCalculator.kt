package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.CHANGE_PAYMENT_METHOD_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.CHOOSE_OTHER_PAYMENT_METHOD_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MAXIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MINIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.ceil

class OrderSummaryPageCalculator @Inject constructor(private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                     private val executorDispatchers: ExecutorDispatchers) {

    private fun shouldButtonStateEnable(orderShipment: OrderShipment, orderCart: OrderCart): Boolean {
        return (orderShipment.isValid() && orderShipment.serviceErrorMessage.isNullOrEmpty() && orderCart.shop.errors.isEmpty() && !orderCart.product.quantity.isStateError)
    }

    private fun generateMinimumAmountPaymentErrorMessage(gatewayName: String, minimumAmount: Long?, isEnableChooseOtherPaymentMethod: Boolean): String {
        var message = "$MINIMUM_AMOUNT_ERROR_MESSAGE $gatewayName"
        if (minimumAmount != null) {
            message += " (${CurrencyFormatUtil.convertPriceValueToIdrFormat(minimumAmount, false).removeDecimalSuffix()})"
        }
        message += "."
        if (isEnableChooseOtherPaymentMethod) {
            message += CHOOSE_OTHER_PAYMENT_METHOD_MESSAGE
        }
        return message
    }

    private fun generateMaximumAmountPaymentErrorMessage(gatewayName: String, maximumAmount: Long?, isEnableChooseOtherPaymentMethod: Boolean): String {
        var message = "$MAXIMUM_AMOUNT_ERROR_MESSAGE $gatewayName"
        if (maximumAmount != null) {
            message += " (${CurrencyFormatUtil.convertPriceValueToIdrFormat(maximumAmount, false).removeDecimalSuffix()})"
        }
        message += "."
        if (isEnableChooseOtherPaymentMethod) {
            message += CHOOSE_OTHER_PAYMENT_METHOD_MESSAGE
        }
        return message
    }

    suspend fun calculateTotal(orderCart: OrderCart, _orderPreference: OrderPreference, shipping: OrderShipment, validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, _orderPayment: OrderPayment,
                               orderTotal: OrderTotal, forceButtonState: OccButtonState?, isNewFlow: Boolean): Pair<OrderPayment, OrderTotal> {
        val quantity = orderCart.product.quantity
        var payment = _orderPayment
        if (quantity.orderQuantity <= 0 || !_orderPreference.isValid) {
            return _orderPayment to orderTotal.copy(orderCost = OrderCost(), buttonState = OccButtonState.DISABLE)
        }
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.main) {
            val totalProductPrice = quantity.orderQuantity * orderCart.product.getPrice().toDouble()
            var purchaseProtectionPriceMultiplier = quantity.orderQuantity
            if (orderCart.product.purchaseProtectionPlanData.source.equals(PurchaseProtectionPlanData.SOURCE_READINESS, true)) {
                purchaseProtectionPriceMultiplier = 1
            }
            val purchaseProtectionPrice = if (orderCart.product.purchaseProtectionPlanData.stateChecked == PurchaseProtectionPlanData.STATE_TICKED) purchaseProtectionPriceMultiplier * orderCart.product.purchaseProtectionPlanData.protectionPricePerProduct else 0
            val totalShippingPrice = shipping.getRealOriginalPrice().toDouble()
            val insurancePrice = shipping.getRealInsurancePrice().toDouble()
            val (productDiscount, shippingDiscount, cashbacks) = calculatePromo(validateUsePromoRevampUiModel)
            var subtotal = totalProductPrice + purchaseProtectionPrice + totalShippingPrice + insurancePrice
            payment = calculateInstallmentDetails(payment, subtotal, if (orderCart.shop.isOfficial == 1) subtotal - productDiscount - shippingDiscount else 0.0, productDiscount + shippingDiscount)
            val fee = payment.getRealFee()
            subtotal += fee
            subtotal -= productDiscount
            subtotal -= shippingDiscount
            val orderCost = OrderCost(subtotal, totalProductPrice, totalShippingPrice, insurancePrice, fee, shippingDiscount, productDiscount, purchaseProtectionPrice, cashbacks)

            var currentState = forceButtonState ?: orderTotal.buttonState
            if (currentState == OccButtonState.NORMAL && (!shouldButtonStateEnable(shipping, orderCart))) {
                currentState = OccButtonState.DISABLE
            }
            payment = payment.copy(ovoErrorData = null, errorData = null)
            if (isNewFlow && payment.revampErrorMessage.message.isNotEmpty()) {
                // new revamp error
                if (payment.isDisablePayButton) {
                    return@withContext payment.copy(isCalculationError = false,
                            errorData = OrderPaymentErrorData(payment.revampErrorMessage.message, payment.revampErrorMessage.button.text, payment.revampErrorMessage.button.action)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = OccButtonState.DISABLE)
                }
                if (payment.errorMessage.message.isNotEmpty() && payment.errorMessage.button.text.isNotEmpty()) {
                    // cc error should disable button pay
                    return@withContext payment.copy(isCalculationError = false,
                            errorData = OrderPaymentErrorData(payment.revampErrorMessage.message, payment.revampErrorMessage.button.text, payment.revampErrorMessage.button.action)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = OccButtonState.DISABLE)
                }
                return@withContext payment.copy(isCalculationError = false,
                        errorData = OrderPaymentErrorData(payment.revampErrorMessage.message, payment.revampErrorMessage.button.text, payment.revampErrorMessage.button.action)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (!isNewFlow && payment.errorTickerMessage.isNotEmpty() && !payment.isEnableNextButton) {
                // scrooge down
                if (payment.isDisablePayButton) {
                    return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = payment.errorTickerMessage, buttonType = OccButtonType.PAY, buttonState = OccButtonState.DISABLE)
                }
                return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = payment.errorTickerMessage, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (!isNewFlow && payment.errorMessage.message.isNotEmpty() && payment.errorMessage.button.text.isNotEmpty()) {
                // cc expired/deleted
                if (currentState == OccButtonState.NORMAL) {
                    currentState = OccButtonState.DISABLE
                }
                return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
            }
            if (payment.isOvo && payment.ovoData.isPhoneNumberMissing) {
                if (payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true,
                            ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, message = payment.ovoData.phoneNumber.errorMessage,
                                    type = OrderPaymentOvoErrorData.TYPE_MISSING_PHONE)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                val tickerMessage = if (isNewFlow) null else payment.ovoData.phoneNumber.errorTicker
                return@withContext payment.copy(isCalculationError = true,
                        ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = payment.ovoData.phoneNumber.errorMessage,
                                type = OrderPaymentOvoErrorData.TYPE_MISSING_PHONE)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = tickerMessage, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.isOvo && payment.ovoData.isActivationRequired) {
                if (payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true,
                            ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, message = payment.ovoData.activation.errorMessage, buttonTitle = payment.ovoData.activation.buttonTitle,
                                    type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = payment.ovoData.callbackUrl)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                val tickerMessage = if (isNewFlow) null else payment.ovoData.activation.errorTicker
                return@withContext payment.copy(isCalculationError = true,
                        ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = payment.ovoData.activation.errorMessage, buttonTitle = payment.ovoData.activation.buttonTitle,
                                type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = payment.ovoData.callbackUrl)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = tickerMessage, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.minimumAmount > subtotal) {
                if (!isNewFlow && payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                            paymentErrorMessage = generateMinimumAmountPaymentErrorMessage(payment.gatewayName, payment.minimumAmount, false),
                            buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                if (isNewFlow) {
                    if (payment.isOvoOnlyCampaign && currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(generateMinimumAmountPaymentErrorMessage(payment.gatewayName, null, false), CHANGE_PAYMENT_METHOD_MESSAGE, OrderPaymentErrorData.ACTION_CHANGE_PAYMENT)) to orderTotal.copy(orderCost = orderCost,
                            paymentErrorMessage = null, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
                }
                return@withContext payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                        paymentErrorMessage = generateMinimumAmountPaymentErrorMessage(payment.gatewayName, payment.minimumAmount, true),
                        buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.maximumAmount > 0 && payment.maximumAmount < subtotal) {
                if (!isNewFlow && payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                            paymentErrorMessage = generateMaximumAmountPaymentErrorMessage(payment.gatewayName, payment.maximumAmount, false),
                            buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                if (isNewFlow) {
                    if (payment.isOvoOnlyCampaign && currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(generateMaximumAmountPaymentErrorMessage(payment.gatewayName, null, false), CHANGE_PAYMENT_METHOD_MESSAGE, OrderPaymentErrorData.ACTION_CHANGE_PAYMENT)) to orderTotal.copy(orderCost = orderCost,
                            paymentErrorMessage = null, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
                }
                return@withContext payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                        paymentErrorMessage = generateMaximumAmountPaymentErrorMessage(payment.gatewayName, payment.maximumAmount, true),
                        buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (!isNewFlow && payment.errorTickerMessage.isNotEmpty() && payment.isEnableNextButton) {
                // OVO only campaign & OVO is not active for legacy apps
                // Also for escape route if needed
                return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = payment.errorTickerMessage, buttonType = OccButtonType.CONTINUE, buttonState = currentState)
            }
            if (payment.isOvo && subtotal > payment.walletAmount) {
                orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_PAYMENT_OVO_BALANCE)
                if (payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true, ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, message = payment.ovoData.topUp.errorMessage, buttonTitle = payment.ovoData.topUp.buttonTitle,
                            type = OrderPaymentOvoErrorData.TYPE_TOP_UP, callbackUrl = payment.ovoData.callbackUrl, isHideDigital = payment.ovoData.topUp.isHideDigital)) to orderTotal.copy(orderCost = orderCost,
                            paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                val tickerMessage = if (isNewFlow) null else payment.ovoData.topUp.errorTicker
                return@withContext payment.copy(isCalculationError = true, ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = isNewFlow, message = payment.ovoData.topUp.errorMessage, buttonTitle = payment.ovoData.topUp.buttonTitle,
                        type = OrderPaymentOvoErrorData.TYPE_TOP_UP, callbackUrl = payment.ovoData.callbackUrl, isHideDigital = payment.ovoData.topUp.isHideDigital)) to orderTotal.copy(orderCost = orderCost,
                        paymentErrorMessage = tickerMessage, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.creditCard.selectedTerm?.isError == true && currentState == OccButtonState.NORMAL) {
                currentState = OccButtonState.DISABLE
            }
            return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
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

        validateUsePromoRevampUiModel?.promoUiModel?.additionalInfoUiModel?.usageSummariesUiModel?.map {
            cashbacks.add(
                    OrderCostCashbackData(
                            description = it.desc,
                            amountStr = it.amountStr,
                            currencyDetailStr = it.currencyDetailStr
                    )
            )
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