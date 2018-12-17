package com.tokopedia.expresscheckout.domain.model

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ProductShipmentMapping(
        var shipmentId: String? = null,
        var shippingIds: String? = null,
        var serviceIdModels: ArrayList<ServiceIdModel>? = null
)