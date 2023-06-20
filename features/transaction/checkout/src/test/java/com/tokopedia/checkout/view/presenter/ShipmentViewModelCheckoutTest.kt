package com.tokopedia.checkout.view.presenter

import androidx.fragment.app.FragmentActivity
import com.tokopedia.checkout.data.model.request.checkout.CheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.FEATURE_TYPE_TOKONOW_PRODUCT
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellItemRequestModel
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.NewUpsellData
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.model.checkout.MessageData
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData
import com.tokopedia.checkout.domain.model.checkout.Prompt
import com.tokopedia.checkout.utils.CheckoutFingerprintUtil
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.fingerprint.util.FingerPrintUtil
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnProductItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnBottomSheetResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.ProductResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException
import java.security.PublicKey

class ShipmentViewModelCheckoutTest : BaseShipmentViewModelTest() {

    @Test
    fun checkoutSuccess_ShouldGoToPaymentPage() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                this.transactionId = transactionId
            }

        // When
        viewModel.processCheckout()

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
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val errorMessage = "error"
        every { view.getStringResource(any()) } returns errorMessage

        // When
        viewModel.processCheckout()

        // Then
        verifyOrder {
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastError(errorMessage)
        }
    }

//    @Test
//    fun checkoutErrorNullRequest_ShouldShowError() {
//        // Given
//        presenter.shipmentCartItemModelList = listOf(
//            ShipmentCartItemModel().apply {
//                cartItemModels = listOf(CartItemModel())
//            }
//        )
//        presenter.setDataCheckoutRequestList(null)
//        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
//        presenter.setUploadPrescriptionData(uploadModel)
//
//        val mockContext = mockk<Activity>()
//        val errorMessage = "error"
//        every { mockContext.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown_short) } returns errorMessage
//        every { mockContext.getString(R.string.message_error_checkout_empty) } returns errorMessage
//        every { view.activityContext } returns mockContext
//
//        // When
//        presenter.processCheckout(false, false, false, "", "", "", false)
//
//        // Then
//        verifyOrder {
//            view.showToastError(errorMessage) // weird ?
//            view.hideLoading()
//            view.setHasRunningApiCall(false)
//            view.showToastError(errorMessage)
//        }
//    }

    @Test
    fun checkoutFailedPriceValidation_ShouldRenderCheckoutPriceUpdate() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val priceValidationData = PriceValidationData().apply {
            isUpdated = true
            message = MessageData()
        }
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                this.isError = true
                this.priceValidationData = priceValidationData
            }

        // When
        viewModel.processCheckout()

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
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        every { view.activity } returns null
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                isError = true
                this.errorMessage = errorMessage
            }

        // When
        viewModel.processCheckout()

        // Then
        coVerifyOrder {
            view.setHasRunningApiCall(false)
            shipmentAnalyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(errorMessage)
            view.hideLoading()
            view.renderCheckoutCartError(errorMessage)
            getShipmentAddressFormV4UseCase(any())
        }
    }

    @Test
    fun `WHEN checkout failed without error message from backend THEN should show error and reload page`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val mockContext = mockk<FragmentActivity>()
        every { view.activity } returns mockContext
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                isError = true
            }
        val errorMessage = "error"
        every { mockContext.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown) } returns errorMessage

        // When
        viewModel.processCheckout()

        // Then
        coVerifyOrder {
            view.setHasRunningApiCall(false)
            shipmentAnalyticsActionListener.sendAnalyticsChoosePaymentMethodFailed(any())
            view.hideLoading()
            view.renderCheckoutCartError(any())
            getShipmentAddressFormV4UseCase(any())
        }
    }

    @Test
    fun `WHEN checkout failed with exception THEN should show error and reload page`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        every { view.activity } returns null
        coEvery { checkoutUseCase(any()) } throws IOException()

        // When
        viewModel.processCheckout()

        // Then
        coVerifyOrder {
            view.hideLoading()
            view.setHasRunningApiCall(false)
            view.showToastError(any())
            getShipmentAddressFormV4UseCase(any())
        }
    }

    @Test
    fun `WHEN generate checkout request with applied promo THEN request should contains promo data`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
//        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "-0-301643",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "-0-301643",
                        cartStringOrder = "239594-0-301643"
                    )
                ),
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            )
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        // Then
        assert(checkoutRequest.promos.isNotEmpty())
//        assert(checkoutRequest?.promoCodes?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN generate checkout request with donation THEN request should contains donation flag true`() {
        // Given
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        viewModel.shipmentDonationModel = ShipmentDonationModel(isChecked = true)

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        // Then
        assert(checkoutRequest.isDonation == 1)
    }

    @Test
    fun `WHEN generate checkout request with egold THEN request should contains egold data`() {
        // Given
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val egoldAmount = 10000L
        viewModel.egoldAttributeModel.value = EgoldAttributeModel().apply {
            isChecked = true
            isEligible = true
            buyEgoldValue = egoldAmount
        }

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        // Then
        assert(checkoutRequest.egold.isEgold)
        assert(checkoutRequest.egold.goldAmount == egoldAmount)
    }

    @Test
    fun `WHEN generate checkout request with corner address THEN request should contains corner address data`() {
        // Given
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        val tmpUserCornerId = "123"
        val tmpCornerId = "456"
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            isCornerAddress = true
            userCornerId = tmpUserCornerId
            cornerId = tmpCornerId
            id = "1"
        }

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        // Then
        assert(checkoutRequest.tokopediaCorner?.isTokopediaCorner == true)
        assert(checkoutRequest.tokopediaCorner?.cornerId == tmpCornerId.toLongOrZero())
        assert(checkoutRequest.tokopediaCorner?.userCornerId == tmpUserCornerId)
    }

    @Test
    fun `WHEN generate checkout params trade in laku 6 THEN request should contains trade in laku 6 data`() {
        // Given
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val deviceId = "12345"
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        // When
        val checkoutParams =
            viewModel.generateCheckoutParams(
                true,
                true,
                false,
                deviceId,
                checkoutRequest,
                ""
            )

        // Then
        assert(checkoutParams.isTradeIn)
        assert(!checkoutParams.isTradeInDropOff)
        assert(checkoutParams.devId == deviceId)
    }

    @Test
    fun `WHEN generate checkout params trade in indopaket THEN request should contains trade in indopaket data`() {
        // Given
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val deviceId = "12345"
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        // When
        val checkoutParams =
            viewModel.generateCheckoutParams(
                true,
                true,
                true,
                deviceId,
                checkoutRequest,
                ""
            )

        // Then
        assert(checkoutParams.isTradeIn)
        assert(checkoutParams.isTradeInDropOff)
        assert(checkoutParams.devId == deviceId)
    }

    @Test
    fun `WHEN generate checkout params with fingerprint supported and fingerprint enabled THEN request should contains fingerprint data`() {
        // Given
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val deviceId = "12345"
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        val mockContext = mockk<FragmentActivity>()
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
        every { view.activity } returns mockContext
        every { CheckoutFingerprintUtil.getEnableFingerprintPayment(mockContext) } returns true
        every { CheckoutFingerprintUtil.getFingerprintPublicKey(mockContext) } returns publicKey
        every { FingerPrintUtil.getPublicKey(publicKey) } returns fingerprintString

        // When
        val checkoutParams =
            viewModel.generateCheckoutParams(
                true,
                true,
                false,
                deviceId,
                checkoutRequest,
                ""
            )

        // Then
        assert(checkoutParams.fingerprintPublickey == fingerprintString)
        assert(checkoutParams.fingerprintSupport == "true")
    }

    @Test
    fun `WHEN generate checkout params with fingerprint supported and fingerprint disabled THEN request should contains fingerprint data`() {
        // Given
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val deviceId = "12345"
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        val mockContext = mockk<FragmentActivity>()
        mockkObject(FingerPrintUtil)
        mockkObject(CheckoutFingerprintUtil)
        val fingerprintString = "abc"
        val publicKey = null
        every { view.activity } returns mockContext
        every { CheckoutFingerprintUtil.getEnableFingerprintPayment(mockContext) } returns true
        every { CheckoutFingerprintUtil.getFingerprintPublicKey(mockContext) } returns publicKey

        // When
        val checkoutParams =
            viewModel.generateCheckoutParams(
                true,
                true,
                false,
                deviceId,
                checkoutRequest,
                ""
            )

        // Then
        assert(checkoutParams.fingerprintSupport == "false")
    }

    @Test
    fun `WHEN checkout multiple product with one error product THEN should checkout success`() {
        // Given
        val shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(cartStringGroup = "").apply {
                        isError = false
                    }
                )
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(cartStringGroup = "").apply {
                        isError = true
                    }
                )
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.shipmentCartItemModelList = shipmentCartItemModelList
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                this.transactionId = transactionId
            }
//        every { view.generateNewCheckoutRequest(any(), any()) } returns listOf(dataCheckoutRequest)

        // When
        viewModel.processCheckout()

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
        val slot = CapturingSlot<CheckoutRequest>()

        // Given
        val shipmentCartItemModelList = ArrayList<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(cartStringGroup = "").apply {
                        isError = false
                    }
                )
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(cartStringGroup = "").apply {
                        isError = true
                    },
                    CartItemModel(cartStringGroup = "").apply {
                        isError = false
                    }
                )
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(cartStringGroup = "").apply {
                        isError = true
                        isShopError = true
                    }
                )
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
                isAllItemError = true
            }
        )
        viewModel.shipmentCartItemModelList = shipmentCartItemModelList
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        coEvery { checkoutUseCase(capture(slot)) } returns
            CheckoutData().apply {
                this.transactionId = transactionId
            }
//        every { view.generateNewCheckoutRequest(capture(slot), any()) } returns listOf(
//            dataCheckoutRequest
//        )

        // When
        viewModel.processCheckout()

        // Then
        assertEquals(2, slot.captured.carts.data[0].groupOrders.size)
        assertEquals(1, slot.captured.carts.data[0].groupOrders[0].shopOrders[0].bundle[0].productData.size)
        assertEquals(1, slot.captured.carts.data[0].groupOrders[1].shopOrders[0].bundle[0].productData.size)
    }

    @Test
    fun `WHEN checkout with donation checked success THEN should go to payment page`() {
        // Given
        viewModel.shipmentDonationModel = ShipmentDonationModel(isChecked = true)
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                this.transactionId = transactionId
            }

        // When
        viewModel.processCheckout()

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
        viewModel.shipmentDonationModel = ShipmentDonationModel(isChecked = false)
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                this.transactionId = transactionId
            }

        // When
        viewModel.processCheckout()

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
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.setPurchaseProtection(true)
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val transactionId = "1234"
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                this.transactionId = transactionId
            }

        // When
        viewModel.processCheckout()

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
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)

        val prompt = Prompt().apply {
            eligible = true
            title = "Title"
            description = "Description"
        }
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                this.isError = true
                this.prompt = prompt
            }

        // When
        viewModel.processCheckout()

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
            ShipmentCartItemModel(cartStringGroup = "239594-0-301643").apply {
                cartItemModels = arrayListOf(
                    CartItemModel(
                        cartStringGroup = "239594-0-301643",
                        cartId = 88
                    )
                )
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-88"
            }
        )
        viewModel.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        viewModel.updateAddOnProductLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

        // Then
        verify {
            view.updateAddOnsData(
                0,
                shipmentCartItemModelList[0].cartItemModels[0].cartStringGroup,
                shipmentCartItemModelList[0].cartItemModels[0].cartId
            )
        }
    }

    @Test
    fun `WHEN update addOn order level data bottomsheet`() {
        // Given
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(
                cartStringGroup = "239594-0-301643",
                addOnsOrderLevelModel = AddOnsDataModel()
            )
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
        viewModel.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        viewModel.updateAddOnOrderLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

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
            view.updateAddOnsData(1, shipmentCartItemModelList[0].cartStringGroup, 0)
        }
    }

    @Test
    fun `WHEN update addOn order level data bottomsheet with AddOnData`() {
        // Given
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel(
                cartStringGroup = "239594-0-301643",
                addOnsOrderLevelModel = AddOnsDataModel()
            )
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
        viewModel.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        viewModel.updateAddOnOrderLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

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
            view.updateAddOnsData(1, shipmentCartItemModelList[0].cartStringGroup, 0)
        }
    }

    @Test
    fun checkoutSuccess_CrossSellModelChecked() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "").apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel =
            arrayListOf(
                ShipmentCrossSellModel().apply {
                    isChecked = true
                    crossSellModel = CrossSellModel()
                }
            )
        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
        viewModel.setUploadPrescriptionData(uploadModel)
        val transactionId = "1234"
        coEvery { checkoutUseCase(any()) } returns
            CheckoutData().apply {
                this.transactionId = transactionId
            }

        // When
        viewModel.processCheckout()

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
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
//        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }

        val listCrossSellModel = arrayListOf(
            ShipmentCrossSellModel(
                isChecked = true
            )
        )
        viewModel.listShipmentCrossSellModel = listCrossSellModel

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        // Then
        assert(checkoutRequest.crossSell?.listItem?.isNotEmpty() == true)
        assert(checkoutRequest.promos.isNotEmpty())
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
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
//        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

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
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
//        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

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
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
//        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

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
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )

        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )
//        val dataCheckoutRequest = DataProvider.provideSingleDataCheckoutRequest()
//        dataCheckoutRequest.shopProducts?.firstOrNull()?.cartString = "239594-0-301643"
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        viewModel.listShipmentCrossSellModel = arrayListOf()

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        // Then
        assert(checkoutRequest.crossSell!!.listItem.isEmpty())
    }

    @Test
    fun `GIVEN checkout with tokonow product WHEN generate checkout request THEN should set feature type tokonow`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "239594-0-301643", isTokoNow = true).apply {
                cartItemModels = listOf(CartItemModel(cartStringGroup = "239594-0-301643"))
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData()
                )
            }
        )
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }

        // When
        val checkoutRequest =
            viewModel.generateCheckoutRequest()

        // Then
        assert(checkoutRequest.featureType == FEATURE_TYPE_TOKONOW_PRODUCT)
    }
}
