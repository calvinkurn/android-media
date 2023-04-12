package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.data.model.request.saveshipmentstate.SaveShipmentStateRequest
import com.tokopedia.checkout.domain.model.saveshipmentstate.SaveShipmentStateData
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryProduct
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentPresenterSaveShipmentStateTest : BaseShipmentPresenterTest() {

    @Test
    fun checkParamsFromPresenter() {
        // Given
        every { view.isTradeInByDropOff } returns false

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartString = "").apply {
                cartItemModels = listOf(
                    CartItemModel(
                        cartString = "",
                        productId = 1
                    )
                )
                selectedShipmentDetailData = ShipmentDetailData().apply {
                    selectedCourier = CourierItemData().apply {
                        shipperId = 1
                        shipperProductId = 2
                    }
                }
            }
        )

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<SaveShipmentStateRequest>()
        coEvery { saveShipmentStateGqlUseCase(capture(capturedRequestParam)) } returns
            SaveShipmentStateData()

        // When
        presenter.processSaveShipmentState()

        // Then
        val params =
            capturedRequestParam.captured
        val saveShipmentDataArray =
            params.requestDataList

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList.size)

        coVerify(exactly = 1) { saveShipmentStateGqlUseCase(any()) }
    }

    @Test
    fun checkParamsFromPresenter_PreOrderProduct() {
        // Given
        every { view.isTradeInByDropOff } returns false

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartString = "").apply {
                cartItemModels = listOf(
                    CartItemModel(
                        cartString = "",
                        productId = 1,
                        isPreOrder = true,
                        preOrderDurationDay = 2
                    )
                )
                selectedShipmentDetailData = ShipmentDetailData().apply {
                    selectedCourier = CourierItemData().apply {
                        shipperId = 1
                        shipperProductId = 2
                    }
                }
            }
        )

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<SaveShipmentStateRequest>()
        coEvery { saveShipmentStateGqlUseCase(capture(capturedRequestParam)) } returns SaveShipmentStateData()

        // When
        presenter.processSaveShipmentState()

        // Then
        val params =
            capturedRequestParam.captured
        val saveShipmentDataArray =
            params.requestDataList

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList.size)

        coVerify(exactly = 1) { saveShipmentStateGqlUseCase(any()) }
    }

    @Test
    fun checkParamsFromPresenter_TradeInProduct() {
        // Given
        every { view.isTradeInByDropOff } returns true

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(cartString = "").apply {
                cartItemModels = listOf(
                    CartItemModel(
                        cartString = "",
                        productId = 1,
                        isPreOrder = true,
                        preOrderDurationDay = 2
                    )
                )
                selectedShipmentDetailData = ShipmentDetailData().apply {
                    selectedCourierTradeInDropOff = CourierItemData().apply {
                        shipperId = 1
                        shipperProductId = 2
                    }
                }
            }
        )

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<SaveShipmentStateRequest>()
        coEvery { saveShipmentStateGqlUseCase(capture(capturedRequestParam)) } returns SaveShipmentStateData()

        // When
        presenter.processSaveShipmentState()

        // Then
        val params =
            capturedRequestParam.captured
        val saveShipmentDataArray =
            params.requestDataList

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList.size)

        coVerify(exactly = 1) { saveShipmentStateGqlUseCase(any()) }
    }

    @Test
    fun checkParamsFromInput_ShouldSendParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel(cartString = "").apply {
            cartItemModels = listOf(
                CartItemModel(
                    cartString = "",
                    productId = 1
                )
            )
            selectedShipmentDetailData = ShipmentDetailData().apply {
                selectedCourier = CourierItemData().apply {
                    shipperId = 1
                    shipperProductId = 2
                }
            }
        }

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<SaveShipmentStateRequest>()
        coEvery { saveShipmentStateGqlUseCase(capture(capturedRequestParam)) } returns SaveShipmentStateData()

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        val params =
            capturedRequestParam.captured
        val saveShipmentDataArray =
            params.requestDataList

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList.size)

        coVerify(exactly = 1) { saveShipmentStateGqlUseCase(any()) }
    }

    @Test
    fun checkParamsFromInput_ShouldNotSendParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel(cartString = "").apply {
            cartItemModels = listOf(
                CartItemModel(
                    cartString = "",
                    productId = 1
                )
            )
            selectedShipmentDetailData = ShipmentDetailData()
        }

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<SaveShipmentStateRequest>()
        coEvery { saveShipmentStateGqlUseCase(capture(capturedRequestParam)) } returns SaveShipmentStateData()

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
//        assertEquals(false, capturedRequestParam.isCaptured)

        coVerify(inverse = true) { saveShipmentStateGqlUseCase(any()) }
    }

    @Test
    fun checkParamsFromInput_WithScheduleDeliveryNotSelected_ShouldSendSelectedParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel(cartString = "").apply {
            cartItemModels = listOf(
                CartItemModel(
                    cartString = "",
                    productId = 1
                )
            )
            selectedShipmentDetailData = ShipmentDetailData().apply {
                selectedCourier = CourierItemData().apply {
                    shipperId = 1
                    shipperProductId = 2
                    scheduleDeliveryUiModel = ScheduleDeliveryUiModel(
                        isSelected = false
                    ).apply {
                        deliveryProduct = DeliveryProduct(
                            shipperId = 3,
                            shipperProductId = 4,
                            validationMetadata = "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}"
                        )
                    }
                }
            }
        }

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<SaveShipmentStateRequest>()
        coEvery { saveShipmentStateGqlUseCase(capture(capturedRequestParam)) } returns SaveShipmentStateData()

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        val params =
            capturedRequestParam.captured
        val saveShipmentDataArray =
            params.requestDataList

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList.size)

        val firstShopProductDataList = data.shopProductDataList.first()
        val shippingInfoData = firstShopProductDataList.shippingInfoData

        assertEquals(1, shippingInfoData.shippingId)
        assertEquals(2, shippingInfoData.spId)
        assertEquals(
            "",
            firstShopProductDataList.validationMetadata
        )
    }

    @Test
    fun checkParamsFromInput_WithScheduleDeliverySelected_ShouldSendSelectedParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel(cartString = "").apply {
            cartItemModels = listOf(
                CartItemModel(
                    cartString = "",
                    productId = 1
                )
            )
            selectedShipmentDetailData = ShipmentDetailData().apply {
                selectedCourier = CourierItemData().apply {
                    shipperId = 1
                    shipperProductId = 2
                    scheduleDeliveryUiModel = ScheduleDeliveryUiModel(
                        isSelected = true
                    ).apply {
                        deliveryProduct = DeliveryProduct(
                            shipperId = 3,
                            shipperProductId = 4
                        )
                    }
                }
            }
            validationMetadata = "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}"
        }

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<SaveShipmentStateRequest>()
        coEvery { saveShipmentStateGqlUseCase(capture(capturedRequestParam)) } returns SaveShipmentStateData()

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        val params =
            capturedRequestParam.captured
        val saveShipmentDataArray =
            params.requestDataList

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList.size)

        val firstShopProductDataList = data.shopProductDataList.first()
        val shippingInfoData = firstShopProductDataList.shippingInfoData
        assertEquals(3, shippingInfoData.shippingId)
        assertEquals(4, shippingInfoData.spId)
        assertEquals(
            "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}",
            firstShopProductDataList.validationMetadata
        )
    }
}
