package com.tokopedia.instantloan.data.model.response

import android.os.Parcel
import android.os.Parcelable

data class LoanPeriodType(
        var value: String? = "",
        var label: String? = "",
        var id: Int,
        var isSelected: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
        parcel.writeString(label)
        parcel.writeInt(id)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoanPeriodType> {
        override fun createFromParcel(parcel: Parcel): LoanPeriodType {
            return LoanPeriodType(parcel)
        }

        override fun newArray(size: Int): Array<LoanPeriodType?> {
            return arrayOfNulls(size)
        }
    }
}