package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.CHOOSE_OTHER_PAYMENT_METHOD_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MAXIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MINIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
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

    private fun generateMinimumAmountPaymentErrorMessage(gatewayName: String, minimumAmount: Long, isEnableChooseOtherPaymentMethod: Boolean): String {
        var message = "$MINIMUM_AMOUNT_ERROR_MESSAGE $gatewayName (${CurrencyFormatUtil.convertPriceValueToIdrFormat(minimumAmount, false).removeDecimalSuffix()})."
        if (isEnableChooseOtherPaymentMethod) {
            message += CHOOSE_OTHER_PAYMENT_METHOD_MESSAGE
        }
        return message
    }

    private fun generateMaximumAmountPaymentErrorMessage(gatewayName: String, maximumAmount: Long, isEnableChooseOtherPaymentMethod: Boolean): String {
        var message = "$MAXIMUM_AMOUNT_ERROR_MESSAGE $gatewayName (${CurrencyFormatUtil.convertPriceValueToIdrFormat(maximumAmount, false).removeDecimalSuffix()})."
        if (isEnableChooseOtherPaymentMethod) {
            message += CHOOSE_OTHER_PAYMENT_METHOD_MESSAGE
        }
        return message
    }

    suspend fun calculateTotal(orderCart: OrderCart, _orderPreference: OrderPreference, shipping: OrderShipment, validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, _orderPayment: OrderPayment,
                               orderTotal: OrderTotal, forceButtonState: OccButtonState?): Pair<OrderPayment, OrderTotal> {
        val quantity = orderCart.product.quantity
        var payment = _orderPayment
        if (quantity.orderQuantity <= 0 || !_orderPreference.isValid) {
            return _orderPayment to orderTotal.copy(orderCost = OrderCost(), buttonState = OccButtonState.DISABLE)
        }
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.main) {
            val totalProductPrice = quantity.orderQuantity * orderCart.product.getPrice().toDouble()
            val totalShippingPrice = shipping.getRealOriginalPrice().toDouble()
            val insurancePrice = shipping.getRealInsurancePrice().toDouble()
            val (productDiscount, shippingDiscount, cashbacks) = calculatePromo(validateUsePromoRevampUiModel)
            var subtotal = totalProductPrice + totalShippingPrice + insurancePrice
            payment = calculateInstallmentDetails(payment, subtotal, if (orderCart.shop.isOfficial == 1) subtotal - productDiscount - shippingDiscount else 0.0, productDiscount + shippingDiscount)
            val fee = payment.getRealFee()
            subtotal += fee
            subtotal -= productDiscount
            subtotal -= shippingDiscount
            val orderCost = OrderCost(subtotal, totalProductPrice, totalShippingPrice, insurancePrice, fee, shippingDiscount, productDiscount, cashbacks)

            var currentState = forceButtonState ?: orderTotal.buttonState
            if (currentState == OccButtonState.NORMAL && (!shouldButtonStateEnable(shipping, orderCart))) {
                currentState = OccButtonState.DISABLE
            }
            payment = payment.copy(ovoErrorData = null)
            if (payment.errorTickerMessage.isNotEmpty() && !payment.isEnableNextButton) {
                // scrooge down
                if (payment.isDisablePayButton) {
                    return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = payment.errorTickerMessage, buttonType = OccButtonType.PAY, buttonState = OccButtonState.DISABLE)
                }
                return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = payment.errorTickerMessage, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.errorMessage.message.isNotEmpty() && payment.errorMessage.button.text.isNotEmpty()) {
                // cc expired/deleted
                if (currentState == OccButtonState.NORMAL) {
                    currentState = OccButtonState.DISABLE
                }
                return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
            }
            if (payment.isOvo && payment.ovoData.isActivationRequired) {
                if (payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true,
                            ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, buttonTitle = payment.ovoData.activation.buttonTitle,
                                    type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = payment.ovoData.callbackUrl)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                return@withContext payment.copy(isCalculationError = true,
                        ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, buttonTitle = payment.ovoData.activation.buttonTitle,
                                type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = payment.ovoData.callbackUrl)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = payment.ovoData.activation.errorTicker, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.minimumAmount > subtotal) {
                if (payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                            paymentErrorMessage = generateMinimumAmountPaymentErrorMessage(payment.gatewayName, payment.minimumAmount, false),
                            buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                return@withContext payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                        paymentErrorMessage = generateMinimumAmountPaymentErrorMessage(payment.gatewayName, payment.minimumAmount, true),
                        buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.maximumAmount > 0 && payment.maximumAmount < subtotal) {
                if (payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                            paymentErrorMessage = generateMaximumAmountPaymentErrorMessage(payment.gatewayName, payment.maximumAmount, false),
                            buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                return@withContext payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                        paymentErrorMessage = generateMaximumAmountPaymentErrorMessage(payment.gatewayName, payment.maximumAmount, true),
                        buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.errorTickerMessage.isNotEmpty() && payment.isEnableNextButton) {
                // OVO only campaign & OVO is not active
                return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = payment.errorTickerMessage, buttonType = OccButtonType.CONTINUE, buttonState = currentState)
            }
            if (payment.isOvo && subtotal > payment.walletAmount) {
                orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_PAYMENT_OVO_BALANCE)
                if (payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true, ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, message = payment.ovoData.topUp.errorMessage, buttonTitle = payment.ovoData.topUp.buttonTitle, type = OrderPaymentOvoErrorData.TYPE_TOP_UP, callbackUrl = payment.ovoData.callbackUrl)) to orderTotal.copy(orderCost = orderCost,
                            paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                return@withContext payment.copy(isCalculationError = true, ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = payment.ovoData.topUp.errorMessage, buttonTitle = payment.ovoData.topUp.buttonTitle, type = OrderPaymentOvoErrorData.TYPE_TOP_UP, callbackUrl = payment.ovoData.callbackUrl)) to orderTotal.copy(orderCost = orderCost,
                        paymentErrorMessage = payment.ovoData.topUp.errorTicker,
                        buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.creditCard.selectedTerm?.isError == true && currentState == OccButtonState.NORMAL) {
                currentState = OccButtonState.DISABLE
            }
            return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
        }
        OccIdlingResource.decrement()
        return result
    }

    private fun calculatePromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?): Triple<Int, Int, ArrayList<Pair<String, String>>> {
        var productDiscount = 0
        var shippingDiscount = 0
        val cashbacks = ArrayList<Pair<String, String>>()
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
            if (summary.type == SummariesUiModel.TYPE_CASHBACK) {
                cashbacks.addAll(summary.details.map { it.description to it.amountStr })
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