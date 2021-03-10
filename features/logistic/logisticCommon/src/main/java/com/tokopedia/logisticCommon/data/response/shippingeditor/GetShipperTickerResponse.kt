package com.tokopedia.logisticCommon.data.response.shippingeditor

import com.google.gson.annotations.SerializedName

data class GetShipperTickerResponse (
        @SerializedName("ongkirShippingEditorGetShipperTicker")
        var ongkirShippingEditorGetShipperTicker: OngkirShippingEditorGetShipperTicker = OngkirShippingEditorGetShipperTicker()
)

data class OngkirShippingEditorGetShipperTicker(
        @SerializedName("status")
        var status: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("data")
        var data: DataTicker = DataTicker()
)

data class DataTicker(
        @SerializedName("header_ticker")
        var headerTicker: HeaderTicker = HeaderTicker(),
        @SerializedName("courier_ticker")
        var courierTicker: List<CourierTicker> = listOf(),
        @SerializedName("warehouses")
        var warehouses: List<Warehouses> = listOf()
)

data class HeaderTicker(
        @SerializedName("header")
        var header: String = "",
        @SerializedName("body")
        var body: String = "",
        @SerializedName("text_link")
        var textLink: String = "",
        @SerializedName("url_link")
        var urlLink: String = "",
        @SerializedName("warehouse_ids")
        var warehouseIds: List<Int> = emptyList(),
        @SerializedName("is_active")
        var isActive: Boolean = false
)

data class CourierTicker(
        @SerializedName("shipper_id")
        var shipperId: Int = -1,
        @SerializedName("warehouse_ids")
        var warehouseIds: List<Int>? = emptyList(),
        @SerializedName("ticker_state")
        var tickerState: Int = -1,
        @SerializedName("is_available")
        var isAvailable: Boolean = false,
        @SerializedName("shipper_product")
        var shipperProduct: List<ShipperProductTicker> = listOf()
)

data class ShipperProductTicker(
        @SerializedName("shipper_product_id")
        var shipperProductId: Int = -1,
        @SerializedName("is_available")
        var isAvailable: Boolean = false
)

data class Warehouses(
        @SerializedName("warehouse_id")
        var warehouseId: Int = 0,
        @SerializedName("warehouse_name")
        var warehouseName: String = "",
        @SerializedName("district_id")
        var districtId: Int = 0,
        @SerializedName("district_name")
        var districtName: String = "",
        @SerializedName("city_id")
        var cityId: Int = 0,
        @SerializedName("city_name")
        var cityName: String = "",
        @SerializedName("province_id")
        var provinceId: Int = 0,
        @SerializedName("province_name")
        var provinceName: String = "",
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("postal_code")
        var postalCode: String = "",
        @SerializedName("is_default")
        var isDefault: Int = 0,
        @SerializedName("latlon")
        var latLon: String = "",
        @SerializedName("latitude")
        var latitude: String = "",
        @SerializedName("longitude")
        var longitude: String = "",
        @SerializedName("address_detail")
        var addressDetail: String = "",
        @SerializedName("country")
        var country: String = "",
        @SerializedName("is_fulfillment")
        var isFulfillment: Boolean = false,
        @SerializedName("warehouse_type")
        var warehouseType: Int = 0,
        @SerializedName("email")
        var email: String = "",
        @SerializedName("shop_id")
        var shopId: ShopId = ShopId(),
        @SerializedName("partner_id")
        var partnerId: PartnerId = PartnerId()
)

data class ShopId(
        @SerializedName("int64")
        var int64: Long = 0,
        @SerializedName("valid")
        var valid: Boolean = false
)

data class PartnerId(
        @SerializedName("int64")
        var int64: Long = 0,
        @SerializedName("valid")
        var valid: Boolean = false
)