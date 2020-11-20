package com.tokopedia.checkout.view.presenter

import android.app.Activity
import com.google.gson.Gson
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.model.checkout.ErrorReporter
import com.tokopedia.checkout.domain.model.checkout.MessageData
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticdata.domain.usecase.EditAddressUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.checkout.request.DataCheckoutRequest
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription
import java.io.IOException

class ShipmentPresenterCheckoutTest {

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
    private lateinit var codCheckoutUseCase: CodCheckoutUseCase

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
    private lateinit var codAnalytics: CodAnalytics

    @MockK
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK
    private lateinit var getInsuranceCartUseCase: GetInsuranceCartUseCase

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
        presenter = ShipmentPresenter(compositeSubscription,
                checkoutUseCase, getShipmentAddressFormGqlUseCase,
                editAddressUseCase, changeShippingAddressGqlUseCase,
                saveShipmentStateGqlUseCase,
                getRatesUseCase, getRatesApiUseCase,
                codCheckoutUseCase, clearCacheAutoApplyStackUseCase, submitHelpTicketUseCase,
                ratesStatesConverter, shippingCourierConverter, shipmentAnalyticsActionListener, userSessionInterface,
                analyticsPurchaseProtection, codAnalytics, checkoutAnalytics,
                getInsuranceCartUseCase, shipmentDataConverter, releaseBookingUseCase,
                validateUsePromoRevampUseCase, gson, TestSchedulers)
        presenter.attachView(view)
    }

    @Test
    fun checkoutSuccess_ShouldGoToPaymentPage() {
        // Given
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        presenter.dataCheckoutRequestList = listOf(DataCheckoutRequest())

        val transactionId = "1234"
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(CheckoutData().apply {
            this.transactionId = transactionId
        })

        // When
        presenter.processCheckout(false, false, false, false, "", "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(transactionId, null, 0, null)
            view.renderCheckoutCartSuccess(any())
        }
    }

    @Test
    fun checkoutErrorEmptyRequest_ShouldShowError() {
        // Given
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        presenter.dataCheckoutRequestList = emptyList()

        val mockContext = mockk<Activity>()
        val errorMessage = "error"
        every { mockContext.getString(R.string.message_error_checkout_empty) } returns errorMessage
        every { view.activityContext } returns mockContext

        // When
        presenter.processCheckout(false, false, false, false, "", "", "")

        // Then
        verifyOrder {
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastError(errorMessage)
        }
    }

    @Test
    fun checkoutErrorNullRequest_ShouldShowError() {
        // Given
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        presenter.dataCheckoutRequestList = null

        val mockContext = mockk<Activity>()
        val errorMessage = "error"
        every { mockContext.getString(R.string.default_request_error_unknown_short) } returns errorMessage
        every { mockContext.getString(R.string.message_error_checkout_empty) } returns errorMessage
        every { view.activityContext } returns mockContext

        // When
        presenter.processCheckout(false, false, false, false, "", "", "")

        // Then
        verifyOrder {
            view.showToastError(errorMessage) // weird ?
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastError(errorMessage)
        }
    }

    @Test
    fun checkoutFailedPriceValidation_ShouldRenderCheckoutPriceUpdate() {
        // Given
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        presenter.dataCheckoutRequestList = listOf(DataCheckoutRequest())

        val priceValidationData = PriceValidationData().apply {
            isUpdated = true
            message = MessageData()
        }
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(CheckoutData().apply {
            this.isError = true
            this.priceValidationData = priceValidationData
        })

        // When
        presenter.processCheckout(false, false, false, false, "", "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.renderCheckoutPriceUpdated(priceValidationData)
        }
    }

    @Test
    fun checkoutFailedErrorReporter_ShouldRenderErrorReporter() {
        // Given
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        presenter.dataCheckoutRequestList = listOf(DataCheckoutRequest())

        val errorReporter = ErrorReporter().apply {
            eligible = true
        }
        val checkoutData = CheckoutData().apply {
            this.isError = true
            this.errorReporter = errorReporter
        }
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(checkoutData)

        // When
        presenter.processCheckout(false, false, false, false, "", "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.renderCheckoutCartErrorReporter(checkoutData)
        }
    }

    @Test
    fun checkoutFailed_ShouldShowErrorAndReloadPage() {
        // Given
        presenter.shipmentCartItemModelList = listOf(ShipmentCartItemModel().apply {
            cartItemModels = listOf(CartItemModel())
        })
        presenter.dataCheckoutRequestList = listOf(DataCheckoutRequest())

        every { view.activityContext } returns null
        every { checkoutUseCase.createObservable(any()) } returns Observable.error(IOException())

        // When
        presenter.processCheckout(false, false, false, false, "0", "0", "0")

        // Then
        verifyOrder {
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastError(any())
            getShipmentAddressFormGqlUseCase.createObservable(any())
        }
    }

    @Test
    fun `WHEN generate checkout request with applied promo THEN request should contains promo data`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        presenter.validateUsePromoRevampUiModel = ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        dataCheckoutRequest.shopProducts[0].cartString = "239594-0-301643"
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)

        // When
        val checkoutRequest = presenter.generateCheckoutRequest(null, false, 0, "")

        // Then
        assert(checkoutRequest.promos.isNotEmpty())
        assert(checkoutRequest.promoCodes.isNotEmpty())
    }

    @Test
    fun `WHEN generate checkout request with insurance THEN request should contains insurance flag true`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)

        // When
        val checkoutRequest = presenter.generateCheckoutRequest(null, true, 0, "")

        // Then
        assert(checkoutRequest.hasMacroInsurance)
    }

    @Test
    fun `WHEN generate checkout request with donation THEN request should contains donation flag true`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)

        // When
        val checkoutRequest = presenter.generateCheckoutRequest(null, false, 1, "")

        // Then
        assert(checkoutRequest.isDonation == 1)
    }

    @Test
    fun `WHEN generate checkout request with egold THEN request should contains egold data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        val egoldAmount = 10000L
        presenter.egoldAttributeModel = EgoldAttributeModel().apply {
            isChecked = true
            isEligible = true
            buyEgoldValue = egoldAmount
        }

        // When
        val checkoutRequest = presenter.generateCheckoutRequest(null, false, 0, "")

        // Then
        assert(checkoutRequest.egoldData.isEgold)
        assert(checkoutRequest.egoldData.egoldAmount == egoldAmount)
    }

    @Test
    fun `WHEN generate checkout request with corner address THEN request should contains corner address data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        val tmpUserCornerId = "123"
        val tmpCornerId = "456"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            isCornerAddress = true
            userCornerId = tmpUserCornerId
            cornerId = tmpCornerId
        }

        // When
        val checkoutRequest = presenter.generateCheckoutRequest(null, false, 0, "")

        // Then
        assert(checkoutRequest.cornerData.isTokopediaCorner)
        assert(checkoutRequest.cornerData.cornerId == tmpCornerId.toInt())
        assert(checkoutRequest.cornerData.userCornerId == tmpUserCornerId)
    }

    @Test
    fun `WHEN generate checkout params trade in laku 6 THEN request should contains trade in laku 6 data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        val deviceId = "12345"
        val checkoutRequest = presenter.generateCheckoutRequest(null, false, 0, "")

        // When
        val checkoutParams = presenter.generateCheckoutParams(true, true, false, deviceId, checkoutRequest)

        // Then
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_IS_TRADE_IN] == true)
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_IS_TRADE_IN_DROP_OFF] == false)
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_DEV_ID] == deviceId)
    }

    @Test
    fun `WHEN generate checkout params trade in indopaket THEN request should contains trade in indopaket data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)
        val deviceId = "12345"
        val checkoutRequest = presenter.generateCheckoutRequest(null, false, 0, "")

        // When
        val checkoutParams = presenter.generateCheckoutParams(true, true, true, deviceId, checkoutRequest)

        // Then
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_IS_TRADE_IN] == true)
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_IS_TRADE_IN_DROP_OFF] == true)
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_DEV_ID] == deviceId)
    }

    @Test
    fun `WHEN checkout multiple product with one error product THEN should checkout success`() {
        // Given
        val shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
                ShipmentCartItemModel().apply {
                    cartItemModels = ArrayList<CartItemModel>()
                    cartItemModels.add(
                            CartItemModel().apply {
                                isError = false
                            }
                    )
                }
        )
        shipmentCartItemModelList.add(
                ShipmentCartItemModel().apply {
                    cartItemModels = ArrayList<CartItemModel>()
                    cartItemModels.add(
                            CartItemModel().apply {
                                isError = true
                            }
                    )
                }
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.dataCheckoutRequestList = listOf(dataCheckoutRequest)

        val transactionId = "1234"
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(CheckoutData().apply {
            this.transactionId = transactionId
        })
        every { view.generateNewCheckoutRequest(any(), any()) } returns listOf(dataCheckoutRequest)

        // When
        presenter.processCheckout(false, false, false, false, "", "", "")

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(transactionId, null, 0, null)
            view.renderCheckoutCartSuccess(any())
        }
    }

}