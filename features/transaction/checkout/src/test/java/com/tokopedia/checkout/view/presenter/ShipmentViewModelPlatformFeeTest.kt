package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeGqlResponse
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeResponse
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentViewModelPlatformFeeTest : BaseShipmentViewModelTest() {

    private var platformFeeParams = PaymentFeeCheckoutRequest()

    private var shipmentMapper = ShipmentMapper()

    @Test
    fun verifyShipmentPlatformFeeDataIsEnabled() {
        val response = DataProvider.provideShipmentAddressFormWithPlatformFeeEnabledResponse()
        val cartShipmentAddressFormData = shipmentMapper
            .convertToShipmentAddressFormData(response.shipmentAddressFormResponse.data)

        coEvery {
            getShipmentAddressFormV4UseCase(
                any()
            )
        } returns cartShipmentAddressFormData
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        assert(viewModel.getShipmentPlatformFeeData().isEnable)
    }

    @Test
    fun verifyShipmentPlatformFeeDataIsDisabled() {
        val response = DataProvider.provideShipmentAddressFormWithPlatformFeeDisabledResponse()
        val cartShipmentAddressFormData = shipmentMapper
            .convertToShipmentAddressFormData(response.shipmentAddressFormResponse.data)

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns cartShipmentAddressFormData
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        assert(!viewModel.getShipmentPlatformFeeData().isEnable)
    }

    @Test
    fun getDynamicPlatformFeeReturnsSuccess() {
        // Given
        val platformFee = PaymentFeeGqlResponse(PaymentFeeResponse(success = true))

        coEvery {
            dynamicPaymentFeeCheckoutUseCase.setParams(any())
        } just Runs
        coEvery {
            dynamicPaymentFeeCheckoutUseCase.execute(any(), any())
        } answers {
            firstArg<(PaymentFeeGqlResponse) -> Unit>().invoke(platformFee)
        }

        // When
        viewModel.getDynamicPaymentFee(platformFeeParams)

        // Then
        verifyOrder {
            view.showPaymentFeeData(platformFee.response)
        }
    }

    @Test
    fun setDynamicPlatformFeeReturnsSuccess() {
        // Given
        val paymentFee = ShipmentPaymentFeeModel(
            title = "testing",
            fee = 10_000.0,
            minRange = 1.0,
            maxRange = 1_000_000.0
        )

        // When
        viewModel.setPlatformFeeData(paymentFee)

        // Then
        assertEquals(paymentFee, viewModel.shipmentCostModel.value.dynamicPlatformFee)
    }

    @Test
    fun getDynamicPlatformFeeReturnsFailed() {
        // Given
        val platformFee = PaymentFeeGqlResponse(PaymentFeeResponse(success = false))
        coEvery {
            dynamicPaymentFeeCheckoutUseCase.setParams(any())
        } just Runs
        coEvery {
            dynamicPaymentFeeCheckoutUseCase.execute(any(), any())
        } answers {
            firstArg<(PaymentFeeGqlResponse) -> Unit>().invoke(platformFee)
        }

        viewModel.getDynamicPaymentFee(platformFeeParams)
        // When

        // Then
        verifyOrder {
            view.showPaymentFeeTickerFailedToLoad(any())
        }
    }

    @Test
    fun getDynamicPlatformFeeReturnsError() {
        // Given
        coEvery {
            dynamicPaymentFeeCheckoutUseCase.setParams(any())
        } just Runs
        coEvery {
            dynamicPaymentFeeCheckoutUseCase.execute(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }

        viewModel.getDynamicPaymentFee(platformFeeParams)
        // When

        // Then
        verifyOrder {
            view.showPaymentFeeTickerFailedToLoad(any())
        }
    }
}
