package com.tokopedia.expresscheckout.view.profile.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class ProfileViewModel(
        var profileId: Int = 0,
        var addressId: Int = 0,
        var templateTitle: String = "",
        var isMainTemplate: Boolean = false,
        var addressTitle: String = "",
        var addressDetail: String = "",
        var cityName: String = "",
        var districtName: String = "",
        var paymentImageUrl: String = "",
        var paymentDetail: String = "",
        var durationDetail: String = "",
        var durationId: Int = 0,
        var isSelected: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel? = null) : this(
            parcel?.readInt() ?: 0,
            parcel?.readInt() ?: 0,
            parcel?.readString() ?: "",
            parcel?.readByte() != 0.toByte(),
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readString() ?: "",
            parcel?.readValue(Int::class.java.classLoader) as? Int ?: 0,
            parcel?.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(profileId)
        parcel.writeInt(addressId)
        parcel.writeString(templateTitle)
        parcel.writeByte(if (isMainTemplate) 1 else 0)
        parcel.writeString(addressTitle)
        parcel.writeString(addressDetail)
        parcel.writeString(cityName)
        parcel.writeString(districtName)
        parcel.writeString(paymentImageUrl)
        parcel.writeString(paymentDetail)
        parcel.writeString(durationDetail)
        parcel.writeValue(durationId)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileViewModel> {
        override fun createFromParcel(parcel: Parcel): ProfileViewModel {
            return ProfileViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ProfileViewModel?> {
            return arrayOfNulls(size)
        }
    }

}