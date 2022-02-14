package com.tokopedia.checkout.bundle.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.bundle.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.bundle.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.bundle.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.bundle.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.bundle.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.bundle.domain.usecase.*
import com.tokopedia.checkout.bundle.view.DataProvider
import com.tokopedia.checkout.bundle.view.ShipmentContract
import com.tokopedia.checkout.bundle.view.ShipmentPresenter
import com.tokopedia.checkout.bundle.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.bundle.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.bundle.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterLoadShipmentAddressFormTest {

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
    private lateinit var clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase

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
    private lateinit var getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase

    private var shipmentDataConverter = ShipmentDataConverter()
    private var shipmentMapper = ShipmentMapper()

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

        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(cartShipmentAddressFormData)
        }
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

        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(CartShipmentAddressFormData(isError = true, errorMessage = errorMessage))
        }

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
        val data = CartShipmentAddressFormData(isError = true, errorMessage = errorMessage, isOpenPrerequisiteSite = true)

        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(data)
        }

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
        val error = RuntimeException("error")
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

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
        val error = CartResponseErrorException("error")
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.showToastError(error.message)
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page with no error and address list is empty THEN should trigger finish activity`() {
        // Given
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(CartShipmentAddressFormData(errorCode = 0, groupAddress = emptyList()))
        }

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
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(CartShipmentAddressFormData(groupAddress = listOf(groupAddress)))
        }

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
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(CartShipmentAddressFormData(groupAddress = listOf(groupAddress), popUpMessage = "message"))
        }

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
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(CartShipmentAddressFormData(groupAddress = listOf(groupAddress), popUpMessage = ""))
        }

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
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(data)
        }

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
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(data)
        }

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
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(data)
        }

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
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(CartShipmentAddressFormData(groupAddress = listOf(groupAddress)))
        }

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
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(CartShipmentAddressFormData(groupAddress = listOf(groupAddress)))
        }

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
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(CartShipmentAddressFormData(errorTicker = errorTicker,
                    groupAddress = listOf(groupAddress), donation = Donation(), egoldAttributes = EgoldAttributeModel()))
        }

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        assert(presenter.shipmentTickerErrorModel.errorMessage == errorTicker)
        assert(presenter.shipmentTickerErrorModel.isError)
        assert(!presenter.shipmentDonationModel.isEnabled)
        assert(!presenter.egoldAttributeModel.isEnabled)
    }

    @Test
    fun `WHEN reload checkout page failed THEN should render checkout page`() {
        // Given
        val error = CartResponseErrorException("error")
        coEvery { getShipmentAddressFormV3UseCase.setParams(any(), any(), any(), any(), any(), any()) } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

        // When
        presenter.processInitialLoadCheckoutPage(true, false, false, false, false, null, "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.showToastError(error.message)
            view.stopTrace()
        }
    }

}