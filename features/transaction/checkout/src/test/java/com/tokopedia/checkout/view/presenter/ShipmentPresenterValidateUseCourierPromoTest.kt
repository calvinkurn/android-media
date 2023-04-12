package com.tokopedia.checkout.view.presenter

import android.app.Activity
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Test

class ShipmentPresenterValidateUseCourierPromoTest : BaseShipmentPresenterTest() {

    @Test
    fun `WHEN validate use success THEN should render promo from courier`() {
        // Given
        val validateUseModel = ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                globalSuccess = true,
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )
        val position = 0
        val noToast = true
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns validateUseModel

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verify {
            view.renderPromoCheckoutFromCourierSuccess(validateUseModel, position, noToast)
        }
    }

    @Test
    fun `WHEN validate use get red state THEN should  show error and reset courier`() {
        // Given
        val errorMessage = "error"
        val cartString = "cart123"
        val validateUseModel = ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                globalSuccess = true,
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        type = "logistic",
                        uniqueId = cartString,
                        messageUiModel = MessageUiModel(state = "red", text = errorMessage)
                    )
                )
            )
        )
        val position = 0
        val noToast = true

        val shipmentCartItemModel = ShipmentCartItemModel(cartString = cartString)
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            validateUseModel
//        every { view.generateValidateUsePromoRequest() } returns ValidateUsePromoRequest()

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verifySequence {
//            view.generateValidateUsePromoRequest()
            view.showToastError(errorMessage)
            view.resetCourier(shipmentCartItemModel)
            view.logOnErrorApplyBo(match { it.message == errorMessage }, shipmentCartItemModel, "")
            view.renderPromoCheckoutFromCourierSuccess(validateUseModel, position, noToast)
        }
    }

    @Test
    fun `WHEN validate use failed THEN should render error`() {
        // Given
        val errorMessage = "error"
        val validateUseModel = ValidateUsePromoRevampUiModel(
            status = "ERROR",
            message = listOf(errorMessage)
        )
        val position = 0
        val noToast = true
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            validateUseModel

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verify {
            view.renderErrorCheckPromoShipmentData(errorMessage)
        }
    }

    @Test
    fun `WHEN validate use failed without error message THEN should render default error message`() {
        // Given
        val validateUseModel = ValidateUsePromoRevampUiModel(
            status = "ERROR",
            message = emptyList()
        )
        val position = 0
        val noToast = true
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            validateUseModel

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verify {
            view.renderErrorCheckPromoShipmentData(DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO)
        }
    }

    @Test
    fun `WHEN validate use failed with exception THEN should show error`() {
        // Given
        val errorMessage = "error"
        val position = 0
        val noToast = true

        val exception = ResponseErrorException()
        val mockContext = mockk<Activity>()
        mockkObject(ErrorHandler.Companion)
        every { view.activityContext } returns mockContext
        every { ErrorHandler.Companion.getErrorMessage(any(), any(), any()) } returns errorMessage
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            exception

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verify {
            view.showToastError(errorMessage)
        }
    }

    @Test
    fun `WHEN validate use failed with akamai exception THEN should show error`() {
        // Given
        val errorMessage = "error"
        val position = 0
        val noToast = true

        val exception = AkamaiErrorException(errorMessage)
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            exception
//        every { view.generateValidateUsePromoRequest() } returns ValidateUsePromoRequest()

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verifySequence {
//            view.generateValidateUsePromoRequest()
            view.showToastError(errorMessage)
            view.resetAllCourier()
            view.cancelAllCourierPromo()
            view.doResetButtonPromoCheckout()
        }
    }
}
