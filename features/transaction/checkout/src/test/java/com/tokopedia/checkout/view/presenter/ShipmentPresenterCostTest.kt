package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
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
        presenter.updateShipmentCostModel()

        // Then
        assertEquals(1000.0, presenter.shipmentCostModel.value.totalItemPrice, 0.0)
        assertEquals(1100.0, presenter.shipmentCostModel.value.totalPrice, 0.0)
        assertEquals(true, presenter.shipmentCostModel.value.hasAddOn)
    }
}
