package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Warehouse(
        @SerializedName("address_detail")
        val addressDetail: String = "",
        @SerializedName("city_id")
        val cityId: String = "",
        @SerializedName("city_name")
        val cityName: String = "",
        @SerializedName("country_name")
        val countryName: String = "",
        @SerializedName("district_id")
        val districtId: String = "",
        @SerializedName("district_name")
        val districtName: String = "",
        @SerializedName("email")
        val email: String = "",
        @SerializedName("is_default")
        val isDefault: Int = 0,
        @SerializedName("is_fulfillment")
        val isFulfillment: Boolean = false,
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("latlon")
        val latlon: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("partner_id")
        val partnerId: String = "",
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("province_id")
        val provinceId: String = "",
        @SerializedName("province_name")
        val provinceName: String = "",
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("warehouse_id")
        val warehouseId: String = "",
        @SerializedName("warehouse_name")
        val warehouseName: String = ""
)