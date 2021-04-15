package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.CHANGE_PAYMENT_METHOD_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MAXIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.MINIMUM_AMOUNT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.ceil

class OrderSummaryPageCalculator @Inject constructor(private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                     private val executorDispatchers: CoroutineDispatchers) {

    private fun shouldButtonStateEnable(orderShipment: OrderShipment, orderCart: OrderCart): Boolean {
        return (orderShipment.isValid() && orderShipment.serviceErrorMessage.isNullOrEmpty() && orderCart.shop.errors.isEmpty() && !orderCart.product.quantity.isStateError)
    }

    private fun generateMinimumAmountPaymentErrorMessage(gatewayName: String): String {
        return "$MINIMUM_AMOUNT_ERROR_MESSAGE $gatewayName."
    }

    private fun generateMaximumAmountPaymentErrorMessage(gatewayName: String): String {
        return "$MAXIMUM_AMOUNT_ERROR_MESSAGE $gatewayName."
    }

    suspend fun calculateTotal(orderCart: OrderCart, _orderPreference: OrderPreference, shipping: OrderShipment,
                               validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, _orderPayment: OrderPayment,
                               orderTotal: OrderTotal, forceButtonState: OccButtonState?, orderPromo: OrderPromo? = null): Pair<OrderPayment, OrderTotal> {
        val quantity = orderCart.product.quantity
        var payment = _orderPayment
        if (quantity.orderQuantity <= 0 || !_orderPreference.isValid) {
            return _orderPayment to orderTotal.copy(orderCost = OrderCost(), buttonState = OccButtonState.DISABLE)
        }
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.immediate) {
            val totalProductPrice = quantity.orderQuantity * orderCart.product.getPrice().toDouble()
            var purchaseProtectionPriceMultiplier = quantity.orderQuantity
            if (orderCart.product.purchaseProtectionPlanData.source.equals(PurchaseProtectionPlanData.SOURCE_READINESS, true)) {
                purchaseProtectionPriceMultiplier = 1
            }
            val purchaseProtectionPrice = if (orderCart.product.purchaseProtectionPlanData.stateChecked == PurchaseProtectionPlanData.STATE_TICKED) purchaseProtectionPriceMultiplier * orderCart.product.purchaseProtectionPlanData.protectionPricePerProduct else 0
            val totalShippingPrice = shipping.getRealOriginalPrice().toDouble()
            val insurancePrice = shipping.getRealInsurancePrice().toDouble()
            val (productDiscount, shippingDiscount, cashbacks) = calculatePromo(validateUsePromoRevampUiModel, orderPromo)
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
            if (payment.revampErrorMessage.message.isNotEmpty()) {
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
            if (payment.isOvo && payment.ovoData.isPhoneNumberMissing) {
                if (payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true, errorData = null,
                            ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = payment.ovoData.phoneNumber.errorMessage,
                                    type = OrderPaymentOvoErrorData.TYPE_MISSING_PHONE)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                return@withContext payment.copy(isCalculationError = true, errorData = null,
                        ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = payment.ovoData.phoneNumber.errorMessage,
                                type = OrderPaymentOvoErrorData.TYPE_MISSING_PHONE)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.isOvo && payment.ovoData.isActivationRequired) {
                if (payment.isOvoOnlyCampaign) {
                    if (currentState == OccButtonState.NORMAL) {
                        currentState = OccButtonState.DISABLE
                    }
                    return@withContext payment.copy(isCalculationError = true, errorData = null,
                            ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = payment.ovoData.activation.errorMessage, buttonTitle = payment.ovoData.activation.buttonTitle,
                                    type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = payment.ovoData.callbackUrl)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
                }
                return@withContext payment.copy(isCalculationError = true, errorData = null,
                        ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = false, message = payment.ovoData.activation.errorMessage, buttonTitle = payment.ovoData.activation.buttonTitle,
                                type = OrderPaymentOvoErrorData.TYPE_ACTIVATION, callbackUrl = payment.ovoData.callbackUrl)) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.minimumAmount > subtotal) {
                var buttonType = OccButtonType.CHOOSE_PAYMENT
                if (payment.isOvoOnlyCampaign && currentState == OccButtonState.NORMAL) {
                    currentState = OccButtonState.DISABLE
                    buttonType = OccButtonType.PAY
                }
                return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(generateMinimumAmountPaymentErrorMessage(payment.gatewayName), CHANGE_PAYMENT_METHOD_MESSAGE, OrderPaymentErrorData.ACTION_CHANGE_PAYMENT)) to orderTotal.copy(orderCost = orderCost,
                        paymentErrorMessage = null, buttonType = buttonType, buttonState = currentState)
            }
            if (payment.maximumAmount > 0 && payment.maximumAmount < subtotal) {
                var buttonType = OccButtonType.CHOOSE_PAYMENT
                if (payment.isOvoOnlyCampaign && currentState == OccButtonState.NORMAL) {
                    currentState = OccButtonState.DISABLE
                    buttonType = OccButtonType.PAY
                }
                return@withContext payment.copy(isCalculationError = true, errorData = OrderPaymentErrorData(generateMaximumAmountPaymentErrorMessage(payment.gatewayName), CHANGE_PAYMENT_METHOD_MESSAGE, OrderPaymentErrorData.ACTION_CHANGE_PAYMENT)) to orderTotal.copy(orderCost = orderCost,
                        paymentErrorMessage = null, buttonType = buttonType, buttonState = currentState)
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
                return@withContext payment.copy(isCalculationError = true, ovoErrorData = OrderPaymentOvoErrorData(isBlockingError = true, message = payment.ovoData.topUp.errorMessage, buttonTitle = payment.ovoData.topUp.buttonTitle,
                        type = OrderPaymentOvoErrorData.TYPE_TOP_UP, callbackUrl = payment.ovoData.callbackUrl, isHideDigital = payment.ovoData.topUp.isHideDigital)) to orderTotal.copy(orderCost = orderCost,
                        paymentErrorMessage = null, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
            }
            if (payment.creditCard.selectedTerm?.isError == true && currentState == OccButtonState.NORMAL) {
                currentState = OccButtonState.DISABLE
            }
            return@withContext payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
        }
        OccIdlingResource.decrement()
        return result
    }

    private fun calculatePromo(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, orderPromo: OrderPromo?): Triple<Int, Int, ArrayList<OrderCostCashbackData>> {
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
        } else if (orderPromo?.lastApply?.additionalInfo?.usageSummaries != null) {
            (orderPromo.lastApply?.additionalInfo?.usageSummaries as List<LastApplyUsageSummariesUiModel>).map {
                if (it.type == SummariesUiModel.TYPE_CASHBACK) {
                    cashbacks.add(
                            OrderCostCashbackData(
                                    description = it.description,
                                    amountStr = it.amountStr,
                                    currencyDetailStr = it.currencyDetailsStr
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