package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.data.model.request.saveshipmentstate.ShipmentStateRequestData
import com.tokopedia.checkout.domain.model.saveshipmentstate.SaveShipmentStateData
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryProduct
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable

class ShipmentPresenterSaveShipmentStateTest : BaseShipmentPresenterTest() {

    @Test
    fun checkParamsFromPresenter() {
        // Given
        every { view.isTradeInByDropOff } returns false

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(
                    CartItemModel().apply {
                        productId = 1
                    }
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

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(
            SaveShipmentStateData()
        )

        // When
        presenter.processSaveShipmentState()

        // Then
        val params =
            capturedRequestParam.captured
                .getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray =
            params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        verify(exactly = 1) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromPresenter_PreOrderProduct() {
        // Given
        every { view.isTradeInByDropOff } returns false

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(
                    CartItemModel().apply {
                        productId = 1
                        isPreOrder = true
                        preOrderDurationDay = 2
                    }
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

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(
            SaveShipmentStateData()
        )

        // When
        presenter.processSaveShipmentState()

        // Then
        val params =
            capturedRequestParam.captured
                .getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray =
            params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        verify(exactly = 1) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromPresenter_TradeInProduct() {
        // Given
        every { view.isTradeInByDropOff } returns true

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(
                    CartItemModel().apply {
                        productId = 1
                        isPreOrder = true
                        preOrderDurationDay = 2
                    }
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

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(
            SaveShipmentStateData()
        )

        // When
        presenter.processSaveShipmentState()

        // Then
        val params =
            capturedRequestParam.captured
                .getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray =
            params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        verify(exactly = 1) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromInput_ShouldSendParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartItemModels = listOf(
                CartItemModel().apply {
                    productId = 1
                }
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

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(
            SaveShipmentStateData()
        )

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        val params =
            capturedRequestParam.captured
                .getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray =
            params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        verify(exactly = 1) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromInput_ShouldNotSendParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartItemModels = listOf(
                CartItemModel().apply {
                    productId = 1
                }
            )
            selectedShipmentDetailData = ShipmentDetailData()
        }

        val addressId = "123"
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            id = addressId
        }

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(
            SaveShipmentStateData()
        )

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        assertEquals(false, capturedRequestParam.isCaptured)

        verify(inverse = true) { saveShipmentStateGqlUseCase.createObservable(any()) }
    }

    @Test
    fun checkParamsFromInput_WithScheduleDeliveryNotSelected_ShouldSendSelectedParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartItemModels = listOf(
                CartItemModel().apply {
                    productId = 1
                }
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

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(SaveShipmentStateData())

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        val params = capturedRequestParam.captured.getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray = params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        val firstShopProductDataList = data.shopProductDataList?.first()
        val shippingInfoData = firstShopProductDataList?.shippingInfoData

        assertEquals(1, shippingInfoData?.shippingId ?: 0)
        assertEquals(2, shippingInfoData?.spId ?: 0)
        assertEquals(
            "",
            firstShopProductDataList?.validationMetadata ?: ""
        )
    }

    @Test
    fun checkParamsFromInput_WithScheduleDeliverySelected_ShouldSendSelectedParam() {
        // Given
        every { view.isTradeInByDropOff } returns false

        val shipmentCartItemModel = ShipmentCartItemModel().apply {
            cartItemModels = listOf(
                CartItemModel().apply {
                    productId = 1
                }
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

        val capturedRequestParam = CapturingSlot<RequestParams>()
        every { saveShipmentStateGqlUseCase.createObservable(capture(capturedRequestParam)) } returns Observable.just(SaveShipmentStateData())

        // When
        presenter.processSaveShipmentState(shipmentCartItemModel)

        // Then
        val params = capturedRequestParam.captured.getObject(SaveShipmentStateGqlUseCase.PARAM_CART_DATA_OBJECT) as HashMap<String, Any>
        val saveShipmentDataArray = params[SaveShipmentStateGqlUseCase.PARAM_CARTS] as List<ShipmentStateRequestData>

        assertEquals(1, saveShipmentDataArray.size)

        val data = saveShipmentDataArray.first()
        assertEquals(addressId, data.addressId)

        val shopProductDataList = data.shopProductDataList
        assertEquals(1, shopProductDataList?.size ?: 0)

        val firstShopProductDataList = data.shopProductDataList?.first()
        val shippingInfoData = firstShopProductDataList?.shippingInfoData
        assertEquals(3, shippingInfoData?.shippingId ?: 0)
        assertEquals(4, shippingInfoData?.spId ?: 0)
        assertEquals(
            "{\"timeslot_id\":2022092014123,\"schedule_date\":\"2022-09-20T00:00:00Z\",\"shipping_price\":10000}",
            firstShopProductDataList?.validationMetadata ?: ""
        )
    }
}
