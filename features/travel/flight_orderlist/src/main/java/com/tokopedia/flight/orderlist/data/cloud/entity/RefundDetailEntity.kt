package com.tokopedia.flight.orderlist.data.cloud.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/08/2019
 */
class RefundDetailEntity(@SerializedName("top_info")
                         @Expose
                         val topInfo: List<KeyValueEntity> = arrayListOf(),
                         @SerializedName("middle_info")
                         @Expose
                         val middleInfo: List<CancellationTitleContentEntity> = arrayListOf(),
                         @SerializedName("bottom_info")
                         @Expose
                         val bottomInfo: List<KeyValueEntity> = arrayListOf(),
                         @SerializedName("note")
                         @Expose
                         val note: List<KeyValueEntity> = arrayListOf()) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(KeyValueEntity),
            parcel.createTypedArrayList(CancellationTitleContentEntity),
            parcel.createTypedArrayList(KeyValueEntity),
            parcel.createTypedArrayList(KeyValueEntity)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(topInfo)
        parcel.writeTypedList(middleInfo)
        parcel.writeTypedList(bottomInfo)
        parcel.writeTypedList(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RefundDetailEntity> {
        override fun createFromParcel(parcel: Parcel): RefundDetailEntity {
            return RefundDetailEntity(parcel)
        }

        override fun newArray(size: Int): Array<RefundDetailEntity?> {
            return arrayOfNulls(size)
        }
    }

}