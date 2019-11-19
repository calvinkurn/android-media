package com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ProductShipmentMappingModel(
        var shipmentId: String? = null,
        var shippingIds: String? = null,
        var serviceIdModels: ArrayList<ServiceIdModel>? = null
)