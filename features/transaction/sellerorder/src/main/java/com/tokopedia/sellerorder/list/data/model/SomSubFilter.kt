package com.tokopedia.sellerorder.list.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2019-09-13.
 */

data class SomSubFilter(
        val id: Int = 0,
        val name: String = "",
        val key: String = "",
        val typeView: String = "",
        val typeFilter: String = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(key)
        parcel.writeString(typeView)
        parcel.writeString(typeFilter)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SomSubFilter> {
        override fun createFromParcel(parcel: Parcel): SomSubFilter {
            return SomSubFilter(parcel)
        }

        override fun newArray(size: Int): Array<SomSubFilter?> {
            return arrayOfNulls(size)
        }
    }
}