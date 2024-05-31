package com.tokopedia.checkout.revamp.view

import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
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

class CheckoutViewModelEditTest : BaseCheckoutViewModelTest() {

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
        assertEquals("", (viewModel.listData.value[4] as CheckoutProductModel).noteToSeller)
    }

    @Test
    fun `GIVEN show lottie WHEN hide lottie THEN flag set shouldShowLottieNotes`() {
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
            CheckoutProductModel("123", shouldShowLottieNotes = true),
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
        viewModel.hideNoteLottie(4)

        // THEN
        assertEquals(false, (viewModel.listData.value[4] as CheckoutProductModel).shouldShowLottieNotes)
    }

    @Test
    fun `GIVEN success update quantity WHEN change quantity product THEN should set quantity product`() {
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
            CheckoutProductModel("123", cartId = 12, quantity = 1, invenageValue = 999, minOrder = 1, maxOrder = 30000, switchInvenage = 1),
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

        val responseSaf = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product(
                                            cartId = 12,
                                            productQuantity = 5,
                                            productInvenageValue = 999,
                                            productMinOrder = 1,
                                            productMaxOrder = 30000,
                                            productSwitchInvenage = 1
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns responseSaf

        // WHEN
        viewModel.updateQuantityProduct(12, 5)

        // THEN
        assertEquals(5, (viewModel.listData.value[4] as CheckoutProductModel).quantity)
    }

    @Test
    fun `GIVEN success update quantity WHEN change quantity product exceed maxOrder value and switchInvenage == 1 THEN should set quantity product with invenage value`() {
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
            CheckoutProductModel("123", cartId = 12, quantity = 1, invenageValue = 100, minOrder = 1, maxOrder = 300, switchInvenage = 1),
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

        val responseSaf = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product(
                                            cartId = 12,
                                            productQuantity = 100,
                                            productInvenageValue = 100,
                                            productMinOrder = 1,
                                            productMaxOrder = 300,
                                            productSwitchInvenage = 1
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns responseSaf

        // WHEN
        viewModel.updateQuantityProduct(12, 105)

        // THEN
        assertEquals(100, (viewModel.listData.value[4] as CheckoutProductModel).quantity)
    }

    @Test
    fun `GIVEN success update quantity WHEN change quantity product exceed maxOrder value and switchInvenage == 0 THEN should set quantity product with maxOrder value`() {
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
            CheckoutProductModel("123", cartId = 12, quantity = 1, invenageValue = 999, minOrder = 1, maxOrder = 30, switchInvenage = 0),
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

        val responseSaf = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product(
                                            cartId = 12,
                                            productQuantity = 30,
                                            productInvenageValue = 999,
                                            productMinOrder = 1,
                                            productMaxOrder = 30,
                                            productSwitchInvenage = 0
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns responseSaf

        // WHEN
        viewModel.updateQuantityProduct(12, 50)

        // THEN
        assertEquals(30, (viewModel.listData.value[4] as CheckoutProductModel).quantity)
    }

    @Test
    fun `GIVEN success update quantity WHEN change quantity product with switchInvenage == 1 and invenage value exceed max order THEN should set quantity product with max order value`() {
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
            CheckoutProductModel("123", cartId = 12, quantity = 1, invenageValue = 100, minOrder = 1, maxOrder = 300, switchInvenage = 1),
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

        val responseSaf = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product(
                                            cartId = 12,
                                            productQuantity = 100,
                                            productInvenageValue = 300,
                                            productMinOrder = 1,
                                            productMaxOrder = 100,
                                            productSwitchInvenage = 1
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns responseSaf

        // WHEN
        viewModel.updateQuantityProduct(12, 105)

        // THEN
        assertEquals(100, (viewModel.listData.value[4] as CheckoutProductModel).quantity)
    }

    @Test
    fun `GIVEN success update quantity WHEN change quantity product below min order value THEN should set quantity product with min order value`() {
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
            CheckoutProductModel("123", cartId = 12, quantity = 1, invenageValue = 100, minOrder = 5, maxOrder = 300, switchInvenage = 1),
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

        val responseSaf = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product(
                                            cartId = 12,
                                            productQuantity = 5,
                                            productInvenageValue = 300,
                                            productMinOrder = 5,
                                            productMaxOrder = 100,
                                            productSwitchInvenage = 1
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns responseSaf

        // WHEN
        viewModel.updateQuantityProduct(12, 3)

        // THEN
        assertEquals(5, (viewModel.listData.value[4] as CheckoutProductModel).quantity)
    }

    @Test
    fun `GIVEN success update quantity WHEN change invenage value is larger than maxOrder value THEN should set quantity product with min order value`() {
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
            CheckoutProductModel("123", cartId = 12, quantity = 1, invenageValue = 100, minOrder = 5, maxOrder = 50, switchInvenage = 1),
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

        val responseSaf = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product(
                                            cartId = 12,
                                            productQuantity = 50,
                                            productInvenageValue = 300,
                                            productMinOrder = 5,
                                            productMaxOrder = 50,
                                            productSwitchInvenage = 1
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns responseSaf

        // WHEN
        viewModel.updateQuantityProduct(12, 3)

        // THEN
        assertEquals(50, (viewModel.listData.value[4] as CheckoutProductModel).quantity)
    }

    @Test
    fun `GIVEN failed update quantity WHEN change quantity product below min order value THEN should set quantity product with min order value`() {
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
            CheckoutProductModel("123", cartId = 12, quantity = 1, prevQuantity = 1, invenageValue = 100, minOrder = 1, maxOrder = 300, switchInvenage = 1),
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
        } returns UpdateCartV2Data(status = "ERROR", data = Data(status = false))

        // WHEN
        viewModel.updateQuantityProduct(12, 3)

        // THEN
        assertEquals(1, (viewModel.listData.value[4] as CheckoutProductModel).quantity)
    }

    @Test
    fun `GIVEN failed update quantity WHEN cart id is not found`() {
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
            CheckoutProductModel("123", cartId = 12, quantity = 1, prevQuantity = 1, invenageValue = 100, minOrder = 1, maxOrder = 300, switchInvenage = 1),
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

        // WHEN
        viewModel.updateQuantityProduct(10, 3)

        // THEN
        assertEquals(1, (viewModel.listData.value[4] as CheckoutProductModel).quantity)
    }
}
