package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentData
import com.tokopedia.checkoutpayment.domain.PaymentWidgetListData
import com.tokopedia.checkoutpayment.domain.TenorListData
import com.tokopedia.checkoutpayment.processor.PaymentValidationReport
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData

data class CheckoutPaymentModel(
    override val cartStringGroup: String = "",
    val widget: CheckoutPaymentWidgetData,
    val metadata: String = "",
    val enable: Boolean = false,
    val defaultErrorMessage: String = "",
    val originalData: OriginalCheckoutPaymentData = OriginalCheckoutPaymentData(),
    val data: PaymentWidgetListData? = null,
    val tenorList: List<TenorListData>? = null,
    val installmentData: GoCicilInstallmentData? = null,
    val validationReport: PaymentValidationReport = PaymentValidationReport.Valid
) : CheckoutItem {

    fun getPaymentMethodName(): String {
        return data?.paymentWidgetData?.firstOrNull()?.gatewayName ?: ""
    }
}

data class OriginalCheckoutPaymentData(
    val gatewayCode: String = "",
    val tenureType: Int = 0,
    val optionId: String = "",
    val metadata: String = ""
)
