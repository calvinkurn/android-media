package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2019-05-16.
 */
data class GetDistrictDataUiModel (
        var title: String = "",
        var formattedAddress: String = "",
        var latitude: String = "",
        var longitude: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(formattedAddress)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GetDistrictDataUiModel> {
        override fun createFromParcel(parcel: Parcel): GetDistrictDataUiModel {
            return GetDistrictDataUiModel(parcel)
        }

        override fun newArray(size: Int): Array<GetDistrictDataUiModel?> {
            return arrayOfNulls(size)
        }
    }

}