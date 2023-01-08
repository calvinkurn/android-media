package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-10-01.
 */

data class DetailUiModel(
        var description: String = "",
        var type: String = "",
        var amountStr: String,
        var amount: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(type)
        parcel.writeString(amountStr)
        parcel.writeLong(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailUiModel> {
        override fun createFromParcel(parcel: Parcel): DetailUiModel {
            return DetailUiModel(parcel)
        }

        override fun newArray(size: Int): Array<DetailUiModel?> {
            return arrayOfNulls(size)
        }
    }
}