package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.checkout.CheckoutData
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
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

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
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
//        viewModel.shipmentCartItemModelList = listOf(
//            ShipmentCartItemModel(cartStringGroup = "").apply {
//                cartItemModels = listOf(CartItemModel(cartStringGroup = ""))
//                selectedShipmentDetailData = ShipmentDetailData(
//                    selectedCourier = CourierItemData()
//                )
//            }
//        )
//        viewModel.recipientAddressModel = RecipientAddressModel().apply {
//            id = "1"
//        }
//        viewModel.listShipmentCrossSellModel = arrayListOf()
//        val uploadModel = mockk<UploadPrescriptionUiModel>(relaxed = true)
//        viewModel.setUploadPrescriptionData(uploadModel)

        coEvery { validateUsePromoRevampUseCase.setParam(any()).executeOnBackground() } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val transactionId = "1234"
        coEvery { checkoutGqlUseCase(any()) } returns
            CheckoutData().apply {
                this.transactionId = transactionId
            }
        var invokeSuccess = false

        // When
        viewModel.checkout("", { b -> }, { checkoutResult ->
            invokeSuccess = true
        })

        // Then
        assertEquals(true, invokeSuccess)
    }
}
