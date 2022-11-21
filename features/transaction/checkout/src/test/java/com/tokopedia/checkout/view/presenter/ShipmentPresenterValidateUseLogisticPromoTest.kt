package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterValidateUseLogisticPromoTest {

    @MockK
    private lateinit var validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase

    @MockK(relaxed = true)
    private lateinit var compositeSubscription: CompositeSubscription

    @MockK
    private lateinit var checkoutUseCase: CheckoutGqlUseCase

    @MockK
    private lateinit var editAddressUseCase: EditAddressUseCase

    @MockK
    private lateinit var changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase

    @MockK
    private lateinit var saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase

    @MockK
    private lateinit var getRatesUseCase: GetRatesUseCase

    @MockK
    private lateinit var getRatesApiUseCase: GetRatesApiUseCase

    @MockK
    private lateinit var getRatesWithScheduleUseCase: GetRatesWithScheduleUseCase

    @MockK
    private lateinit var clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase

    @MockK
    private lateinit var ratesStatesConverter: RatesResponseStateConverter

    @MockK
    private lateinit var shippingCourierConverter: ShippingCourierConverter

    @MockK(relaxed = true)
    private lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    private lateinit var analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    @MockK(relaxed = true)
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK(relaxed = true)
    private lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    private lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    private lateinit var getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase

    @MockK(relaxed = true)
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

    private var shipmentDataConverter = ShipmentDataConverter()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ShipmentPresenter(
                compositeSubscription, checkoutUseCase, getShipmentAddressFormV3UseCase,
                editAddressUseCase, changeShippingAddressGqlUseCase, saveShipmentStateGqlUseCase,
                getRatesUseCase, getRatesApiUseCase, clearCacheAutoApplyStackUseCase,
                ratesStatesConverter, shippingCourierConverter,
                shipmentAnalyticsActionListener, userSessionInterface, analyticsPurchaseProtection,
                checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase, prescriptionIdsUseCase,
                validateUsePromoRevampUseCase, gson, TestSchedulers, eligibleForAddressUseCase,
                getRatesWithScheduleUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun validateUseSuccess_ShouldUpdateTickerAndButtonPromo() {
        // Given
        val promoUiModel = PromoUiModel(
                globalSuccess = true,
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        errorCode = "200",
                        promoUiModel = promoUiModel
                )
        )

        // When
        val cartPosition = 0
        presenter.doValidateUseLogisticPromo(cartPosition, "", ValidateUsePromoRequest(), "", true)

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.updateButtonPromoCheckout(promoUiModel, true)
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
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", uniqueId = cartString+"2", code = promoCode, messageUiModel = MessageUiModel(state = "green", text = errorMessage)),
                        PromoCheckoutVoucherOrdersItemUiModel(type = "merchant", uniqueId = cartString, code = promoCode+"m", messageUiModel = MessageUiModel(state = "green", text = errorMessage)),
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        errorCode = "200",
                        promoUiModel = promoUiModel
                )
        )

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            this.cartString = cartString
        }
        val shipmentCartItemModel2 = ShipmentCartItemModel().apply {
            this.cartString = cartString + "2"
        }
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel2, shipmentCartItemModel)

        // When
        val cartPosition = 0
        presenter.doValidateUseLogisticPromo(cartPosition, cartString, ValidateUsePromoRequest(), promoCode, true)

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.resetCourier(shipmentCartItemModel)
            view.updateButtonPromoCheckout(promoUiModel, true)
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
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", uniqueId = cartString, code = promoCode, messageUiModel = MessageUiModel(state = "red", text = errorMessage))
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        errorCode = "200",
                        promoUiModel = promoUiModel
                )
        )

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            this.cartString = cartString
        }
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        val cartPosition = 0
        presenter.doValidateUseLogisticPromo(cartPosition, cartString, ValidateUsePromoRequest(), promoCode, true)

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(errorMessage)
            view.resetCourier(shipmentCartItemModel)
            view.updateButtonPromoCheckout(promoUiModel, true)
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
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", uniqueId = cartString, messageUiModel = MessageUiModel(state = "green")),
                        PromoCheckoutVoucherOrdersItemUiModel(type = "global", uniqueId = cartString, messageUiModel = MessageUiModel(state = "red", text = errorMessage))
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        errorCode = "200",
                        promoUiModel = promoUiModel
                )
        )

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            this.cartString = cartString
        }
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        val cartPosition = 0
        val validateUsePromoRequest = ValidateUsePromoRequest(
                orders = listOf(
                        OrdersItem(
                                codes = arrayListOf("logisticpromo", "globalpromo")
                        )
                )
        )
        presenter.setLatValidateUseRequest(validateUsePromoRequest)
        presenter.doValidateUseLogisticPromo(cartPosition, "", validateUsePromoRequest, "", true)

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.updateButtonPromoCheckout(promoUiModel, true)
        }
        assertEquals(validateUsePromoRequest, presenter.lastValidateUseRequest)
    }

    @Test
    fun validateUseError_ShouldShowErrorAndResetCourier() {
        // Given
        val errorMessage = "error"
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(Throwable(errorMessage))

        // When
        val cartPosition = 0
        presenter.doValidateUseLogisticPromo(cartPosition, "", ValidateUsePromoRequest(), "", true)

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(errorMessage)
            view.showToastError(errorMessage)
            view.resetCourier(cartPosition)
        }
    }

    @Test
    fun validateUseErrorAkamai_ShouldShowErrorAndResetCourierAndClearPromo() {
        // Given
        val errorMessage = "error"
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(AkamaiErrorException(errorMessage))

        // When
        val cartPosition = 0
        presenter.doValidateUseLogisticPromo(cartPosition, "", ValidateUsePromoRequest(), "", true)

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(errorMessage)
            view.showToastError(errorMessage)
            view.resetAllCourier()
            view.cancelAllCourierPromo()
            view.doResetButtonPromoCheckout()
        }
    }

    @Test
    fun `WHEN validate use failed with error message THEN should show error message and reset courier`() {
        // Given
        val cartPosition = 1
        val errorMessage = "error"
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "ERROR",
                        message = listOf(errorMessage)
                )
        )
        every { checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(any()) } just Runs
        mockkObject(PromoRevampAnalytics)
        every { PromoRevampAnalytics.eventCheckoutViewPromoMessage(any()) } just Runs

        // When
        presenter.doValidateUseLogisticPromo(cartPosition, "", ValidateUsePromoRequest(), "", true)

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(errorMessage)
            view.resetCourier(cartPosition)
        }
    }

    @Test
    fun `WHEN validate use failed without error message THEN should show default BO error message`() {
        // Given
        val cartPosition = 1
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "ERROR",
                        message = emptyList()
                )
        )
        every { checkoutAnalytics.eventClickLanjutkanTerapkanPromoError(any()) } just Runs
        mockkObject(PromoRevampAnalytics)
        every { PromoRevampAnalytics.eventCheckoutViewPromoMessage(any()) } just Runs

        // When
        presenter.doValidateUseLogisticPromo(cartPosition, "", ValidateUsePromoRequest(), "", true)

        // Then
        verifySequence {
            view.setStateLoadingCourierStateAtIndex(cartPosition, true)
            view.setStateLoadingCourierStateAtIndex(cartPosition, false)
            view.showToastError(DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO)
            view.resetCourier(cartPosition)
        }
    }

    @Test
    fun `WHEN validate use with detached view THEN should do nothing`() {
        // Given
        val cartPosition = 1
        presenter.detachView()

        // When
        presenter.doValidateUseLogisticPromo(cartPosition, "", ValidateUsePromoRequest(), "", true)

        // Then
        verify(inverse = true) {
            view.setStateLoadingCourierStateAtIndex(any(), any())
        }
    }
}
