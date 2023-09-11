package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.domain.model.platformfee.PaymentFee
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeGqlResponse
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeResponse
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class CheckoutViewModelPaymentTest : BaseCheckoutViewModelTest() {

    @Test
    fun `GIVEN disabled platform fee WHEN update cost THEN should not hit platform fee`() {
        // Given
        viewModel.shipmentPlatformFeeData = ShipmentPlatformFeeData(isEnable = false)

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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(1000.0, viewModel.listData.value.cost()!!.finalItemPrice, 0.0)
        coVerify(inverse = true) {
            getPaymentFeeCheckoutUseCase.executeOnBackground()
        }
    }

    @Test
    fun `GIVEN enable platform fee WHEN update cost THEN should hit platform fee`() {
        // Given
        viewModel.shipmentPlatformFeeData = ShipmentPlatformFeeData(isEnable = true)

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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery { getPaymentFeeCheckoutUseCase.executeOnBackground() } returns PaymentFeeGqlResponse(
            PaymentFeeResponse(
                success = true,
                data = listOf(
                    PaymentFee(code = CheckoutViewModel.PLATFORM_FEE_CODE, title = "title")
                )
            )
        )

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(1000.0, viewModel.listData.value.cost()!!.finalItemPrice, 0.0)
        coVerify {
            getPaymentFeeCheckoutUseCase.executeOnBackground()
        }
    }

    @Test
    fun `GIVEN in range platform fee WHEN update cost THEN should not hit platform fee`() {
        // Given
        viewModel.shipmentPlatformFeeData = ShipmentPlatformFeeData(isEnable = true)

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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(dynamicPlatformFee = ShipmentPaymentFeeModel(fee = 1000.0, minRange = 1.0, maxRange = 10000.0)),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery { getPaymentFeeCheckoutUseCase.executeOnBackground() } returns PaymentFeeGqlResponse(
            PaymentFeeResponse(
                success = true,
                data = listOf(
                    PaymentFee(code = CheckoutViewModel.PLATFORM_FEE_CODE, title = "title")
                )
            )
        )

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(1000.0, viewModel.listData.value.cost()!!.finalItemPrice, 0.0)
        assertEquals("Rp2.000", viewModel.listData.value.cost()!!.totalPriceString)
        coVerify(inverse = true) {
            getPaymentFeeCheckoutUseCase.executeOnBackground()
        }
    }

    @Test
    fun `GIVEN error get platform fee WHEN update cost THEN should show ticker`() {
        // Given
        viewModel.shipmentPlatformFeeData = ShipmentPlatformFeeData(isEnable = true, errorWording = "error wording")

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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(dynamicPlatformFee = ShipmentPaymentFeeModel(fee = 1000.0)),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery { getPaymentFeeCheckoutUseCase.executeOnBackground() } returns PaymentFeeGqlResponse(
            PaymentFeeResponse(
                success = false
            )
        )

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(1000.0, viewModel.listData.value.cost()!!.finalItemPrice, 0.0)
        assertEquals("Rp1.000", viewModel.listData.value.cost()!!.totalPriceString)
        assertEquals(ShipmentPaymentFeeModel(isShowTicker = true, ticker = "error wording"), viewModel.listData.value.cost()!!.dynamicPlatformFee)
    }

    @Test
    fun `GIVEN failed get platform fee WHEN update cost THEN should show ticker`() {
        // Given
        viewModel.shipmentPlatformFeeData = ShipmentPlatformFeeData(isEnable = true, errorWording = "error wording")

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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(dynamicPlatformFee = ShipmentPaymentFeeModel(fee = 1000.0)),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery { getPaymentFeeCheckoutUseCase.executeOnBackground() } throws IOException()

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(1000.0, viewModel.listData.value.cost()!!.finalItemPrice, 0.0)
        assertEquals("Rp1.000", viewModel.listData.value.cost()!!.totalPriceString)
        assertEquals(ShipmentPaymentFeeModel(isShowTicker = true, ticker = "error wording"), viewModel.listData.value.cost()!!.dynamicPlatformFee)
    }
}
