package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.revamp.view.widget.CheckoutDropshipWidget
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.unifycomponents.Toaster
import org.junit.Assert
import org.junit.Test

class CheckoutViewModelDropshipTest : BaseCheckoutViewModelTest() {
    @Test
    fun set_validation_dropship_phone() {
        // given
        val orderModel = CheckoutOrderModel(
            "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = ""))
        )
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel(
                    voucherOrders = listOf(
                        LastApplyVoucherOrdersItemUiModel(
                            code = "",
                            "12",
                            message = LastApplyMessageUiModel(state = "green"),
                            type = "logistic",
                            cartStringGroup = "123"
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.setValidationDropshipPhone("08181111", true, 5)

        // then
        Assert.assertEquals("08181111", orderModel.dropshipPhone)
        Assert.assertEquals(true, orderModel.isDropshipPhoneValid)
    }

    @Test
    fun set_validation_dropship_name() {
        // given
        val orderModel = CheckoutOrderModel(
            "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = ""))
        )
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel(
                    voucherOrders = listOf(
                        LastApplyVoucherOrdersItemUiModel(
                            code = "",
                            "12",
                            message = LastApplyMessageUiModel(state = "green"),
                            type = "logistic",
                            cartStringGroup = "123"
                        )
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.setValidationDropshipName("aaaaa", true, 5)

        // then
        Assert.assertEquals("aaaaa", orderModel.dropshipName)
        Assert.assertEquals(true, orderModel.isDropshipNameValid)
    }

    @Test
    fun validate_toaster_when_select_dropship_but_protection_addon_is_selected() {
        // given
        val orderModel = CheckoutOrderModel(
            "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = ""))
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
            CheckoutProductModel(
                "123",
                addOnProduct = AddOnProductDataModel(
                    listAddOnProductData = arrayListOf(
                        AddOnProductDataItemModel(uniqueId = "a1", status = 0),
                        AddOnProductDataItemModel(uniqueId = "a2", status = 1, type = 4)
                    )
                )
            ),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.setDropshipSwitch(true, 5)

        // then
        Assert.assertEquals(
            CheckoutPageToaster(Toaster.TYPE_NORMAL, toasterMessage = "Fitur dropshipper tidak dapat digunakan ketika menggunakan layanan tambahan"),
            latestToaster
        )
        Assert.assertEquals(
            CheckoutDropshipWidget.State.DISABLED,
            (viewModel.listData.value[5] as CheckoutOrderModel).stateDropship
        )
    }

    @Test
    fun validate_init_state_and_flag_dropship_when_default_dropship() {
        // given
        val orderModel = CheckoutOrderModel(
            "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = ""))
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
            CheckoutProductModel(
                "123",
                addOnProduct = AddOnProductDataModel(
                    listAddOnProductData = arrayListOf(
                        AddOnProductDataItemModel(uniqueId = "a1", status = 0),
                        AddOnProductDataItemModel(uniqueId = "a2", status = 1, type = 4)
                    )
                )
            ),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.setDropshipSwitch(false, 5)

        // then
        Assert.assertEquals(
            CheckoutDropshipWidget.State.INIT,
            (viewModel.listData.value[5] as CheckoutOrderModel).stateDropship
        )

        Assert.assertEquals(
            false,
            (viewModel.listData.value[5] as CheckoutOrderModel).useDropship
        )
    }

    @Test
    fun validate_flag_dropship_when_selected() {
        // given
        val orderModel = CheckoutOrderModel(
            "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = ""))
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
            CheckoutProductModel(
                "123",
                addOnProduct = AddOnProductDataModel(
                    listAddOnProductData = arrayListOf(
                        AddOnProductDataItemModel(uniqueId = "a1", status = 0),
                        AddOnProductDataItemModel(uniqueId = "a2", status = 0, type = 4)
                    )
                )
            ),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.setDropshipSwitch(true, 5)

        // then
        Assert.assertEquals(
            true,
            (viewModel.listData.value[5] as CheckoutOrderModel).useDropship
        )
    }
}
