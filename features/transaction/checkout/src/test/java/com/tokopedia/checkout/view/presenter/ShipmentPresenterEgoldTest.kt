package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import org.junit.Test

class ShipmentPresenterEgoldTest : BaseShipmentPresenterTest() {

    @Test
    fun `GIVEN eligible egold WHEN set shipment cost model THEN should update egold value`() {
        // Given
        val expectedEgoldValue = 250L
        presenter.egoldAttributeModel.value = EgoldAttributeModel().apply {
            isEligible = true
            isTiering = false
            minEgoldRange = 50
            maxEgoldRange = 1000
        }

        // When
        presenter.shipmentCostModel.value =
            ShipmentCostModel().apply {
                totalPrice = 99750.0
            }

        // Then
//        verify {
//            view.renderDataChanged()
//        }
        assert(presenter.egoldAttributeModel.value!!.buyEgoldValue == expectedEgoldValue)
    }

    @Test
    fun `GIVEN ineligible egold WHEN set shipment cost model THEN should update egold value`() {
        // Given
        val expectedEgoldValue = 0L
        presenter.egoldAttributeModel.value = EgoldAttributeModel().apply {
            isEligible = false
            isTiering = false
            minEgoldRange = 50
            maxEgoldRange = 1000
        }

        // When
        presenter.shipmentCostModel.value =
            ShipmentCostModel().apply {
                totalPrice = 99750.0
            }

        // Then
//        verify(inverse = true) {
//            view.renderDataChanged()
//        }
        assert(presenter.egoldAttributeModel.value!!.buyEgoldValue == expectedEgoldValue)
    }

    @Test
    fun `WHEN calculate egold value without tiering THEN should correctly calculated`() {
        // Given
        val expectedEgoldValue = 250L
        presenter.shipmentCostModel.value =
            ShipmentCostModel().apply {
                totalPrice = 99750.0
            }
        presenter.egoldAttributeModel.value = EgoldAttributeModel().apply {
            isTiering = false
            minEgoldRange = 50
            maxEgoldRange = 1000
        }

        // When
//        presenter.updateEgoldBuyValue()

        // Then
//        verify {
//            view.renderDataChanged()
//        }
        assert(presenter.egoldAttributeModel.value!!.buyEgoldValue == expectedEgoldValue)
    }

    @Test
    fun `WHEN calculate egold value with tiering THEN should correctly calculated`() {
        // Given
        val expectedEgoldValue = 5700L
        presenter.shipmentCostModel.value =
            ShipmentCostModel().apply {
                totalPrice = 50300.0
            }
        presenter.egoldAttributeModel.value = EgoldAttributeModel().apply {
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
//        presenter.updateEgoldBuyValue()

        // Then
//        verify {
//            view.renderDataChanged()
//        }
        assert(presenter.egoldAttributeModel.value!!.buyEgoldValue == expectedEgoldValue)
    }

    @Test
    fun `GIVEN no match minimum total amount WHEN calculate egold value with tiering THEN should result 0 egold value`() {
        // Given
        val expectedEgoldValue = 0L
        presenter.shipmentCostModel.value =
            ShipmentCostModel().apply {
                totalPrice = 300.0
            }
        presenter.egoldAttributeModel.value = EgoldAttributeModel().apply {
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
//        presenter.updateEgoldBuyValue()

        // Then
//        verify {
//            view.renderDataChanged()
//        }
        assert(presenter.egoldAttributeModel.value!!.buyEgoldValue == expectedEgoldValue)
    }
}
