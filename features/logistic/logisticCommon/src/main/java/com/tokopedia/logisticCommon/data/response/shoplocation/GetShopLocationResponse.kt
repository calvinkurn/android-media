package com.tokopedia.logisticCommon.data.response.shoplocation

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetShopLocationResponse(
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
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    var id: Long = 0,
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
    @SuppressLint("Invalid Data Type")
    @SerializedName("warehouse_id")
    var warehouseId: Long = 0,
    @SerializedName("warehouse_name")
    var warehouseName: String = "",
    @SerializedName("warehouse_type")
    var warehouseType: Int = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("shop_id")
    var shopId: ShopId = ShopId(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("partner_id")
    var partnerId: PartnerId = PartnerId(),
    @SerializedName("address_detail")
    var addressDetail: String = "",
    @SerializedName("postal_code")
    var postalCode: String = "",
    @SerializedName("latlon")
    var latLon: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("district_id")
    var districtId: Long = 0,
    @SerializedName("district_name")
    var districtName: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("city_id")
    var cityId: Long = 0,
    @SerializedName("city_name")
    var cityName: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("province_id")
    var provinceId: Long = 0,
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
    @SuppressLint("Invalid Data Type")
    @SerializedName("valid")
    var valid: Boolean = false
)

data class PartnerId(
    @SerializedName("int64")
    var int64: Long = 0,
    @SuppressLint("Invalid Data Type")
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