package com.tokopedia.topads.edit.data.param

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 13/4/20.
 */

class NegKeyword() : Parcelable {
    @field:SerializedName("name")
    var name: String? = ""
    @field:SerializedName("isChecked")
    var isChecked: Boolean = false
    @field:SerializedName("type")
    var type: String = "negative_phrase"

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        type = parcel.readString()
        isChecked = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(type)
        dest?.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NegKeyword> {
        override fun createFromParcel(parcel: Parcel): NegKeyword {
            return NegKeyword(parcel)
        }

        override fun newArray(size: Int): Array<NegKeyword?> {
            return arrayOfNulls(size)
        }
    }
}