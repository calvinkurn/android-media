@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.tokopedia.logisticCommon.data.entity.address

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 2019-05-28.
 */
@Parcelize
data class SaveAddressDataModel(
    var id: Long = 0,
    var title: String = "",
    var formattedAddress: String = "",
    var addressName: String = "",
    var receiverName: String = "",
    var address1: String = "",
    var address1Notes: String = "",
    var address2: String = "",
    var postalCode: String = "",
    var phone: String = "",
    var cityId: Long = 0,
    var provinceId: Long = 0,
    var districtId: Long = 0,
    var cityName: String = "",
    var provinceName: String = "",
    var districtName: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var editDetailAddress: String = "",
    var selectedDistrict: String = "",
    var zipCodes: List<String> = emptyList(),
    var applyNameAsNewUserFullname: Boolean = false,
    var setAsPrimaryAddresss: Boolean = false,
    var isAnaPositive: String = "",
    var shopId: Long = 0,
    var warehouseId: Long = 0,
    var warehouses: List<WarehouseDataModel> = listOf(),
    var serviceType: String = "",
    var isTokonowRequest: Boolean = false
) : Parcelable {
    fun hasPinpoint(): Boolean {
        return latitude.isValidPinpoint() && longitude.isValidPinpoint()
    }

    private fun String.isValidPinpoint(): Boolean {
        return this.isNotBlank() && this != EMPTY_COORDINATE
    }

    companion object {
        private const val EMPTY_COORDINATE = "0.0"
    }
}
