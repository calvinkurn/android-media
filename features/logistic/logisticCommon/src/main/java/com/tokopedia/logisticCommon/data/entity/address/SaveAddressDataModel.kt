@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.tokopedia.logisticCommon.data.entity.address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 2019-05-28.
 */
@Parcelize
data class SaveAddressDataModel (
        var id: Int = 0,
        var title: String = "",
        var formattedAddress: String = "",
        var addressName: String = "",
        var receiverName: String = "",
        var address1: String = "",
        var address2: String = "",
        var postalCode: String = "",
        var phone: String = "",
        var cityId: Int = 0,
        var provinceId: Int = 0,
        var districtId: Int = 0,
        var latitude: String = "",
        var longitude: String = "",
        var editDetailAddress: String = "",
        var selectedDistrict: String = "",
        var zipCodes: List<String> = emptyList(),
        var shopId: Long = 0,
        var warehouseId: Long = 0) : Parcelable