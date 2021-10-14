package com.tokopedia.logisticCommon.data.model

data class CustomProductLogisticModel (
    var cplProduct: List<CPLProductModel> = listOf(),
    var shipperList: List<ShipperListCPLModel> = listOf()
)

data class CPLProductModel(
    var productId: Long = 0,
    var cplStatus: Int = 0,
    var shipperServices: List<Long> = listOf()
)

data class ShipperListCPLModel(
    var header: String = "",
    var description: String = "",
    var shipper: List<ShipperCPLModel> = listOf()
)

data class ShipperCPLModel(
    var shipperId: Long = 0,
    var shipperName: String = "",
    var logo: String = "",
    var shipperProduct: List<ShipperProductCPLModel> = listOf(),
    var isActive: Boolean = false
)

data class ShipperProductCPLModel(
    var shipperProductId: Long = 0,
    var shipperProductName: String = "",
    var uiHidden: Boolean = false,
    var isActive: Boolean = false
)

