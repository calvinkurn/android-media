package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.domain.response.Warehouse

class Address(
        @SerializedName("address_id")
        val addressId: String = "",
        @SerializedName("receiver_name")
        val receiverName: String = "",
        @SerializedName("address_name")
        val addressName: String = "",
        @SerializedName("address_street")
        val addressStreet: String = "",
        @SerializedName("district_id")
        val districtId: String = "",
        @SerializedName("district_name")
        val districtName: String = "",
        @SerializedName("city_id")
        val cityId: String = "",
        @SerializedName("city_name")
        val cityName: String = "",
        @SerializedName("province_id")
        val provinceId: String = "",
        @SerializedName("province_name")
        val provinceName: String = "",
        @SerializedName("country")
        val country: String = "",
        @SerializedName("phone")
        val phone: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("state")
        val state: Int = 0,
        @SerializedName("state_detail")
        val stateDetail: String = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("tokonow")
        val tokoNow: AddressTokoNow = AddressTokoNow()
)

class AddressTokoNow(
        @SerializedName("is_modified")
        val isModified: Boolean = false,
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("warehouse_id")
        val warehouseId: String = "",
        @SerializedName("warehouses")
        val warehouses: List<Warehouse> = emptyList(),
        @SerializedName("service_type")
        val serviceType: String = ""
)
