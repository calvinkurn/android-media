package com.tokopedia.checkout.view.helper

import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShopShipment

data class ShipmentGetCourierHolderData(
    val shipperId: Int,
    val spId: Int,
    val itemPosition: Int,
    val shipmentCartItemModel: ShipmentCartItemModel,
    val shopShipmentList: List<ShopShipment>,
    val isInitialLoad: Boolean,
//    val cartString: String,
    val isTradeInDropOff: Boolean,
    val isForceReload: Boolean,
    val ratesParam: RatesParam
//    val promoCode: String
)
