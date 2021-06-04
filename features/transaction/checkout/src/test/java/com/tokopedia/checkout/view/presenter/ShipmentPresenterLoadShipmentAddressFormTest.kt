package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription
import java.io.IOException

class ShipmentPresenterLoadShipmentAddressFormTest {

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
    private var shipmentMapper = ShipmentMapper()

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
    fun firstLoadCheckoutPage_ShouldHideInitialLoadingAndRenderPage() {
        // Given
        val data = DataProvider.provideShipmentAddressFormResponse()
        val cartShipmentAddressFormData = shipmentMapper.convertToShipmentAddressFormData(data.shipmentAddressFormResponse.data)
        presenter.shipmentButtonPaymentModel = ShipmentButtonPaymentModel()

        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(cartShipmentAddressFormData)
        every { shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(any()) } just Runs

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.renderCheckoutPage(any(), any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun firstLoadCheckoutPageError_ShouldHideInitialLoadingAndShowToastError() {
        // Given
        val errorMessage = "error"
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(CartShipmentAddressFormData(isError = true, errorMessage = errorMessage))

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.showToastError(errorMessage)
        }
    }

    @Test
    fun firstLoadCheckoutPageErrorPrerequisite_ShouldHideInitialLoadingAndShowCacheExpired() {
        // Given
        val errorMessage = "error"
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(CartShipmentAddressFormData(isError = true, errorMessage = errorMessage, isOpenPrerequisiteSite = true))

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.onCacheExpired(errorMessage)
        }
    }

    @Test
    fun firstLoadCheckoutPageFailedException_ShouldHideInitialLoadingAndShowToastError() {
        // Given
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.error(IOException())

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.showToastError(any())
            view.stopTrace()
        }
    }

    @Test
    fun firstLoadCheckoutPageFailedCartException_ShouldHideInitialLoadingAndShowToastError() {
        // Given
        val errorMessage = "error"
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.error(CartResponseErrorException(errorMessage))

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.showToastError(errorMessage)
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page and data is null THEN should trigger finish activity`() {
        // Given
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(null)

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.onShipmentAddressFormEmpty()
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page with no error and address list is empty THEN should trigger finish activity`() {
        // Given
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(CartShipmentAddressFormData(errorCode = 0, groupAddress = emptyList()))

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.onShipmentAddressFormEmpty()
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get state address id match THEN should render checkout page`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = UserAddress.STATE_CHOSEN_ADDRESS_MATCH)
        }
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(CartShipmentAddressFormData(groupAddress = listOf(groupAddress)))

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPage(any(), any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get state address id not match THEN should render checkout page and show toaster`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = UserAddress.STATE_ADDRESS_ID_NOT_MATCH)
        }
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(CartShipmentAddressFormData(groupAddress = listOf(groupAddress), popUpMessage = "message"))

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.updateLocalCacheAddressData(any())
            view.renderCheckoutPage(any(), any(), any())
            view.showToastNormal(any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get state address id not match but no toaster message THEN should render checkout page and don't show toaster`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = UserAddress.STATE_ADDRESS_ID_NOT_MATCH)
        }
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(CartShipmentAddressFormData(groupAddress = listOf(groupAddress), popUpMessage = ""))

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.updateLocalCacheAddressData(any())
            view.renderCheckoutPage(any(), any(), any())
            view.stopTrace()
        }

        verify(inverse = true) {
            view.showToastNormal(any())
        }
    }

    @Test
    fun `WHEN load checkout page get error code open address list THEN should navigate to address list page`() {
        // Given
        val data = CartShipmentAddressFormData().apply {
            errorCode = CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST
            groupAddress = listOf(
                    GroupAddress().apply {
                        userAddress = UserAddress(state = UserAddress.STATE_DISTRICT_ID_NOT_MATCH)
                    }
            )
        }

        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(data)

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPageNoMatchedAddress(any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get error code open address list and group address is empty THEN should navigate to address list page with default address state`() {
        // Given
        val defaultAddressState = 0
        val data = CartShipmentAddressFormData().apply {
            errorCode = CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST
            groupAddress = listOf()
        }

        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(data)

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPageNoMatchedAddress(any(), defaultAddressState)
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get state no address THEN should navigate to add new address page`() {
        // Given
        val data = CartShipmentAddressFormData().apply {
            errorCode = CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS
            groupAddress = listOf(
                    GroupAddress().apply {
                        userAddress = UserAddress(state = UserAddress.STATE_NO_ADDRESS)
                    }
            )
        }

        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(data)

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPageNoAddress(any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get default value address state THEN should render checkout page`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(CartShipmentAddressFormData(groupAddress = listOf(groupAddress)))

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPage(any(), any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN reload checkout page success THEN should render checkout page`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(CartShipmentAddressFormData(groupAddress = listOf(groupAddress)))

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPage(any(), any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN reload checkout page success with error ticker THEN should initialize shipment ticker error model with error ticker and other data is disabled`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val errorTicker = "error ticker message"
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.just(CartShipmentAddressFormData(errorTicker = errorTicker,
                groupAddress = listOf(groupAddress), donation = Donation(), egoldAttributes = EgoldAttributeModel()))

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        assertEquals(errorTicker, presenter.shipmentTickerErrorModel.errorMessage)
        assertEquals(true, presenter.shipmentTickerErrorModel.isError)
        assertEquals(false, presenter.recipientAddressModel.isEnabled)
        assertEquals(false, presenter.shipmentDonationModel.isEnabled)
        assertEquals(false, presenter.egoldAttributeModel.isEnabled)
    }

    @Test
    fun `WHEN reload checkout page failed THEN should render checkout page`() {
        // Given
        val errorMessage = "error"
        every { getShipmentAddressFormGqlUseCase.createObservable(any()) } returns Observable.error(CartResponseErrorException(errorMessage))

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.showToastError(errorMessage)
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN generate shipment address form request with corner address THEN params should contains corner id`() {
        // Given
        val cornerId = "123"

        // When
        val params = presenter.generateShipmentAddressFormParams(true, false, false, cornerId, null, null)

        // Then
        assert(params[GetShipmentAddressFormGqlUseCase.PARAM_KEY_CORNER_ID] == cornerId.toInt())
    }

    @Test
    fun `WHEN generate shipment address form request with invalid corner address THEN params should not contains corner id`() {
        // Given
        val cornerId = "abc"

        // When
        val params = presenter.generateShipmentAddressFormParams(true, false, false, cornerId, null, null)

        // Then
        assert(params[GetShipmentAddressFormGqlUseCase.PARAM_KEY_CORNER_ID] == null)
    }

    @Test
    fun `WHEN generate shipment address form request for leasing flow THEN params should contains leasing id`() {
        // Given
        val leasingId = "123"

        // When
        val params = presenter.generateShipmentAddressFormParams(true, false, false, null, null, leasingId)

        // Then
        assert(params[GetShipmentAddressFormGqlUseCase.PARAM_KEY_VEHICLE_LEASING_ID] == leasingId.toInt())
    }

    @Test
    fun `WHEN generate shipment address form request for leasing flow with invalid leasing id THEN params should not contains leasing id`() {
        // Given
        val leasingId = "abc"

        // When
        val params = presenter.generateShipmentAddressFormParams(true, false, false, null, null, leasingId)

        // Then
        assert(params[GetShipmentAddressFormGqlUseCase.PARAM_KEY_VEHICLE_LEASING_ID] == null)
    }

    @Test
    fun `WHEN generate shipment address form request for trade in flow THEN params should contains trade in data`() {
        // Given
        val deviceId = "123"

        // When
        val params = presenter.generateShipmentAddressFormParams(true, true, false, null, deviceId, null)

        // Then
        assert(params[GetShipmentAddressFormGqlUseCase.PARAM_KEY_IS_TRADEIN] == true)
        assert(params[GetShipmentAddressFormGqlUseCase.PARAM_KEY_DEVICE_ID] == deviceId)
    }
}