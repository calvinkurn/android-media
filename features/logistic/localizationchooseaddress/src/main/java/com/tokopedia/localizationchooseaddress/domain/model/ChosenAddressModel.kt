package com.tokopedia.localizationchooseaddress.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChosenAddressModel (
        var addressId: Long = 0,
        var receiverName: String = "",
        var addressName: String = "",
        var districtId: Int = 0,
        var cityId: Int = 0,
        var cityName: String = "",
        var districtName: String = "",
        var status: Int = 0,
        var latitude: String = "",
        var longitude: String = "",
        var postalCode: String = "",
        var tokonowModel: TokonowModel = TokonowModel(),
        var errorCode: ErrorChosenAddressModel = ErrorChosenAddressModel()

): Parcelable