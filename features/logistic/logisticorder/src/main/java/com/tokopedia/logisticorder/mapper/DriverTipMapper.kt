package com.tokopedia.logisticorder.mapper

import com.tokopedia.logisticorder.domain.response.GetDriverTipResponse
import com.tokopedia.logisticorder.domain.response.LastDriver
import com.tokopedia.logisticorder.domain.response.Payment
import com.tokopedia.logisticorder.domain.response.Prepayment
import com.tokopedia.logisticorder.uimodel.LastDriverModel
import com.tokopedia.logisticorder.uimodel.LogisticDriverModel
import com.tokopedia.logisticorder.uimodel.PaymentModel
import com.tokopedia.logisticorder.uimodel.PrepaymentModel
import javax.inject.Inject

class DriverTipMapper @Inject constructor() {

    fun mapDriverTipData(response: GetDriverTipResponse): LogisticDriverModel {
        val data = response.response
        return LogisticDriverModel().apply {
            status = data.status
            lastDriver = mapTippingLastDriverData(data.lastDriver)
            prepayment = mapPrePaymentData(data.prepayment)
            payment = mapPaymentData(data.payment)
        }
    }

    private fun mapTippingLastDriverData(lastDriver: LastDriver): LastDriverModel {
        return LastDriverModel().apply {
            phone = lastDriver.phone
            name = lastDriver.name
            phone = lastDriver.phone
            licenseNumber = lastDriver.licenseNumber
            isChanged = lastDriver.isChanged
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
