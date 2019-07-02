package com.tokopedia.instantloan.data.model.response

import android.os.Parcel
import android.os.Parcelable

data class LoanPeriodMonth(
        var value: String? = "",
        var label: String? = "",
        var id: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
        parcel.writeString(value)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoanPeriodMonth> {
        override fun createFromParcel(parcel: Parcel): LoanPeriodMonth {
            return LoanPeriodMonth(parcel)
        }

        override fun newArray(size: Int): Array<LoanPeriodMonth?> {
            return arrayOfNulls(size)
        }
    }
}