package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class Warehouse(
        @SerializedName("warehouse_id")
        var warehouseId: String = "",
        @SerializedName("partner_id")
        var partnerId: String = "",
        @SerializedName("shop_id")
        var shopId: String = "",
        @SerializedName("warehouse_name")
        var warehouseName: String = "",
        @SerializedName("district_id")
        var districtId: String = "",
        @SerializedName("district_name")
        var districtName: String = "",
        @SerializedName("city_id")
        var cityId: String = "",
        @SerializedName("city_name")
        var cityName: String = "",
        @SerializedName("province_id")
        var provinceId: String = "",
        @SerializedName("province_name")
        var provinceName: String = "",
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("postal_code")
        var postalCode: String = "",
        @SerializedName("is_default")
        var isDefault: Int = 0,
        @SerializedName("latlon")
        var latlon: String = "",
        @SerializedName("latitude")
        var latitude: String = "",
        @SerializedName("longitude")
        var longitude: String = "",
        @SerializedName("email")
        var email: String = "",
        @SerializedName("address_detail")
        var addressDetail: String = "",
        @SerializedName("country_name")
        var countryName: String = ""
)
