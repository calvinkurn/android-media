package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.model.checkout.MessageData
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData
import com.tokopedia.checkout.domain.model.checkout.Prompt
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataModel
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnStateResponse
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnsResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EthicalDrugDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.unifycomponents.Toaster
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class CheckoutViewModelCheckoutTest : BaseCheckoutViewModelTest() {

    @Test
    fun checkoutSuccess_ShouldGoToPaymentPage() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val transactionId = "1234"
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.transactionId = transactionId
        }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(true, invokeSuccess)
    }

    @Test
    fun checkoutSuccessTradeIn_ShouldGoToPaymentPage() {
        // Given
        viewModel.isTradeIn = true
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val transactionId = "1234"
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.transactionId = transactionId
        }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(true, invokeSuccess)
        verify {
            mTrackerShipment.sendEnhancedECommerceCheckout(any(), match { it.keys.size == 3 }, any(), any(), any(), CheckoutTradeInAnalytics.EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN, any(), any(), "4")
        }
    }

    @Test
    fun checkoutErrorEmptyRequest_ShouldShowError() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                isAllItemError = true,
                isError = true,
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val transactionId = "1234"
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.transactionId = transactionId
        }
        var invokeSuccess = false

        coEvery { getShipmentAddressFormV4UseCase.invoke(any()) } returns CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product()
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        assertEquals(
            CheckoutPageToaster(
                Toaster.TYPE_ERROR,
                "Barangmu lagi nggak bisa dibeli. Silakan balik ke keranjang untuk cek belanjaanmu."
            ),
            latestToaster
        )
    }

    @Test
    fun checkoutFailedPriceValidation_ShouldRenderCheckoutPriceUpdate() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        val priceValidationData = PriceValidationData().apply {
            isUpdated = true
            message = MessageData()
        }

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.isError = true
            this.priceValidationData = priceValidationData
        }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        assertEquals(
            CheckoutPageState.PriceValidation(priceValidationData),
            viewModel.pageState.value
        )
    }

    @Test
    fun `WHEN checkout failed with error message from backend THEN should show error and reload page`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val errorMessage = "backend error message"
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.isError = true
            this.errorMessage = errorMessage
        }
        var invokeSuccess = false

        coEvery { getShipmentAddressFormV4UseCase.invoke(any()) } returns CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product()
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        assertEquals(CheckoutPageToaster(Toaster.TYPE_ERROR, errorMessage), latestToaster)
        verify {
            mTrackerShipment.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(
                errorMessage
            )
        }
    }

    @Test
    fun `WHEN checkout failed without error message from backend THEN should show default error and reload page`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val errorMessage = ""
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.isError = true
            this.errorMessage = errorMessage
        }
        var invokeSuccess = false

        coEvery { getShipmentAddressFormV4UseCase.invoke(any()) } returns CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product()
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        assertEquals(
            CheckoutPageToaster(
                Toaster.TYPE_ERROR,
                "Terjadi kesalahan. Ulangi beberapa saat lagi"
            ),
            latestToaster
        )
        verify {
            mTrackerShipment.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(
                errorMessage
            )
        }
    }

    @Test
    fun `WHEN checkout failed with exception THEN should show error and reload page`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val exception = IOException()
        coEvery { checkoutGqlUseCase(any()) } throws exception
        var invokeSuccess = false

        coEvery { getShipmentAddressFormV4UseCase.invoke(any()) } returns CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product()
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        assertEquals(true, latestToaster != null)
        verify {
            mTrackerShipment.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(
                exception.message
            )
        }
    }

    @Test
    fun checkoutWithNoCourier_ShouldShowRedWidgetAndToaster() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment()),
            CheckoutProductModel("234"),
            CheckoutOrderModel(
                "234",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.checkout("", { }, {})

        // Then
        assertEquals(
            CheckoutPageToaster(
                Toaster.TYPE_NORMAL,
                "Pilih pengiriman dulu yuk sebelum lanjut bayar."
            ),
            latestToaster
        )
        verify {
            mTrackerShipment.eventClickBuyCourierSelectionClickPilihMetodePembayaranCourierNotComplete()
        }
        assertEquals(CheckoutPageState.ScrollTo(5), viewModel.pageState.value)
        assertEquals(true, (viewModel.listData.value[5] as CheckoutOrderModel).isShippingBorderRed)
        assertEquals(
            true,
            (viewModel.listData.value[5] as CheckoutOrderModel).isTriggerShippingVibrationAnimation
        )
    }

    @Test
    fun checkoutWithNoPrescription_ShouldShowError() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                ethicalDrugDataModel = EthicalDrugDataModel(needPrescription = true)
            ),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(
                    courierItemData = CourierItemData(
                        shipperId = 1,
                        shipperProductId = 1
                    )
                ),
                hasEthicalProducts = true
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    frontEndValidation = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        var triggerEpharmacyTracker: Boolean? = null
        viewModel.checkout("", {
            triggerEpharmacyTracker = it
        }, {})

        // Then
        assertEquals(true, triggerEpharmacyTracker)
        assertEquals(
            true,
            (viewModel.listData.value[6] as CheckoutEpharmacyModel).epharmacy.isError
        )
        assertEquals(
            false,
            (viewModel.listData.value[6] as CheckoutEpharmacyModel).epharmacy.isIncompletePrescriptionError
        )
    }

    @Test
    fun checkoutWithIncompletePrescription_ShouldShowError() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                ethicalDrugDataModel = EthicalDrugDataModel(needPrescription = true)
            ),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(
                    courierItemData = CourierItemData(
                        shipperId = 1,
                        shipperProductId = 1
                    )
                ),
                hasEthicalProducts = true
            ),
            CheckoutProductModel(
                "234",
                ethicalDrugDataModel = EthicalDrugDataModel(needPrescription = true)
            ),
            CheckoutOrderModel(
                "234",
                shipment = CheckoutOrderShipment(
                    courierItemData = CourierItemData(
                        shipperId = 1,
                        shipperProductId = 1
                    )
                ),
                hasEthicalProducts = true,
                tokoConsultationId = "1234",
                partnerConsultationId = "1234",
                consultationDataString = "1234"
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    frontEndValidation = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        var triggerEpharmacyTracker: Boolean? = null
        viewModel.checkout("", {
            triggerEpharmacyTracker = it
        }, {})

        // Then
        assertEquals(true, triggerEpharmacyTracker)
        assertEquals(
            true,
            (viewModel.listData.value[8] as CheckoutEpharmacyModel).epharmacy.isError
        )
        assertEquals(
            true,
            (viewModel.listData.value[8] as CheckoutEpharmacyModel).epharmacy.isIncompletePrescriptionError
        )
    }

    @Test
    fun checkoutWithIncompletePrescriptionIds_ShouldShowError() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                ethicalDrugDataModel = EthicalDrugDataModel(needPrescription = true)
            ),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(
                    courierItemData = CourierItemData(
                        shipperId = 1,
                        shipperProductId = 1
                    )
                ),
                hasEthicalProducts = true
            ),
            CheckoutProductModel(
                "234",
                ethicalDrugDataModel = EthicalDrugDataModel(needPrescription = true)
            ),
            CheckoutOrderModel(
                "234",
                shipment = CheckoutOrderShipment(
                    courierItemData = CourierItemData(
                        shipperId = 1,
                        shipperProductId = 1
                    )
                ),
                hasEthicalProducts = true,
                prescriptionIds = listOf("1234")
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    frontEndValidation = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        var triggerEpharmacyTracker: Boolean? = null
        viewModel.checkout("", {
            triggerEpharmacyTracker = it
        }, {})

        // Then
        assertEquals(true, triggerEpharmacyTracker)
        assertEquals(
            true,
            (viewModel.listData.value[8] as CheckoutEpharmacyModel).epharmacy.isError
        )
        assertEquals(
            true,
            (viewModel.listData.value[8] as CheckoutEpharmacyModel).epharmacy.isIncompletePrescriptionError
        )
    }

    @Test
    fun checkoutSuccessWithRedPromo_ShouldGoToPaymentPage() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", cartStringOrder = "1"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(
                    courierItemData = CourierItemData(
                        shipperId = 1,
                        shipperProductId = 1,
                        logPromoCode = "boCode"
                    )
                )
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel(
                    codes = listOf("a"),
                    voucherOrders = listOf(
                        LastApplyVoucherOrdersItemUiModel(
                            code = "b",
                            uniqueId = "1",
                            type = "cashback",
                            cartStringGroup = "123"
                        ),
                        LastApplyVoucherOrdersItemUiModel(
                            code = "boCode",
                            uniqueId = "1",
                            type = "logistic",
                            spId = 1,
                            shippingId = 1,
                            cartStringGroup = "123"
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                codes = listOf("a"),
                messageUiModel = MessageUiModel(state = "red"),
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "b",
                        uniqueId = "1",
                        cartStringGroup = "123",
                        type = "cashback",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "boCode",
                        uniqueId = "1",
                        cartStringGroup = "123",
                        type = "logistic",
                        spId = 1,
                        shippingId = 1,
                        messageUiModel = MessageUiModel(state = "red")
                    )
                )
            )
        )

        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel(
            SuccessDataUiModel(true)
        )

        val transactionId = "1234"
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.transactionId = transactionId
        }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(true, invokeSuccess)
        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(
                match {
                    it.orderData.codes.contains("a") && it.orderData.orders[0].codes.contains(
                        "boCode"
                    )
                }
            )
        }
        coVerify {
            checkoutGqlUseCase(
                match {
                    it.carts.promos.isEmpty() &&
                        it.carts.data[0].groupOrders[0].shopOrders[0].promos.size == 1 &&
                        it.carts.data[0].groupOrders[0].shopOrders[0].promos.indexOfFirst { p -> p.code == "b" } == 0
                }
            )
        }
    }

    @Test
    fun checkoutWithRedPromo_clearPromoFailed_ShouldShowError() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", cartStringOrder = "1"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(
                    courierItemData = CourierItemData(
                        shipperId = 1,
                        shipperProductId = 1,
                        logPromoCode = "boCode"
                    )
                )
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel(
                    codes = listOf("a"),
                    voucherOrders = listOf(
                        LastApplyVoucherOrdersItemUiModel(
                            code = "b",
                            uniqueId = "1",
                            type = "cashback",
                            cartStringGroup = "123"
                        ),
                        LastApplyVoucherOrdersItemUiModel(
                            code = "boCode",
                            uniqueId = "1",
                            type = "logistic",
                            spId = 1,
                            shippingId = 1,
                            cartStringGroup = "123"
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                codes = listOf("a"),
                messageUiModel = MessageUiModel(state = "red"),
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "b",
                        uniqueId = "1",
                        cartStringGroup = "123",
                        type = "cashback",
                        messageUiModel = MessageUiModel(state = "green")
                    ),
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "boCode",
                        uniqueId = "1",
                        cartStringGroup = "123",
                        type = "logistic",
                        spId = 1,
                        shippingId = 1,
                        messageUiModel = MessageUiModel(state = "red")
                    )
                )
            )
        )

        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        } throws IOException()

        val transactionId = "1234"
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.transactionId = transactionId
        }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(
                match {
                    it.orderData.codes.contains("a") && it.orderData.orders[0].codes.contains(
                        "boCode"
                    )
                }
            )
        }
        coVerify(inverse = true) {
            checkoutGqlUseCase(any())
        }
        assertEquals(
            CheckoutPageToaster(
                Toaster.TYPE_ERROR,
                CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
            ),
            latestToaster
        )
    }

    @Test
    fun `WHEN checkout error with prompt THEN should show prompt`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val prompt = Prompt().apply {
            eligible = true
            title = "Title"
            description = "Description"
        }
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.isError = true
            this.prompt = prompt
        }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        assertEquals(CheckoutPageState.Prompt(prompt), viewModel.pageState.value)
    }

    @Test
    fun checkoutWithPromoFailed_shouldShowError() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } throws IOException()

        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.transactionId = "1234"
        }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        coVerify(inverse = true) {
            checkoutGqlUseCase(any())
        }
        assertEquals(
            CheckoutPageToaster(
                Toaster.TYPE_ERROR,
                CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
            ),
            latestToaster
        )
    }

    @Test
    fun checkoutSuccessWithAddOns_ShouldGoToPaymentPage() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                cartId = 12,
                addOnProduct = AddOnProductDataModel(
                    listAddOnProductData = arrayListOf(
                        AddOnProductDataItemModel(uniqueId = "a1", status = 0),
                        AddOnProductDataItemModel(uniqueId = "a2", status = 1)
                    )
                )
            ),
            CheckoutProductModel(
                "123",
                cartId = 13
            ),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            saveAddOnProductUseCase.executeOnBackground()
        } returns SaveAddOnStateResponse(SaveAddOnsResponse(status = "OK"))

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val transactionId = "1234"
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.transactionId = transactionId
        }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(true, invokeSuccess)
        coVerify {
            saveAddOnProductUseCase.setParams(
                match { it.addOns[0].addOnKey == "12" && it.addOns[0].addOnData.size == 2 && it.addOns[1].addOnKey == "13" && it.addOns[1].addOnData.isEmpty() && it.addOns.size == 2 },
                false
            )
        }
    }

    @Test
    fun checkoutWithAddOnsFailed_ShouldShowError() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
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
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                cartId = 12,
                addOnProduct = AddOnProductDataModel(
                    listAddOnProductData = arrayListOf(
                        AddOnProductDataItemModel(uniqueId = "a1", status = 0),
                        AddOnProductDataItemModel(uniqueId = "a2", status = 1)
                    )
                )
            ),
            CheckoutProductModel(
                "123",
                cartId = 13
            ),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        val ioException = IOException()
        coEvery {
            saveAddOnProductUseCase.executeOnBackground()
        } throws ioException

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val transactionId = "1234"
        coEvery { checkoutGqlUseCase(any()) } returns CheckoutData().apply {
            this.transactionId = transactionId
        }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        assertEquals(
            CheckoutPageToaster(Toaster.TYPE_ERROR, throwable = ioException),
            latestToaster
        )
    }
}
