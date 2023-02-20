package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShopShipment

data class OrderShippingDuration(
    val shipmentDetailData: ShipmentDetailData = ShipmentDetailData(),
    val selectedServiceId: Int = 0,
    val shopShipmentList: List<ShopShipment> = listOf(),
    val products: ArrayList<Product> = arrayListOf(),
    val cartString: String = "",
    val isDisableOrderPrioritas: Boolean = false,
    val pslCode: String = "",
    val cartData: String = ""
)
