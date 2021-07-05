package com.tokopedia.logisticCommon.data.response.shoplocation

import com.google.gson.annotations.SerializedName

data class GetShopLocationResponse (
		@SerializedName("ShopLocGetAllLocations")
		var shopLocations: ShopLocations = ShopLocations()
)

data class ShopLocations(
		@SerializedName("status")
		var status: String = "",
		@SerializedName("message")
		var message: String = "",
		@SerializedName("error")
		var error: Error = Error(),
		@SerializedName("data")
		var data: Data = Data()
)

data class Error(
		@SerializedName("id")
		var id: Int = 0,
		@SerializedName("description")
		var description: String = ""
)

data class Data(
		@SerializedName("general_ticker")
		var generalTicker: GeneralTicker = GeneralTicker(),
		@SerializedName("warehouses")
		var warehouse: List<Warehouse> = listOf()
)

data class GeneralTicker(
		@SerializedName("header")
		var header: String = "",
		@SerializedName("body")
		var body: String = "",
		@SerializedName("body_link_text")
		var bodyLinkText: String = "",
		@SerializedName("body_link_url")
		var bodyLinkUrl: String = ""
)

data class Warehouse(
		@SerializedName("warehouse_id")
		var warehouseId: Int = 0,
		@SerializedName("warehouse_name")
		var warehouseName: String = "",
		@SerializedName("warehouse_type")
		var warehouseType: Int = 0,
		@SerializedName("shop_id")
		var shopId: ShopId = ShopId(),
		@SerializedName("partner_id")
		var partnerId: PartnerId = PartnerId(),
		@SerializedName("address_detail")
		var addressDetail: String = "",
		@SerializedName("postal_code")
		var postalCode: String = "",
		@SerializedName("latlon")
		var latLon: String = "",
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
		@SerializedName("country")
		var country: String = "",
		@SerializedName("status")
		var status: Int = 0,
		@SerializedName("is_covered_by_couriers")
		var isCoveredByCouriers: Boolean = false,
		@SerializedName("ticker")
		var ticker: Ticker = Ticker()
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

data class Ticker(
		@SerializedName("text_inactive")
		var textInactive: String = "",
		@SerializedName("text_courier_setting")
		var textCourierSetting: String = "",
		@SerializedName("link_courier_setting")
		var linkCourierSetting: String = ""
)