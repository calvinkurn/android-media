package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.api.CommonPurchaseApiUrl
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.model.checkout.ErrorReporter
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.checkout.data.model.request.checkout.DataCheckoutRequest
import com.tokopedia.purchase_platform.common.feature.helpticket.data.request.SubmitHelpTicketRequest
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterHelpTicketTest {

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

    @MockK
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK
    private lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    private lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    private lateinit var getShipmentAddressFormGqlUseCase: GetShipmentAddressFormGqlUseCase

    private var shipmentDataConverter = ShipmentDataConverter()

    private val gson = Gson()

    private lateinit var presenter: ShipmentPresenter

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
    fun show_error_reporter_dialog() {
        // Given
        val data = CheckoutData().apply {
            isError = true
            errorReporter = ErrorReporter(eligible = true)
        }
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(data)
        presenter.shipmentCartItemModelList = emptyList()
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))

        // When
        presenter.processCheckout(false, false, false, "", "", "")

        // Then
        verify(exactly = 1) {
            view.renderCheckoutCartErrorReporter(data)
        }
    }

    @Test
    fun submitHelpTicketSuccess() {
        // Given
        val result = SubmitTicketResult(status = true)
        every {
            submitHelpTicketUseCase.createObservable(match {
                val request = it.getObject(SubmitHelpTicketUseCase.PARAM) as SubmitHelpTicketRequest
                request.page == SubmitHelpTicketUseCase.PAGE_CHECKOUT && request.requestUrl == CommonPurchaseApiUrl.PATH_CHECKOUT
            })
        } returns Observable.just(result)

        // When
        presenter.processSubmitHelpTicket(CheckoutData().apply {
            jsonResponse = ""
            errorMessage = ""
            errorReporter = ErrorReporter()
        })

        // Then
        verify(exactly = 1) {
            view.renderSubmitHelpTicketSuccess(result)
        }
    }

    @Test
    fun submitHelpTicketError() {
        // Given
        val responseErrorMessage = "something wrong"
        every {
            submitHelpTicketUseCase.createObservable(match {
                val request = it.getObject(SubmitHelpTicketUseCase.PARAM) as SubmitHelpTicketRequest
                request.page == SubmitHelpTicketUseCase.PAGE_CHECKOUT && request.requestUrl == CommonPurchaseApiUrl.PATH_CHECKOUT
            })
        } returns Observable.just(SubmitTicketResult(status = false, message = responseErrorMessage))

        // When
        presenter.processSubmitHelpTicket(CheckoutData().apply {
            jsonResponse = ""
            errorMessage = ""
            errorReporter = ErrorReporter()
        })

        // Then
        verify(exactly = 1) {
            view.showToastError(responseErrorMessage)
        }
    }

    @Test
    fun submitHelpTicketFailed() {
        // Given
        every {
            submitHelpTicketUseCase.createObservable(match {
                val request = it.getObject(SubmitHelpTicketUseCase.PARAM) as SubmitHelpTicketRequest
                request.page == SubmitHelpTicketUseCase.PAGE_CHECKOUT && request.requestUrl == CommonPurchaseApiUrl.PATH_CHECKOUT
            })
        } returns Observable.error(Exception())

        // When
        presenter.processSubmitHelpTicket(CheckoutData().apply {
            jsonResponse = ""
            errorMessage = ""
            errorReporter = ErrorReporter()
        })

        // Then
        verify(exactly = 1) {
            view.showToastError(any())
        }
    }
}