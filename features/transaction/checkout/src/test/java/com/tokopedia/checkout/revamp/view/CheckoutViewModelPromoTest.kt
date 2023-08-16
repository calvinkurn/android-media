package com.tokopedia.checkout.revamp.view

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
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutViewModelPromoTest : BaseCheckoutViewModelTest() {

    @Test
    fun validate_clear_all_bo() {
        // given
        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel(
            SuccessDataUiModel(true)
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200"
        )

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
            CheckoutProductModel("123", cartStringOrder = "12"),
            CheckoutOrderModel(
                "123",
                boCode = "boCode",
                boUniqueId = "12",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "boCode"))
            ),
            CheckoutProductModel("234", cartStringOrder = "23"),
            CheckoutOrderModel(
                "234",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel(
                    voucherOrders = listOf(
                        LastApplyVoucherOrdersItemUiModel(
                            code = "boCode",
                            message = LastApplyMessageUiModel(state = "green")
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.validateClearAllBoPromo(
            ValidateUsePromoRequest(
                orders = listOf(
                    OrdersItem(cartStringGroup = "123"),
                    OrdersItem(cartStringGroup = "234")
                )
            ),
            PromoUiModel()
        )

        // then
        assertEquals(
            listOf(
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
                CheckoutProductModel("123", cartStringOrder = "12"),
                CheckoutOrderModel(
                    "123",
                    boCode = "boCode",
                    boUniqueId = "12",
                    shipment = CheckoutOrderShipment()
                ),
                CheckoutProductModel("234", cartStringOrder = "23"),
                CheckoutOrderModel(
                    "234",
                    shipment = CheckoutOrderShipment(courierItemData = CourierItemData())
                ),
                CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
                CheckoutPromoModel(promo = LastApplyUiModel()),
                CheckoutCostModel(totalPriceString = "Rp0"),
                CheckoutCrossSellGroupModel(),
                CheckoutButtonPaymentModel(enable = true, totalPrice = "Rp0")
            ),
            viewModel.listData.value
        )
    }
}
