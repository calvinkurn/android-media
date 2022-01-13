package com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import com.tokopedia.localizationchooseaddress.domain.response.Warehouse

data class LocalizationChooseAddress(
        @SerializedName("address_id")
        val addressId: String = "",
        @SerializedName("address_name")
        val addressName: String = "",
        @SerializedName("address")
        val address: String = "",
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("phone")
        val phone: String = "",
        @SerializedName("receiver_name")
        val receiverName: String = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("country")
        val country: String = "",
        @SerializedName("province_id")
        val provinceId: String = "",
        @SerializedName("province_name")
        val provinceName: String = "",
        @SerializedName("city_id")
        val cityId: String = "",
        @SerializedName("city_name")
        val cityName: String = "",
        @SerializedName("district_id")
        val districtId: String = "",
        @SerializedName("district_name")
        val districtName: String = "",
        @SerializedName("address_2")
        val address2: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("corner_id")
        val cornerId: String = "",
        @SerializedName("is_corner")
        val isCorner: Boolean = false,
        @SerializedName("is_primary")
        val isPrimary: Boolean = false,
        @SerializedName("buyer_store_code")
        val buyerStoreCode: String = "",
        @SerializedName("type")
        val type: Int = 0,
        @SerializedName("state")
        val state: Int = 0,
        @SerializedName("state_detail")
        val stateDetail: String = "",
        @SerializedName("tokonow")
        val tokoNow: LocalizationChooseAddressTokoNow = LocalizationChooseAddressTokoNow()
) {
    companion object {
        const val STATE_EMPTY = 0
        const val STATE_CHOSEN_ADDRESS_MATCH = 101
        const val STATE_ADDRESS_ID_NOT_MATCH = 102
        const val STATE_DISTRICT_ID_NOT_MATCH = 103
        const val STATE_NO_ADDRESS = 104
    }
}

data class LocalizationChooseAddressTokoNow(
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