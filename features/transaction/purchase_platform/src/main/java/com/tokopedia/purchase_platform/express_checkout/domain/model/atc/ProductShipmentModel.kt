package com.tokopedia.purchase_platform.express_checkout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ProductShipmentModel(
        var shipmentId: String? = null,
        var serviceId: ArrayList<String>? = null
)