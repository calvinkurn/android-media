package com.tokopedia.checkout.view.presenter

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryMapData
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject

class ShipmentViewModelValidateUseLogisticPromoTest : BaseShipmentViewModelTest() {

    @Test
    fun validateUseSuccess_ShouldUpdateTickerAndButtonPromo() {
        // Given
        val promoUiModel = PromoUiModel(
            globalSuccess = true,
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    messageUiModel = MessageUiModel(state = "green")
                )
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.updateButtonPromoCheckout(promoUiModel, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
        }
    }

    @Test
    fun validateUseNoState_ShouldShowErrorAndResetCourier() {
        // Given
        val errorMessage = "error"
        val cartString = "cart123"
        val promoCode = "promoCode123"
        val promoUiModel = PromoUiModel(
            globalSuccess = true,
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    cartStringGroup = cartString + "2",
                    code = promoCode,
                    messageUiModel = MessageUiModel(state = "green", text = errorMessage)
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "merchant",
                    cartStringGroup = cartString,
                    code = promoCode + "m",
                    messageUiModel = MessageUiModel(state = "green", text = errorMessage)
                )
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = cartString
        )
        val shipmentCartItemModel2 = ShipmentCartItemModel(
            cartStringGroup = cartString + "2"
        )
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel2, shipmentCartItemModel)

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(
            cartPosition,
            cartString,
            ValidateUsePromoRequest(),
            promoCode,
            true,
            CourierItemData()
        )

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.updateButtonPromoCheckout(promoUiModel, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.resetCourier(shipmentCartItemModel)
            view.logOnErrorApplyBo(match { it.message == "voucher order not found" }, shipmentCartItemModel, promoCode)
        }
    }

    @Test
    fun validateUseRedState_ShouldShowErrorAndResetCourier() {
        // Given
        val errorMessage = "error"
        val cartString = "cart123"
        val promoCode = "promoCode123"
        val promoUiModel = PromoUiModel(
            globalSuccess = true,
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    cartStringGroup = cartString,
                    code = promoCode,
                    messageUiModel = MessageUiModel(state = "red", text = errorMessage)
                )
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = cartString
        )
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(
            cartPosition,
            cartString,
            ValidateUsePromoRequest(),
            promoCode,
            true,
            CourierItemData()
        )

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.updateButtonPromoCheckout(promoUiModel, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(errorMessage)
            view.resetCourier(shipmentCartItemModel)
            view.logOnErrorApplyBo(match { it.message == errorMessage }, shipmentCartItemModel, promoCode)
        }
    }

    @Test
    fun validateUseRedStateOnAnotherPromo_ShouldUpdateTickerAndButtonPromo_AndKeepLastValidateUseRequest() {
        // Given
        val errorMessage = "error"
        val cartString = "cart123"
        val promoUiModel = PromoUiModel(
            globalSuccess = true,
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "logistic",
                    cartStringGroup = cartString,
                    messageUiModel = MessageUiModel(state = "green")
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    type = "global",
                    cartStringGroup = cartString,
                    messageUiModel = MessageUiModel(state = "red", text = errorMessage)
                )
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )

        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = cartString
        )
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        val cartPosition = 0
        val validateUsePromoRequest = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(
                    codes = arrayListOf("logisticpromo", "globalpromo")
                )
            )
        )
        viewModel.setLastValidateUseRequest(validateUsePromoRequest)
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", validateUsePromoRequest, "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.updateButtonPromoCheckout(promoUiModel, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
        }
        assertEquals(validateUsePromoRequest, viewModel.lastValidateUseRequest)
    }

    @Test
    fun validateUseError_ShouldShowErrorAndResetCourier() {
        // Given
        val errorMessage = "error"
        val throwable = Throwable(errorMessage)
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            throwable

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(errorMessage)
            view.showToastError(errorMessage)
            view.resetCourier(cartPosition)
            view.getShipmentCartItemModel(cartPosition)
            view.logOnErrorApplyBo(match { it.message == throwable.message }, cartPosition, "")
        }
    }

    @Test
    fun validateUseErrorWithBoCode_ShouldShowErrorResetCourierAndClearBo() {
        // Given
        val errorMessage = "error"
        val throwable = Throwable(errorMessage)
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            throwable
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                boCode = "bo123",
                cartItemModels = listOf(CartItemModel(cartStringGroup = "123"))
            )
        )
        every { view.getShipmentCartItemModel(any()) } returns viewModel.shipmentCartItemModelList[0] as ShipmentCartItemModel
        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns ClearPromoUiModel()

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(errorMessage)
            view.showToastError(errorMessage)
            view.resetCourier(cartPosition)
            view.getShipmentCartItemModel(cartPosition)
            view.logOnErrorApplyBo(match { it.message == throwable.message }, cartPosition, "")
        }
        assertEquals("", (viewModel.shipmentCartItemModelList[0] as ShipmentCartItemModel).boCode)
        assertEquals("", (viewModel.shipmentCartItemModelList[0] as ShipmentCartItemModel).boUniqueId)
    }

    @Test
    fun validateUseErrorAkamai_ShouldShowErrorAndResetCourierAndClearPromo() {
        // Given
        val errorMessage = "error"
        val akamaiErrorException = AkamaiErrorException(errorMessage)
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            akamaiErrorException

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(errorMessage)
            view.showToastError(errorMessage)
            view.resetAllCourier()
            view.doResetButtonPromoCheckout()
            view.logOnErrorApplyBo(match { it is AkamaiErrorException && it.message == errorMessage }, cartPosition, "")
        }
    }

    @Test
    fun `WHEN validate use failed with error message THEN should show error message and reset courier`() {
        // Given
        val cartPosition = 1
        val errorMessage = "error"
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "ERROR",
                message = listOf(errorMessage)
            )
        every { checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(any()) } just Runs
        mockkObject(PromoRevampAnalytics)
        every { PromoRevampAnalytics.eventCheckoutViewPromoMessage(any()) } just Runs

        // When
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(errorMessage)
            view.resetCourier(cartPosition)
            view.getShipmentCartItemModel(cartPosition)
        }
    }

    @Test
    fun `WHEN validate use failed without error message THEN should show default BO error message`() {
        // Given
        val cartPosition = 1
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "ERROR",
                message = emptyList()
            )
        every { checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(any()) } just Runs
        mockkObject(PromoRevampAnalytics)
        every { PromoRevampAnalytics.eventCheckoutViewPromoMessage(any()) } just Runs

        // When
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO)
            view.resetCourier(cartPosition)
            view.getShipmentCartItemModel(cartPosition)
        }
    }

    @Test
    fun `WHEN validate use failed with error message & cart has bo code THEN should reset courier & clear bo`() {
        // Given
        val cartPosition = 1
        val errorMessage = "error"
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "ERROR",
                message = listOf(errorMessage)
            )
        every { checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(any()) } just Runs
        mockkObject(PromoRevampAnalytics)
        every { PromoRevampAnalytics.eventCheckoutViewPromoMessage(any()) } just Runs
        every { view.getShipmentCartItemModel(any()) } returns ShipmentCartItemModel(
            cartStringGroup = "",
            boCode = "asdf",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "")
            )
        )

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns ClearPromoUiModel()

        // When
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(errorMessage)
            view.resetCourier(cartPosition)
            view.getShipmentCartItemModel(cartPosition)
        }

        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN validate use failed without error message & cart has bo code THEN should show default BO error message & clear bo`() {
        // Given
        val cartPosition = 1
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "ERROR",
                message = emptyList()
            )
        every { checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(any()) } just Runs
        mockkObject(PromoRevampAnalytics)
        every { PromoRevampAnalytics.eventCheckoutViewPromoMessage(any()) } just Runs

        every { view.getShipmentCartItemModel(any()) } returns ShipmentCartItemModel(
            cartStringGroup = "",
            boCode = "asdf",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "")
            )
        )

        coEvery { clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground() } returns ClearPromoUiModel()

        // When
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO)
            view.resetCourier(cartPosition)
            view.getShipmentCartItemModel(cartPosition)
        }

        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun validateUseSuccessFromScheduleDelivery_ShouldCompletePublisher() {
        // Given
        val promoUiModel = PromoUiModel(
            globalSuccess = true,
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )
        val cartString = "123"
        val testSubscriber = TestSubscriber.create<Boolean>()
        val donePublisher = PublishSubject.create<Boolean>()
        donePublisher.subscribe(testSubscriber)
        val shipmentScheduleDeliveryMapData = ShipmentScheduleDeliveryMapData(
            donePublisher,
            shouldStopInClearCache = false,
            shouldStopInValidateUsePromo = true
        )
        viewModel.setScheduleDeliveryMapData(cartString, shipmentScheduleDeliveryMapData)

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(cartPosition, cartString, ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.updateButtonPromoCheckout(promoUiModel, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
        }
        testSubscriber.assertCompleted()
    }

    @Test
    fun validateUseErrorFromScheduleDelivery_ShouldCompletePublisher() {
        // Given
        val errorMessage = "error"
        val throwable = Throwable(errorMessage)
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            throwable
        val cartString = "123"
        val testSubscriber = TestSubscriber.create<Boolean>()
        val donePublisher = PublishSubject.create<Boolean>()
        donePublisher.subscribe(testSubscriber)
        val shipmentScheduleDeliveryMapData = ShipmentScheduleDeliveryMapData(
            donePublisher,
            shouldStopInClearCache = false,
            shouldStopInValidateUsePromo = true
        )
        viewModel.setScheduleDeliveryMapData(cartString, shipmentScheduleDeliveryMapData)

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(cartPosition, cartString, ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(any())
            view.resetCourier(cartPosition)
            view.getShipmentCartItemModel(cartPosition)
            view.logOnErrorApplyBo(match { it.message == throwable.message }, cartPosition, "")
        }
        testSubscriber.assertCompleted()
    }

    @Test
    fun validateUseSuccessFromGetShippingRates_ShouldCompletePublisher() {
        // Given
        val promoUiModel = PromoUiModel(
            globalSuccess = true,
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
            )
        )
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns
            ValidateUsePromoRevampUiModel(
                status = "OK",
                errorCode = "200",
                promoUiModel = promoUiModel
            )
        val cartString = "123"

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(cartPosition, cartString, ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.updateButtonPromoCheckout(promoUiModel, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
        }
    }

    @Test
    fun validateUseErrorFromGetShippingRates_ShouldCompletePublisher() {
        // Given
        val errorMessage = "error"
        val throwable = Throwable(errorMessage)
        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } throws
            throwable
        val cartString = "123"

        // When
        val cartPosition = 0
        viewModel.doValidateUseLogisticPromoNew(cartPosition, cartString, ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(any())
            view.resetCourier(cartPosition)
            view.getShipmentCartItemModel(cartPosition)
            view.logOnErrorApplyBo(match { it.message == throwable.message }, cartPosition, "")
        }
    }

    @Test
    fun `WHEN validate use with detached view THEN should do nothing`() {
        // Given
        val cartPosition = 1
        viewModel.detachView()

        // When
        viewModel.doValidateUseLogisticPromoNew(cartPosition, "", ValidateUsePromoRequest(), "", true, CourierItemData())

        // Then
        verify(inverse = true) {
            view.setStateLoadingCourierStateAtIndex(any(), any())
        }
    }
}
