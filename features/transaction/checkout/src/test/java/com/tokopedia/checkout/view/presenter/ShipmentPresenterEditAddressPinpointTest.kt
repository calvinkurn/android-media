package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterEditAddressPinpointTest {

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
    fun pinpointSuccess_ShouldRenderEditAddressSuccess() {
        // Given
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            addressName = "address 1"
            street = "street 1"
            postalCode = "12345"
            destinationDistrictId = "1"
            cityId = "1"
            provinceId = "1"
            recipientName = "user 1"
            recipientPhoneNumber = "1234567890"
        }

        val latitude = "123"
        val longitude = "456"

        every { editAddressUseCase.createObservable(any()) } returns Observable.just("""
            {
                "data": {
                    "is_success": 1
                }
            }
        """.trimIndent())

        // When
        presenter.editAddressPinpoint(latitude, longitude, ShipmentCartItemModel(), LocationPass())

        // Then
        verifyOrder {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.renderEditAddressSuccess(latitude, longitude)
        }
    }

    @Test
    fun pinpointFailed_ShouldNavigateToSetPinpointWithErrorMessage() {
        // Given
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            addressName = "address 1"
            street = "street 1"
            postalCode = "12345"
            destinationDistrictId = "1"
            cityId = "1"
            provinceId = "1"
            recipientName = "user 1"
            recipientPhoneNumber = "1234567890"
        }

        val latitude = "123"
        val longitude = "456"
        val locationPass = LocationPass()

        val errorMessage = "error"

        every { editAddressUseCase.createObservable(any()) } returns Observable.just("""
            {
                "data": {
                    "is_success": 0
                },
                "message_error": ["$errorMessage"]
            }
        """.trimIndent())

        // When
        presenter.editAddressPinpoint(latitude, longitude, ShipmentCartItemModel(), locationPass)

        // Then
        verifyOrder {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.navigateToSetPinpoint(errorMessage, locationPass)
        }
    }

    @Test
    fun pinpointError_ShouldShowToastError() {
        // Given
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            addressName = "address 1"
            street = "street 1"
            postalCode = "12345"
            destinationDistrictId = "1"
            cityId = "1"
            provinceId = "1"
            recipientName = "user 1"
            recipientPhoneNumber = "1234567890"
        }

        val latitude = "123"
        val longitude = "456"

        every { editAddressUseCase.createObservable(any()) } returns Observable.error(Throwable())

        // When
        presenter.editAddressPinpoint(latitude, longitude, ShipmentCartItemModel(), LocationPass())

        // Then
        verifyOrder {
            view.showLoading()
            view.setHasRunningApiCall(true)
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.showToastError(any())
        }
    }
}