package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.DetailsItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentViewModelCostTest : BaseShipmentViewModelTest() {

    @Test
    fun `GIVEN mixed error cart WHEN update cost THEN should calculate not error cart only`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        isError = false,
                        quantity = 1,
                        price = 1000.0
                    ),
                    CartItemModel(
                        cartStringGroup = "",
                        isError = true,
                        quantity = 2,
                        price = 1000.0
                    )
                )
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(1000.0, viewModel.shipmentCostModel.value.totalItemPrice, 0.0)
    }

    @Test
    fun `GIVEN mixed ppp cart WHEN update cost THEN should calculate cart with ppp price`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        isError = false,
                        quantity = 1,
                        price = 1000.0
                    ),
                    CartItemModel(
                        cartStringGroup = "",
                        isError = false,
                        quantity = 2,
                        price = 1000.0,
                        isProtectionOptIn = true,
                        protectionPrice = 200.0
                    )
                )
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(3000.0, viewModel.shipmentCostModel.value.totalItemPrice, 0.0)
        assertEquals(200.0, viewModel.shipmentCostModel.value.purchaseProtectionFee, 0.0)
        assertEquals(2, viewModel.shipmentCostModel.value.totalPurchaseProtectionItem)
    }

    @Test
    fun `GIVEN bundling cart WHEN update cost THEN should calculate cart with bundle price`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        bundlingItemPosition = ShipmentMapper.BUNDLING_ITEM_HEADER,
                        cartStringGroup = "",
                        isError = false,
                        quantity = 1,
                        price = 1000.0,
                        bundleQuantity = 1,
                        bundlePrice = 1500.0,
                        isBundlingItem = true
                    ),
                    CartItemModel(
                        bundlingItemPosition = ShipmentMapper.BUNDLING_ITEM_FOOTER,
                        cartStringGroup = "",
                        isError = false,
                        quantity = 1,
                        price = 1000.0,
                        isBundlingItem = true
                    )
                )
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(1500.0, viewModel.shipmentCostModel.value.totalItemPrice, 0.0)
        assertEquals(2, viewModel.shipmentCostModel.value.totalItem)
    }

    @Test
    fun `GIVEN add ons product level cart WHEN update cost THEN should calculate cart with add ons product level price`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        isError = false,
                        quantity = 1,
                        price = 1000.0,
                        addOnProductLevelModel = AddOnsDataModel(
                            status = 1,
                            addOnsDataItemModelList = listOf(
                                AddOnDataItemModel(
                                    addOnPrice = 100.0
                                )
                            )
                        )
                    )
                )
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(1000.0, viewModel.shipmentCostModel.value.totalItemPrice, 0.0)
        assertEquals(1100.0, viewModel.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(true, viewModel.shipmentCostModel.value.hasAddOn)
    }

    @Test
    fun `GIVEN add ons order level cart WHEN update cost THEN should calculate cart with add ons order level price`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        isError = false,
                        quantity = 1,
                        price = 1000.0
                    )
                ),
                addOnsOrderLevelModel = AddOnsDataModel(
                    status = 1,
                    addOnsDataItemModelList = listOf(
                        AddOnDataItemModel(
                            addOnPrice = 100.0
                        )
                    )
                )
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(1000.0, viewModel.shipmentCostModel.value.totalItemPrice, 0.0)
        assertEquals(1100.0, viewModel.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(true, viewModel.shipmentCostModel.value.hasAddOn)
    }

    @Test
    fun `GIVEN has loading item WHEN update checkout button THEN should return disabled`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        isError = false,
                        quantity = 1,
                        price = 1000.0
                    )
                ),
                selectedShipmentDetailData = ShipmentDetailData(selectedCourier = CourierItemData()),
                isStateLoadingCourierState = true
            )
        )

        // When
        viewModel.updateCheckoutButtonData()

        // Then
        assertEquals(false, viewModel.shipmentButtonPayment.value.enable)
    }

    @Test
    fun `WHEN reset promo benefit THEN should return empty promo benefit`() {
        // Given
        viewModel.shipmentCostModel.value = ShipmentCostModel(
            isHasDiscountDetails = true,
            discountAmount = 1,
            discountLabel = "discount",
            shippingDiscountAmount = 2,
            shippingDiscountLabel = "ship_discount",
            productDiscountAmount = 3,
            productDiscountLabel = "prod_discount",
            cashbackAmount = 4,
            cashbackLabel = "cashback"
        )

        // When
        viewModel.resetPromoBenefit()

        // Then
        assertEquals(false, viewModel.shipmentCostModel.value.isHasDiscountDetails)
        assertEquals(0, viewModel.shipmentCostModel.value.discountAmount)
        assertEquals("", viewModel.shipmentCostModel.value.discountLabel)
        assertEquals(0, viewModel.shipmentCostModel.value.shippingDiscountAmount)
        assertEquals("", viewModel.shipmentCostModel.value.shippingDiscountLabel)
        assertEquals(0, viewModel.shipmentCostModel.value.productDiscountAmount)
        assertEquals("", viewModel.shipmentCostModel.value.productDiscountLabel)
        assertEquals(0, viewModel.shipmentCostModel.value.cashbackAmount)
        assertEquals("", viewModel.shipmentCostModel.value.cashbackLabel)
    }

    @Test
    fun `Given summary promo with details WHEN set promo benefit THEN should set promo benefit with detail`() {
        // Given
        val summariesUiModels = listOf(
            SummariesItemUiModel(
                type = SummariesUiModel.TYPE_DISCOUNT,
                details = listOf(
                    DetailsItemUiModel(
                        type = SummariesUiModel.TYPE_SHIPPING_DISCOUNT,
                        amount = 1000,
                        description = "ship_discount"
                    ),
                    DetailsItemUiModel(
                        type = SummariesUiModel.TYPE_PRODUCT_DISCOUNT,
                        amount = 2000,
                        description = "prod_discount"
                    )
                )
            ),
            SummariesItemUiModel(
                type = SummariesUiModel.TYPE_CASHBACK,
                amount = 3000,
                description = "cashback"
            )
        )

        // When
        viewModel.setPromoBenefit(summariesUiModels)

        // Then
        assertEquals(true, viewModel.shipmentCostModel.value.isHasDiscountDetails)
        assertEquals(0, viewModel.shipmentCostModel.value.discountAmount)
        assertEquals(null, viewModel.shipmentCostModel.value.discountLabel)
        assertEquals(1000, viewModel.shipmentCostModel.value.shippingDiscountAmount)
        assertEquals("ship_discount", viewModel.shipmentCostModel.value.shippingDiscountLabel)
        assertEquals(2000, viewModel.shipmentCostModel.value.productDiscountAmount)
        assertEquals("prod_discount", viewModel.shipmentCostModel.value.productDiscountLabel)
        assertEquals(3000, viewModel.shipmentCostModel.value.cashbackAmount)
        assertEquals("cashback", viewModel.shipmentCostModel.value.cashbackLabel)
    }

    @Test
    fun `Given summary promo with no details & no courier set WHEN set promo benefit THEN should not set promo benefit`() {
        // Given
        val summariesUiModels = listOf(
            SummariesItemUiModel(
                type = SummariesUiModel.TYPE_DISCOUNT,
                amount = 1000,
                description = "disc"
            )
        )
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "")
        )

        // When
        viewModel.setPromoBenefit(summariesUiModels)

        // Then
        assertEquals(false, viewModel.shipmentCostModel.value.isHasDiscountDetails)
        assertEquals(0, viewModel.shipmentCostModel.value.discountAmount)
        assertEquals(null, viewModel.shipmentCostModel.value.discountLabel)
        assertEquals(0, viewModel.shipmentCostModel.value.shippingDiscountAmount)
        assertEquals(null, viewModel.shipmentCostModel.value.shippingDiscountLabel)
        assertEquals(0, viewModel.shipmentCostModel.value.productDiscountAmount)
        assertEquals(null, viewModel.shipmentCostModel.value.productDiscountLabel)
        assertEquals(0, viewModel.shipmentCostModel.value.cashbackAmount)
        assertEquals(null, viewModel.shipmentCostModel.value.cashbackLabel)
    }

    @Test
    fun `Given summary promo with no details & courier set WHEN set promo benefit THEN should set promo benefit with no details`() {
        // Given
        val summariesUiModels = listOf(
            SummariesItemUiModel(
                type = SummariesUiModel.TYPE_DISCOUNT,
                amount = 1000,
                description = "disc"
            )
        )
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )

        // When
        viewModel.setPromoBenefit(summariesUiModels)

        // Then
        assertEquals(false, viewModel.shipmentCostModel.value.isHasDiscountDetails)
        assertEquals(1000, viewModel.shipmentCostModel.value.discountAmount)
        assertEquals("disc", viewModel.shipmentCostModel.value.discountLabel)
        assertEquals(0, viewModel.shipmentCostModel.value.shippingDiscountAmount)
        assertEquals(null, viewModel.shipmentCostModel.value.shippingDiscountLabel)
        assertEquals(0, viewModel.shipmentCostModel.value.productDiscountAmount)
        assertEquals(null, viewModel.shipmentCostModel.value.productDiscountLabel)
        assertEquals(0, viewModel.shipmentCostModel.value.cashbackAmount)
        assertEquals(null, viewModel.shipmentCostModel.value.cashbackLabel)
    }

    @Test
    fun `GIVEN cart with mixed shipment WHEN update cost THEN should calculate shipment cost correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0
                    )
                ),
                selectedShipmentDetailData = ShipmentDetailData()
            ),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0
                    )
                ),
                selectedShipmentDetailData = ShipmentDetailData(selectedCourier = CourierItemData(shipperPrice = 1000))
            ),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0
                    )
                ),
                isError = true,
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(1000.0, viewModel.shipmentCostModel.value.shippingFee, 0.0)
    }

    @Test
    fun `GIVEN cart tradein dropoff with no shipment WHEN update cost THEN should calculate shipment cost correctly`() {
        // Given
        viewModel.isTradeIn = true
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0
                    )
                ),
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(0.0, viewModel.shipmentCostModel.value.shippingFee, 0.0)
    }

    @Test
    fun `GIVEN cart with cross sell WHEN update cost THEN should calculate cross sell cost correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0
                    )
                )
            )
        )
        viewModel.listShipmentCrossSellModel = arrayListOf(
            ShipmentCrossSellModel(isChecked = true, crossSellModel = CrossSellModel(price = 100.0)),
            ShipmentCrossSellModel(isChecked = false, crossSellModel = CrossSellModel(price = 100.0))
        )
        viewModel.shipmentNewUpsellModel = ShipmentNewUpsellModel(isShow = true, isSelected = true, price = 200)

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(2300.0, viewModel.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(2, viewModel.shipmentCostModel.value.listCrossSell.size)
    }

    @Test
    fun `GIVEN cart with donation WHEN update cost THEN should calculate donation cost correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0
                    )
                )
            )
        )
        viewModel.shipmentDonationModel = ShipmentDonationModel(Donation(nominal = 3000), isChecked = true)

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(5000.0, viewModel.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(3000.0, viewModel.shipmentCostModel.value.donation, 0.0)
    }

    @Test
    fun `GIVEN cart with donation unchecked WHEN update cost THEN should calculate donation cost correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0
                    )
                )
            )
        )
        viewModel.shipmentDonationModel = ShipmentDonationModel(Donation(nominal = 3000), isChecked = false)

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(2000.0, viewModel.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(0.0, viewModel.shipmentCostModel.value.donation, 0.0)
    }

    @Test
    fun `GIVEN cart with donation changed to unchecked WHEN update cost THEN should calculate donation cost correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0
                    )
                )
            )
        )
        viewModel.shipmentDonationModel = ShipmentDonationModel(Donation(nominal = 3000), isChecked = true)
        viewModel.updateShipmentCostModel()
        viewModel.shipmentDonationModel = ShipmentDonationModel(Donation(nominal = 3000), isChecked = false)

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(2000.0, viewModel.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(0.0, viewModel.shipmentCostModel.value.donation, 0.0)
    }

    @Test
    fun `GIVEN cart with trade in price WHEN update cost THEN should calculate total cost correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0,
                        isValidTradeIn = true,
                        oldDevicePrice = 1000
                    )
                )
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(1000.0, viewModel.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(1000.0, viewModel.shipmentCostModel.value.tradeInPrice, 0.0)
    }

    @Test
    fun `GIVEN cart with leasing price WHEN update cost THEN should calculate total cost correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = ""),
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 2,
                        price = 1000.0
                    )
                ),
                isLeasingProduct = true,
                bookingFee = 1000
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(3000.0, viewModel.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(1000, viewModel.shipmentCostModel.value.bookingFee)
    }
}
