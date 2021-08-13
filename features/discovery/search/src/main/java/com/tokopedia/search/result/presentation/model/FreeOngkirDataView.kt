package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable

data class FreeOngkirDataView(
        val isActive: Boolean = false,
        val imageUrl: String = ""
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FreeOngkirDataView> {
        override fun createFromParcel(parcel: Parcel): FreeOngkirDataView {
            return FreeOngkirDataView(parcel)
        }

        override fun newArray(size: Int): Array<FreeOngkirDataView?> {
            return arrayOfNulls(size)
        }
    }
}