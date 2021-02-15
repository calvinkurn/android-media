package com.tokopedia.logisticCommon.data.entity.address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
) : Parcelable