package com.tokopedia.checkout.view.presenter

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ShipmentPresenterPromoTest : BaseShipmentPresenterTest() {

    @Test
    fun `WHEN update promo checkout data THEN should set last apply data correctly`() {
        // Given
        val promoUiModel = PromoUiModel(codes = listOf("code"))

        // When
        presenter.updatePromoCheckoutData(promoUiModel)

        // Then
        assertEquals(promoUiModel.codes, presenter.lastApplyData.value.codes)
    }

    @Test
    fun `WHEN reset promo checkout data THEN should reset last apply data correctly`() {
        // Given
        val promoUiModel = PromoUiModel(codes = listOf("code"))
        presenter.updatePromoCheckoutData(promoUiModel)

        // When
        presenter.resetPromoCheckoutData()

        // Then
        assertEquals(0, presenter.lastApplyData.value.codes.size)
    }

    @Test
    fun `GIVEN cart with shipment courier WHEN generate validate use request THEN should set courier correctly`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
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
        val validateUsePromoRequest = presenter.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(false, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
    }

    @Test
    fun `GIVEN cart tradein dropoff without shipment courier WHEN generate validate use request THEN should set courier correctly`() {
        // Given
        presenter.isTradeIn = true
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1")
                ),
                selectedShipmentDetailData = ShipmentDetailData()
            )
        )

        // When
        val validateUsePromoRequest = presenter.generateValidateUsePromoRequest()

        // Then
        assertEquals(0, validateUsePromoRequest.orders[0].spId)
        assertEquals(false, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
    }

    @Test
    fun `GIVEN cart tradein dropoff with shipment courier WHEN generate validate use request THEN should set courier correctly`() {
        // Given
        presenter.isTradeIn = true
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        presenter.shipmentCartItemModelList = listOf(
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
        val validateUsePromoRequest = presenter.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(false, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
    }

    @Test
    fun `GIVEN cart tradein dropoff with shipment courier & bo WHEN generate validate use request THEN should set courier & bo correctly`() {
        // Given
        presenter.isTradeIn = true
        presenter.recipientAddressModel = RecipientAddressModel().apply {
            selectedTabIndex = 1
        }
        presenter.shipmentCartItemModelList = listOf(
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
        val validateUsePromoRequest = presenter.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(true, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
        assertEquals(true, validateUsePromoRequest.orders[0].codes.contains("code"))
        assertEquals(true, presenter.bboPromoCodes.contains("code"))
    }

    @Test
    fun `GIVEN cart with shipment courier and bo WHEN generate validate use request THEN should set courier & bo correctly`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
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
        val validateUsePromoRequest = presenter.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(true, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
        assertEquals(true, validateUsePromoRequest.orders[0].codes.contains("code"))
        assertEquals(true, presenter.bboPromoCodes.contains("code"))
    }

    @Test
    fun `GIVEN previous validate use request without courier & bo and cart with shipment courier & bo WHEN generate validate use request THEN should set courier & bo correctly`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        presenter.generateValidateUsePromoRequest()
        (presenter.shipmentCartItemModelList[0] as ShipmentCartItemModel).apply {
            selectedShipmentDetailData = ShipmentDetailData(
                selectedCourier = CourierItemData(
                    shipperProductId = 1,
                    freeShippingMetadata = "free_shipping_metadata"
                )
            )
            voucherLogisticItemUiModel = VoucherLogisticItemUiModel(code = "code")
        }

        // When
        val validateUsePromoRequest = presenter.generateValidateUsePromoRequest()

        // Then
        assertEquals(1, validateUsePromoRequest.orders[0].spId)
        assertEquals(true, validateUsePromoRequest.orders[0].freeShippingMetadata.isNotEmpty())
        assertEquals(true, validateUsePromoRequest.orders[0].codes.contains("code"))
        assertEquals(true, presenter.bboPromoCodes.contains("code"))
        assertEquals(1, validateUsePromoRequest.orders[1].spId)
        assertEquals(true, validateUsePromoRequest.orders[1].freeShippingMetadata.isNotEmpty())
        assertEquals(true, validateUsePromoRequest.orders[1].codes.contains("code"))
    }

    @Test
    fun `GIVEN last apply with mixed codes WHEN generate validate use request THEN should set codes correctly`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel(
                cartStringGroup = "123",
                cartItemModels = listOf(
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "1"),
                    CartItemModel(cartStringGroup = "123", cartStringOrder = "2")
                )
            )
        )
        presenter.lastApplyData.value = LastApplyUiModel(
            codes = listOf("global_code"),
            voucherOrders = listOf(
                LastApplyVoucherOrdersItemUiModel(code = "1", cartStringGroup = "123", uniqueId = "1", type = "merchant"),
                LastApplyVoucherOrdersItemUiModel(code = "2", cartStringGroup = "123", uniqueId = "2", type = "logistic")
            )
        )

        // When
        val validateUsePromoRequest = presenter.generateValidateUsePromoRequest()

        // Then
        assertEquals(true, validateUsePromoRequest.codes.contains("global_code"))
        assertEquals(true, validateUsePromoRequest.orders[0].codes.contains("1"))
        assertEquals(true, validateUsePromoRequest.orders[1].codes.isEmpty())
    }
}
