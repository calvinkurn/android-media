@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2019-05-28.
 */
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
        var zipCodes: List<String> = emptyList()) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(formattedAddress)
        parcel.writeString(addressName)
        parcel.writeString(receiverName)
        parcel.writeString(address1)
        parcel.writeString(address2)
        parcel.writeString(postalCode)
        parcel.writeString(phone)
        parcel.writeInt(cityId)
        parcel.writeInt(provinceId)
        parcel.writeInt(districtId)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(editDetailAddress)
        parcel.writeString(selectedDistrict)
        parcel.writeStringList(zipCodes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SaveAddressDataModel> {
        override fun createFromParcel(parcel: Parcel): SaveAddressDataModel {
            return SaveAddressDataModel(parcel)
        }

        override fun newArray(size: Int): Array<SaveAddressDataModel?> {
            return arrayOfNulls(size)
        }
    }
}