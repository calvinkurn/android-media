package com.tokopedia.checkout.view.helper

import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShopShipment

data class ShipmentGetCourierHolderData(
    val shipperId: Int,
    val spId: Int,
    val itemPosition: Int,
    val shipmentDetailData: ShipmentDetailData,
    val shipmentCartItemModel: ShipmentCartItemModel,
    val shopShipmentList: List<ShopShipment>,
    val isInitialLoad: Boolean,
    val products: ArrayList<Product>,
    val cartString: String,
    val isTradeInDropOff: Boolean,
    val recipientAddressModel: RecipientAddressModel,
    val isForceReload: Boolean,
    val skipMvc: Boolean,
    val ratesParam: RatesParam
)
