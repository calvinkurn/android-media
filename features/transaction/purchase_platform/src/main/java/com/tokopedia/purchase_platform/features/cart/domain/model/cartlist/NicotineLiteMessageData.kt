package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist

import android.os.Parcel
import android.os.Parcelable

data class NicotineLiteMessageData(
        val text: String = "",
        val url: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NicotineLiteMessageData> {
        override fun createFromParcel(parcel: Parcel): NicotineLiteMessageData {
            return NicotineLiteMessageData(parcel)
        }

        override fun newArray(size: Int): Array<NicotineLiteMessageData?> {
            return arrayOfNulls(size)
        }
    }
}