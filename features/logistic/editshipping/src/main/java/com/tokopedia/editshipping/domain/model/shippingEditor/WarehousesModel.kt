package com.tokopedia.editshipping.domain.model.shippingEditor

data class WarehousesModel(
        var warehouseId: Long = 0,
        var warehouseName: String = "",
        var districtId: Long = 0,
        var districtName: String = "",
        var cityId: Long = 0,
        var cityName: String = "",
        var provinceId: Long = 0,
        var provinceName: String = "",
        var status: Int = 0,
        var postalCode: String = "",
        var isDefault: Int = 0,
        var latLon: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var addressDetail: String = "",
        var country: String = "",
        var isFulfillment: Boolean = false,
        var warehouseType: Int = 0,
        var email: String = "false",
        var shopId: ShopIdModel = ShopIdModel(),
        var partnerId: PartnerIdModel = PartnerIdModel(),
        var isShown: Boolean = false
)

data class ShopIdModel(
        var int64: Long = 0,
        var valid: Boolean = false
)

data class PartnerIdModel(
        var int64: Long = 0,
        var valid: Boolean = false
)