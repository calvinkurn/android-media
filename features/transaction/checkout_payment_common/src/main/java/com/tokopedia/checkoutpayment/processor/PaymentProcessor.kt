package com.tokopedia.checkoutpayment.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkoutpayment.data.CreditCardTenorListRequest
import com.tokopedia.checkoutpayment.data.GetPaymentWidgetRequest
import com.tokopedia.checkoutpayment.data.GoCicilInstallmentRequest
import com.tokopedia.checkoutpayment.data.PaymentFeeRequest
import com.tokopedia.checkoutpayment.domain.CreditCardTenorListUseCase
import com.tokopedia.checkoutpayment.domain.DynamicPaymentFeeUseCase
import com.tokopedia.checkoutpayment.domain.GetPaymentWidgetUseCase
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentData
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentOptionUseCase
import com.tokopedia.checkoutpayment.domain.PaymentWidgetListData
import com.tokopedia.checkoutpayment.domain.TenorListData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetState
import com.tokopedia.checkoutpayment.view.OrderPaymentFee
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PaymentProcessor @Inject constructor(
    private val creditCardTenorListUseCase: CreditCardTenorListUseCase,
    private val goCicilInstallmentOptionUseCase: GoCicilInstallmentOptionUseCase,
    private val dynamicPaymentFeeUseCase: DynamicPaymentFeeUseCase,
    private val getPaymentWidgetUseCase: GetPaymentWidgetUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    suspend fun getCreditCardTenorList(param: CreditCardTenorListRequest): List<TenorListData>? {
        return withContext(dispatchers.io) {
            try {
                val creditCardData = creditCardTenorListUseCase(param)
                if (creditCardData.errorMsg.isNotEmpty()) {
                    return@withContext null
                }
                return@withContext creditCardData.tenorList
            } catch (e: Exception) {
                Timber.d(e)
                return@withContext null
            }
        }
    }

    suspend fun getGocicilInstallmentOption(param: GoCicilInstallmentRequest): GoCicilInstallmentData? {
        return withContext(dispatchers.io) {
            try {
                return@withContext goCicilInstallmentOptionUseCase(param)
            } catch (e: Exception) {
                Timber.d(e)
                return@withContext null
            }
        }
    }

    suspend fun getPaymentFee(param: PaymentFeeRequest): List<OrderPaymentFee>? {
        return withContext(dispatchers.io) {
            try {
                return@withContext dynamicPaymentFeeUseCase(param)
            } catch (e: Exception) {
                Timber.d(e)
                return@withContext null
            }
        }
    }

    suspend fun getPaymentWidget(param: GetPaymentWidgetRequest): PaymentWidgetListData? {
        return withContext(dispatchers.io) {
            try {
                return@withContext getPaymentWidgetUseCase(param)
            } catch (e: Exception) {
                Timber.d(e)
                return@withContext null
            }
        }
    }

    fun validatePayment(
        payment: PaymentWidgetListData,
        tenorList: List<TenorListData>?,
        installmentData: GoCicilInstallmentData?,
        total: Double
    ): PaymentValidationReport {
        val paymentData = payment.paymentWidgetData.firstOrNull()
        if (paymentData != null) {
            if (paymentData.errorDetails.message.isNotEmpty()) {
                // error?
                return PaymentValidationReport.ServerError
            }

            if (paymentData.walletData.phoneNumberRegistration.isRequired) {
                return PaymentValidationReport.MissingPhoneNumberError
            }

            if (paymentData.walletData.activation.isRequired) {
                return PaymentValidationReport.WalletActivationError
            }

            if (total < paymentData.amountValidation.minimumAmount) {
                return PaymentValidationReport.MinimumAmountError
            }

            if (total > paymentData.amountValidation.maximumAmount) {
                return PaymentValidationReport.MaximumAmountError
            }

            if (paymentData.walletData.walletType > 0) {
                if (total > paymentData.walletData.walletAmount) {
                    return PaymentValidationReport.WalletAmountError
                }
            }

            if (paymentData.mandatoryHit.contains("gocicil")) {
                if (installmentData?.installmentOptions?.find { it.isActive } == null) {
                    return PaymentValidationReport.UnavailableTenureError
                }
            }

            if (paymentData.mandatoryHit.contains("tenor_list")) {
                if (tenorList?.find { !it.disable } == null) {
                    return PaymentValidationReport.UnavailableTenureError
                }
            }

            return PaymentValidationReport.Valid
        }
        // error
        return PaymentValidationReport.NullPaymentError
    }

    fun generateCheckoutPaymentWidgetData(
        payment: PaymentWidgetListData,
        tenorList: List<TenorListData>?,
        installmentData: GoCicilInstallmentData?,
        currentWidget: CheckoutPaymentWidgetData,
        validationReport: PaymentValidationReport,
        defaultErrorMessage: String
    ): CheckoutPaymentWidgetData {
        if (currentWidget.state == CheckoutPaymentWidgetState.Error) {
            return currentWidget.copy(errorMessage = defaultErrorMessage)
        }
        if (validationReport is PaymentValidationReport.NullPaymentError) {
            return currentWidget.copy(
                state = CheckoutPaymentWidgetState.Error,
                errorMessage = defaultErrorMessage
            )
        }
        val latestWidget = generateBaseCheckoutPaymentWidgetData(payment, tenorList, installmentData, currentWidget)
        val currentData = payment.paymentWidgetData.first()
        return when (validationReport) {
            PaymentValidationReport.MaximumAmountError -> {
                latestWidget.copy(
                    errorMessage = currentData.amountValidation.maximumAmountErrorMessage
                )
            }

            PaymentValidationReport.MinimumAmountError -> {
                latestWidget.copy(
                    errorMessage = currentData.amountValidation.minimumAmountErrorMessage
                )
            }

            PaymentValidationReport.MissingPhoneNumberError -> {
                latestWidget.copy(
                    errorMessage = currentData.walletData.phoneNumberRegistration.errorMessage
                )
            }

            PaymentValidationReport.NullPaymentError -> {
                // already handled above
                latestWidget
            }

            PaymentValidationReport.ServerError -> {
                latestWidget.copy(
                    errorMessage = currentData.errorDetails.message
                )
            }

            PaymentValidationReport.UnavailableTenureError -> {
                latestWidget.copy(
                    errorMessage = currentData.installmentPaymentData.errorMessageUnavailableTenure
                )
            }

            PaymentValidationReport.Valid -> {
                latestWidget
            }

            PaymentValidationReport.WalletActivationError -> {
                latestWidget.copy(
                    errorMessage = currentData.walletData.activation.errorMessage
                )
            }

            PaymentValidationReport.WalletAmountError -> {
                latestWidget.copy(
                    errorMessage = currentData.walletData.topUp.errorMessage
                )
            }
        }
    }

    private fun generateBaseCheckoutPaymentWidgetData(
        payment: PaymentWidgetListData,
        tenorList: List<TenorListData>?,
        installmentData: GoCicilInstallmentData?,
        currentWidget: CheckoutPaymentWidgetData
    ): CheckoutPaymentWidgetData {
        val currentData = payment.paymentWidgetData.firstOrNull() ?: return currentWidget
        return currentWidget.copy(
            logoUrl = currentData.imageUrl,
            title = currentData.gatewayName
        )
    }
}

sealed interface PaymentValidationReport {

    object NullPaymentError : PaymentValidationReport

    object ServerError : PaymentValidationReport

    object MinimumAmountError : PaymentValidationReport
    object MaximumAmountError : PaymentValidationReport

    object UnavailableTenureError : PaymentValidationReport

    object MissingPhoneNumberError : PaymentValidationReport

    object WalletActivationError : PaymentValidationReport

    object WalletAmountError : PaymentValidationReport

    object Valid : PaymentValidationReport
}
