package com.tokopedia.flight.orderlist.data.cloud.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/08/2019
 */
class KeyValueEntity(@SerializedName("key")
                     @Expose
                     val key: String = "",
                     @SerializedName("value")
                     @Expose
                     val value: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KeyValueEntity> {
        override fun createFromParcel(parcel: Parcel): KeyValueEntity {
            return KeyValueEntity(parcel)
        }

        override fun newArray(size: Int): Array<KeyValueEntity?> {
            return arrayOfNulls(size)
        }
    }

}