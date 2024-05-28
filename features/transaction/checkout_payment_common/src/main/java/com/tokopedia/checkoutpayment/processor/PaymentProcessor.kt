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
import com.tokopedia.checkoutpayment.domain.PaymentWidgetData.Companion.MANDATORY_HIT_CC_TENOR_LIST
import com.tokopedia.checkoutpayment.domain.PaymentWidgetData.Companion.MANDATORY_HIT_INSTALLMENT_OPTIONS
import com.tokopedia.checkoutpayment.domain.PaymentWidgetListData
import com.tokopedia.checkoutpayment.domain.TenorListData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetState
import com.tokopedia.checkoutpayment.view.OrderPaymentFee
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
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
                val response = getPaymentWidgetUseCase.invoke(param)
                return@withContext response
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

            if (paymentData.mandatoryHit.contains(MANDATORY_HIT_INSTALLMENT_OPTIONS)) {
                if (installmentData?.installmentOptions?.find { it.isActive } == null) {
                    return PaymentValidationReport.UnavailableTenureError
                }
            }

            if (paymentData.mandatoryHit.contains(MANDATORY_HIT_CC_TENOR_LIST)) {
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
                    description = currentData.amountValidation.maximumAmountErrorMessage,
                    isDescriptionRed = true
                )
            }

            PaymentValidationReport.MinimumAmountError -> {
                latestWidget.copy(
                    description = currentData.amountValidation.minimumAmountErrorMessage,
                    isDescriptionRed = true
                )
            }

            PaymentValidationReport.MissingPhoneNumberError -> {
                latestWidget.copy(
                    description = currentData.walletData.phoneNumberRegistration.errorMessage,
                    isDescriptionRed = true
                )
            }

            PaymentValidationReport.NullPaymentError -> {
                // already handled above
                latestWidget
            }

            PaymentValidationReport.ServerError -> {
                latestWidget.copy(
                    description = currentData.errorDetails.message,
                    isDescriptionRed = true,
                    actionButtonText = ""
                )
            }

            PaymentValidationReport.UnavailableTenureError -> {
                latestWidget.copy(
                    installmentText = DEFAULT_PAYMENT_INSTALLMENT_TEXT
                )
            }

            PaymentValidationReport.Valid -> {
                latestWidget.copy(
                    isDescriptionRed = false,
                    isTitleRed = false,
                    actionButtonText = ""
                )
            }

            PaymentValidationReport.WalletActivationError -> {
                // for future phase (ocs)
//                latestWidget.copy(
//                    title = currentData.walletData.activation.errorMessage,
//                    subtitle = "",
//                    isTitleRed = true,
//                    actionButtonText = currentData.walletData.activation.buttonTitle
//                )
                latestWidget.copy(
                    subtitle = "",
                    description = currentData.walletData.activation.errorMessage,
                    isDescriptionRed = true,
                    actionButtonText = currentData.walletData.activation.buttonTitle
                )
            }

            PaymentValidationReport.WalletAmountError -> {
//                if (currentData.mandatoryHit.contains(MANDATORY_HIT_INSTALLMENT_OPTIONS)) {
                latestWidget.copy(
                    description = currentData.walletData.topUp.errorMessage,
                    isDescriptionRed = true,
                    actionButtonText = currentData.walletData.topUp.buttonTitle
                )
//                } else {
                // for future phases (ocs)
//                    latestWidget.copy(
//                        title = currentData.walletData.topUp.errorMessage,
//                        isTitleRed = true,
//                        actionButtonText = currentData.walletData.topUp.buttonTitle
//                    )
//                }
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
        val selectedTenure = currentData.installmentPaymentData.selectedTenure
        var installmentText = ""
        if (!tenorList.isNullOrEmpty()) {
            if (selectedTenure > 0) {
                tenorList.firstOrNull { it.tenure == selectedTenure }?.let {
                    installmentText = "Cicil ${it.tenure}x ${CurrencyFormatUtil.convertPriceValueToIdrFormat(it.amount, false).removeDecimalSuffix()}"
                }
            } else {
                installmentText = FULL_PAYMENT_INSTALLMENT_TEXT
            }
        }
        if (installmentData != null) {
            if (selectedTenure > 0) {
                installmentData.installmentOptions.firstOrNull { it.installmentTerm == selectedTenure }?.let {
                    installmentText = "Cicil ${it.installmentTerm}x ${CurrencyFormatUtil.convertPriceValueToIdrFormat(it.installmentAmountPerPeriod, false).removeDecimalSuffix()}"
                }
            } else {
                installmentText = FULL_PAYMENT_INSTALLMENT_TEXT
            }
        }
        var subtitle = ""
        if (currentData.installmentPaymentData.creditCardAttribute.maskedNumber.isNotEmpty()) {
            subtitle = currentData.installmentPaymentData.creditCardAttribute.maskedNumber
        }
        if (currentData.walletData.walletType > 0) {
            subtitle = "(${CurrencyFormatUtil.convertPriceValueToIdrFormat(currentData.walletData.walletAmount, false).removeDecimalSuffix()})"
        }
        return currentWidget.copy(
            logoUrl = currentData.imageUrl,
            title = currentData.gatewayName,
            subtitle = subtitle,
            description = currentData.description,
            installmentText = installmentText
        )
    }

    companion object {
        private const val FULL_PAYMENT_INSTALLMENT_TEXT = "Bayar Penuh"
        private const val DEFAULT_PAYMENT_INSTALLMENT_TEXT = "Pilih Lama Pembayaran"
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
