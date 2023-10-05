package com.tokopedia.checkout.view.presenter

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentViewModelPromoTest : BaseShipmentViewModelTest() {

    @Test
    fun `WHEN update promo checkout data THEN should set last apply data correctly`() {
        // Given
        val promoUiModel = PromoUiModel(codes = listOf("code"))

        // When
        viewModel.updatePromoCheckoutData(promoUiModel)

        // Then
        assertEquals(promoUiModel.codes, viewModel.lastApplyData.value.codes)
    }

    @Test
    fun `WHEN reset promo checkout data THEN should reset last apply data correctly`() {
        // Given
        val promoUiModel = PromoUiModel(codes = listOf("code"))
        viewModel.updatePromoCheckoutData(promoUiModel)

        // When
        viewModel.resetPromoCheckoutData()

        // Then
        assertEquals(0, viewModel.lastApplyData.value.codes.size)
    }

    @Test
    fun `GIVEN cart with shipment courier WHEN generate validate use request THEN should set courier correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(
                cartStringGroup = "123"
            ),
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1")
                ),
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData(
                        shipperProductId = 1
                    )
                )
            )
        )

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(false, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
    }

    @Test
    fun `GIVEN cart tradein dropoff without shipment courier WHEN generate validate use request THEN should set courier correctly`() {
        // Given
        viewModel.isTradeIn = true
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1")
                ),
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(0, validateUsePromoRequest.orders[0].spId)
        assertEquals(false, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
    }

    @Test
    fun `GIVEN cart tradein dropoff with shipment courier WHEN generate validate use request THEN should set courier correctly`() {
        // Given
        viewModel.isTradeIn = true
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1")
                ),
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourierTradeInDropOff = CourierItemData(
                        shipperProductId = 1
                    )
                )
            )
        )

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(false, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
    }

    @Test
    fun `GIVEN cart tradein dropoff with shipment courier & bo WHEN generate validate use request THEN should set courier & bo correctly`() {
        // Given
        viewModel.isTradeIn = true
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1")
                ),
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourierTradeInDropOff = CourierItemData(
                        shipperProductId = 1,
                        freeShippingMetadata = "free_shipping_metadata"
                    )
                ),
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            )
        )

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(true, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
        assertEquals(true, validateUsePromoRequest.orders[0].codes.contains("code"))
        assertEquals(true, viewModel.bboPromoCodes.contains("code"))
    }

    @Test
    fun `GIVEN cart with shipment courier and bo WHEN generate validate use request THEN should set courier & bo correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1")
                ),
                selectedShipmentDetailData = ShipmentDetailData(
                    selectedCourier = CourierItemData(
                        shipperProductId = 1,
                        freeShippingMetadata = "free_shipping_metadata"
                    )
                ),
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
            )
        )

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(true, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
        assertEquals(true, validateUsePromoRequest.orders[0].codes.contains("code"))
        assertEquals(true, viewModel.bboPromoCodes.contains("code"))
    }

    @Test
    fun `GIVEN previous validate use request without courier & bo and cart with shipment courier & bo WHEN generate validate use request THEN should set courier & bo correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemTopModel(cartStringGroup = "123"),
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        viewModel.generateValidateUsePromoRequest()
        (viewModel.shipmentCartItemModelList[1] as ShipmentCartItemModel).apply {
            selectedShipmentDetailData = ShipmentDetailData(
                selectedCourier = CourierItemData(
                    shipperProductId = 1,
                    freeShippingMetadata = "free_shipping_metadata"
                )
            )
            voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
        }

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()
        viewModel.setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(true, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
        assertEquals(true, validateUsePromoRequest.orders[0].codes.contains("code"))
        assertEquals(true, viewModel.bboPromoCodes.contains("code"))
        assertEquals(1, validateUsePromoRequest.orders[1].spId)
        assertEquals(true, validateUsePromoRequest.orders[1].freeShippingMetadata.isNotEmpty())
        assertEquals(false, validateUsePromoRequest.orders[1].codes.contains("code"))
    }

    @Test
    fun `GIVEN last apply with mixed codes WHEN generate validate use request THEN should set codes correctly`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        viewModel.lastApplyData.value = LastApplyUiModel(
            codes = listOf("global_code"),
            voucherOrders = listOf(
                LastApplyVoucherOrdersItemUiModel(code = "1", cartStringGroup = "123", uniqueId = "1", type = "merchant"),
                LastApplyVoucherOrdersItemUiModel(code = "2", cartStringGroup = "123", uniqueId = "2", type = "logistic")
            )
        )

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()
        viewModel.setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)

        // Then
        assertEquals(true, validateUsePromoRequest.codes.contains("global_code"))
        assertEquals(true, validateUsePromoRequest.orders[0].codes.contains("1"))
        assertEquals(true, validateUsePromoRequest.orders[1].codes.isEmpty())
    }

    @Test
    fun `GIVEN trade in WHEN generate validate use request THEN should set trade in`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        viewModel.isTradeIn = true

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.isTradeIn)
        assertEquals(0, validateUsePromoRequest.isTradeInDropOff)
    }

    @Test
    fun `GIVEN trade in dropoff WHEN generate validate use request THEN should set trade in dropoff`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        viewModel.isTradeIn = true
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.isTradeIn)
        assertEquals(1, validateUsePromoRequest.isTradeInDropOff)
    }

    @Test
    fun `GIVEN ocs WHEN generate validate use request THEN should set ocs`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        viewModel.isOneClickShipment = true

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals("ocs", validateUsePromoRequest.cartType)
    }

    @Test
    fun `GIVEN trade in WHEN generate validate use request with last request THEN should set trade in`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        viewModel.generateValidateUsePromoRequest()
        viewModel.isTradeIn = true

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.isTradeIn)
        assertEquals(0, validateUsePromoRequest.isTradeInDropOff)
    }

    @Test
    fun `GIVEN trade in dropoff WHEN generate validate use request with last request THEN should set trade in dropoff`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        viewModel.generateValidateUsePromoRequest()
        viewModel.isTradeIn = true
        viewModel.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.isTradeIn)
        assertEquals(1, validateUsePromoRequest.isTradeInDropOff)
    }

    @Test
    fun `GIVEN ocs WHEN generate validate use request with last request THEN should set ocs`() {
        // Given
        viewModel.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        viewModel.generateValidateUsePromoRequest()
        viewModel.isOneClickShipment = true

        // When
        val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest()

        // Then
        assertEquals("ocs", validateUsePromoRequest.cartType)
    }
}
