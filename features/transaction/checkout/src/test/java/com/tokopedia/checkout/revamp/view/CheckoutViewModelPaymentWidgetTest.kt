package com.tokopedia.checkout.revamp.view

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
import com.tokopedia.checkoutpayment.domain.PaymentWidgetData
import com.tokopedia.checkoutpayment.domain.PaymentWidgetListData
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
import org.junit.Test
import java.io.IOException

class CheckoutViewModelPaymentWidgetTest: BaseCheckoutViewModelTest() {

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
            paymentWidgetData = listOf(PaymentWidgetData(
                gatewayCode = "VA"
            ))
        )

        coEvery {
            dynamicPaymentFeeUseCase(any())
        } returns emptyList()

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(CheckoutPaymentWidgetState.Normal, viewModel.listData.value.payment()!!.widget.state)
        coVerify(exactly = 1) {
            dynamicPaymentFeeUseCase(any())
        }
    }
}
