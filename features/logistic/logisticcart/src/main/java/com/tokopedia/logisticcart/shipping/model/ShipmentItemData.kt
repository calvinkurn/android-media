package com.tokopedia.logisticcart.shipping.model

import com.tokopedia.logisticcart.shipping.model.CourierItemData

/**
 * Created by Irfan Khoirul on 25/01/18.
 */
class ShipmentItemData {
    var serviceId = 0
    var type: String? = null
    var singlePriceRange: String? = null
    var multiplePriceRange: String? = null
    var deliveryTimeRange: String? = null
    var isLessThanADayDelivery = false
    var courierItemData: List<CourierItemData>? = null
    var isSelected = false
}