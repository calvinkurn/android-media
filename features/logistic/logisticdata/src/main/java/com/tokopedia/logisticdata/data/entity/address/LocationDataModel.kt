package com.tokopedia.logisticdata.data.entity.address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationDataModel(
        var addrId: Int = 0,
        var addrName: String = "",
        var address1: String = "",
        var address2: String = "",
        var city: Int = 0,
        var cityName: String = "",
        var country: String = "",
        var district: Int = 0,
        var districtName: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var openingHours: String = "",
        var phone: String = "",
        var postalCode: String = "",
        var province: Int = 0,
        var provinceName: String = "",
        var receiverName: String = "",
        var status: Int = 0,
        var storeCode: String = "",
        var storeDistance: String = "",
        var type: Int = 0
) : Parcelable