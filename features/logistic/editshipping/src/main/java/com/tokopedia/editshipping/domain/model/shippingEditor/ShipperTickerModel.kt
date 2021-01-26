package com.tokopedia.editshipping.domain.model.shippingEditor

data class ShipperTickerModel(
        var headerTicker: HeaderTickerModel = HeaderTickerModel(),
        var courierTicker: List<CourierTickerModel> = emptyList(),
        var warehouses: List<WarehousesModel> = emptyList()
)

data class HeaderTickerModel(
        var header: String = "",
        var body: String = "",
        var textLink: String = "",
        var urlLink: String = "",
        var warehouseModel: List<WarehousesModel> = emptyList(),
        var isActive: Boolean = false
)

data class CourierTickerModel(
        var shipperId: Int = -1,
        var warehouses: List<WarehousesModel> = emptyList(),
        var tickerState: Int = -1,
        var isAvailable: Boolean = false,
        var shipperProduct: List<ShipperProductTickerModel> = listOf()
)

data class ShipperProductTickerModel(
        var shipperProductId: Int = -1,
        var isAvailable: Boolean = false
)
