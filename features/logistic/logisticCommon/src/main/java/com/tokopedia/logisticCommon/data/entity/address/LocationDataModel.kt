package com.tokopedia.logisticCommon.data.entity.address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationDataModel(
        var addrId: String = "",
        var addrName: String = "",
        var address1: String = "",
        var address2: String = "",
        var city: String = "",
        var cityName: String = "",
        var country: String = "",
        var district: String = "",
        var districtName: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var openingHours: String = "",
        var phone: String = "",
        var postalCode: String = "",
        var province: String = "",
        var provinceName: String = "",
        var receiverName: String = "",
        var status: Int = 0,
        var storeCode: String = "",
        var storeDistance: String = "",
        var type: Int = 0
) : Parcelable