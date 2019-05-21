package com.tokopedia.instantloan.data.model.response

import android.os.Parcel
import android.os.Parcelable

data class LoanPeriodType(
        var value: String? = "",
        var label: String? = "",
        var id: Int
) : Parcelable, LoanParam {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
        parcel.writeString(label)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoanPeriodYear> {
        override fun createFromParcel(parcel: Parcel): LoanPeriodYear {
            return LoanPeriodYear(parcel)
        }

        override fun newArray(size: Int): Array<LoanPeriodYear?> {
            return arrayOfNulls(size)
        }
    }
}