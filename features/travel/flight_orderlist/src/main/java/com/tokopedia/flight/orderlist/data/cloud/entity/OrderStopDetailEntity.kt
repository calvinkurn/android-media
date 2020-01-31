package com.tokopedia.flight.orderlist.data.cloud.entity

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderStopDetailEntity(
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("city")
        @Expose
        val city: String = "")
    : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderStopDetailEntity> {
        override fun createFromParcel(parcel: Parcel): OrderStopDetailEntity {
            return OrderStopDetailEntity(parcel)
        }

        override fun newArray(size: Int): Array<OrderStopDetailEntity?> {
            return arrayOfNulls(size)
        }
    }
}
