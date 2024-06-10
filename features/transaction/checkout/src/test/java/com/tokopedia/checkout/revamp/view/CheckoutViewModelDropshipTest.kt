package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.revamp.view.widget.CheckoutDropshipWidget
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataModel
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnStateResponse
import com.tokopedia.purchase_platform.common.feature.addons.data.response.SaveAddOnsResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.unifycomponents.Toaster
import io.mockk.coEvery
import org.junit.Assert.assertEquals
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
        assertEquals("08181111", orderModel.dropshipPhone)
        assertEquals(true, orderModel.isDropshipPhoneValid)
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
        assertEquals("aaaaa", orderModel.dropshipName)
        assertEquals(true, orderModel.isDropshipNameValid)
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
        assertEquals(
            CheckoutPageToaster(Toaster.TYPE_NORMAL, toasterMessage = "Fitur dropshipper tidak dapat digunakan ketika menggunakan layanan tambahan"),
            latestToaster
        )
        assertEquals(
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
        assertEquals(
            CheckoutDropshipWidget.State.INIT,
            (viewModel.listData.value[5] as CheckoutOrderModel).stateDropship
        )

        assertEquals(
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
        assertEquals(
            true,
            (viewModel.listData.value[5] as CheckoutOrderModel).useDropship
        )
    }

    @Test
    fun validate_dropship_with_protection_add_on_opt_in() {
        // given
        val orderModel = CheckoutOrderModel(
            useDropship = true,
            cartStringGroup = "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "", isSelected = true))
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
        viewModel.setAddon(true, AddOnProductDataItemModel(uniqueId = "a2"), 4)

        // then
        assertEquals(
            AddOnProductDataModel(
                listAddOnProductData = arrayListOf(
                    AddOnProductDataItemModel(uniqueId = "a1", status = 0),
                    AddOnProductDataItemModel(uniqueId = "a2", status = 1, type = 4)
                )
            ),
            (viewModel.listData.value[4] as CheckoutProductModel).addOnProduct
        )
        assertEquals(
            CheckoutPageToaster(Toaster.TYPE_NORMAL, toasterMessage = "Fitur dropshipper tidak dapat digunakan ketika menggunakan layanan tambahan"),
            latestToaster
        )
        assertEquals(
            CheckoutDropshipWidget.State.DISABLED,
            (viewModel.listData.value[5] as CheckoutOrderModel).stateDropship
        )
    }

    @Test
    fun validate_dropship_without_protection_add_on_opt_in() {
        // given
        val orderModel = CheckoutOrderModel(
            useDropship = true,
            cartStringGroup = "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "", isSelected = true))
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
        viewModel.setAddon(false, AddOnProductDataItemModel(uniqueId = "a2"), 4)

        // then
        assertEquals(
            CheckoutDropshipWidget.State.INIT,
            (viewModel.listData.value[5] as CheckoutOrderModel).stateDropship
        )
    }

    @Test
    fun checkout_with_dropship_name_empty_should_show_toaster() {
        // Given
        val orderModel = CheckoutOrderModel(
            isDropshipperDisabled = false,
            dropshipPhone = "0818111111",
            isDropshipPhoneValid = true,
            useDropship = true,
            cartStringGroup = "123",
            shipment = CheckoutOrderShipment(
                courierItemData = CourierItemData(
                    logPromoCode = "",
                    isSelected = true,
                    isAllowDropshiper = true
                )
            )
        )
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
            orderModel,
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
        viewModel.checkout(false, "", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(false, invokeSuccess)
        assertEquals(
            CheckoutPageToaster(Toaster.TYPE_NORMAL, toasterMessage = "Pastikan Anda telah melengkapi informasi tambahan.", source = "local"),
            latestToaster
        )
        assertEquals(
            CheckoutDropshipWidget.State.ERROR,
            (viewModel.listData.value[6] as CheckoutOrderModel).stateDropship
        )
    }

    @Test
    fun checkout_with_dropship_complete_should_go_to_payment_page() {
        // Given
        val orderModel = CheckoutOrderModel(
            dropshipPhone = "0818111111",
            isDropshipPhoneValid = true,
            dropshipName = "aaaaaa",
            isDropshipNameValid = true,
            useDropship = true,
            cartStringGroup = "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "", isSelected = true))
        )
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
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData()),
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
        viewModel.checkout(false, "", { }, {
            invokeSuccess = true
        })

        // Then
        assertEquals(true, invokeSuccess)
    }
}
