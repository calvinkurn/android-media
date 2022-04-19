package com.tokopedia.logisticCommon.data.entity.address

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAddress(
        var addressId: String = "",
        var addressName: String = "",
        var address: String = "",
        var postalCode: String = "",
        var phone: String = "",
        var receiverName: String = "",
        var status: Int = 0,
        var country: String = "",
        var provinceId: String = "",
        var provinceName: String = "",
        var cityId: String = "",
        var cityName: String = "",
        var districtId: String = "",
        var districtName: String = "",
        var address2: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var cornerId: String = "",
        var isCorner: Boolean = false,
        var state: Int = 0,
        var stateDetail: String = "",
        var tokoNow: UserAddressTokoNow = UserAddressTokoNow()
) : Parcelable {

    companion object {
        const val STATE_CHOSEN_ADDRESS_MATCH = 101
        const val STATE_ADDRESS_ID_NOT_MATCH = 102
        const val STATE_DISTRICT_ID_NOT_MATCH = 103
        const val STATE_NO_ADDRESS = 104
    }
}

@Parcelize
data class UserAddressTokoNow(
    val isModified: Boolean = false,
    val shopId: String = "",
    val warehouseId: String = "",
    val warehouses: List<WarehouseDataModel> = emptyList(),
    val serviceType: String = ""
): Parcelable