package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.NewUpsellData
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.domain.model.cartshipmentform.UpsellData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnWordingData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    @MockK(relaxed = true)
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

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
            checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase, prescriptionIdsUseCase,
            validateUsePromoRevampUseCase, gson, TestSchedulers, eligibleForAddressUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun firstLoadCheckoutPage_ShouldHideInitialLoadingAndRenderPage() {
        // Given
        val data = DataProvider.provideShipmentAddressFormResponse()
        val cartShipmentAddressFormData =
            shipmentMapper.convertToShipmentAddressFormData(data.shipmentAddressFormResponse.data)
        presenter.shipmentButtonPaymentModel = ShipmentButtonPaymentModel()

        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(cartShipmentAddressFormData)
        }
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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

        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    isError = true,
                    errorMessage = errorMessage
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        val data = CartShipmentAddressFormData(
            isError = true,
            errorMessage = errorMessage,
            isOpenPrerequisiteSite = true
        )

        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(data)
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    errorCode = 0,
                    groupAddress = emptyList()
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress)
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    popUpMessage = "message"
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    popUpMessage = ""
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(data)
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(data)
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(data)
        }
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            firstArg<(KeroAddrIsEligibleForAddressFeatureData) -> Unit>().invoke(
                KeroAddrIsEligibleForAddressFeatureData()
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPageNoAddress(any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN should navigate to add new address page failed THEN should show toaster`() {
        // Given
        val data = CartShipmentAddressFormData().apply {
            errorCode = CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS
            groupAddress = listOf(
                GroupAddress().apply {
                    userAddress = UserAddress(state = UserAddress.STATE_NO_ADDRESS)
                }
            )
        }

        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(data)
        }
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.showToastError(any())
            view.stopTrace()
        }
    }

    @Test
    fun `GIVEN load checkout page with last apply WHEN get last apply THEN should return last apply data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val lastApplyUiModel = LastApplyUiModel(
            codes = listOf("promoCode")
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    lastApplyData = lastApplyUiModel
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        assertEquals(lastApplyUiModel, presenter.lastApplyData)
    }

    @Test
    fun `GIVEN load checkout page with cod data WHEN get cod data THEN should return cod data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val codData = CodModel(
            isCod = true,
            counterCod = 1
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    cod = codData
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        assertEquals(codData, presenter.codData)
    }

    @Test
    fun `GIVEN load checkout page with ineligible promo dialog data WHEN get ineligible promo dialog data THEN should return ineligible promo dialog data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val ineligiblePromoDialog = true
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    isIneligiblePromoDialogEnabled = ineligiblePromoDialog
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        assertEquals(ineligiblePromoDialog, presenter.isIneligiblePromoDialogEnabled)
    }

    @Test
    fun `GIVEN load checkout page with show onboarding data WHEN get show onboarding data THEN should return show onboarding data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val showOnboarding = true
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    isShowOnboarding = showOnboarding
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        assertEquals(showOnboarding, presenter.isShowOnboarding)
    }

    @Test
    fun `GIVEN load checkout page with pop up data WHEN load checkout page THEN should show pop up`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val popUp = PopUpData(
            title = "title",
            description = "desc"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    popup = popUp
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        verify {
            view.showPopUp(popUp)
        }
    }

    @Test
    fun `GIVEN load checkout page with pop up data without title WHEN load checkout page THEN should not show pop up`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val popUp = PopUpData(
            title = "",
            description = "desc"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    popup = popUp
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        verify(inverse = true) {
            view.showPopUp(popUp)
        }
    }

    @Test
    fun `GIVEN load checkout page with pop up data without description WHEN load checkout page THEN should not show pop up`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val popUp = PopUpData(
            title = "title",
            description = ""
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    popup = popUp
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        verify(inverse = true) {
            view.showPopUp(popUp)
        }
    }

    @Test
    fun `GIVEN load checkout page with cross sell data WHEN load checkout page THEN should set cross sell data with the right index`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val crossSell = listOf(CrossSellModel())
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    crossSell = crossSell
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        assertEquals(
            listOf(ShipmentCrossSellModel(index = 0)),
            presenter.listShipmentCrossSellModel
        )
    }

    @Test
    fun `GIVEN load checkout page with empty cross sell data WHEN load checkout page THEN should set cross sell data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val crossSell = arrayListOf<CrossSellModel>()
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    crossSell = crossSell
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        assertTrue(presenter.listShipmentCrossSellModel.size == 0)
    }

    @Test
    fun `GIVEN load checkout page with ppp data WHEN load checkout page THEN should set ppp page true and should send ppp impression`() {
        // Given
        val purchaseProtectionPlanData = PurchaseProtectionPlanData(
            isProtectionAvailable = true,
            protectionTitle = "title",
            protectionPricePerProduct = 1000
        )
        val productCatId = 1
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
            groupShop = listOf(
                GroupShop(
                    products = listOf(
                        Product(
                            purchaseProtectionPlanData = purchaseProtectionPlanData,
                            productCatId = productCatId
                        )
                    )
                )
            )
        }
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    addOnWording = AddOnWordingData()
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        verify {
            analyticsPurchaseProtection.eventImpressionOfProduct(
                any(),
                listOf("${purchaseProtectionPlanData.protectionTitle} - ${purchaseProtectionPlanData.protectionPricePerProduct} - $productCatId")
            )
        }
    }

    @Test
    fun `GIVEN null shipment button data WHEN get shipment button data THEN should return new shipment button data`() {
        // Given
        presenter.shipmentButtonPaymentModel = null

        // Then
        assertEquals(0, presenter.shipmentButtonPaymentModel.quantity)
        assertEquals("-", presenter.shipmentButtonPaymentModel.totalPrice)
    }

    @Test
    fun `GIVEN not null shipment button data WHEN get shipment button data THEN should return shipment button data`() {
        // Given
        presenter.shipmentButtonPaymentModel = ShipmentButtonPaymentModel(
            totalPrice = "Rp1.000",
            quantity = 1
        )

        // Then
        assertEquals(1, presenter.shipmentButtonPaymentModel.quantity)
        assertEquals("Rp1.000", presenter.shipmentButtonPaymentModel.totalPrice)
    }

    @Test
    fun `GIVEN null shipment cost data WHEN get shipment cost data THEN should return new shipment cost data`() {
        // Given
        presenter.shipmentCostModel = null

        // Then
        assertEquals(ShipmentCostModel(), presenter.shipmentCostModel)
    }

    @Test
    fun `GIVEN not null shipment cost data WHEN get shipment cost data THEN should return shipment cost data`() {
        // Given
        val shipmentCostModel = ShipmentCostModel(
            totalPrice = 1000.0
        )
        presenter.shipmentCostModel = shipmentCostModel

        // Then
        assertEquals(shipmentCostModel, presenter.shipmentCostModel)
    }

    @Test
    fun `GIVEN coupon state not changed WHEN get coupon state data THEN should return coupon state not changed`() {
        assertEquals(false, presenter.couponStateChanged)
    }

    @Test
    fun `GIVEN coupon state changed WHEN get coupon state data THEN should return coupon state changed`() {
        // Given
        presenter.couponStateChanged = true

        // Then
        assertEquals(true, presenter.couponStateChanged)
    }

    @Test
    fun `GIVEN null campaign timer WHEN get campaign timer data THEN should return null`() {
        // Then
        assertEquals(null, presenter.campaignTimer)
    }

    @Test
    fun `GIVEN campaign timer not show timer WHEN get campaign timer data THEN should return null`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val campaignTimerUi = CampaignTimerUi(showTimer = false)
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    campaignTimerUi = campaignTimerUi
                )
            )
        }
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        assertEquals(null, presenter.campaignTimer)
    }

    @Test
    fun `GIVEN campaign timer show timer WHEN get campaign timer data THEN should return campaign timer data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val campaignTimerUi = CampaignTimerUi(showTimer = true)
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    campaignTimerUi = campaignTimerUi
                )
            )
        }
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        assertEquals(campaignTimerUi, presenter.campaignTimer)
    }

    @Test
    fun `WHEN load checkout page get default value address state THEN should render checkout page`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress)
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress)
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    errorTicker = errorTicker,
                    groupAddress = listOf(groupAddress),
                    donation = Donation(),
                    egoldAttributes = EgoldAttributeModel()
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

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
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.showToastError(error.message)
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page with upsell THEN should have corresponding upsell model`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = UpsellData(
            isShow = true,
            title = "title",
            description = "desc",
            appLink = "applink",
            image = "image"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    upsell = upsell
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )

        // Then
        assertEquals(
            ShipmentUpsellModel(
                isShow = true,
                title = "title",
                description = "desc",
                appLink = "applink",
                image = "image"
            ),
            presenter.shipmentUpsellModel
        )
    }

    @Test
    fun `WHEN load checkout page with new upsell THEN should have corresponding new upsell model`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    newUpsell = upsell
                )
            )
        }

        // When
        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            true
        )

        // Then
        coVerify {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                true
            )
        }
        assertEquals(
            ShipmentNewUpsellModel(
                isShow = true,
                description = "desc",
                appLink = "applink",
                image = "image",
                isSelected = true,
                price = 100,
                priceWording = "Rp100",
                duration = "duration",
                summaryInfo = "wording",
                buttonText = "button"
            ),
            presenter.shipmentNewUpsellModel
        )
    }

    @Test
    fun `WHEN presenter detached THEN all usecases is unsubscribed`() {
        // When
        presenter.detachView()

        // Then
        verify {
            getShipmentAddressFormV3UseCase.cancelJobs()
            eligibleForAddressUseCase.cancelJobs()
        }
    }
}
