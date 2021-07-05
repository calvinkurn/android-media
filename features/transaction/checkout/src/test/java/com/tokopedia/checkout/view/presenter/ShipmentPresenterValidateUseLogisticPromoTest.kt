package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterValidateUseLogisticPromoTest {

    @MockK
    private lateinit var validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase

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
    private lateinit var clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase

    @MockK
    private lateinit var submitHelpTicketUseCase: SubmitHelpTicketUseCase

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
    private lateinit var getShipmentAddressFormGqlUseCase: GetShipmentAddressFormGqlUseCase

    private var shipmentDataConverter = ShipmentDataConverter()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ShipmentPresenter(
                compositeSubscription, checkoutUseCase, getShipmentAddressFormGqlUseCase,
                editAddressUseCase, changeShippingAddressGqlUseCase, saveShipmentStateGqlUseCase,
                getRatesUseCase, getRatesApiUseCase, clearCacheAutoApplyStackUseCase,
                submitHelpTicketUseCase, ratesStatesConverter, shippingCourierConverter,
                shipmentAnalyticsActionListener, userSessionInterface, analyticsPurchaseProtection,
                checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase,
                validateUsePromoRevampUseCase, gson, TestSchedulers)
        presenter.attachView(view)
    }

    @Test
    fun validateUseSuccess_ShouldUpdateTickerAndButtonPromo() {
        // Given
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        // When
        presenter.doValidateuseLogisticPromo(0, "", ValidateUsePromoRequest())

        // Then
        verify {
            view.updateButtonPromoCheckout(promoUiModel, true)
        }
    }

    @Test
    fun validateUseRedState_ShouldShowErrorAndResetCourier() {
        // Given
        val errorMessage = "error"
        val cartString = "cart123"
        val promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                        PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", uniqueId = cartString, messageUiModel = MessageUiModel(state = "red", text = errorMessage))
                )
        )
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(
                ValidateUsePromoRevampUiModel(
                        status = "OK",
                        promoUiModel = promoUiModel
                )
        )

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            this.cartString = cartString
        }
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        val cartPosition = 0
        presenter.doValidateuseLogisticPromo(cartPosition, "", ValidateUsePromoRequest())

        // Then
        verifySequence {
            view.showToastError(errorMessage)
            view.resetCourier(shipmentCartItemModel)
            view.updateButtonPromoCheckout(promoUiModel, true)
        }
    }

    @Test
    fun validateUseError_ShouldShowErrorAndResetCourier() {
        // Given
        val errorMessage = "error"
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(Throwable(errorMessage))

        // When
        val cartPosition = 0
        presenter.doValidateuseLogisticPromo(cartPosition, "", ValidateUsePromoRequest())

        // Then
        verifySequence {
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
        presenter.doValidateuseLogisticPromo(cartPosition, "", ValidateUsePromoRequest())

        // Then
        verifySequence {
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
        presenter.doValidateuseLogisticPromo(cartPosition, "", ValidateUsePromoRequest())

        // Then
        verifySequence {
            view.showToastError(errorMessage)
            view.resetCourier(cartPosition)
        }
    }

}