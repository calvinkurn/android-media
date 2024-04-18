package com.tokopedia.checkoutpayment.processor

import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentData
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentOption
import com.tokopedia.checkoutpayment.domain.PaymentAmountValidation
import com.tokopedia.checkoutpayment.domain.PaymentInstallmentData
import com.tokopedia.checkoutpayment.domain.PaymentWalletAction
import com.tokopedia.checkoutpayment.domain.PaymentWalletData
import com.tokopedia.checkoutpayment.domain.PaymentWidgetData
import com.tokopedia.checkoutpayment.domain.PaymentWidgetListData
import com.tokopedia.checkoutpayment.domain.TenorListData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class PaymentProcessorTest {

    private val processor: PaymentProcessor = PaymentProcessor(
        creditCardTenorListUseCase = mockk(),
        goCicilInstallmentOptionUseCase = mockk(),
        dynamicPaymentFeeUseCase = mockk(),
        getPaymentWidgetUseCase = mockk(),
        dispatchers = CoroutineTestDispatchers
    )

    @Test
    fun `GIVEN required phone registration WHEN validate payment THEN should return missing phone report`() {
        // GIVEN
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    walletData = PaymentWalletData(
                        phoneNumberRegistration = PaymentWalletAction(
                            isRequired = true
                        )
                    )
                )
            )
        )

        // WHEN
        val result = processor.validatePayment(paymentWidgetListData, null, null, 1000.0)

        // THEN
        assertEquals(PaymentValidationReport.MissingPhoneNumberError, result)
    }

    @Test
    fun `GIVEN required wallet activation WHEN validate payment THEN should return wallet activation report`() {
        // GIVEN
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    walletData = PaymentWalletData(
                        activation = PaymentWalletAction(
                            isRequired = true
                        )
                    )
                )
            )
        )

        // WHEN
        val result = processor.validatePayment(paymentWidgetListData, null, null, 1000.0)

        // THEN
        assertEquals(PaymentValidationReport.WalletActivationError, result)
    }

    @Test
    fun `GIVEN total below minimum WHEN validate payment THEN should return minimum amount report`() {
        // GIVEN
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    amountValidation = PaymentAmountValidation(
                        minimumAmount = 50000
                    )
                )
            )
        )

        // WHEN
        val result = processor.validatePayment(paymentWidgetListData, null, null, 1000.0)

        // THEN
        assertEquals(PaymentValidationReport.MinimumAmountError, result)
    }

    @Test
    fun `GIVEN total above maximum WHEN validate payment THEN should return maximum amount report`() {
        // GIVEN
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    amountValidation = PaymentAmountValidation(
                        maximumAmount = 100
                    )
                )
            )
        )

        // WHEN
        val result = processor.validatePayment(paymentWidgetListData, null, null, 1000.0)

        // THEN
        assertEquals(PaymentValidationReport.MaximumAmountError, result)
    }

    @Test
    fun `GIVEN total above wallet amount WHEN validate payment THEN should return wallet amount report`() {
        // GIVEN
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    amountValidation = PaymentAmountValidation(
                        maximumAmount = 100000
                    ),
                    walletData = PaymentWalletData(
                        walletType = 1,
                        walletAmount = 10
                    )
                )
            )
        )

        // WHEN
        val result = processor.validatePayment(paymentWidgetListData, null, null, 1000.0)

        // THEN
        assertEquals(PaymentValidationReport.WalletAmountError, result)
    }

    @Test
    fun `GIVEN no valid installment options WHEN validate payment THEN should return unavailable tenure report`() {
        // GIVEN
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    amountValidation = PaymentAmountValidation(
                        maximumAmount = 100000
                    ),
                    mandatoryHit = listOf("getInstallmentInfo")
                )
            )
        )

        // WHEN
        val result = processor.validatePayment(paymentWidgetListData, null,
            GoCicilInstallmentData(installmentOptions = listOf(GoCicilInstallmentOption(isActive = false))), 1000.0)

        // THEN
        assertEquals(PaymentValidationReport.UnavailableTenureError, result)
    }

    @Test
    fun `GIVEN no valid tenure options WHEN validate payment THEN should return unavailable tenure report`() {
        // GIVEN
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    amountValidation = PaymentAmountValidation(
                        maximumAmount = 100000
                    ),
                    mandatoryHit = listOf("CreditCardTenorList")
                )
            )
        )

        // WHEN
        val result = processor.validatePayment(paymentWidgetListData, listOf(TenorListData(disable = true)), null, 1000.0)

        // THEN
        assertEquals(PaymentValidationReport.UnavailableTenureError, result)
    }

    @Test
    fun `GIVEN no valid tenure options WHEN generate payment widget THEN should return error state`() {
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    imageUrl = "imageUrl",
                    gatewayName = "name",
                    amountValidation = PaymentAmountValidation(
                        maximumAmount = 100000
                    ),
                    mandatoryHit = listOf("CreditCardTenorList"),
                    installmentPaymentData = PaymentInstallmentData(
                        errorMessageUnavailableTenure = "error"
                    )
                )
            )
        )

        val currentWidget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal)

        val result = processor.generateCheckoutPaymentWidgetData(
            paymentWidgetListData,
            listOf(TenorListData(disable = true)),
            null,
            currentWidget,
            PaymentValidationReport.UnavailableTenureError,
            "")

        assertEquals(CheckoutPaymentWidgetData(
            state = CheckoutPaymentWidgetState.Normal,
            logoUrl = "imageUrl",
            title = "name",
            installmentText = "Pilih periode pembayaran"
        ), result)
    }

    @Test
    fun `GIVEN total above wallet amount WHEN generate payment widget THEN should return error state`() {
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    imageUrl = "imageUrl",
                    gatewayName = "name",
                    description = "desc",
                    amountValidation = PaymentAmountValidation(
                        maximumAmount = 100000
                    ),
                    walletData = PaymentWalletData(
                        walletType = 1,
                        walletAmount = 10,
                        topUp = PaymentWalletAction(
                            errorMessage = "error"
                        )
                    )
                )
            )
        )

        val currentWidget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal)

        val result = processor.generateCheckoutPaymentWidgetData(
            paymentWidgetListData,
            null,
            null,
            currentWidget,
            PaymentValidationReport.WalletAmountError,
            "")

        assertEquals(CheckoutPaymentWidgetData(
            state = CheckoutPaymentWidgetState.Normal,
            logoUrl = "imageUrl",
            title = "name",
            description = "error",
            isDescriptionRed = true,
            subtitle = "(Rp10)"
        ), result)
    }

    @Test
    fun `GIVEN total above maximum WHEN generate payment widget THEN should return error state`() {
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    imageUrl = "imageUrl",
                    gatewayName = "name",
                    description = "desc",
                    amountValidation = PaymentAmountValidation(
                        maximumAmount = 10,
                        maximumAmountErrorMessage = "error"
                    )
                )
            )
        )

        val currentWidget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal)

        val result = processor.generateCheckoutPaymentWidgetData(
            paymentWidgetListData,
            null,
            null,
            currentWidget,
            PaymentValidationReport.MaximumAmountError,
            "")

        assertEquals(CheckoutPaymentWidgetData(
            state = CheckoutPaymentWidgetState.Normal,
            logoUrl = "imageUrl",
            title = "name",
            description = "error",
            isDescriptionRed = true
        ), result)
    }

    @Test
    fun `GIVEN total below minimum WHEN generate payment widget THEN should return error state`() {
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    imageUrl = "imageUrl",
                    gatewayName = "name",
                    description = "desc",
                    amountValidation = PaymentAmountValidation(
                        minimumAmount = 50000,
                        minimumAmountErrorMessage = "error"
                    )
                )
            )
        )

        val currentWidget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal)

        val result = processor.generateCheckoutPaymentWidgetData(
            paymentWidgetListData,
            null,
            null,
            currentWidget,
            PaymentValidationReport.MinimumAmountError,
            "")

        assertEquals(CheckoutPaymentWidgetData(
            state = CheckoutPaymentWidgetState.Normal,
            logoUrl = "imageUrl",
            title = "name",
            description = "error",
            isDescriptionRed = true
        ), result)
    }

    @Test
    fun `GIVEN required wallet activation WHEN generate payment widget THEN should return error state`() {
        val paymentWidgetListData = PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    imageUrl = "imageUrl",
                    gatewayName = "name",
                    description = "desc",
                    walletData = PaymentWalletData(
                        activation = PaymentWalletAction(
                            isRequired = true,
                            errorMessage = "error",
                            buttonTitle = "button"
                        )
                    )
                )
            )
        )

        val currentWidget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal)

        val result = processor.generateCheckoutPaymentWidgetData(
            paymentWidgetListData,
            null,
            null,
            currentWidget,
            PaymentValidationReport.WalletActivationError,
            "")

        assertEquals(CheckoutPaymentWidgetData(
            state = CheckoutPaymentWidgetState.Normal,
            logoUrl = "imageUrl",
            title = "name",
            description = "error",
            isDescriptionRed = true,
            actionButtonText = "button"
        ), result)
    }
}
