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
        var isActive: Boolean = false
)

data class CourierTickerModel(
        var shipperId: Int = -1,
        var warehouseIds: List<Int> = emptyList(),
        var tickerState: Int = -1,
        var isAvailable: Boolean = false,
        var shipperProduct: ShipperProductTickerModel = ShipperProductTickerModel()
)

data class ShipperProductTickerModel(
        var shipperProductId: Int = -1,
        var isAvailable: Boolean = false
)

data class WarehousesModel(
        var warehouseId: Int = 0,
        var warehouseName: String = "",
        var districtId: Int = 0,
        var districtName: String = "",
        var cityId: Int = 0,
        var cityName: String = "",
        var provinceId: Int = 0,
        var provinceName: String = "",
        var status: Int = 0,
        var postalCode: String = "",
        var isDefault: Int = 0,
        var latLon: String = "",
        var addressDetail: String = "",
        var country: String = "",
        var isFulfillment: Boolean = false,
        var warehouseType: Int = 0,
        var email: String = "false",
        var shopId: ShopIdModel = ShopIdModel(),
        var partnerId: PartnerIdModel = PartnerIdModel()
)

data class ShopIdModel(
        var int64: Int = 0,
        var valid: Boolean = false
)

data class PartnerIdModel(
        var int64: Int = 0,
        var valid: Boolean = false
)