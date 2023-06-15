package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.DetailsItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentPresenterCostTest : BaseShipmentPresenterTest() {

    @Test
    fun `GIVEN mixed error cart WHEN update cost THEN should calculate not error cart only`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
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
        presenter.updateShipmentCostModel()

        // Then
        assertEquals(1000.0, presenter.shipmentCostModel.value.totalItemPrice, 0.0)
    }

    @Test
    fun `GIVEN mixed ppp cart WHEN update cost THEN should calculate cart with ppp price`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
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
        presenter.updateShipmentCostModel()

        // Then
        assertEquals(3000.0, presenter.shipmentCostModel.value.totalItemPrice, 0.0)
        assertEquals(200.0, presenter.shipmentCostModel.value.purchaseProtectionFee, 0.0)
        assertEquals(2, presenter.shipmentCostModel.value.totalPurchaseProtectionItem)
    }

    @Test
    fun `GIVEN bundling cart WHEN update cost THEN should calculate cart with bundle price`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
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
        presenter.updateShipmentCostModel()

        // Then
        assertEquals(1500.0, presenter.shipmentCostModel.value.totalItemPrice, 0.0)
        assertEquals(2, presenter.shipmentCostModel.value.totalItem)
    }

    @Test
    fun `GIVEN add ons product level cart WHEN update cost THEN should calculate cart with add ons product level price`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
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
                    )
                )
            )
        )

        // When
        presenter.updateShipmentCostModel()

        // Then
        assertEquals(1000.0, presenter.shipmentCostModel.value.totalItemPrice, 0.0)
        assertEquals(1100.0, presenter.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(true, presenter.shipmentCostModel.value.hasAddOn)
    }

    @Test
    fun `GIVEN has loading item WHEN update checkout button THEN should return disabled`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
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
        presenter.updateCheckoutButtonData()

        // Then
        assertEquals(false, presenter.shipmentButtonPayment.value.enable)
    }

    @Test
    fun `WHEN reset promo benefit THEN should return empty promo benefit`() {
        // Given
        presenter.shipmentCostModel.value = ShipmentCostModel(
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
        presenter.resetPromoBenefit()

        // Then
        assertEquals(false, presenter.shipmentCostModel.value.isHasDiscountDetails)
        assertEquals(0, presenter.shipmentCostModel.value.discountAmount)
        assertEquals("", presenter.shipmentCostModel.value.discountLabel)
        assertEquals(0, presenter.shipmentCostModel.value.shippingDiscountAmount)
        assertEquals("", presenter.shipmentCostModel.value.shippingDiscountLabel)
        assertEquals(0, presenter.shipmentCostModel.value.productDiscountAmount)
        assertEquals("", presenter.shipmentCostModel.value.productDiscountLabel)
        assertEquals(0, presenter.shipmentCostModel.value.cashbackAmount)
        assertEquals("", presenter.shipmentCostModel.value.cashbackLabel)
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
        presenter.setPromoBenefit(summariesUiModels)

        // Then
        assertEquals(true, presenter.shipmentCostModel.value.isHasDiscountDetails)
        assertEquals(0, presenter.shipmentCostModel.value.discountAmount)
        assertEquals(null, presenter.shipmentCostModel.value.discountLabel)
        assertEquals(1000, presenter.shipmentCostModel.value.shippingDiscountAmount)
        assertEquals("ship_discount", presenter.shipmentCostModel.value.shippingDiscountLabel)
        assertEquals(2000, presenter.shipmentCostModel.value.productDiscountAmount)
        assertEquals("prod_discount", presenter.shipmentCostModel.value.productDiscountLabel)
        assertEquals(3000, presenter.shipmentCostModel.value.cashbackAmount)
        assertEquals("cashback", presenter.shipmentCostModel.value.cashbackLabel)
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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartStringGroup = "")
        )

        // When
        presenter.setPromoBenefit(summariesUiModels)

        // Then
        assertEquals(false, presenter.shipmentCostModel.value.isHasDiscountDetails)
        assertEquals(0, presenter.shipmentCostModel.value.discountAmount)
        assertEquals(null, presenter.shipmentCostModel.value.discountLabel)
        assertEquals(0, presenter.shipmentCostModel.value.shippingDiscountAmount)
        assertEquals(null, presenter.shipmentCostModel.value.shippingDiscountLabel)
        assertEquals(0, presenter.shipmentCostModel.value.productDiscountAmount)
        assertEquals(null, presenter.shipmentCostModel.value.productDiscountLabel)
        assertEquals(0, presenter.shipmentCostModel.value.cashbackAmount)
        assertEquals(null, presenter.shipmentCostModel.value.cashbackLabel)
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
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )

        // When
        presenter.setPromoBenefit(summariesUiModels)

        // Then
        assertEquals(false, presenter.shipmentCostModel.value.isHasDiscountDetails)
        assertEquals(1000, presenter.shipmentCostModel.value.discountAmount)
        assertEquals("disc", presenter.shipmentCostModel.value.discountLabel)
        assertEquals(0, presenter.shipmentCostModel.value.shippingDiscountAmount)
        assertEquals(null, presenter.shipmentCostModel.value.shippingDiscountLabel)
        assertEquals(0, presenter.shipmentCostModel.value.productDiscountAmount)
        assertEquals(null, presenter.shipmentCostModel.value.productDiscountLabel)
        assertEquals(0, presenter.shipmentCostModel.value.cashbackAmount)
        assertEquals(null, presenter.shipmentCostModel.value.cashbackLabel)
    }

    @Test
    fun `GIVEN cart with mixed shipment WHEN update cost THEN should calculate shipment cost correctly`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
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
        presenter.updateShipmentCostModel()

        // Then
        assertEquals(1000.0, presenter.shipmentCostModel.value.shippingFee, 0.0)
    }

    @Test
    fun `GIVEN cart tradein dropoff with no shipment WHEN update cost THEN should calculate shipment cost correctly`() {
        // Given
        presenter.isTradeIn = true
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        presenter.shipmentCartItemModelList = listOf(
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
        presenter.updateShipmentCostModel()

        // Then
        assertEquals(0.0, presenter.shipmentCostModel.value.shippingFee, 0.0)
    }

    @Test
    fun `GIVEN cart with cross sell WHEN update cost THEN should calculate cross sell cost correctly`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
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
        presenter.listShipmentCrossSellModel = arrayListOf(
            ShipmentCrossSellModel(isChecked = true, crossSellModel = CrossSellModel(price = 100.0)),
            ShipmentCrossSellModel(isChecked = false, crossSellModel = CrossSellModel(price = 100.0))
        )
        presenter.shipmentNewUpsellModel = ShipmentNewUpsellModel(isShow = true, isSelected = true, price = 200)

        // When
        presenter.updateShipmentCostModel()

        // Then
        assertEquals(2300.0, presenter.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(2, presenter.shipmentCostModel.value.listCrossSell.size)
    }
}
