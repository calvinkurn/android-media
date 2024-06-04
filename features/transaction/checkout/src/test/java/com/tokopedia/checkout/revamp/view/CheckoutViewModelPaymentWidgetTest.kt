package com.tokopedia.checkout.revamp.view

import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkoutpayment.domain.CreditCardTenorListData
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentData
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentOption
import com.tokopedia.checkoutpayment.domain.PaymentAmountValidation
import com.tokopedia.checkoutpayment.domain.PaymentWidgetData
import com.tokopedia.checkoutpayment.domain.PaymentWidgetListData
import com.tokopedia.checkoutpayment.domain.TenorListData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetState
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.io.IOException

class CheckoutViewModelPaymentWidgetTest : BaseCheckoutViewModelTest() {

    @Test
    fun `GIVEN failed get payment widget WHEN get payment widget THEN should not hit platform fee & show error`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } throws IOException()

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Error, viewModel.listData.value.payment()!!.widget.state)
        coVerify(inverse = true) {
            dynamicPaymentFeeUseCase(any())
        }
    }

    @Test
    fun `GIVEN empty payment widget data WHEN get payment widget THEN should not hit platform fee & show error`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData()

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Error, viewModel.listData.value.payment()!!.widget.state)
        coVerify(inverse = true) {
            dynamicPaymentFeeUseCase(any())
        }
    }

    @Test
    fun `GIVEN VA payment widget data WHEN get payment widget THEN should hit platform fee & show widget`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA"
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        coVerify(exactly = 1) {
            dynamicPaymentFeeUseCase(any())
        }
    }

    @Test
    fun `GIVEN failed get platform fee WHEN get payment widget THEN should show error`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA"
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } throws IOException()

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Error, viewModel.listData.value.payment()!!.widget.state)
        coVerify(exactly = 1) {
            dynamicPaymentFeeUseCase(any())
        }
    }

    @Test
    fun `GIVEN CC payment widget data WHEN get payment widget THEN should hit platform fee & show widget`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA",
                    mandatoryHit = listOf("CreditCardTenorList")
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            creditCardTenorListUseCase(any())
        } returns CreditCardTenorListData(
            tenorList = listOf(
                TenorListData(),
                TenorListData()
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        coVerify(exactly = 2) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(exactly = 2) {
            creditCardTenorListUseCase(any())
        }
    }

    @Test
    fun `GIVEN failed update cart WHEN get payment widget THEN should show error`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA",
                    mandatoryHit = listOf("CreditCardTenorList")
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            creditCardTenorListUseCase(any())
        } returns CreditCardTenorListData(
            tenorList = listOf(
                TenorListData(),
                TenorListData()
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } throws IOException()

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Error, viewModel.listData.value.payment()!!.widget.state)
        coVerify(exactly = 1) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(exactly = 1) {
            creditCardTenorListUseCase(any())
        }
    }

    @Test
    fun `GIVEN failed get tenor WHEN get payment widget THEN should show error`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA",
                    mandatoryHit = listOf("CreditCardTenorList"),
                    amountValidation = PaymentAmountValidation(
                        maximumAmount = 10000
                    )
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            creditCardTenorListUseCase(any())
        } throws IOException()

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        assertEquals("Pilih Lama Pembayaran", viewModel.listData.value.payment()!!.widget.installmentText)
        assertNotNull(latestToaster)
        coVerify(exactly = 1) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(exactly = 1) {
            creditCardTenorListUseCase(any())
        }
    }

    @Test
    fun `GIVEN Gocicil payment widget data WHEN get payment widget THEN should hit platform fee & show widget`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA",
                    mandatoryHit = listOf("getInstallmentInfo")
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            goCicilInstallmentOptionUseCase(any())
        } returns GoCicilInstallmentData(
            installmentOptions = listOf(
                GoCicilInstallmentOption(installmentTerm = 1, isActive = true),
                GoCicilInstallmentOption(installmentTerm = 2, isActive = true, isRecommended = true)
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        assertEquals(2, viewModel.listData.value.payment()!!.data!!.paymentWidgetData.first().installmentPaymentData.selectedTenure)
        coVerify(exactly = 2) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(exactly = 2) {
            goCicilInstallmentOptionUseCase(any())
        }
    }

    @Test
    fun `GIVEN failed get installment WHEN get payment widget THEN should show error`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA",
                    mandatoryHit = listOf("getInstallmentInfo"),
                    amountValidation = PaymentAmountValidation(
                        maximumAmount = 10000
                    )
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            goCicilInstallmentOptionUseCase(any())
        } throws IOException()

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        assertEquals("Pilih Lama Pembayaran", viewModel.listData.value.payment()!!.widget.installmentText)
        assertNotNull(latestToaster)
        coVerify(exactly = 1) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(exactly = 1) {
            goCicilInstallmentOptionUseCase(any())
        }
    }

    @Test
    fun `WHEN choose payment THEN should rehit get payment widget`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA"
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(
            status = "OK",
            data = Data(
                status = true
            )
        )

        // When
        viewModel.choosePayment("newGateway", "newMetadata")

        // Then
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        coVerify(exactly = 1) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(exactly = 1) {
            getPaymentWidgetUseCase(
                match {
                    it.chosenPayment.gatewayCode == "newGateway" && it.chosenPayment.metadata == "newMetadata" && it.chosenPayment.optionId == "" && it.chosenPayment.tenureType == 0
                }
            )
        }
    }

    @Test
    fun `GIVEN failed hit update cart WHEN choose payment THEN should not rehit get payment widget`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA"
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } throws IOException()

        // When
        viewModel.choosePayment("newGateway", "newMetadata")

        // Then
        assertNotNull(latestToaster)
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        coVerify(inverse = true) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(inverse = true) {
            getPaymentWidgetUseCase(
                match {
                    it.chosenPayment.gatewayCode == "newGateway" && it.chosenPayment.metadata == "newMetadata" && it.chosenPayment.optionId == "" && it.chosenPayment.tenureType == 0
                }
            )
        }
    }

    @Test
    fun `GIVEN failed hit update cart WHEN choose installment cc THEN should set selected tenure`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(
                widget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal),
                enable = true,
                data = PaymentWidgetListData(
                    paymentWidgetData = listOf(
                        PaymentWidgetData(
                            gatewayCode = "cc",
                            mandatoryHit = listOf("CreditCardTenorList")
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA"
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } throws IOException()

        // When
        viewModel.chooseInstallmentCC(
            TenorListData(
                type = "12",
                gatewayCode = "abc"
            ),
            emptyList()
        )

        // Then
        assertNotNull(latestToaster)
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        assertEquals(0, viewModel.listData.value.payment()!!.data!!.paymentWidgetData.first().installmentPaymentData.selectedTenure)
        assertEquals("cc", viewModel.listData.value.payment()!!.data!!.paymentWidgetData.first().gatewayCode)
        coVerify(inverse = true) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(inverse = true) {
            getPaymentWidgetUseCase(any())
        }
    }

    @Test
    fun `GIVEN success update cart WHEN choose installment cc THEN should not rehit get payment widget`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(
                widget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal),
                enable = true,
                data = PaymentWidgetListData(
                    paymentWidgetData = listOf(
                        PaymentWidgetData(
                            gatewayCode = "cc",
                            metadata = """{"express_checkout_param":{"installment_term":0}, "gateway_code":"VA"}""",
                            mandatoryHit = listOf("CreditCardTenorList")
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA"
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            creditCardTenorListUseCase(any())
        } returns CreditCardTenorListData(
            tenorList = listOf(
                TenorListData(type = "FULL", gatewayCode = "abc0"),
                TenorListData(type = "3", gatewayCode = "abc3"),
                TenorListData(type = "6", gatewayCode = "abc6"),
                TenorListData(type = "12", gatewayCode = "abc12")
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // When
        viewModel.chooseInstallmentCC(
            TenorListData(
                type = "12",
                gatewayCode = "abc"
            ),
            listOf(TenorListData())
        )

        // Then
        assertNull(latestToaster)
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        coVerify {
            updateCartUseCase.get().setParams(any(), "update_payment", match { it.gatewayCode == "abc" })
        }
        coVerify {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(inverse = true) {
            getPaymentWidgetUseCase(any())
        }
        assertEquals(12, viewModel.listData.value.payment()!!.data!!.paymentWidgetData.first().installmentPaymentData.selectedTenure)
        assertEquals("abc", viewModel.listData.value.payment()!!.data!!.paymentWidgetData.first().gatewayCode)
    }

    @Test
    fun `GIVEN failed hit update cart WHEN choose installment gocicil THEN should set selected tenure`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(
                widget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal),
                enable = true,
                data = PaymentWidgetListData(
                    paymentWidgetData = listOf(
                        PaymentWidgetData(
                            gatewayCode = "gocicil",
                            mandatoryHit = listOf("getInstallmentInfo")
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA"
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } throws IOException()

        // When
        viewModel.chooseInstallment(GoCicilInstallmentOption(installmentTerm = 12), emptyList(), "", false)

        // Then
        assertNotNull(latestToaster)
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        assertEquals(0, viewModel.listData.value.payment()!!.data!!.paymentWidgetData.first().installmentPaymentData.selectedTenure)
        coVerify(inverse = true) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(inverse = true) {
            getPaymentWidgetUseCase(any())
        }
    }

    @Test
    fun `GIVEN success update cart WHEN choose installment gocicil THEN should not rehit get payment widget`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(
                widget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal),
                enable = true,
                data = PaymentWidgetListData(
                    paymentWidgetData = listOf(
                        PaymentWidgetData(
                            gatewayCode = "gocicil",
                            mandatoryHit = listOf("getInstallmentInfo")
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA"
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            goCicilInstallmentOptionUseCase(any())
        } returns GoCicilInstallmentData(
            installmentOptions = listOf(
                GoCicilInstallmentOption(installmentTerm = 0),
                GoCicilInstallmentOption(installmentTerm = 3),
                GoCicilInstallmentOption(installmentTerm = 6),
                GoCicilInstallmentOption(installmentTerm = 12, isActive = true)
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // When
        viewModel.chooseInstallment(GoCicilInstallmentOption(installmentTerm = 12), emptyList(), "", false)

        // Then
        assertNull(latestToaster)
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        coVerify {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(inverse = true) {
            getPaymentWidgetUseCase(any())
        }
        assertEquals(12, viewModel.listData.value.payment()!!.data!!.paymentWidgetData.first().installmentPaymentData.selectedTenure)
    }

    @Test
    fun `GIVEN VA payment widget data WHEN force reload payment THEN should hit payment & show widget`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(
                widget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal), enable = true,
                data = PaymentWidgetListData(
                    paymentWidgetData = listOf(PaymentWidgetData(gatewayCode = "VA"))
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            getPaymentWidgetUseCase(any())
        } returns PaymentWidgetListData(
            paymentWidgetData = listOf(
                PaymentWidgetData(
                    gatewayCode = "VA"
                )
            )
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // When
        viewModel.forceReloadPayment()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        coVerify(exactly = 1) {
            dynamicPaymentFeeUseCase(any())
        }
        coVerify(exactly = 1) {
            getPaymentWidgetUseCase(any())
        }
    }
}
