package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable

data class BadgeItemDataView(
        val imageUrl: String = "",
        val title: String = "",
        val isShown: Boolean = false,
): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrl)
        parcel.writeString(title)
        parcel.writeByte(if (isShown) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BadgeItemDataView> {
        override fun createFromParcel(parcel: Parcel): BadgeItemDataView {
            return BadgeItemDataView(parcel)
        }

        override fun newArray(size: Int): Array<BadgeItemDataView?> {
            return arrayOfNulls(size)
        }
    }
}