package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject
import kotlin.math.ceil

class OrderSummaryPageCalculator @Inject constructor(private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private fun shouldButtonStateEnable(orderShipment: OrderShipment, orderCart: OrderCart): Boolean {
        return (orderShipment.isValid() && orderShipment.serviceErrorMessage.isNullOrEmpty() && orderCart.shop.errors.isEmpty() && !orderCart.product.quantity.isStateError)
    }

    fun calculateTotal(orderCart: OrderCart, _orderPreference: OrderPreference, shipping: OrderShipment, validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?, _orderPayment: OrderPayment, total: OrderTotal): Pair<OrderPayment, OrderTotal> {
        val orderTotal = total.copy(buttonState = if (shouldButtonStateEnable(shipping, orderCart)) OccButtonState.NORMAL else OccButtonState.DISABLE)
        val quantity = orderCart.product.quantity
        var payment = _orderPayment
        if (quantity.orderQuantity <= 0 || !_orderPreference.isValid) {
            return _orderPayment to orderTotal.copy(orderCost = OrderCost(), buttonState = OccButtonState.DISABLE)
        }
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

        var currentState = orderTotal.buttonState
        if (currentState == OccButtonState.NORMAL && (!shouldButtonStateEnable(shipping, orderCart))) {
            currentState = OccButtonState.DISABLE
        }
        if (payment.errorTickerMessage.isNotEmpty()) {
            return payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = payment.errorTickerMessage, buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
        }
        if (payment.errorMessage.message.isNotEmpty() && payment.errorMessage.button.text.isNotEmpty()) {
            if (currentState == OccButtonState.NORMAL) {
                currentState = OccButtonState.DISABLE
            }
            return payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
        }
        if (payment.minimumAmount > subtotal) {
            return payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                    paymentErrorMessage = "Belanjaanmu kurang dari min. transaksi ${payment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(payment.minimumAmount, false).removeDecimalSuffix()}). Silahkan pilih pembayaran lain.",
                    buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
        }
        if (payment.maximumAmount > 0 && payment.maximumAmount < subtotal) {
            return payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                    paymentErrorMessage = "Belanjaanmu melebihi limit transaksi ${payment.gatewayName} (${CurrencyFormatUtil.convertPriceValueToIdrFormat(payment.maximumAmount, false).removeDecimalSuffix()}). Silahkan pilih pembayaran lain.",
                    buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
        }
        if (payment.gatewayCode.contains(OrderSummaryPageViewModel.OVO_GATEWAY_CODE) && subtotal > payment.walletAmount) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_PAYMENT_OVO_BALANCE)
            return payment.copy(isCalculationError = true) to orderTotal.copy(orderCost = orderCost,
                    paymentErrorMessage = OrderSummaryPageViewModel.OVO_INSUFFICIENT_ERROR_MESSAGE,
                    buttonType = OccButtonType.CHOOSE_PAYMENT, buttonState = currentState)
        }
        if (payment.creditCard.selectedTerm?.isError == true && currentState == OccButtonState.NORMAL) {
            currentState = OccButtonState.DISABLE
        }
        return payment.copy(isCalculationError = false) to orderTotal.copy(orderCost = orderCost, paymentErrorMessage = null, buttonType = OccButtonType.PAY, buttonState = currentState)
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