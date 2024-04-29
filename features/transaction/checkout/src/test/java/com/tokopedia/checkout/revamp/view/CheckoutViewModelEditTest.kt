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
import com.tokopedia.checkoutpayment.domain.PaymentWidgetListData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetState
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class CheckoutViewModelEditTest: BaseCheckoutViewModelTest() {

    @Test
    fun `GIVEN success update cart WHEN set product note THEN should set note`() {
        // GIVEN
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
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal), enable = true, data = PaymentWidgetListData()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        // WHEN
        viewModel.setProductNote("abc", 4)

        // THEN
        assertEquals("abc", (viewModel.listData.value[4] as CheckoutProductModel).noteToSeller)
    }
    @Test
    fun `GIVEN failed update cart WHEN set product note THEN should set note`() {
        // GIVEN
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
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(state = CheckoutPaymentWidgetState.Normal), enable = true, data = PaymentWidgetListData()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } throws IOException()

        // WHEN
        viewModel.setProductNote("abc", 4)

        // THEN
        assertEquals("abc", (viewModel.listData.value[4] as CheckoutProductModel).noteToSeller)
    }
}
