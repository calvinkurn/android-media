package com.tokopedia.checkout.view.presenter

import android.app.Activity
import com.google.gson.Gson
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.model.request.checkout.FEATURE_TYPE_TOKONOW_PRODUCT
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellItemRequestModel
import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.NewUpsellData
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.model.checkout.MessageData
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData
import com.tokopedia.checkout.domain.model.checkout.Prompt
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.utils.CheckoutFingerprintUtil
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.fingerprint.util.FingerPrintUtil
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain.UpdateDynamicDataPassingUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnProductItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnBottomSheetResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.ProductResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription
import java.io.IOException
import java.security.PublicKey

class ShipmentPresenterCheckoutTest {

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

    @MockK(relaxed = true)
    private lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    private lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

    @MockK
    private lateinit var updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    private lateinit var getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase

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
            updateDynamicDataPassingUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun checkoutSuccess_ShouldGoToPaymentPage() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                this.transactionId = transactionId
            }
        )

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
                transactionId,
                "",
                0,
                ""
            )
            view.renderCheckoutCartSuccess(any())
        }
    }

    @Test
    fun checkoutErrorEmptyRequest_ShouldShowError() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(emptyList())
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val mockContext = mockk<Activity>()
        val errorMessage = "error"
        every { mockContext.getString(R.string.message_error_checkout_empty) } returns errorMessage
        every { view.activityContext } returns mockContext

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(null)
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val mockContext = mockk<Activity>()
        val errorMessage = "error"
        every { mockContext.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown_short) } returns errorMessage
        every { mockContext.getString(R.string.message_error_checkout_empty) } returns errorMessage
        every { view.activityContext } returns mockContext

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val priceValidationData = PriceValidationData().apply {
            isUpdated = true
            message = MessageData()
        }
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                this.isError = true
                this.priceValidationData = priceValidationData
            }
        )

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.renderCheckoutPriceUpdated(priceValidationData)
        }
    }

    @Test
    fun `WHEN checkout failed with error message from backend THEN should show error and reload page`() {
        // Given
        val errorMessage = "backend error message"
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        every { view.activityContext } returns null
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                isError = true
                this.errorMessage = errorMessage
            }
        )

        // When
        presenter.processCheckout(false, false, false, "0", "0", "0", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            shipmentAnalyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(errorMessage)
            view.hideLoading()
            view.renderCheckoutCartError(errorMessage)
            getShipmentAddressFormV3UseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN checkout failed without error message from backend THEN should show error and reload page`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val mockContext = mockk<Activity>()
        every { view.activityContext } returns mockContext
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                isError = true
            }
        )
        val errorMessage = "error"
        every { mockContext.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown) } returns errorMessage

        // When
        presenter.processCheckout(false, false, false, "0", "0", "0", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            shipmentAnalyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(any())
            view.hideLoading()
            view.renderCheckoutCartError(any())
            getShipmentAddressFormV3UseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN checkout failed with exception THEN should show error and reload page`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        every { view.activityContext } returns null
        every { checkoutUseCase.createObservable(any()) } returns Observable.error(IOException())

        // When
        presenter.processCheckout(false, false, false, "0", "0", "0", false)

        // Then
        verifyOrder {
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastError(any())
            getShipmentAddressFormV3UseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN generate checkout request with applied promo THEN request should contains promo data`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        presenter.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // Then
        assert(checkoutRequest.promos?.isNotEmpty() == true)
        assert(checkoutRequest.promoCodes?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN generate checkout request with donation THEN request should contains donation flag true`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.listShipmentCrossSellModel = arrayListOf()

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 1, arrayListOf(), "", arrayListOf())

        // Then
        assert(checkoutRequest.isDonation == 1)
    }

    @Test
    fun `WHEN generate checkout request with egold THEN request should contains egold data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        val egoldAmount = 10000L
        presenter.egoldAttributeModel = EgoldAttributeModel().apply {
            isChecked = true
            isEligible = true
            buyEgoldValue = egoldAmount
        }

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // Then
        assert(checkoutRequest.egoldData?.isEgold == true)
        assert(checkoutRequest.egoldData?.egoldAmount == egoldAmount)
    }

    @Test
    fun `WHEN generate checkout request with corner address THEN request should contains corner address data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        val tmpUserCornerId = "123"
        val tmpCornerId = "456"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            isCornerAddress = true
            userCornerId = tmpUserCornerId
            cornerId = tmpCornerId
        }

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // Then
        assert(checkoutRequest.cornerData?.isTokopediaCorner == true)
        assert(checkoutRequest.cornerData?.cornerId == tmpCornerId.toLongOrZero())
        assert(checkoutRequest.cornerData?.userCornerId == tmpUserCornerId)
    }

    @Test
    fun `WHEN generate checkout params trade in laku 6 THEN request should contains trade in laku 6 data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        val deviceId = "12345"
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // When
        val checkoutParams =
            presenter.generateCheckoutParams(
                true,
                true,
                false,
                deviceId,
                checkoutRequest,
                ""
            )

        // Then
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_IS_TRADE_IN] == true)
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_IS_TRADE_IN_DROP_OFF] == false)
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_DEV_ID] == deviceId)
    }

    @Test
    fun `WHEN generate checkout params trade in indopaket THEN request should contains trade in indopaket data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        val deviceId = "12345"
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // When
        val checkoutParams =
            presenter.generateCheckoutParams(
                true,
                true,
                true,
                deviceId,
                checkoutRequest,
                ""
            )

        // Then
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_IS_TRADE_IN] == true)
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_IS_TRADE_IN_DROP_OFF] == true)
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_DEV_ID] == deviceId)
    }

    @Test
    fun `WHEN generate checkout params with fingerprint supported and fingerprint enabled THEN request should contains fingerprint data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        val deviceId = "12345"
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        val mockContext = mockk<Activity>()
        mockkObject(FingerPrintUtil)
        mockkObject(CheckoutFingerprintUtil)
        val fingerprintString = "abc"
        val publicKey = object : PublicKey {
            override fun getAlgorithm(): String {
                return ""
            }

            override fun getFormat(): String {
                return ""
            }

            override fun getEncoded(): ByteArray {
                return ByteArray(0)
            }
        }
        every { view.activityContext } returns mockContext
        every { CheckoutFingerprintUtil.getEnableFingerprintPayment(mockContext) } returns true
        every { CheckoutFingerprintUtil.getFingerprintPublicKey(mockContext) } returns publicKey
        every { FingerPrintUtil.getPublicKey(publicKey) } returns fingerprintString

        // When
        val checkoutParams =
            presenter.generateCheckoutParams(
                true,
                true,
                false,
                deviceId,
                checkoutRequest,
                ""
            )

        // Then
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_FINGERPRINT_PUBLICKEY] == fingerprintString)
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_FINGERPRINT_SUPPORT] == "true")
    }

    @Test
    fun `WHEN generate checkout params with fingerprint supported and fingerprint disabled THEN request should contains fingerprint data`() {
        // Given
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        val deviceId = "12345"
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        val mockContext = mockk<Activity>()
        mockkObject(FingerPrintUtil)
        mockkObject(CheckoutFingerprintUtil)
        val fingerprintString = "abc"
        val publicKey = null
        every { view.activityContext } returns mockContext
        every { CheckoutFingerprintUtil.getEnableFingerprintPayment(mockContext) } returns true
        every { CheckoutFingerprintUtil.getFingerprintPublicKey(mockContext) } returns publicKey

        // When
        val checkoutParams =
            presenter.generateCheckoutParams(
                true,
                true,
                false,
                deviceId,
                checkoutRequest,
                ""
            )

        // Then
        assert(checkoutParams[CheckoutGqlUseCase.PARAM_FINGERPRINT_SUPPORT] == "false")
    }

    @Test
    fun `WHEN checkout multiple product with one error product THEN should checkout success`() {
        // Given
        val shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartItemModels = arrayListOf(
                    CartItemModel().apply {
                        isError = false
                    }
                )
            }
        )
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartItemModels = arrayListOf(
                    CartItemModel().apply {
                        isError = true
                    }
                )
            }
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                this.transactionId = transactionId
            }
        )
        every { view.generateNewCheckoutRequest(any(), any()) } returns listOf(dataCheckoutRequest)

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
                transactionId,
                "",
                0,
                ""
            )
            view.renderCheckoutCartSuccess(any())
        }
    }

    @Test
    fun `WHEN checkout multiple products with two non-error product THEN should checkout only non-error products`() {
        val slot = CapturingSlot<ArrayList<ShipmentCartItemModel>>()

        // Given
        val shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartItemModels = arrayListOf(
                    CartItemModel().apply {
                        isError = false
                    }
                )
            }
        )
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartItemModels = arrayListOf(
                    CartItemModel().apply {
                        isError = true
                    },
                    CartItemModel().apply {
                        isError = false
                    }
                )
            }
        )
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartItemModels = arrayListOf(
                    CartItemModel().apply {
                        isError = true
                        isShopError = true
                    }
                )
                isAllItemError = true
            }
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                this.transactionId = transactionId
            }
        )
        every { view.generateNewCheckoutRequest(capture(slot), any()) } returns listOf(
            dataCheckoutRequest
        )

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

        // Then
        assertEquals(2, slot.captured.size)
        assertEquals(1, slot.captured[0].cartItemModels.filter { !it.isError }.size)
        assertEquals(1, slot.captured[1].cartItemModels.filter { !it.isError }.size)
    }

    @Test
    fun `WHEN checkout with donation checked success THEN should go to payment page`() {
        // Given
        presenter.shipmentDonationModel = ShipmentDonationModel(isChecked = true)
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                this.transactionId = transactionId
            }
        )

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
                transactionId,
                "",
                0,
                ""
            )
            view.renderCheckoutCartSuccess(any())
        }
    }

    @Test
    fun `WHEN checkout with donation unchecked success THEN should go to payment page`() {
        // Given
        presenter.shipmentDonationModel = ShipmentDonationModel(isChecked = false)
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                this.transactionId = transactionId
            }
        )

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
                transactionId,
                "",
                0,
                ""
            )
            view.renderCheckoutCartSuccess(any())
        }
    }

    @Test
    fun `WHEN checkout with purchase protection checked is success THEN should go to payment page`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.setPurchaseProtection(true)
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                this.transactionId = transactionId
            }
        )

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
                transactionId,
                "",
                0,
                ""
            )
            analyticsPurchaseProtection.eventClickOnBuy(any(), any())
            view.renderCheckoutCartSuccess(any())
        }
    }

    @Test
    fun `WHEN checkout error with prompt THEN should show prompt`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)

        val prompt = Prompt().apply {
            eligible = true
            title = "Title"
            description = "Description"
        }
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                this.isError = true
                this.prompt = prompt
            }
        )

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.renderPrompt(prompt)
        }
    }

    @Test
    fun `WHEN update addOn product level data bottomsheet`() {
        // Given
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartItemModels = arrayListOf(
                    CartItemModel().apply {
                        cartId = 88
                        cartString = "239594-0-301643"
                    }
                )
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-88"
            }
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        presenter.updateAddOnProductLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

        // Then
        verify {
            view.updateAddOnsData(AddOnsDataModel(), 0)
        }
    }

    @Test
    fun `WHEN update addOn order level data bottomsheet`() {
        // Given
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartString = "239594-0-301643"
                addOnsOrderLevelModel = AddOnsDataModel()
            }
        )

        val productResultList = arrayListOf<ProductResult>()
        productResultList.add(
            ProductResult().apply {
                productImageUrl = "https://images.tokopedia.net/img/android/product_icon.jpeg"
                productName = "testProductName"
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-0"
                addOnBottomSheet = AddOnBottomSheetResult().apply {
                    products = productResultList
                }
            }
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        presenter.updateAddOnOrderLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

        val productList = arrayListOf<AddOnProductItemModel>()
        productList.add(
            AddOnProductItemModel().apply {
                productImageUrl = "https://images.tokopedia.net/img/android/product_icon.jpeg"
                productName = "testProductName"
            }
        )
        val addOnsDataModel = AddOnsDataModel().apply {
            addOnsBottomSheetModel = AddOnBottomSheetModel().apply {
                products = productList
            }
        }

        // Then
        verify {
            view.updateAddOnsData(addOnsDataModel, 1)
        }
    }

    @Test
    fun `WHEN update addOn order level data bottomsheet with AddOnData`() {
        // Given
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartString = "239594-0-301643"
                addOnsOrderLevelModel = AddOnsDataModel()
            }
        )

        val productResultList = arrayListOf<ProductResult>()
        productResultList.add(
            ProductResult().apply {
                productImageUrl = "https://images.tokopedia.net/img/android/product_icon.jpeg"
                productName = "testProductName"
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-0"
                addOnBottomSheet = AddOnBottomSheetResult().apply {
                    products = productResultList
                }
                addOnData = listOf(
                    AddOnData()
                )
            }
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        presenter.updateAddOnOrderLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

        val productList = arrayListOf<AddOnProductItemModel>()
        productList.add(
            AddOnProductItemModel().apply {
                productImageUrl = "https://images.tokopedia.net/img/android/product_icon.jpeg"
                productName = "testProductName"
            }
        )
        val addOnsDataModel = AddOnsDataModel().apply {
            addOnsBottomSheetModel = AddOnBottomSheetModel().apply {
                products = productList
            }
            addOnsDataItemModelList = listOf(
                AddOnDataItemModel()
            )
        }

        // Then
        verify {
            view.updateAddOnsData(addOnsDataModel, 1)
        }
    }

    @Test
    fun checkoutSuccess_CrossSellModelChecked() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
            }
        )
        presenter.setDataCheckoutRequestList(listOf(DataCheckoutRequest()))
        presenter.listShipmentCrossSellModel = arrayListOf(
            ShipmentCrossSellModel().apply {
                isChecked = true
                crossSellModel = CrossSellModel()
            }
        )
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        presenter.setUploadPrescriptionData(uploadModel)
        val transactionId = "1234"
        every { checkoutUseCase.createObservable(any()) } returns Observable.just(
            CheckoutData().apply {
                this.transactionId = transactionId
            }
        )

        // When
        presenter.processCheckout(false, false, false, "", "", "", false)

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
                transactionId,
                "",
                0,
                ""
            )
            view.renderCheckoutCartSuccess(any())
        }
    }

    @Test
    fun `WHEN generate checkout request when cross sell model is not empty`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        presenter.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))

        val listCrossSellModel = arrayListOf(
            ShipmentCrossSellModel(
                isChecked = true
            )
        )
        presenter.listShipmentCrossSellModel = listCrossSellModel

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, listCrossSellModel, "", arrayListOf())

        // Then
        assert(checkoutRequest.crossSell?.listItem?.isNotEmpty() == true)
        assert(checkoutRequest.promos?.isNotEmpty() == true)
        assert(checkoutRequest.promoCodes?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN generate checkout request with new upsell selected then cross sell model is not empty`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            isSelected = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button",
            id = "1",
            additionalVerticalId = "2",
            transactionType = "upsell"
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
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.listShipmentCrossSellModel = arrayListOf()

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // Then
        assert(checkoutRequest.crossSell!!.listItem.isNotEmpty())
        assertEquals(
            CrossSellItemRequestModel(1, 100.0, "upsell", 2),
            checkoutRequest.crossSell!!.listItem[0]
        )
    }

    @Test
    fun `WHEN generate checkout request with new upsell not selected then cross sell model is empty`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            isSelected = false,
            description = "desc",
            appLink = "applink",
            image = "image",
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button",
            id = "1",
            additionalVerticalId = "2",
            transactionType = "upsell"
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
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.listShipmentCrossSellModel = arrayListOf()

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // Then
        assert(checkoutRequest.crossSell!!.listItem.isEmpty())
    }

    @Test
    fun `WHEN generate checkout request with new upsell not showed then cross sell model is empty`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = false,
            isSelected = false,
            description = "desc",
            appLink = "applink",
            image = "image",
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button",
            id = "1",
            additionalVerticalId = "2",
            transactionType = "upsell"
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
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.listShipmentCrossSellModel = arrayListOf()

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // Then
        assert(checkoutRequest.crossSell!!.listItem.isEmpty())
    }

    @Test
    fun `WHEN generate checkout request with new upsell selected but not showed then cross sell model is empty`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = false,
            isSelected = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button",
            id = "1",
            additionalVerticalId = "2",
            transactionType = "upsell"
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
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))
        presenter.listShipmentCrossSellModel = arrayListOf()

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // Then
        assert(checkoutRequest.crossSell!!.listItem.isEmpty())
    }

    @Test
    fun `GIVEN checkout with tokonow product WHEN generate checkout request THEN should set feature type tokonow`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        presenter.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)
        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        dataCheckoutRequest.shopProducts?.firstOrNull()?.isTokoNow = true
        presenter.setDataCheckoutRequestList(listOf(dataCheckoutRequest))

        // When
        val checkoutRequest =
            presenter.generateCheckoutRequest(null, 0, arrayListOf(), "", arrayListOf())

        // Then
        assert(checkoutRequest.featureType == FEATURE_TYPE_TOKONOW_PRODUCT)
    }
}
