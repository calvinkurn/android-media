package com.tokopedia.flight.orderlist.data.cloud.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/08/2019
 */
class CancellationTitleContentEntity(@SerializedName("title")
                                     @Expose
                                     val title: String = "",
                                     @SerializedName("content")
                                     @Expose
                                     val content: List<KeyValueEntity> = arrayListOf()) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArrayList(KeyValueEntity)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeTypedList(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CancellationTitleContentEntity> {
        override fun createFromParcel(parcel: Parcel): CancellationTitleContentEntity {
            return CancellationTitleContentEntity(parcel)
        }

        override fun newArray(size: Int): Array<CancellationTitleContentEntity?> {
            return arrayOfNulls(size)
        }
    }

}