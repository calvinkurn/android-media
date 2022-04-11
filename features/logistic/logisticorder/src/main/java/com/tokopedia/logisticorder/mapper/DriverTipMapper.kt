package com.tokopedia.logisticorder.mapper

import com.tokopedia.logisticorder.domain.response.*
import com.tokopedia.logisticorder.uimodel.*
import javax.inject.Inject

class DriverTipMapper @Inject constructor(){

    fun mapDriverTipData(response: GetDriverTipResponse): LogisticDriverModel {
        val data = response.response
        return LogisticDriverModel().apply {
            status = data.status
            tippingLastDriver = mapTippingLastDriverData(data.tippingLastDriver)
            prepayment = mapPrePaymentData(data.prepayment)
            payment = mapPaymentData(data.payment)
        }
    }

    private fun mapTippingLastDriverData(tippingLastDriver: TippingLastDriver): TippingLastDriverModel {
        return TippingLastDriverModel().apply {
            phone = tippingLastDriver.phone
            name = tippingLastDriver.name
            phone = tippingLastDriver.phone
            licenseNumber = tippingLastDriver.licenseNumber
            isChanged = tippingLastDriver.isChanged
        }
    }

    private fun mapPrePaymentData(prePaymentData: Prepayment): PrepaymentModel {
        return PrepaymentModel().apply {
            info = prePaymentData.info
            presetAmount = prePaymentData.presetAmount
            maxAmount = prePaymentData.maxAmount
            minAmount = prePaymentData.minAmount
            paymentLink = prePaymentData.paymentLink
        }
    }

    private fun mapPaymentData(paymentData: Payment): PaymentModel {
        return PaymentModel().apply {
            amount = paymentData.amount
            amountFormatted = paymentData.amountFormatted
            method = paymentData.method
            methodIcon = paymentData.methodIcon
        }
    }
}