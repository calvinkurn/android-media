package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentViewModelEgoldTest : BaseShipmentViewModelTest() {

    @Test
    fun `GIVEN eligible egold WHEN set shipment cost model THEN should update egold value`() {
        // Given
        val expectedEgoldValue = 250L
        viewModel.egoldAttributeModel.value = EgoldAttributeModel().apply {
            isEligible = true
            isTiering = false
            minEgoldRange = 50
            maxEgoldRange = 1000
            isChecked = true
        }
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 1,
                        price = 99750.0
                    )
                )
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(expectedEgoldValue, viewModel.egoldAttributeModel.value!!.buyEgoldValue)
        assertEquals(viewModel.shipmentCostModel.value.emasPrice, viewModel.egoldAttributeModel.value!!.buyEgoldValue.toDouble(), 0.0)
    }

    @Test
    fun `GIVEN ineligible egold WHEN set shipment cost model THEN should update egold value`() {
        // Given
        val expectedEgoldValue = 0L
        viewModel.egoldAttributeModel.value = EgoldAttributeModel().apply {
            isEligible = false
            isTiering = false
            minEgoldRange = 50
            maxEgoldRange = 1000
        }
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 1,
                        price = 99750.0
                    )
                )
            )
        )

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(expectedEgoldValue, viewModel.egoldAttributeModel.value!!.buyEgoldValue)
    }

    @Test
    fun `WHEN calculate egold value without tiering THEN should correctly calculated`() {
        // Given
        val expectedEgoldValue = 250L
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 1,
                        price = 99750.0
                    )
                )
            )
        )
        viewModel.egoldAttributeModel.value = EgoldAttributeModel().apply {
            isEligible = true
            isTiering = false
            minEgoldRange = 50
            maxEgoldRange = 1000
        }

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(expectedEgoldValue, viewModel.egoldAttributeModel.value!!.buyEgoldValue)
    }

    @Test
    fun `WHEN calculate egold value with tiering THEN should correctly calculated`() {
        // Given
        val expectedEgoldValue = 5700L
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "",
                cartItemModels = listOf(
                    CartItemModel(
                        cartStringGroup = "",
                        quantity = 1,
                        price = 50300.0
                    )
                )
            )
        )
        viewModel.egoldAttributeModel.value = EgoldAttributeModel().apply {
            isEligible = true
            isTiering = true
            minEgoldRange = 50
            maxEgoldRange = 1000
            egoldTieringModelArrayList = arrayListOf<EgoldTieringModel>().apply {
                add(
                    EgoldTieringModel().apply {
                        minTotalAmount = 500
                        minAmount = 2000
                        maxAmount = 5999
                        basisAmount = 4000
                    }
                )
                add(
                    EgoldTieringModel().apply {
                        minTotalAmount = 98001
                        minAmount = 2000
                        maxAmount = 11999
                        basisAmount = 10000
                    }
                )
                add(
                    EgoldTieringModel().apply {
                        minTotalAmount = 998001
                        minAmount = 2000
                        maxAmount = 51999
                        basisAmount = 50000
                    }
                )
            }
        }

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(expectedEgoldValue, viewModel.egoldAttributeModel.value!!.buyEgoldValue)
    }

    @Test
    fun `GIVEN no match minimum total amount WHEN calculate egold value with tiering THEN should result 0 egold value`() {
        // Given
        val expectedEgoldValue = 0L
        viewModel.shipmentCostModel.value =
            ShipmentCostModel().apply {
                totalPrice = 300.0
            }
        viewModel.egoldAttributeModel.value = EgoldAttributeModel().apply {
            isEligible = true
            isTiering = true
            minEgoldRange = 50
            maxEgoldRange = 1000
            egoldTieringModelArrayList = arrayListOf<EgoldTieringModel>().apply {
                add(
                    EgoldTieringModel().apply {
                        minTotalAmount = 500
                        minAmount = 2000
                        maxAmount = 5999
                        basisAmount = 4000
                    }
                )
                add(
                    EgoldTieringModel().apply {
                        minTotalAmount = 98001
                        minAmount = 2000
                        maxAmount = 11999
                        basisAmount = 10000
                    }
                )
                add(
                    EgoldTieringModel().apply {
                        minTotalAmount = 998001
                        minAmount = 2000
                        maxAmount = 51999
                        basisAmount = 50000
                    }
                )
            }
        }

        // When
        viewModel.updateShipmentCostModel()

        // Then
        assertEquals(expectedEgoldValue, viewModel.egoldAttributeModel.value!!.buyEgoldValue)
    }
}
