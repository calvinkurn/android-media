package com.tokopedia.sellerorder.oldlist.data.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.createIntList
import com.tokopedia.kotlin.extensions.view.writeIntList

/**
 * Created by fwidjaja on 2019-09-13.
 */

data class SomSubFilter(
        val id: Int = 0,
        val name: String = "",
        val key: String = "",
        val typeView: String = "",
        val typeFilter: String = "",
        val listValue: List<Int> = arrayListOf(),
        var isChecked: Boolean = false

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createIntList(),
            parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(key)
        parcel.writeString(typeView)
        parcel.writeString(typeFilter)
        parcel.writeIntList(listValue)
        parcel.writeByte(if (isChecked) 1 else 0)
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