package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutViewModelCalculatorTest : BaseCheckoutViewModelTest() {

    @Test
    fun calculate_total_without_shipping() {
        // given
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
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.calculateTotal()

        // then
        assertEquals(CheckoutCostModel(), viewModel.listData.value.cost())
    }

    @Test
    fun `GIVEN mixed error cart WHEN update cost THEN should calculate not error cart only`() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", isError = false, quantity = 1, price = 1000.0),
            CheckoutProductModel("123", isError = true, quantity = 2, price = 1000.0),
            CheckoutOrderModel("123"),
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
    }

    @Test
    fun `GIVEN bundling cart WHEN update cost THEN should calculate cart with bundle price`() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                bundlingItemPosition = ShipmentMapper.BUNDLING_ITEM_HEADER,
                isError = false,
                quantity = 1,
                price = 1000.0,
                bundleQuantity = 1,
                bundlePrice = 1500.0,
                isBundlingItem = true
            ),
            CheckoutProductModel(
                "123",
                bundlingItemPosition = ShipmentMapper.BUNDLING_ITEM_FOOTER,
                isError = false,
                quantity = 1,
                price = 1000.0,
                isBundlingItem = true
            ),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(1500.0, viewModel.listData.value.cost()!!.finalItemPrice, 0.0)
        assertEquals(2, viewModel.listData.value.cost()!!.totalItem)
    }

    @Test
    fun `GIVEN single offer bmgm cart WHEN update cost THEN should calculate cart with bmgm total discount price`() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                isError = false,
                quantity = 1,
                price = 1000.0,
                isBMGMItem = true,
                shouldShowBmgmInfo = true,
                bmgmOfferName = "tokopedia 1",
                bmgmOfferMessage = listOf("jakarta"),
                bmgmTotalDiscount = 500.0,
                bmgmItemPosition = ShipmentMapper.BMGM_ITEM_HEADER
            ),
            CheckoutProductModel(
                "123",
                isError = false,
                quantity = 1,
                price = 2000.0,
                isBMGMItem = true,
                shouldShowBmgmInfo = true,
                bmgmOfferName = "tokopedia 2",
                bmgmOfferMessage = listOf("medan"),
                bmgmTotalDiscount = 500.0,
                bmgmItemPosition = ShipmentMapper.BMGM_ITEM_DEFAULT
            ),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(2500.0, viewModel.listData.value.cost()!!.finalItemPrice, 0.0)
        assertEquals(2, viewModel.listData.value.cost()!!.totalItem)
    }

    @Test
    fun `GIVEN multi offer bmgm cart WHEN update cost THEN should calculate cart with bmgm total with correct discount price`() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                isError = false,
                quantity = 1,
                price = 1000.0,
                isBMGMItem = true,
                shouldShowBmgmInfo = true,
                bmgmOfferName = "tokopedia 1",
                bmgmOfferMessage = listOf("jakarta"),
                bmgmTotalDiscount = 500.0,
                bmgmItemPosition = ShipmentMapper.BMGM_ITEM_HEADER
            ),
            CheckoutProductModel(
                "123",
                isError = false,
                quantity = 1,
                price = 2000.0,
                isBMGMItem = true,
                shouldShowBmgmInfo = false,
                bmgmOfferName = "tokopedia 2",
                bmgmOfferMessage = listOf("jakarta"),
                bmgmTotalDiscount = 500.0,
                bmgmItemPosition = ShipmentMapper.BMGM_ITEM_DEFAULT
            ),
            CheckoutProductModel(
                "123",
                isError = false,
                quantity = 1,
                price = 1000.0,
                isBMGMItem = true,
                shouldShowBmgmInfo = true,
                bmgmOfferName = "tokopedia 3",
                bmgmOfferMessage = listOf("medan"),
                bmgmTotalDiscount = 600.0,
                bmgmItemPosition = ShipmentMapper.BMGM_ITEM_HEADER
            ),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.calculateTotal()

        // Then
        assertEquals(2900.0, viewModel.listData.value.cost()!!.finalItemPrice, 0.0)
        assertEquals(3, viewModel.listData.value.cost()!!.totalItem)
    }

    @Test
    fun `GIVEN add ons product level cart WHEN update cost THEN should calculate cart with add ons product level price`() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                isError = false,
                quantity = 1,
                price = 1000.0,
                addOnGiftingProductLevelModel = AddOnGiftingDataModel(
                    status = 1,
                    addOnsDataItemModelList = listOf(
                        AddOnGiftingDataItemModel(
                            addOnPrice = 100.0
                        )
                    )
                )
            ),
            CheckoutOrderModel("123"),
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
        assertEquals(1100.0, viewModel.listData.value.cost()!!.totalPrice, 0.0)
        assertEquals(true, viewModel.listData.value.cost()!!.hasAddOn)
    }

    @Test
    fun `GIVEN add ons order level cart WHEN update cost THEN should calculate cart with add ons order level price`() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", isError = false, quantity = 1, price = 1000.0),
            CheckoutOrderModel(
                "123",
                addOnsOrderLevelModel = AddOnGiftingDataModel(
                    status = 1,
                    addOnsDataItemModelList = listOf(
                        AddOnGiftingDataItemModel(
                            addOnPrice = 100.0
                        )
                    )
                )
            ),
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
        assertEquals(1100.0, viewModel.listData.value.cost()!!.totalPrice, 0.0)
        assertEquals(true, viewModel.listData.value.cost()!!.hasAddOn)
    }
}
