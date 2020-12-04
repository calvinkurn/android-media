package com.tokopedia.manageaddress.domain.model.shoplocation

data class ShopLocationModel (
        var listWarehouse: List<Warehouse> = emptyList()
)

data class Warehouse(
        var warehouseId: Int = 0,
        var warehouseName: String = "",
        var warehouseType: Int = 0,
        var shopId: ShopId = ShopId(),
        var partnerId: PartnerId = PartnerId(),
        var addressDetail: String = "",
        var postalCode: String = "",
        var latLon: String = "",
        var districtId: Int = 0,
        var districtName: String = "",
        var cityId: Int = 0,
        var cityName: String = "",
        var provinceId: Int = 0,
        var provinceName: String = "",
        var country: String = "",
        var status: Int = 0,
        var isCoveredByCouriers: Boolean = false,
        var ticker: Ticker = Ticker()
)

data class ShopId(
        var int64: Int = 0,
        var valid: Boolean = false
)

data class PartnerId(
        var int64: Int = 0,
        var valid: Boolean = false
)

data class Ticker(
        var textInactive: String = "",
        var textCourierSetting: String = "",
        var linkCourierSetting: String = ""
)