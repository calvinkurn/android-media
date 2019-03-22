package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 22/03/19.
 */

data class SummariesUiModel(
        var description: String = "",
        var type: String = "",
        var amountStr: String = "",
        var amount: Int = -1
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(type)
        parcel.writeString(amountStr)
        parcel.writeInt(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SummariesUiModel> {
        override fun createFromParcel(parcel: Parcel): SummariesUiModel {
            return SummariesUiModel(parcel)
        }

        override fun newArray(size: Int): Array<SummariesUiModel?> {
            return arrayOfNulls(size)
        }
    }
}