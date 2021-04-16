package com.tokopedia.checkout.view.presenter

import android.app.Activity
import com.google.gson.Gson
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
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

class ShipmentPresenterValidateUseCourierPromoTest {

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
    fun `WHEN validate use success THEN should render promo from courier`() {
        // Given
        val validateUseModel = ValidateUsePromoRevampUiModel(
                status = "OK",
                promoUiModel = PromoUiModel(
                        voucherOrderUiModels = listOf(
                                PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", messageUiModel = MessageUiModel(state = "green"))
                        )
                )
        )
        val position = 0
        val noToast = true
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(validateUseModel)

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
                promoUiModel = PromoUiModel(
                        voucherOrderUiModels = listOf(
                                PromoCheckoutVoucherOrdersItemUiModel(type = "logistic", uniqueId = cartString, messageUiModel = MessageUiModel(state = "red", text = errorMessage))
                        )
                )
        )
        val position = 0
        val noToast = true

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            this.cartString = cartString
        }
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(validateUseModel)
        every { view.generateValidateUsePromoRequest() } returns ValidateUsePromoRequest()

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verifySequence {
            view.generateValidateUsePromoRequest()
            view.showToastError(errorMessage)
            view.resetCourier(shipmentCartItemModel)
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
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.just(validateUseModel)

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verify {
            view.renderErrorCheckPromoShipmentData(errorMessage)
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
        mockkStatic(ErrorHandler::class)
        every { view.activityContext } returns mockContext
        every { ErrorHandler.getErrorMessage(mockContext, exception) } returns errorMessage
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(exception)

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
        every { validateUsePromoRevampUseCase.createObservable(any()) } returns Observable.error(exception)
        every { view.generateValidateUsePromoRequest() } returns ValidateUsePromoRequest()

        // When
        presenter.processCheckPromoCheckoutCodeFromSelectedCourier("code", position, noToast)

        // Then
        verifySequence {
            view.generateValidateUsePromoRequest()
            view.showToastError(errorMessage)
            view.resetAllCourier()
            view.cancelAllCourierPromo()
            view.doResetButtonPromoCheckout()
        }
    }

}